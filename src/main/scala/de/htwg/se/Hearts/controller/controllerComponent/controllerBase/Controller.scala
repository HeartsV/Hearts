package de.htwg.se.Hearts.controller.controllerComponent.controllerBase

import de.htwg.se.Hearts.util._
import de.htwg.se.Hearts.model._
import de.htwg.se.Hearts.controller.controllerComponent._
import de.htwg.se.Hearts.controller.deckmanagerComponent._
import de.htwg.se.Hearts.controller.LeaderBoardComponent._
import de.htwg.se.Hearts.controller.PlayerTurnComponent._
import de.htwg.se.Hearts.controller.scoringComponent._

import de.htwg.se.Hearts.controller.deckmanagerComponent.deckmanagerBase._
import de.htwg.se.Hearts.controller.LeaderBoardComponent.LeaderBoardBase._
import de.htwg.se.Hearts.controller.PlayerTurnComponent.PlayerTurnBase._
import de.htwg.se.Hearts.controller.scoringComponent.scoringBase._

class Controller(var game: Game) extends Observable with ControllerInterface:

  var state: State = MainScreenState(this)
  var sortingStrategy: Strategy = SortByRankStrategy()

  val deckmanger: DeckmanagerInterface = Deckmanager()
  val scoringService: ScoringInterface = HeartsScoring()
  val turnService: PlayerTurnInterface = PlayerTurn()
  val leaderboardService: LeaderBoardInterface = LeaderBoard()

  def processInput(input: String): Game =
    game = state.processInput(input)
    notifyObservers
    game

  def changeState(newState: State): Unit = state = newState

  def pngUrl(c: Card): String = s"/cards/${c.pngName}"
  def cardsPathList(list: List[Card]): List[String] = list.map(pngUrl)

  def getPlayersWithPoints: List[(String, Int)] =
    leaderboardService.playersWithPoints(game.players)

  def rankPlayers(players: List[(String, Int)]): List[(Int, String, Int)] =
    leaderboardService.rankPlayers(players)

  def getCurrentPlayerName: String = game.getCurrentPlayer.get.name

  def checkGameOver: Boolean =
    val maxScorePoints = game.players.map(p => p -> p.points).toMap
    maxScorePoints.exists { case (_, point) => point >= game.maxScore.get }

  def getKeepProcessRunning: Boolean = game.keepProcessRunning
  def getPlayerSize: Int = game.playerNumber.get
  def getTrickCards: List[Card] = game.trickCards
  def getPlayerHand: List[Card] = game.players(game.currentPlayerIndex.get).hand
  def getSortingStrategy: Strategy = sortingStrategy
  def passCurrentPlayer: Player = game.getCurrentPlayer.get
  def passStateString: String = state.getStateString

  def setStrategy(strategy: Strategy): Unit = this.sortingStrategy = strategy
  def executeStrategy: Vector[Player] =
    game.players.map(p => p.copy(hand = sortingStrategy.execute(p)))

  def dealNewRound(game: Game): Vector[Player] =
    deckmanger.deal(deckmanger.shuffle(deckmanger.createDeck), game)

  def updateCurrentPlayer: Int = turnService.nextPlayerIndex(game)
  def updateCurrentWinner(newWinner: (Int, Card), builderGame: Game): (Option[Int], Option[Card]) =
    turnService.updateCurrentWinner(newWinner, builderGame)

  def addPointsToPlayers: Vector[Player] = scoringService.addPointsToPlayers(game)

  def getLastCardPlayed: Either[String,Card] = game.lastCardPlayed

