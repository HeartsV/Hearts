package de.htwg.se.Hearts.controller.controllerComponent
import de.htwg.se.Hearts.util._
import de.htwg.se.Hearts.model._
import de.htwg.se.Hearts.controller._
import de.htwg.se.Hearts.controller.controllerComponent.controllerBase.Strategy
import de.htwg.se.Hearts.model.gameComponent.gameBase.Card
import de.htwg.se.Hearts.model.gameComponent.gameBase.Game
import de.htwg.se.Hearts.model.gameComponent.gameBase.Player
import de.htwg.se.Hearts.model.gameComponent.GameInterface

trait ControllerInterface extends Observable:

    def add(s:Observer): Unit
    def passStateString: String
    def processInput(input: String): GameInterface
    def cardsPathList(list: List[Card]): List[String]
    def getPlayersWithPoints: List[(String, Int)]
    def rankPlayers(players: List[(String, Int)]): List[(Int, String, Int)]
    def getCurrentPlayerName: String
    def getPlayerSize: Int
    def getTrickCards: List[Card]
    def passCurrentPlayer: Player
    def getSortingStrategy: Strategy
    def getKeepProcessRunning: Boolean
    def getPlayerHand: List[Card]
    def dealNewRound(game: GameInterface): Vector[Player]
    def getLastCardPlayed: Either[String,Card]
    def setGame(game: GameInterface):Unit
    def getGame: GameInterface



