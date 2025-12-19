package de.htwg.se.Hearts.model.gameComponent.gameBase

import de.htwg.se.Hearts.model.gameComponent.GameInterface

case class Game(
    playerNumber: Option[Int] = None,
    startWithHearts: Boolean = false,
    keepProcessRunning: Boolean = true,
    firstCard: Boolean = true,
    players: Vector[Player] = Vector.empty,
    maxScore: Option[Int] = None,
    currentPlayerIndex: Option[Int] = None,
    trickCards: List[Card] = Nil,
    highestCard: Option[Card] = None,
    currentWinnerIndex: Option[Int] = None,
    lastCardPlayed: Either[String, Card] = Left("No Card")) extends GameInterface:

    def getPlayerNumber: Option[Int] = playerNumber
    def getStartWithHearts: Boolean = startWithHearts
    def getKeepProcessRunning: Boolean = keepProcessRunning
    def getFirstCard: Boolean = firstCard
    def getPlayers: Vector[Player] = players
    def getMaxScore: Option[Int] = maxScore
    def getCurrentPlayer: Option[Player] = currentPlayerIndex.flatMap(players.lift)
    def getTrickCards: List[Card] = trickCards
    def getHighestCard: Option[Card] = highestCard
    def getCurrentWinnerIndex: Option[Int] = currentWinnerIndex
    def getLastCardPlayed: Either[String, Card] = lastCardPlayed


