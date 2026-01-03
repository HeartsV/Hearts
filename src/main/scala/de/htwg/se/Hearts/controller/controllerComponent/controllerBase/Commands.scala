package de.htwg.se.Hearts.controller.controllerComponent.controllerBase
import de.htwg.se.Hearts.model.gameComponent.GameInterface
import de.htwg.se.Hearts.model.gameComponent.BuilderInterface
import de.htwg.se.Hearts.model.gameComponent.gameBase.GameBuilder
import de.htwg.se.Hearts.model.gameComponent.Suit
import de.htwg.se.Hearts.model.gameComponent.Rank
import de.htwg.se.Hearts.util._
import de.htwg.se.Hearts.controller.controllerComponent.ControllerInterface
import de.htwg.se.Hearts.model.gameComponent.BuilderInterface
import de.htwg.se.Hearts.model.gameComponent.gameBase.Game
import de.htwg.se.Hearts.model.gameComponent.CoRInterface
import de.htwg.se.Hearts.model.gameComponent.gameBase.ChainOfResponsibility
import de.htwg.se.Hearts.model.gameComponent.gameBase.{Director, GameBuilder}
import de.htwg.se.Hearts.model.gameComponent.gameBase.Player

class SetPlayerNumberCommand(gameController: ControllerInterface, backup: (GameInterface, State), index: Option[Int]) extends Command:
    override def redoStep = ???
    override def undoStep = ???

    override def doStep: Unit =
        val builder:BuilderInterface = GameBuilder(gameController.getGame.asInstanceOf[Game])
        if index.exists(intInput => intInput >= 3 && intInput <= 4) then
            gameController.changeState(GetPlayerNamesState(gameController))
            builder.setPlayerNumber(index.get)
            gameController.game = builder.getGame
        else
            gameController.game = builder.getGame

class AddPlayerCommand(gameController: ControllerInterface, backup: (GameInterface, State), name: String) extends Command:
    override def redoStep = ???
    override def undoStep = ???

    override def doStep: Unit =
        val builder:BuilderInterface = GameBuilder(gameController.game.asInstanceOf[Game])

        if !name.trim.equals("") then
            builder.setPlayers(builder.getPlayers :+ Player(name))
        else
            builder.setPlayers(builder.getPlayers :+ Player(s"P${builder.getPlayers.size + 1}"))

        if builder.getPlayers.size == builder.getPlayerNumber then
            builder.setPlayers(gameController.dealNewRound(builder.getCopy))
            gameController.changeState(SetMaxScoreState(gameController))

        gameController.game = builder.getGame

class SetMaxScoreCommand(gameController: ControllerInterface, backup: (GameInterface, State), index: Option[Int]) extends Command:
    override def redoStep = ???
    override def undoStep = ???

    override def doStep: Unit =
        val builder:BuilderInterface = GameBuilder(gameController.game.asInstanceOf[Game])

        if index.exists(intInput => intInput >= 1 && intInput <= 400) then
            gameController.changeState(GamePlayState(gameController))
            builder.setMaxScore(index.get)
        else
            gameController.changeState(GamePlayState(gameController))
            builder.setMaxScore(100)
        builder.setCurrentPlayerIndex(gameController.turnService.nextPlayerIndex(builder.getCopy))
        gameController.game = builder.getGame

class SetSortingRankCommand(gameController: ControllerInterface, backup: (GameInterface, State), index: Option[Int]) extends Command:
    override def redoStep = ???
    override def undoStep = ???

    override def doStep: Unit =
        val builder:BuilderInterface = GameBuilder(gameController.game.asInstanceOf[Game])
        builder.setPlayers(gameController.executeStrategy)
        gameController.setStrategy(SortByRankStrategy())
        builder.setLastPlayedCard(Left("Cards sorted by rank"))
        gameController.game = builder.getGame

class SetSortingSuitCommand(gameController: ControllerInterface, backup: (GameInterface, State), index: Option[Int]) extends Command:
    override def redoStep = ???
    override def undoStep = ???

    override def doStep: Unit =
        val builder:BuilderInterface = GameBuilder(gameController.game.asInstanceOf[Game])
        gameController.setStrategy(SortBySuitStrategy())
        builder.setPlayers(gameController.executeStrategy)
        builder.setLastPlayedCard(Left("Cards sorted by suit"))
        gameController.game = builder.getGame

class NewCommand(gameController: ControllerInterface, backup: (GameInterface, State)) extends Command:
    override def redoStep = ???
    override def undoStep = ???

    override def doStep: Unit = gameController.changeState(GetPlayerNumberState(gameController))

class AgainCommand(gameController: ControllerInterface, backup: (GameInterface, State)) extends Command:
    override def redoStep = ???
    override def undoStep = ???

    override def doStep: Unit =
        val builder:BuilderInterface = GameBuilder(gameController.game.asInstanceOf[Game])
        builder.setPlayers(gameController.dealNewRound(builder.getCopy))
            val director = Director()
            director.resetForNextGame(builder.asInstanceOf[GameBuilder])
            gameController.changeState(GamePlayState(gameController))
        gameController.game = builder.getGame

class QuitCommand(gameController: ControllerInterface, backup: (GameInterface, State)) extends Command:
    override def redoStep = ???
    override def undoStep = ???

    override def doStep: Unit = gameController.changeState(MainScreenState(gameController))

class ExitCommand(gameController: ControllerInterface, backup: (GameInterface, State)) extends Command:
    override def redoStep = ???
    override def undoStep = ???

    override def doStep: Unit =
        val builder:BuilderInterface = GameBuilder(gameController.game.asInstanceOf[Game])
        builder.setKeepProcessRunning(false)
        gameController.game = builder.getGame

class RulesCommand(gameController: ControllerInterface, backup: (GameInterface, State)) extends Command:
    override def redoStep = ???
    override def undoStep = ???

    override def doStep: Unit = gameController.changeState(RulesScreenState(gameController))

class BackCommand(gameController: ControllerInterface, backup: (GameInterface, State)) extends Command:
    override def redoStep = ???
    override def undoStep = ???

    override def doStep: Unit =
        if gameController.game.getPlayers.equals(Vector.empty) then gameController.changeState(MainScreenState(gameController))
        else gameController.changeState(GamePlayState(gameController))

class ContinueCommand(gameController: ControllerInterface, backup: (GameInterface, State)) extends Command:
    override def redoStep = ???
    override def undoStep = ???

    override def doStep: Unit =
        val builder:BuilderInterface = GameBuilder(gameController.game.asInstanceOf[Game])
        builder.setPlayers(gameController.dealNewRound(builder.getCopy))
        builder.setFirstCard(true)
        builder.setStartWithHearts(false)

        gameController.changeState(GamePlayState(gameController))
        gameController.game = builder.getGame


class PlayCardCommand(gameController: ControllerInterface, backup: (GameInterface, State), index: Option[Int]) extends Command:
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

            gameController.game = builder.getGame

