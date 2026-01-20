package de.htwg.se.Hearts.model.gameComponent

import de.htwg.se.Hearts.model.gameComponent.gameBase._
import play.api.libs.json._
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
    def getErrorOrLastCardPlayed: Either[String, CardInterface]
    def getCurrentPlayerIndex: Option[Int]
    def gameFromXML(gameNode: Node): GameInterface
    def optCardToXml(opt: Option[CardInterface]): Elem
    def eitherCardToXml(e: Either[String, CardInterface]): Elem

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
    def setErrorOrLastPlayedCard(card: Either[String, CardInterface]): Unit
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

    def fileNameForSave: String = this match
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

    def stringForSave: String =
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

object CardInterface:
    given Writes[CardInterface] = Writes { c =>
        Json.obj(
            "rank" -> c.getRank.stringForSave,
            "suit" -> c.getSuit.fileNameForSave
        )
    }

    given Reads[CardInterface] = Reads { js =>
        for
            rankStr <- (js \ "rank").validate[String]
            suitStr <- (js \ "suit").validate[String]
        yield Card(Rank.valueOf(rankStr), Suit.valueOf(suitStr))
    }

object PlayerInterface:
    given Writes[PlayerInterface] = Writes { p =>
        Json.obj(
            "name"     -> p.getName,
            "hand"     -> Json.toJson(p.getHand),
            "wonCards" -> Json.toJson(p.getWonCards),
            "points"   -> p.getPoints
        )
    }

    given Reads[PlayerInterface] = Reads { js =>
        for
            name     <- (js \ "name").validate[String]
            hand     <- (js \ "hand").validate[List[CardInterface]]
            wonCards <- (js \ "wonCards").validate[List[CardInterface]]
            points   <- (js \ "points").validate[Int]
        yield Player(name, hand, wonCards, points)
    }

object GameInterface:

    given Writes[Either[String, CardInterface]] = Writes {
        case Left(msg)   => Json.obj("left" -> msg)
        case Right(card) => Json.obj("right" -> Json.toJson(card))
    }

    given Reads[Either[String, CardInterface]] = Reads { js =>
        (js \ "right").validateOpt[CardInterface] match
        case JsSuccess(Some(c), _) => JsSuccess(Right(c))
        case _ =>
            (js \ "left").validateOpt[String] match
            case JsSuccess(Some(msg), _) => JsSuccess(Left(msg))
            case _                       => JsSuccess(Left("No Card"))
    }

    given Writes[GameInterface] = Writes { g =>
        Json.obj(
            "playerNumber"       -> g.getPlayerNumber,
            "startWithHearts"    -> g.getStartWithHearts,
            "keepProcessRunning" -> g.getKeepProcessRunning,
            "firstCard"          -> g.getFirstCard,
            "players"            -> Json.toJson(g.getPlayers),
            "maxScore"           -> g.getMaxScore,
            "currentPlayerIndex" -> g.getCurrentPlayerIndex,
            "trickCards"         -> Json.toJson(g.getTrickCards),
            "highestCard"        -> g.getHighestCard.map(Json.toJson(_)),
            "currentWinnerIndex" -> g.getCurrentWinnerIndex,
            "errorOrlastCardPlayed"     -> Json.toJson(g.getErrorOrLastCardPlayed)
        )
    }

    given Reads[GameInterface] = Reads { js =>
        for
            playerNumber       <- (js \ "playerNumber").validateOpt[Int]
            startWithHearts    <- (js \ "startWithHearts").validate[Boolean]
            keepProcessRunning <- (js \ "keepProcessRunning").validate[Boolean]
            firstCard          <- (js \ "firstCard").validate[Boolean]
            players            <- (js \ "players").validate[Vector[PlayerInterface]]
            maxScore           <- (js \ "maxScore").validateOpt[Int]
            currentPlayerIndex <- (js \ "currentPlayerIndex").validateOpt[Int]
            trickCards         <- (js \ "trickCards").validate[List[CardInterface]]
            highestCard        <- (js \ "highestCard").validateOpt[CardInterface]
            currentWinnerIndex <- (js \ "currentWinnerIndex").validateOpt[Int]
            errorOrlastCardPlayed     <- (js \ "errorOrlastCardPlayed").validate[Either[String, CardInterface]]
        yield Game(
            playerNumber = playerNumber,
            startWithHearts = startWithHearts,
            keepProcessRunning = keepProcessRunning,
            firstCard = firstCard,
            players = players,
            maxScore = maxScore,
            currentPlayerIndex = currentPlayerIndex,
            trickCards = trickCards,
            highestCard = highestCard,
            currentWinnerIndex = currentWinnerIndex,
            errorOrlastCardPlayed = errorOrlastCardPlayed
        )
    }


