package de.htwg.se.Hearts.model

import scala.collection.mutable.ListBuffer

class Player(val name: String) {

    val hand: ListBuffer[Card] = ListBuffer()
    val wonCards:ListBuffer[Card] = ListBuffer()

    def removeCard(index : Int) : Boolean =
        hand.remove(index-1)
        true

    def handToString(): String =
        (1 to hand.size).map(index => s"  $index".padTo(5, ' ')).mkString("|", "|", "|") + "\n" +
        hand.map(card => s" $card ").mkString("|", "|", "|")

    def addAllCards(cards: List[Card]): Unit = hand.addAll(cards)
}
