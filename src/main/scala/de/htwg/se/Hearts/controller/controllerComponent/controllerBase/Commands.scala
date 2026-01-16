package de.htwg.se.Hearts.controller.controllerComponent.controllerBase
import de.htwg.se.Hearts.model.gameComponent.GameInterface
import de.htwg.se.Hearts.model.gameComponent.BuilderInterface
import de.htwg.se.Hearts.model.gameComponent.Suit
import de.htwg.se.Hearts.model.gameComponent.Rank
import de.htwg.se.Hearts.util._
import de.htwg.se.Hearts.controller.controllerComponent.ControllerInterface
import de.htwg.se.Hearts.model.gameComponent.BuilderInterface
import de.htwg.se.Hearts.model.gameComponent.CoRInterface
import de.htwg.se.Hearts.model.gameComponent.DirectorInterface
import de.htwg.se.Hearts.model.gameComponent.PlayerInterface

class RedoCommand(var gameController: Option[Controller] = None, var backup: Option[(GameInterface, State)] = None) extends Command:
    override def setup(newController:Controller):Unit = gameController = Some(newController)
    override def storeBackup: Unit = backup = Some((gameController.get.game,gameController.get.state))

    override def undoStep =
        gameController.get.game = backup.get._1
        gameController.get.state = backup.get._2
    override def execute: Boolean =
        gameController.get.redo
        false


class UndoCommand(var gameController: Option[Controller] = None, var backup: Option[(GameInterface, State)] = None) extends Command:
    override def setup(newController:Controller):Unit = gameController = Some(newController)
    override def storeBackup: Unit = backup = Some((gameController.get.game,gameController.get.state))

    override def undoStep =
        gameController.get.game = backup.get._1
        gameController.get.state = backup.get._2
    override def execute: Boolean =
        gameController.get.undo
        false

class SetPlayerNumberCommand(var gameController: Option[Controller] = None, var backup: Option[(GameInterface, State)] = None, index: Option[Int]) extends Command:
    override def setup(newController:Controller):Unit = gameController = Some(newController)
    override def storeBackup: Unit = backup = Some((gameController.get.game,gameController.get.state))

    override def undoStep =
        gameController.get.game = backup.get._1
        gameController.get.state = backup.get._2

    override def execute: Boolean =
        val director = injector.getInstance(classOf[DirectorInterface])
        director.copyGameState(gameController.get.game)
        //val builder:BuilderInterface = GameBuilder(gameController.get.getGame.asInstanceOf[Game])
        if index.exists(intInput => intInput >= 3 && intInput <= 4) then
            gameController.get.changeState(GetPlayerNamesState(gameController.get))
            director.getBuilder.setPlayerNumber(Some(index.get))
        gameController.get.game = director.getBuilder.getGame
        true

class AddPlayerCommand(var gameController: Option[Controller] = None, var backup: Option[(GameInterface, State)] = None, name: String) extends Command:
    override def setup(newController:Controller):Unit = gameController = Some(newController)
    override def storeBackup: Unit = backup = Some((gameController.get.game,gameController.get.state))

    override def undoStep =
        gameController.get.game = backup.get._1
        gameController.get.state = backup.get._2

    override def execute: Boolean =
        val director = injector.getInstance(classOf[DirectorInterface])
        director.copyGameState(gameController.get.game)

        if !name.trim.equals("") then
            val player = injector.getInstance(classOf[PlayerInterface])
            director.getBuilder.setPlayers(director.getBuilder.getPlayers :+ player.setName(name))
        else
            val player = injector.getInstance(classOf[PlayerInterface])
            director.getBuilder.setPlayers(director.getBuilder.getPlayers :+ player.setName(s"P${director.getBuilder.getPlayers.size + 1}"))

        if director.getBuilder.getPlayers.size == director.getBuilder.getPlayerNumber then
            director.getBuilder.setPlayers(gameController.get.dealNewRound(director.getBuilder.getCopy))
            gameController.get.changeState(SetMaxScoreState(gameController.get))

        gameController.get.game = director.getBuilder.getGame
        true

class SetMaxScoreCommand(var gameController: Option[Controller] = None, var backup: Option[(GameInterface, State)] = None, index: Option[Int]) extends Command:
    override def setup(newController:Controller):Unit = gameController = Some(newController)
    override def storeBackup: Unit = backup = Some((gameController.get.game,gameController.get.state))

    override def undoStep =
        gameController.get.game = backup.get._1
        gameController.get.state = backup.get._2

    override def execute: Boolean =
        val director = injector.getInstance(classOf[DirectorInterface])
        director.copyGameState(gameController.get.game)

        if index.exists(intInput => intInput >= 1 && intInput <= 400) then
            gameController.get.changeState(GamePlayState(gameController.get))
            director.getBuilder.setMaxScore(Some(index.get))
        else
            gameController.get.changeState(GamePlayState(gameController.get))
            director.getBuilder.setMaxScore(Some(100))
        director.getBuilder.setCurrentPlayerIndex(Some(gameController.get.getNextPlayerIndex(director.getBuilder.getCopy)))
        gameController.get.game = director.getBuilder.getGame
        true

class SetSortingRankCommand(var gameController: Option[Controller] = None, var backup: Option[(GameInterface, State)] = None) extends Command:
    override def setup(newController:Controller):Unit = gameController = Some(newController)
    override def storeBackup: Unit = backup = Some((gameController.get.game,gameController.get.state))

    override def undoStep =
        gameController.get.game = backup.get._1
        gameController.get.state = backup.get._2

    override def execute: Boolean =
        val director = injector.getInstance(classOf[DirectorInterface])
        director.copyGameState(gameController.get.game)
        gameController.get.setStrategy(SortByRankStrategy())
        director.getBuilder.setLastPlayedCard(Left("Cards sorted by rank"))
        gameController.get.game = director.getBuilder.getGame
        false

class SetSortingSuitCommand(var gameController: Option[Controller] = None, var backup: Option[(GameInterface, State)] = None) extends Command:
    override def setup(newController:Controller):Unit = gameController = Some(newController)
    override def storeBackup: Unit = backup = Some((gameController.get.game,gameController.get.state))

    override def undoStep =
        gameController.get.game = backup.get._1
        gameController.get.state = backup.get._2

    override def execute: Boolean =
        val director = injector.getInstance(classOf[DirectorInterface])
        director.copyGameState(gameController.get.game)
        gameController.get.setStrategy(SortBySuitStrategy())
        director.getBuilder.setLastPlayedCard(Left("Cards sorted by suit"))
        gameController.get.game = director.getBuilder.getGame
        false

class NewCommand(var gameController: Option[Controller] = None, var backup: Option[(GameInterface, State)] = None) extends Command:
    override def setup(newController:Controller):Unit = gameController = Some(newController)
    override def storeBackup: Unit = backup = Some((gameController.get.game,gameController.get.state))

    override def undoStep =
        gameController.get.game = backup.get._1
        gameController.get.state = backup.get._2

    override def execute: Boolean =
        gameController.get.changeState(GetPlayerNumberState(gameController.get))
        true

class AgainCommand(var gameController: Option[Controller] = None, var backup: Option[(GameInterface, State)] = None) extends Command:
    override def setup(newController:Controller):Unit = gameController = Some(newController)
    override def storeBackup: Unit = backup = Some((gameController.get.game,gameController.get.state))

    override def undoStep =
        gameController.get.game = backup.get._1
        gameController.get.state = backup.get._2

    override def execute: Boolean =
        val director = injector.getInstance(classOf[DirectorInterface])
        director.copyGameState(gameController.get.getGame)
        director.resetForNextGame
        director.getBuilder.setPlayers(gameController.get.dealNewRound(director.getBuilder.getCopy))
        gameController.get.changeState(GamePlayState(gameController.get))
        director.getBuilder.setCurrentPlayerIndex(Some(gameController.get.getNextPlayerIndex(director.getBuilder.getCopy)))
        gameController.get.game = director.getBuilder.getGame
        true

class QuitCommand(var gameController: Option[Controller] = None, var backup: Option[(GameInterface, State)] = None) extends Command:
    override def setup(newController:Controller):Unit = gameController = Some(newController)
    override def storeBackup: Unit = backup = Some((gameController.get.game,gameController.get.state))

    override def undoStep =
        gameController.get.game = backup.get._1
        gameController.get.state = backup.get._2

    override def execute: Boolean =
        gameController.get.changeState(MainScreenState(gameController.get))
        true

class ExitCommand(var gameController: Option[Controller] = None, var backup: Option[(GameInterface, State)] = None) extends Command:
    override def setup(newController:Controller):Unit = gameController = Some(newController)
    override def storeBackup: Unit = backup = Some((gameController.get.game,gameController.get.state))

    override def undoStep =
        gameController.get.game = backup.get._1
        gameController.get.state = backup.get._2

    override def execute: Boolean =
        val director = injector.getInstance(classOf[DirectorInterface])
        director.copyGameState(gameController.get.game)
        director.getBuilder.setKeepProcessRunning(false)
        gameController.get.game = director.getBuilder.getGame
        true

class RulesCommand(var gameController: Option[Controller] = None, var backup: Option[(GameInterface, State)] = None) extends Command:
    override def setup(newController:Controller):Unit = gameController = Some(newController)
    override def storeBackup: Unit = backup = Some((gameController.get.game,gameController.get.state))

    override def undoStep =
        gameController.get.game = backup.get._1
        gameController.get.state = backup.get._2

    override def execute: Boolean =
        gameController.get.changeState(RulesScreenState(gameController.get))
        false

class BackCommand(var gameController: Option[Controller] = None, var backup: Option[(GameInterface, State)] = None) extends Command:
    override def setup(newController:Controller):Unit = gameController = Some(newController)
    override def storeBackup: Unit = backup = Some((gameController.get.game,gameController.get.state))

    override def undoStep =
        gameController.get.game = backup.get._1
        gameController.get.state = backup.get._2

    override def execute: Boolean =
        if gameController.get.game.getPlayers.equals(Vector.empty) then gameController.get.changeState(MainScreenState(gameController.get))
        else gameController.get.changeState(GamePlayState(gameController.get))
        false

class ContinueCommand(var gameController: Option[Controller] = None, var backup: Option[(GameInterface, State)] = None) extends Command:
    override def setup(newController:Controller):Unit = gameController = Some(newController)
    override def storeBackup: Unit = backup = Some((gameController.get.game,gameController.get.state))

    override def undoStep =
        gameController.get.game = backup.get._1
        gameController.get.state = backup.get._2

    override def execute: Boolean =
        val director = injector.getInstance(classOf[DirectorInterface])
        director.copyGameState(gameController.get.game)
        director.getBuilder.setPlayers(gameController.get.dealNewRound(director.getBuilder.getCopy))
        director.getBuilder.setTrickCards(List.empty)
        director.getBuilder.setCurrentWinnerAndHighestCard(None, None)
        director.getBuilder.setFirstCard(true)
        director.getBuilder.setStartWithHearts(false)
        director.getBuilder.setLastPlayedCard(Left(""))
        gameController.get.changeState(GamePlayState(gameController.get))
        director.getBuilder.setCurrentPlayerIndex(Some(gameController.get.getNextPlayerIndex(director.getBuilder.getCopy)))
        gameController.get.game = director.getBuilder.getGame
        true


class PlayCardCommand(var gameController: Option[Controller] = None, var backup: Option[(GameInterface, State)] = None, index: Option[Int]) extends Command:
    override def setup(newController:Controller):Unit = gameController = Some(newController)
    override def storeBackup: Unit = backup = Some((gameController.get.game,gameController.get.state))

    override def undoStep =
        gameController.get.game = backup.get._1
        gameController.get.state = backup.get._2

    override def execute: Boolean =
            val director = injector.getInstance(classOf[DirectorInterface])
            director.copyGameState(gameController.get.getGame)
            if director.getBuilder.getTrickSize == director.getBuilder.getPlayerNumber then
                director.getBuilder.setTrickCards(List())
                director.getBuilder.setCurrentWinnerAndHighestCard(None, None)
            val cOR: CoRInterface = injector.getInstance((classOf[CoRInterface]))
            val result = cOR.validateMove(director.getBuilder.getCopy, gameController.get.getPlayerHand, index)
            result match
                case Left(_) => director.getBuilder.setLastPlayedCard(result)
                case Right(cardToPlay) =>
                    director.moveCard(cardToPlay)

                    if director.getBuilder.getTrickSize == director.getBuilder.getPlayerNumber then
                        director.getBuilder.updatePlayer(
                            director.getBuilder.getCurrentWinnerIndex.get,
                            director.getBuilder.getPlayers(director.getBuilder.getCurrentWinnerIndex.get).addWonCards(director.getBuilder.getTrickCards)
                        )

                    director.getBuilder.setLastPlayedCard(result)
                    director.getBuilder.setCurrentPlayerIndex(Some(gameController.get.getNextPlayerIndex(director.getBuilder.getCopy)))

                    if director.getBuilder.getPlayers.forall(_.getHand.size == 0) then
                        director.getBuilder.setPlayers(gameController.get.getAddPointsToPlayers(director.getBuilder.getCopy))
                        if !gameController.get.checkGameOver(director.getBuilder.getCopy) then gameController.get.changeState(ShowScoreState(gameController.get))
                        else gameController.get.changeState(GameOverState(gameController.get))


            gameController.get.game = director.getBuilder.getGame
            true


