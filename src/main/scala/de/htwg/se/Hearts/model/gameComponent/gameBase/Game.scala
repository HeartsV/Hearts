package de.htwg.se.Hearts.model.gameComponent.gameBase

import de.htwg.se.Hearts.model.gameComponent.GameInterface
import com.google.inject.Inject
import de.htwg.se.Hearts.model.gameComponent.CardInterface
import de.htwg.se.Hearts.model.gameComponent.PlayerInterface
import de.htwg.se.Hearts.model.gameComponent.Rank
import de.htwg.se.Hearts.model.gameComponent.Suit
import scala.xml.Node

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


    def gameFromXML(gameNode: Node): GameInterface =

        def optInt(label: String): Option[Int] = (gameNode \ label).headOption.map(_.text.trim).filter(_.nonEmpty).map(_.toInt)

        def reqBool(label: String, default: Boolean): Boolean =
            (gameNode \ label).headOption.map(_.text.trim).filter(_.nonEmpty) match
            case Some(v) => v.toBoolean
            case None    => default

        val players: Vector[Player] = (gameNode \ "players" \ "player").map(playerFromXML).toVector

        val trickCards: List[Card] = (gameNode \ "trickCards" \ "card").map(cardFromXML).toList

        val highestCard: Option[Card] = (gameNode \ "highestCard" \ "card").headOption.map(cardFromXML)

        val lastCardPlayed: Either[String, Card] =
            if ((gameNode \ "lastCardPlayed" \ "right").nonEmpty)
            Right(cardFromXML((gameNode \ "lastCardPlayed" \ "right" \ "card").head))
            else {
            val msg = (gameNode \ "lastCardPlayed" \ "left").text.trim
            Left(if (msg.nonEmpty) msg else "No Card")
            }

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
            lastCardPlayed      = lastCardPlayed
        )



    def playerFromXML(node: scala.xml.Node): Player =
        Player(
            (node \ "name").text,
            (node \ "hand" \ "card").map(n => cardFromXML(n)).toList,
            (node \ "wonCards" \ "card").map(n => cardFromXML(n)).toList,
            (node \ "points").text.toInt
        )

    def cardFromXML(node: scala.xml.Node): Card = Card(Rank.valueOf((node \ "rank").text),Suit.valueOf((node \ "suit").text))


