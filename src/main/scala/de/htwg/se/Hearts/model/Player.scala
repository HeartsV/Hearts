package de.htwg.se.Hearts.model

import scala.collection.mutable.ListBuffer

class Player(val name: String) {

    val hand: ListBuffer[Card] = ListBuffer()
    val wonCards:ListBuffer[Card] = ListBuffer()
    var points: Int = 0

    def removeCard(card : Card) : Boolean =
        hand.remove(hand.indexOf(card))
        true

    def addAllCards(cards: List[Card]): Unit = hand.addAll(cards)
}
