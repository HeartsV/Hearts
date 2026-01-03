package de.htwg.se.Hearts.controller.controllerComponent.controllerBase

import de.htwg.se.Hearts.util._
import de.htwg.se.Hearts.controller.controllerComponent._
import de.htwg.se.Hearts.model.gameComponent.DeckManagerInterface
import de.htwg.se.Hearts.controller.leaderBoardComponent._
import de.htwg.se.Hearts.controller.playerTurnComponent._
import de.htwg.se.Hearts.controller.scoringComponent._
import de.htwg.se.Hearts.model.gameComponent.gameBase.*
import de.htwg.se.Hearts.controller.leaderBoardComponent.leaderBoardBase._
import de.htwg.se.Hearts.controller.playerTurnComponent.playerTurnBase._
import de.htwg.se.Hearts.controller.scoringComponent.scoringBase._
import de.htwg.se.Hearts.model.gameComponent.gameBase.GameBuilder
import de.htwg.se.Hearts.model.gameComponent.gameBase.DeckManager
import de.htwg.se.Hearts.model.gameComponent.gameBase.Card
import de.htwg.se.Hearts.model.gameComponent.gameBase.Game
import de.htwg.se.Hearts.model.gameComponent.gameBase.Player
import de.htwg.se.Hearts.model.gameComponent.GameInterface
import java.lang.Thread.Builder
import de.htwg.se.Hearts.model.gameComponent.BuilderInterface

class Controller(var game: GameInterface) extends Observable with ControllerInterface:

    var state: State = MainScreenState(this)
    var sortingStrategy: Strategy = SortByRankStrategy()

    val deckmanger: DeckManagerInterface = DeckManager()
    val scoringService: ScoringInterface = HeartsScoring()
    val turnService: PlayerTurnInterface = PlayerTurn()
    val leaderboardService: LeaderBoardInterface = LeaderBoard()

    def processInput(input: String): GameInterface =
        game = state.processInput(input)
        notifyObservers
        game

    def changeState(newState: State): Unit = state = newState

    def getGame:GameInterface = game

    def setGame(newGame: GameInterface): Unit = game = newGame

    def pngUrl(c: Card): String = s"/cards/${c.pngName}"
    def cardsPathList(list: List[Card]): List[String] = list.map(pngUrl)

    def getPlayersWithPoints: List[(String, Int)] =
        leaderboardService.playersWithPoints(game.getPlayers)

    def rankPlayers(players: List[(String, Int)]): List[(Int, String, Int)] =
        leaderboardService.rankPlayers(players)

    def getCurrentPlayerName: String = game.getCurrentPlayer.get.name

    def checkGameOver: Boolean =
        val maxScorePoints = game.getPlayers.map(p => p -> p.points).toMap
        maxScorePoints.exists { case (_, point) => point >= game.getMaxScore.get }

    def getKeepProcessRunning: Boolean = game.getKeepProcessRunning
    def getPlayerSize: Int = game.getPlayers.size
    def getTrickCards: List[Card] = game.getTrickCards
    def getPlayerHand: List[Card] =
        sortingStrategy.execute(game.getPlayers(game.getCurrentPlayerIndex.get))
    def getSortingStrategy: Strategy = sortingStrategy
    def passCurrentPlayer: Player = game.getCurrentPlayer.get
    def passStateString: String = state.getStateString

    def setStrategy(strategy: Strategy): Unit =
        this.sortingStrategy = strategy

    def executeStrategy: Vector[Player] =
        game.getPlayers.map(p => p.copy(hand = sortingStrategy.execute(p)))

    def dealNewRound(game: GameInterface): Vector[Player] =
        deckmanger.deal(deckmanger.shuffle(deckmanger.createDeck), game)

    def updateCurrentPlayer: Int = turnService.nextPlayerIndex(game)
    def updateCurrentWinner(newWinner: (Int, Card), builderGame: Game): (Option[Int], Option[Card]) =
        turnService.updateCurrentWinner(newWinner, builderGame)

    def addPointsToPlayers: Vector[Player] = scoringService.addPointsToPlayers(game)

    def getLastCardPlayed: Either[String,Card] = game.getLastCardPlayed

