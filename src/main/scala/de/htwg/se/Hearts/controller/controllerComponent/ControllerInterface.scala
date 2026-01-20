package de.htwg.se.Hearts.controller.controllerComponent
import de.htwg.se.Hearts.util._
import de.htwg.se.Hearts.model._
import de.htwg.se.Hearts.controller._
import de.htwg.se.Hearts.controller.controllerComponent.controllerBase.Strategy
import de.htwg.se.Hearts.model.gameComponent.gameBase.Card
import de.htwg.se.Hearts.model.gameComponent.gameBase.Game
import de.htwg.se.Hearts.model.gameComponent.gameBase.Player
import de.htwg.se.Hearts.model.gameComponent.GameInterface
import de.htwg.se.Hearts.controller.controllerComponent.controllerBase.State
import de.htwg.se.Hearts.model.gameComponent.CardInterface
import de.htwg.se.Hearts.model.gameComponent.PlayerInterface
import de.htwg.se.Hearts.model.fileIOComponent.FileIOInterface

trait ControllerInterface extends Observable:

    def add(s:Observer): Unit
    def passStateString: String
    def processInput(next: Command): Unit
    def cardsPathList(list: List[CardInterface]): List[String]
    def getPlayersWithPoints: List[(String, Int)]
    def rankPlayers(players: List[(String, Int)]): List[(Int, String, Int)]
    def getCurrentPlayerName: String
    def getPlayerSize: Int
    def getTrickCards: List[CardInterface]
    def passCurrentPlayer: PlayerInterface
    def getSortingStrategy: Strategy
    def getKeepProcessRunning: Boolean
    def getPlayerHand: List[CardInterface]
    def dealNewRound(game: GameInterface): Vector[PlayerInterface]
    def getErrorOrLastCardPlayed: Either[String,CardInterface]
    def setGame(game: GameInterface):Unit
    def getGame: GameInterface
    def getState: State
    def changeState(newState:State): Unit
    def getFileIO: FileIOInterface
    def errorOrLastCardPlayedToString: String



