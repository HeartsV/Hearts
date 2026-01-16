package de.htwg.se.Hearts.model.gameComponent

import de.htwg.se.Hearts.model.gameComponent.gameBase._
import scala.xml.Elem
import scala.xml.Node

trait DirectorInterface():
    def resetForNextGame: Unit
    def copyGameState(gameState: GameInterface): Unit
    def moveCard(playedCard:CardInterface): Unit
    def getBuilder: BuilderInterface

trait GameInterface():
    def getPlayerNumber: Option[Int]
    def getStartWithHearts: Boolean
    def getKeepProcessRunning: Boolean
    def getFirstCard: Boolean
    def getPlayers: Vector[PlayerInterface]
    def getMaxScore: Option[Int]
    def getCurrentPlayer: Option[PlayerInterface]
    def getTrickCards: List[CardInterface]
    def getHighestCard: Option[CardInterface]
    def getCurrentWinnerIndex: Option[Int]
    def getLastCardPlayed: Either[String, CardInterface]
    def getCurrentPlayerIndex: Option[Int]
    def gameFromXML(gameNode: Node): GameInterface

trait BuilderInterface():
    def reset: Unit
    def setPlayerNumber(playerNumber: Option[Int]): Unit
    def setStartWithHearts(swh: Boolean): Unit
    def setKeepProcessRunning(kpr: Boolean): Unit
    def setFirstCard(fc: Boolean): Unit
    def setPlayers(players: Vector[PlayerInterface]): Unit
    def updatePlayer(index: Int, updatedPlayer: Player): Unit
    def setMaxScore(maxScore: Option[Int]): Unit
    def setCurrentPlayerIndex(cpi: Option[Int]): Unit
    def setTrickCards(trick: List[CardInterface]): Unit
    def addCard(card: CardInterface): Unit
    def setCurrentWinnerAndHighestCard(newWinner: (Option[Int], Option[CardInterface])): Unit
    def setLastPlayedCard(card: Either[String, CardInterface]): Unit
    def getGame: GameInterface
    def getCopy: GameInterface
    def getTrickSize: Int
    def getPlayerNumber: Int
    def getPlayers: Vector[PlayerInterface]
    def getCurrentPlayer: Option[PlayerInterface]
    def getCurrentPlayerIndex: Option[Int]
    def getFirstCard: Boolean
    def getTrickCards: List[CardInterface]
    def getCurrentWinnerIndex: Option[Int]
    def getStartWithHearts: Boolean
    def getHighestCard: Option[CardInterface]

trait CardInterface:
    override def toString: String
    def compare(that: Card): Int
    def pngName: String
    def getRank: Rank
    def getSuit: Suit
    def cardToXML: Elem


trait PlayerInterface:
    def getHand: List[CardInterface]
    def getPoints: Int
    def getName: String
    def getWonCards: List[CardInterface]
    def setName(name: String): Player
    def removeCard(card: CardInterface): Player
    def addAllCards(cards: List[CardInterface]): Player
    def addWonCards(cards: List[CardInterface]): Player
    def addPoints(newPoints: Int): Player
    def resetPoints(): PlayerInterface
    def playerToXML: Elem

trait CoRInterface:
    def validateMove(game: GameInterface, playerHand: List[CardInterface], index: Option[Int]): Either[String, CardInterface]

trait DeckManagerInterface:
    def createDeck: List[Card]
    def shuffle(deck: List[Card]): List[Card]
    def filterOneCardOut(deck: List[Card], game: GameInterface): List[Card]
    def deal(deck: List[Card], game: GameInterface): Vector[Player]

enum Suit extends Ordered[Suit]:

    case Hearts, Spades, Diamonds, Clubs

    def fileName: String = this match
        case Hearts     => "hearts"
        case Spades     => "spades"
        case Diamonds   => "diamonds"
        case Clubs      => "clubs"

    def fileNameForXML: String = this match
        case Hearts     => "Hearts"
        case Spades     => "Spades"
        case Diamonds   => "Diamonds"
        case Clubs      => "Clubs"

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
            
    def stringForXML: String =
        this match
            case Two   => "Two"
            case Three => "Three"
            case Four  => "Four"
            case Five  => "Five"
            case Six   => "Six"
            case Seven => "Seven"
            case Eight => "Eight"
            case Nine  => "Nine"
            case Ten   => "Ten"
            case Jack  => "Jack"
            case Queen => "Queen"
            case King  => "King"
            case Ace   => "Ace"

