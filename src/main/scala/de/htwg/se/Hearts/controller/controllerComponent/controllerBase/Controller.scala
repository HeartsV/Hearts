package de.htwg.se.Hearts.controller.controllerComponent.controllerBase

import de.htwg.se.Hearts.util._
import de.htwg.se.Hearts.controller.controllerComponent.ControllerInterface
import de.htwg.se.Hearts.model.gameComponent.DeckManagerInterface
import de.htwg.se.Hearts.controller.leaderBoardComponent.LeaderBoardInterface
import de.htwg.se.Hearts.controller.playerTurnComponent.PlayerTurnInterface
import de.htwg.se.Hearts.controller.scoringComponent.ScoringInterface
import de.htwg.se.Hearts.model.gameComponent.GameInterface
import de.htwg.se.Hearts.model.gameComponent.BuilderInterface
import com.google.inject.Inject
import com.google.inject.{Injector, Guice}
import de.htwg.se.Hearts.HeartsModule
import de.htwg.se.Hearts.model.gameComponent.CardInterface
import de.htwg.se.Hearts.model.gameComponent.PlayerInterface
import de.htwg.se.Hearts.model.fileIOComponent.FileIOInterface
import de.htwg.se.Hearts.model.gameComponent.DirectorInterface


class Controller(var game: GameInterface) extends Observable with ControllerInterface:

    var msg = ""
    var state: State = MainScreenState(this)
    var sortingStrategy: Strategy = SortByRankStrategy()
    val injector: Injector = Guice.createInjector(HeartsModule())
    val fileIo = injector.getInstance(classOf[FileIOInterface])
    val turnService = injector.getInstance(classOf[PlayerTurnInterface])
    val deckmanger = injector.getInstance(classOf[DeckManagerInterface])
    val scoringService = injector.getInstance(classOf[ScoringInterface])
    val leaderboardService =  injector.getInstance(classOf[LeaderBoardInterface])

    val history = CommandHistory()
    def processInput(next: Command): Unit =
        next.setup(this)
        next.storeBackup
        if next.execute then
            history.push(next)
            history.clearRedoStack
        notifyObservers

    def undo:Unit =
        val command = history.pop
        if(command != None)
            command.get.undoStep
            history.redoPush(command.get)

    def redo:Unit =
        val command = history.redoPop
        if(command != None)
            command.get.execute
            history.push(command.get)


    def changeState(newState: State): Unit = state = newState
    def getGame:GameInterface = game
    def setGame(newGame: GameInterface): Unit = game = newGame
    def pngUrl(c: CardInterface): String = s"/cards/${c.pngName}"
    def cardsPathList(list: List[CardInterface]): List[String] = list.map(pngUrl)
    def getPlayersWithPoints: List[(String, Int)] = leaderboardService.playersWithPoints(game.getPlayers)
    def rankPlayers(players: List[(String, Int)]): List[(Int, String, Int)] = leaderboardService.rankPlayers(players)
    def getCurrentPlayerName: String = game.getCurrentPlayer.get.getName
    def checkGameOver(game: GameInterface): Boolean =
        val maxScorePoints = game.getPlayers.map(p => p -> p.getPoints).toMap
        maxScorePoints.exists { case (_, point) => point >= game.getMaxScore.get }
    def getKeepProcessRunning: Boolean = game.getKeepProcessRunning
    def getPlayerSize: Int = game.getPlayers.size
    def getTrickCards: List[CardInterface] = game.getTrickCards
    def getPlayerHand: List[CardInterface] = sortingStrategy.execute(game.getPlayers(game.getCurrentPlayerIndex.get))
    def getSortingStrategy: Strategy = sortingStrategy
    def passCurrentPlayer: PlayerInterface = game.getCurrentPlayer.get
    def passStateString: String = state.getStateString
    def setStrategy(strategy: Strategy): Unit = this.sortingStrategy = strategy
    def dealNewRound(bgame: GameInterface): Vector[PlayerInterface] = deckmanger.deal(deckmanger.shuffle(deckmanger.createDeck), bgame)
    def addPointsToPlayers: Vector[PlayerInterface] = scoringService.addPointsToPlayers(game)
    def getErrorOrLastCardPlayed: Either[String,CardInterface] = game.getErrorOrLastCardPlayed
    def getState: State = state
    def getNextPlayerIndex(bgame: GameInterface): Int = turnService.nextPlayerIndex(bgame)
    def getAddPointsToPlayers(bgame: GameInterface): Vector[PlayerInterface] = scoringService.addPointsToPlayers(bgame)
    def getFileIO: FileIOInterface = fileIo
    def errorOrLastCardPlayedToString: String =
        getErrorOrLastCardPlayed match
            case Left("No Card") | Left("No Card\n") => "" + "\n"
            case Left(error) => "\n" + error + "\n"
            case Right(card) => card.toString + " played" + "\n"



