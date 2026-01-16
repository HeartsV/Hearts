package de.htwg.se.Hearts.model.gameComponent.gameBase

import de.htwg.se.Hearts.model.gameComponent.GameInterface
import com.google.inject.Inject
import de.htwg.se.Hearts.model.gameComponent.CardInterface
import de.htwg.se.Hearts.model.gameComponent.PlayerInterface

case class Game(
    playerNumber: Option[Int] = None,
    startWithHearts: Boolean = false,
    keepProcessRunning: Boolean = true,
    firstCard: Boolean = true,
    players: Vector[PlayerInterface] = Vector.empty,
    maxScore: Option[Int] = None,
    currentPlayerIndex: Option[Int] = None,
    trickCards: List[CardInterface] = Nil,
    highestCard: Option[CardInterface] = None,
    currentWinnerIndex: Option[Int] = None,
    lastCardPlayed: Either[String, CardInterface] = Left("No Card")) extends GameInterface:

    def getPlayerNumber: Option[Int] = playerNumber
    def getStartWithHearts: Boolean = startWithHearts
    def getKeepProcessRunning: Boolean = keepProcessRunning
    def getFirstCard: Boolean = firstCard
    def getPlayers: Vector[PlayerInterface] = players
    def getMaxScore: Option[Int] = maxScore
    def getCurrentPlayer: Option[PlayerInterface] = currentPlayerIndex.flatMap(players.lift)
    def getTrickCards: List[CardInterface] = trickCards
    def getHighestCard: Option[CardInterface] = highestCard
    def getCurrentWinnerIndex: Option[Int] = currentWinnerIndex
    def getLastCardPlayed: Either[String, CardInterface] = lastCardPlayed
    def getCurrentPlayerIndex: Option[Int] = currentPlayerIndex


