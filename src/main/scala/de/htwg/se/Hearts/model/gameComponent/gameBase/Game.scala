package de.htwg.se.Hearts.model.gameComponent.gameBase

import de.htwg.se.Hearts.model.gameComponent.GameInterface
import com.google.inject.Inject
import de.htwg.se.Hearts.model.gameComponent.CardInterface
import de.htwg.se.Hearts.model.gameComponent.PlayerInterface
import de.htwg.se.Hearts.model.gameComponent.Rank
import de.htwg.se.Hearts.model.gameComponent.Suit
import scala.xml._

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
    errorOrlastCardPlayed: Either[String, CardInterface] = Left("No Card")) extends GameInterface:

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
    def getErrorOrLastCardPlayed: Either[String, CardInterface] = errorOrlastCardPlayed
    def getCurrentPlayerIndex: Option[Int] = currentPlayerIndex


    def playerFromXML(node: scala.xml.Node): Player =
        Player(
            (node \ "name").text,
            (node \ "hand" \ "card").map(n => cardFromXML(n)).toList,
            (node \ "wonCards" \ "card").map(n => cardFromXML(n)).toList,
            (node \ "points").text.toInt
        )
    def cardFromXML(node: scala.xml.Node): Card = Card(Rank.valueOf((node \ "rank").text),Suit.valueOf((node \ "suit").text))


    def eitherCardFromXML(parent: Node, label: String): Either[String, Card] =
    (parent \ label).headOption match
        case None => Left(s"$label fehlt")

        case Some(container) =>
            if (container \ "right").nonEmpty then
                val cardNodeOpt = (container \ "right" \ "card").headOption
                cardNodeOpt match
                case Some(cn) => Right(cardFromXML(cn))
                case None     => Left("right whitout <card>")
            else
                val msg = (container \ "left" \ "message").text.stripTrailing() + "\n"
                Left(if msg.nonEmpty then msg else "No Card\n")
    def gameFromXML(gameNode: Node): GameInterface =

        def optInt(label: String): Option[Int] = (gameNode \ label).headOption.map(_.text.trim).filter(_.nonEmpty).map(_.toInt)

        def reqBool(label: String, default: Boolean): Boolean =
            (gameNode \ label).headOption.map(_.text.trim).filter(_.nonEmpty) match
            case Some(v) => v.toBoolean
            case None    => default

        val players: Vector[Player] = (gameNode \ "players" \ "player").map(playerFromXML).toVector

        val trickCards: List[Card] = (gameNode \ "trickCards" \ "card").map(cardFromXML).toList

        val highestCard: Option[Card] = (gameNode \ "highestCard" \ "card").headOption.map(cardFromXML)

        val errorOrlastCardPlayed: Either[String, Card] =
            eitherCardFromXML(gameNode, "errorOrlastCardPlayed")


        Game(
            playerNumber        = optInt("playNumber"),
            startWithHearts     = reqBool("startWithHearts", default = false),
            keepProcessRunning  = reqBool("keepProcessRunning", default = true),
            firstCard           = reqBool("firstCard", default = true),
            players             = players,
            maxScore            = optInt("maxScore"),
            currentPlayerIndex  = optInt("currentPlayerIndex"),
            trickCards          = trickCards,
            highestCard         = highestCard,
            currentWinnerIndex  = optInt("currentWinnerIndex"),
            errorOrlastCardPlayed      = errorOrlastCardPlayed
        )

    def optCardToXml(opt: Option[CardInterface]): Elem =
        opt match {
            case Some(card) => card.cardToXML
            case None       => Elem(null, "card", scala.xml.Null, TopScope, minimizeEmpty = false)
        }

    def eitherCardToXml(e: Either[String, CardInterface]): Elem =
        val a: Elem = e match
            case Right(card) => <right>{ card.cardToXML }</right>
            case Left(msg) => {<left><message>{ msg }</message></left>}
        a







