package de.htwg.se.Hearts.model.gameComponent

import de.htwg.se.Hearts.model.gameComponent.gameBase._


trait GameInterface():
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
    def getCurrentPlayerIndex: Option[Int]

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
    def setLastPlayedCard(card: Either[String, Card]): Unit
    def getGame: Game

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

trait CoRInterface:
	def validateMove(game: Game, playerHand: List[Card], index: Int): Either[String, Card]

trait DeckManagerInterface:
	def createDeck: List[Card]
	def shuffle(deck: List[Card]): List[Card]
	def deal(deck: List[Card], game: Game): Vector[Player]

enum Suit extends Ordered[Suit]:

    case Hearts, Spades, Diamonds, Clubs

    def fileName: String = this match
        case Hearts     => "hearts"
        case Spades     => "spades"
        case Diamonds   => "diamonds"
        case Clubs      => "clubs"

    def compare(that: Suit): Int =
        this.ordinal.compare(that.asInstanceOf[Suit].ordinal)

    override def toString: String = this match
        case Hearts     => "\u2665"
        case Spades     => "\u2660"
        case Diamonds   => "\u2666"
        case Clubs      => "\u2663"

enum Rank(val value: Int) extends Ordered[Rank]:

    case Two   extends Rank(1)
    case Three extends Rank(2)
    case Four  extends Rank(3)
    case Five  extends Rank(4)
    case Six   extends Rank(5)
    case Seven extends Rank(6)
    case Eight extends Rank(7)
    case Nine  extends Rank(8)
    case Ten   extends Rank(9)
    case Jack  extends Rank(10)
    case Queen extends Rank(11)
    case King  extends Rank(12)
    case Ace   extends Rank(13)

    def compare(that: Rank): Int =
        this.value.compare(that.value)

    override def toString: String =
        this match
            case Two   => "2"
            case Three => "3"
            case Four  => "4"
            case Five  => "5"
            case Six   => "6"
            case Seven => "7"
            case Eight => "8"
            case Nine  => "9"
            case Ten   => "10"
            case Jack  => "J"
            case Queen => "Q"
            case King  => "K"
            case Ace   => "A"
