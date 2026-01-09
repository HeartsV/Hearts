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

class SetPlayerNumberCommand(var gameController: Option[Controller] = None, var backup: Option[(GameInterface, State)] = None, index: Option[Int]) extends Command:
    override def setup(newController:Controller):Unit = gameController = Some(newController)
    override def storeBackup (game:GameInterface,state:State): Unit = backup = Some((gameController.get.game,gameController.get.state))
    override def redoStep = ???
    override def undoStep = ???

    override def doStep: Unit =
        val builder:BuilderInterface = GameBuilder(gameController.get.getGame.asInstanceOf[Game])
        if index.exists(intInput => intInput >= 3 && intInput <= 4) then
            gameController.get.changeState(GetPlayerNamesState(gameController.get))
            builder.setPlayerNumber(index.get)
        gameController.get.game = builder.getGame

class AddPlayerCommand(var gameController: Option[Controller] = None, var backup: Option[(GameInterface, State)] = None, name: String) extends Command:
    override def setup(newController:Controller):Unit = gameController = Some(newController)
    override def storeBackup (game:GameInterface,state:State): Unit = backup = Some((gameController.get.game,gameController.get.state))
    override def redoStep = ???
    override def undoStep = ???

    override def doStep: Unit =
        val builder:BuilderInterface = GameBuilder(gameController.get.game.asInstanceOf[Game])

        if !name.trim.equals("") then
            builder.setPlayers(builder.getPlayers :+ Player(name))
        else
            builder.setPlayers(builder.getPlayers :+ Player(s"P${builder.getPlayers.size + 1}"))

        if builder.getPlayers.size == builder.getPlayerNumber then
            builder.setPlayers(gameController.get.dealNewRound(builder.getCopy))
            gameController.get.changeState(SetMaxScoreState(gameController.get))

        gameController.get.game = builder.getGame

class SetMaxScoreCommand(var gameController: Option[Controller] = None, var backup: Option[(GameInterface, State)] = None, index: Option[Int]) extends Command:
    override def setup(newController:Controller):Unit = gameController = Some(newController)
    override def storeBackup (game:GameInterface,state:State): Unit = backup = Some((gameController.get.game,gameController.get.state))
    override def redoStep = ???
    override def undoStep = ???

    override def doStep: Unit =
        val builder:BuilderInterface = GameBuilder(gameController.get.game.asInstanceOf[Game])

        if index.exists(intInput => intInput >= 1 && intInput <= 400) then
            gameController.get.changeState(GamePlayState(gameController.get))
            builder.setMaxScore(index.get)
        else
            gameController.get.changeState(GamePlayState(gameController.get))
            builder.setMaxScore(100)
        builder.setCurrentPlayerIndex(gameController.get.turnService.nextPlayerIndex(builder.getCopy))
        gameController.get.game = builder.getGame

class SetSortingRankCommand(var gameController: Option[Controller] = None, var backup: Option[(GameInterface, State)] = None) extends Command:
    override def setup(newController:Controller):Unit = gameController = Some(newController)
    override def storeBackup (game:GameInterface,state:State): Unit = backup = Some((gameController.get.game,gameController.get.state))
    override def redoStep = ???
    override def undoStep = ???

    override def doStep: Unit =
        val builder:BuilderInterface = GameBuilder(gameController.get.game.asInstanceOf[Game])
        builder.setPlayers(gameController.get.executeStrategy)
        gameController.get.setStrategy(SortByRankStrategy())
        builder.setLastPlayedCard(Left("Cards sorted by rank"))
        gameController.get.game = builder.getGame

class SetSortingSuitCommand(var gameController: Option[Controller] = None, var backup: Option[(GameInterface, State)] = None) extends Command:
    override def setup(newController:Controller):Unit = gameController = Some(newController)
    override def storeBackup (game:GameInterface,state:State): Unit = backup = Some((gameController.get.game,gameController.get.state))
    override def redoStep = ???
    override def undoStep = ???

    override def doStep: Unit =
        val builder:BuilderInterface = GameBuilder(gameController.get.game.asInstanceOf[Game])
        gameController.get.setStrategy(SortBySuitStrategy())
        builder.setPlayers(gameController.get.executeStrategy)
        builder.setLastPlayedCard(Left("Cards sorted by suit"))
        gameController.get.game = builder.getGame

class NewCommand(var gameController: Option[Controller] = None, var backup: Option[(GameInterface, State)] = None) extends Command:
    override def setup(newController:Controller):Unit = gameController = Some(newController)
    override def storeBackup (game:GameInterface,state:State): Unit = backup = Some((gameController.get.game,gameController.get.state))
    override def redoStep = ???
    override def undoStep = ???

    override def doStep: Unit = gameController.get.changeState(GetPlayerNumberState(gameController.get))

class AgainCommand(var gameController: Option[Controller] = None, var backup: Option[(GameInterface, State)] = None) extends Command:
    override def setup(newController:Controller):Unit = gameController = Some(newController)
    override def storeBackup (game:GameInterface,state:State): Unit = backup = Some((gameController.get.game,gameController.get.state))
    override def redoStep = ???
    override def undoStep = ???

    override def doStep: Unit =
        val builder:BuilderInterface = GameBuilder(gameController.get.game.asInstanceOf[Game])
        builder.setPlayers(gameController.get.dealNewRound(builder.getCopy))
            val director = Director()
            director.resetForNextGame(builder.asInstanceOf[GameBuilder])
            gameController.get.changeState(GamePlayState(gameController.get))
        gameController.get.game = builder.getGame

class QuitCommand(var gameController: Option[Controller] = None, var backup: Option[(GameInterface, State)] = None) extends Command:
    override def setup(newController:Controller):Unit = gameController = Some(newController)
    override def storeBackup (game:GameInterface,state:State): Unit = backup = Some((gameController.get.game,gameController.get.state))
    override def redoStep = ???
    override def undoStep = ???

    override def doStep: Unit = gameController.get.changeState(MainScreenState(gameController.get))

class ExitCommand(var gameController: Option[Controller] = None, var backup: Option[(GameInterface, State)] = None) extends Command:
    override def setup(newController:Controller):Unit = gameController = Some(newController)
    override def storeBackup (game:GameInterface,state:State): Unit = backup = Some((gameController.get.game,gameController.get.state))
    override def redoStep = ???
    override def undoStep = ???

    override def doStep: Unit =
        val builder:BuilderInterface = GameBuilder(gameController.get.game.asInstanceOf[Game])
        builder.setKeepProcessRunning(false)
        gameController.get.game = builder.getGame

class RulesCommand(var gameController: Option[Controller] = None, var backup: Option[(GameInterface, State)] = None) extends Command:
    override def setup(newController:Controller):Unit = gameController = Some(newController)
    override def storeBackup (game:GameInterface,state:State): Unit = backup = Some((gameController.get.game,gameController.get.state))
    override def redoStep = ???
    override def undoStep = ???

    override def doStep: Unit = gameController.get.changeState(RulesScreenState(gameController.get))

class BackCommand(var gameController: Option[Controller] = None, var backup: Option[(GameInterface, State)] = None) extends Command:
    override def setup(newController:Controller):Unit = gameController = Some(newController)
    override def storeBackup (game:GameInterface,state:State): Unit = backup = Some((gameController.get.game,gameController.get.state))
    override def redoStep = ???
    override def undoStep = ???

    override def doStep: Unit =
        if gameController.get.game.getPlayers.equals(Vector.empty) then gameController.get.changeState(MainScreenState(gameController.get))
        else gameController.get.changeState(GamePlayState(gameController.get))

class ContinueCommand(var gameController: Option[Controller] = None, var backup: Option[(GameInterface, State)] = None) extends Command:
    override def setup(newController:Controller):Unit = gameController = Some(newController)
    override def storeBackup (game:GameInterface,state:State): Unit = backup = Some((gameController.get.game,gameController.get.state))
    override def redoStep = ???
    override def undoStep = ???

    override def doStep: Unit =
        val builder:BuilderInterface = GameBuilder(gameController.get.game.asInstanceOf[Game])
        builder.setPlayers(gameController.get.dealNewRound(builder.getCopy))
        builder.setFirstCard(true)
        builder.setStartWithHearts(false)

        gameController.get.changeState(GamePlayState(gameController.get))
        gameController.get.game = builder.getGame


class PlayCardCommand(var gameController: Option[Controller] = None, var backup: Option[(GameInterface, State)] = None, index: Option[Int]) extends Command:
    override def setup(newController:Controller):Unit = gameController = Some(newController)
    override def storeBackup (game:GameInterface,state:State): Unit = backup = Some((gameController.get.game,gameController.get.state))
    override def redoStep = ???
    override def undoStep = ???

    override def doStep: Unit =
            val builder: BuilderInterface = GameBuilder(gameController.get.game.asInstanceOf[Game])
            builder.setPlayers(gameController.get.executeStrategy)
            if builder.getTrickSize == builder.getPlayerNumber then
                builder.setTrickCards(List())
                builder.setCurrentWinnerAndHighestCard(None, None)
            val cOR: CoRInterface = ChainOfResponsibility()
            val result = cOR.validateMove(
                builder.getCopy,
                gameController.get.getPlayerHand,
                index
            )

            result match
            case Left(_) => builder.setLastPlayedCard(result)
            case Right(cardToPlay) =>
                    val sortedHand = gameController.get.getPlayerHand

                    builder.setPlayers(
                        builder.getPlayers.updated(
                            builder.getCurrentPlayerIndex.get,
                            builder.getCurrentPlayer.get.removeCard(cardToPlay)
                        )
                    )

                    builder.addCard(cardToPlay)

                    builder.setCurrentWinnerAndHighestCard(
                        gameController.get.turnService.updateCurrentWinner(
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
                    builder.setCurrentPlayerIndex(gameController.get.turnService.nextPlayerIndex(builder.getCopy))

                    if builder.getPlayers.forall(_.hand.size == 0) then
                        if !gameController.get.checkGameOver then gameController.get.changeState(ShowScoreState(gameController.get))
                        else gameController.get.changeState(GameOverState(gameController.get))

                        builder.setPlayers(gameController.get.scoringService.addPointsToPlayers(builder.getCopy))

            gameController.get.game = builder.getGame

