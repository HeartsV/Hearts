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
    currentWinner: Option[Player] = None,
    firstPlayer: Option[Player] = None,
    lastCardPlayed: Either[String, Card] = Left("No Card")):

  def addPlayer(newPlayer: Player): Game =copy(players = players :+ newPlayer)

  def addCard(newCard: Card): Game = copy(trickCards = trickCards :+ newCard)

  def clearTrick: Game = copy(trickCards = List()) // fehlt hier nicht noch higest card und so oder machen wir das über set TRick?

  def setTrick(player: Player, newCard: Card): Game = copy(highestCard = Some(newCard), currentWinner = Some(player))

  def setFirstPlayer(player: Player): Game = copy(firstPlayer = Some(player))

  def setMaxScore(score: Int): Game = copy(maxScore = Some(score))

  def setCurrentPlayerIndex(index: Int): Game = copy(currentPlayerIndex = Some(index))

  def getCurrentPlayer: Option[Player] = currentPlayerIndex.flatMap(players.lift)

  def setStartWithHearts(a: Boolean): Game = copy(startWithHearts = a)

  def setFirstCard(a: Boolean): Game = copy(firstCard = a)

  def updatePlayer(index: Int, updatedPlayer: Player): Game = copy(players = players.updated(index, updatedPlayer))
