package de.htwg.se.Hearts.model

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
    firstPlayer: Option[Player] = None,
    lastCardPlayed: Either[String, Card] = Left("No Card")):

  def getCurrentPlayer: Option[Player] = currentPlayerIndex.flatMap(players.lift)

