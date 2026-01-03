package de.htwg.se.Hearts.controller.controllerComponent.controllerBase
import de.htwg.se.Hearts.model.gameComponent.GameInterface
import de.htwg.se.Hearts.model.gameComponent.BuilderInterface
import de.htwg.se.Hearts.model.gameComponent.gameBase.GameBuilder

import de.htwg.se.Hearts.util._
import de.htwg.se.Hearts.model.gameComponent.BuilderInterface
import de.htwg.se.Hearts.model.gameComponent.gameBase.Game
import de.htwg.se.Hearts.model.gameComponent.CoRInterface
import de.htwg.se.Hearts.model.gameComponent.gameBase.ChainOfResponsibility


class PlayCardCommand(gameController:Controller,backup:GameInterface,index:Option[Int]) extends Command:
    override def execute: Unit =
            val builder:BuilderInterface = GameBuilder(gameController.game.asInstanceOf[Game])
            builder.setPlayers(gameController.executeStrategy)
            if builder.getTrickSize == builder.getPlayerNumber then
                builder.setTrickCards(List())
                builder.setCurrentWinnerAndHighestCard(None, None)
            val cOR: CoRInterface = ChainOfResponsibility()
            val result = cOR.validateMove(
                builder.getCopy,
                gameController.getPlayerHand,
                index
            )

            result match
            case Left(_) => builder.setLastPlayedCard(result)
            case Right(cardToPlay) =>
                    val sortedHand = gameController.getPlayerHand

                    builder.setPlayers(
                        builder.game.players.updated(
                            builder.game.currentPlayerIndex.get,
                            builder.game.getCurrentPlayer.get.removeCard(cardToPlay)
                        )
                    )

                    builder.addCard(cardToPlay)

                    builder.setCurrentWinnerAndHighestCard(
                        gameController.turnService.updateCurrentWinner(
                            (builder.game.currentPlayerIndex.get, cardToPlay),
                            builder.game
                        )
                    )

                    if builder.game.firstCard then builder.setFirstCard(false)

                    if (cardToPlay.suit == Suit.Hearts || (cardToPlay.getRank == Rank.Queen && cardToPlay.getSuit == Suit.Spades) && !builder.game.startWithHearts)
                        builder.setStartWithHearts(true)
                    if builder.game.trickCards.size == builder.game.playerNumber.get then
                        builder.updatePlayer(
                            builder.game.currentWinnerIndex.get,
                            builder.game.players(builder.game.currentWinnerIndex.get).addWonCards(builder.game.trickCards)
                        )

                    builder.setLastPlayedCard(result)
                    builder.setCurrentPlayerIndex(gameController.turnService.nextPlayerIndex(builder.game))

                    if builder.game.players.forall(_.hand.size == 0) then
                        if !gameController.checkGameOver then gameController.changeState(ShowScoreState(gameController))
                        else gameController.changeState(GameOverState(gameController))

                        builder.setPlayers(gameController.scoringService.addPointsToPlayers(builder.game))

            builder.getGame

        else
            builder.setLastPlayedCard(Left("No allowed input!"))
            builder.getGame
