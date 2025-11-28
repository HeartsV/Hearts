package de.htwg.se.Hearts.model

import scala.collection.mutable.ListBuffer
/*
class Player(val name: String) {

    val hand: ListBuffer[Card] = ListBuffer()
    val wonCards:ListBuffer[Card] = ListBuffer()
    var points: Int = 0

    def removeCard(card : Card) : Boolean =
        hand.remove(hand.indexOf(card))
        true

    def addAllCards(cards: List[Card]): Unit = hand.addAll(cards)
}*/

case class Player(name: String, hand:List[Card]= Nil, wonCards: List[Card] = Nil, points: Int = 0):
    def removeCard(card: Card): Player = copy(hand = hand.filterNot(_ == card))

    def addAllCards(cards: List[Card]): Player = copy(hand = hand ++ cards)

    def addWonCards(cards: List[Card]): Player = copy(wonCards = wonCards ++ cards)
    
    def addPoints(newPoints: Int): Player = copy(points = points + newPoints)