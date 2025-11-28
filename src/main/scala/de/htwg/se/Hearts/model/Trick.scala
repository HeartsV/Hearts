package de.htwg.se.Hearts.model

import scala.collection.mutable.ListBuffer

case class Trick(
    cards: List[Card] = Nil,
    highestCard: Option[Card] = None,
    currentWinner: Option[Player] = None,
    firstPlayer: Option[Player] = None):

    def addCard(player: Player, newCard: Card): Trick =
        copy(cards :+ newCard)

    def clearTrick: Trick =
        Trick()

    def setTrick(player: Player, newCard: Card): Trick = copy(highestCard = Some(newCard), currentWinner = Some(player))

    def setFirstPlayer(player: Player): Trick = copy(firstPlayer = Some(player))

    def trickToString: String =
        if cards.nonEmpty then cards.map(card => s" $card ").mkString("|", "|", "|")
        else "|"