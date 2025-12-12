package de.htwg.se.Hearts.model

case class Player(name: String, hand:List[Card]= Nil, wonCards: List[Card] = Nil, points: Int = 0):
    def removeCard(card: Card): Player = copy(hand = hand.filterNot(_ == card))

    def addAllCards(cards: List[Card]): Player = copy(hand = hand ++ cards)

    def addWonCards(cards: List[Card]): Player = copy(wonCards = wonCards ++ cards)

    def addPoints(newPoints: Int): Player = copy(points = points + newPoints)