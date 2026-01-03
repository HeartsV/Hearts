package de.htwg.se.Hearts.util
import de.htwg.se.Hearts.controller.controllerComponent.ControllerInterface
import de.htwg.se.Hearts.model.gameComponent.GameInterface


trait Command(
    val gameController:ControllerInterface,
    val backup:GameInterface) {

    //def saveBackup():


    def undo = gameController.setGame(backup)


    def execute: Unit
}

class PlayCardCommand extends Command(gameController:ControllerInterface,backup:GameInterface):
    override def execute: Unit =
        val builder = GameBuilder(controller.game)
        builder.setPlayers(controller.executeStrategy)
        input.trim.toLowerCase match
            case "suit" | "s" =>
                controller.setStrategy(SortBySuitStrategy())
                builder.setPlayers(controller.executeStrategy)
                builder.setLastPlayedCard(Left("Cards sorted by suit"))
                builder.getGame
            case "rank" | "r" =>
                builder.setPlayers(controller.executeStrategy)
                controller.setStrategy(SortByRankStrategy())
                builder.setLastPlayedCard(Left("Cards sorted by rank"))
                builder.getGame
            case "rules" | "ru" =>
                controller.changeState(RulesScreenState(controller))
                builder.getGame
            case "exit" | "e" =>
                builder.setKeepProcessRunning(false)
                builder.getGame
            case _ =>
                if !input.toIntOption.equals(None) then
                    if builder.game.trickCards.size == builder.game.playerNumber.get then
                        builder.setTrickCards(List())
                        builder.setCurrentWinnerAndHighestCard(None, None)
                    val result = cOR.validateMove(
                        builder.game,
                        controller.getPlayerHand,
                        input.toInt - 1
                    )

                    result match
                    case Left(_) => builder.setLastPlayedCard(result)
                    case Right(cardToPlay) =>
                            val sortedHand = controller.getPlayerHand

                            builder.setPlayers(
                                builder.game.players.updated(
                                    builder.game.currentPlayerIndex.get,
                                    builder.game.getCurrentPlayer.get.removeCard(cardToPlay)
                                )
                            )

                            builder.addCard(cardToPlay)

                            builder.setCurrentWinnerAndHighestCard(
                                controller.turnService.updateCurrentWinner(
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
                            builder.setCurrentPlayerIndex(controller.turnService.nextPlayerIndex(builder.game))

                            if builder.game.players.forall(_.hand.size == 0) then
                                if !controller.checkGameOver then controller.changeState(ShowScoreState(controller))
                                else controller.changeState(GameOverState(controller))

                                builder.setPlayers(controller.scoringService.addPointsToPlayers(builder.game))

                    builder.getGame

                else
                    builder.setLastPlayedCard(Left("No allowed input!"))
                    builder.getGame


