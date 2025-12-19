package de.htwg.se.Hearts.model.gameComponent

import de.htwg.se.Hearts.model.gameComponent.gameBase._

trait GameInterface:
	def getPlayerNumber: Option[Int]
	def getStartWithHearts: Boolean
	def getKeepProcessRunning: Boolean
	def getFirstCard: Boolean
	def getPlayers: Vector[Player]
	def getMaxScore: Option[Int]
	def getCurrentPlayer: Option[Player]
	def getTrickCards: List[Card]
	def getHighestCard: Option[Card]
	def getCurrentWinnerIndex: Option[Int]
	def getLastCardPlayed: Either[String, Card]

trait BuilderInterface:
	def reset: Unit
	def setPlayerNumber(playerNumber: Int): Unit
	def setStartWithHearts(swh: Boolean): Unit
	def setKeepProcessRunning(kpr: Boolean): Unit
	def setFirstCard(fc: Boolean): Unit
	def setPlayers(players: Vector[Player]): Unit
	def setMaxScore(maxScore: Int): Unit
	def setCurrentPlayerIndex(cpi: Int): Unit
	def setTrickCards(trick: List[Card]): Unit
	def setCurrentWinnerAndHighestCard(newWinner: (Option[Int], Option[Card])): Unit
	def setFirstPlayer(firstP: Player): Unit
	def setLastPlayedCard(card: Either[String, Card]): Unit

trait CardInterface:
	override def toString: String
	def compare(that: Card): Int
	def pngName: String
	def getRank: Rank
	def getSuit: Suit

trait PlayerInterface:
	def removeCard(card: Card): Player
	def addAllCards(cards: List[Card]): Player
	def addWonCards(cards: List[Card]): Player
	def addPoints(newPoints: Int): Player
