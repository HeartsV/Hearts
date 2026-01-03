package de.htwg.se.Hearts.controller.controllerComponent.controllerBase
import de.htwg.se.Hearts.model.gameComponent.GameInterface
import de.htwg.se.Hearts.model.gameComponent.BuilderInterface
import de.htwg.se.Hearts.model.gameComponent.gameBase.GameBuilder
import de.htwg.se.Hearts.model.gameComponent.Suit
import de.htwg.se.Hearts.model.gameComponent.Rank
import de.htwg.se.Hearts.util._
import de.htwg.se.Hearts.model.gameComponent.BuilderInterface
import de.htwg.se.Hearts.model.gameComponent.gameBase.Game
import de.htwg.se.Hearts.model.gameComponent.CoRInterface
import de.htwg.se.Hearts.model.gameComponent.gameBase.ChainOfResponsibility

class SetPlayerNumberCommand(gameController: Controller, backup: (GameInterface, State), index: Option[Int]) extends Command:
    override def redoStep = ???
    override def undoStep = ???

    override def doStep: Unit = ???

class AddPlayerCommand(gameController: Controller, backup: (GameInterface, State), index: Option[Int]) extends Command:
    override def redoStep = ???
    override def undoStep = ???

    override def doStep: Unit = ???

class SetMaxScoreCommand(gameController: Controller, backup: (GameInterface, State), index: Option[Int]) extends Command:
    override def redoStep = ???
    override def undoStep = ???

    override def doStep: Unit = ???

class SetSortingRankCommand(gameController: Controller, backup: (GameInterface, State), index: Option[Int]) extends Command:
    override def redoStep = ???
    override def undoStep = ???

    override def doStep: Unit = ???

class SetSortingSuitCommand(gameController: Controller, backup: (GameInterface, State), index: Option[Int]) extends Command:
    override def redoStep = ???
    override def undoStep = ???

    override def doStep: Unit = ???

class NewCommand(gameController: Controller, backup: (GameInterface, State), index: Option[Int]) extends Command:
    override def redoStep = ???
    override def undoStep = ???

    override def doStep: Unit = ???

class AgainCommand(gameController: Controller, backup: (GameInterface, State), index: Option[Int]) extends Command:
    override def redoStep = ???
    override def undoStep = ???

    override def doStep: Unit = ???

class QuitCommand(gameController: Controller, backup: (GameInterface, State), index: Option[Int]) extends Command:
    override def redoStep = ???
    override def undoStep = ???

    override def doStep: Unit = ???

class ExitCommand(gameController: Controller, backup: (GameInterface, State), index: Option[Int]) extends Command:
    override def redoStep = ???
    override def undoStep = ???

    override def doStep: Unit = ???

class RulesCommand(gameController: Controller, backup: (GameInterface, State), index: Option[Int]) extends Command:
    override def redoStep = ???
    override def undoStep = ???

    override def doStep: Unit = ???

class BackCommand(gameController: Controller, backup: (GameInterface, State), index: Option[Int]) extends Command:
    override def redoStep = ???
    override def undoStep = ???

    override def doStep: Unit = ???


class PlayCardCommand(gameController: Controller, backup: (GameInterface, State), index: Option[Int]) extends Command:
    override def redoStep = ???
    override def undoStep = ???

    override def doStep: Unit =
            val builder: BuilderInterface = GameBuilder(gameController.game.asInstanceOf[Game])
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
                        builder.getPlayers.updated(
                            builder.getCurrentPlayerIndex.get,
                            builder.getCurrentPlayer.get.removeCard(cardToPlay)
                        )
                    )

                    builder.addCard(cardToPlay)

                    builder.setCurrentWinnerAndHighestCard(
                        gameController.turnService.updateCurrentWinner(
                            (builder.getCurrentPlayerIndex.get, cardToPlay),
                            builder.getCopy
                        )
                    )

                    if builder.getFirstCard then builder.setFirstCard(false)

                    if (cardToPlay.suit == Suit.Hearts || (cardToPlay.getRank == Rank.Queen && cardToPlay.getSuit == Suit.Spades) && !builder.getStartWithHearts)
                        builder.setStartWithHearts(true)
                    if builder.getTrickSize == builder.getPlayerNumber then
                        builder.updatePlayer(
                            builder.getCurrentWinnerIndex.get,
                            builder.getPlayers(builder.getCurrentWinnerIndex.get).addWonCards(builder.getTrickCards)
                        )

                    builder.setLastPlayedCard(result)
                    builder.setCurrentPlayerIndex(gameController.turnService.nextPlayerIndex(builder.getCopy))

                    if builder.getPlayers.forall(_.hand.size == 0) then
                        if !gameController.checkGameOver then gameController.changeState(ShowScoreState(gameController))
                        else gameController.changeState(GameOverState(gameController))

                        builder.setPlayers(gameController.scoringService.addPointsToPlayers(builder.getCopy))

            builder.getGame

