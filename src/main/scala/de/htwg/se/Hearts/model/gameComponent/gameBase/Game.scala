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
        Game(
            (gameNode \ "playNumber").headOption.map(_.text.toInt),
            (gameNode \ "startWithHearts").text.toBoolean,
            (gameNode \ "keepProcessRunning").text.toBoolean,
            (gameNode \ "firstCard").text.toBoolean,
            (gameNode \ "players" \ "player").map(n=>playerFromXML(n)).toVector,
            (gameNode \ "maxScore").headOption.map(_.text.toInt),
            (gameNode \ "currentPlayerIndex").headOption.map(_.text.toInt),
            (gameNode \ "trickCards" \ "card").map(n => cardFromXML(n)).toList,
            (gameNode \ "highestCard" \ "card").headOption.map(cardFromXML),
            (gameNode \ "currentWinnerIndex").headOption.map(_.text.toInt),
            if ((gameNode \ "lastCardPlayed" \ "right").nonEmpty)
            Right(cardFromXML((gameNode \ "lastCardPlayed" \ "right" \ "card").head))
            else
            Left((gameNode \ "lastCardPlayed" \ "left").text)
        )

    def playerFromXML(node: scala.xml.Node): Player =
        Player(
            (node \ "name").text,
            (node \ "hand" \ "card").map(n => cardFromXML(n)).toList,
            (node \ "wonCards" \ "card").map(n => cardFromXML(n)).toList,
            (node \ "points").text.toInt
        )

    def cardFromXML(node: scala.xml.Node): Card = Card(Rank.valueOf((node \ "rank").text),Suit.valueOf((node \ "suit").text))


