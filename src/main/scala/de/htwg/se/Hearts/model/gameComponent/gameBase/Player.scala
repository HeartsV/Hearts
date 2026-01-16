package de.htwg.se.Hearts.model.gameComponent.gameBase

import de.htwg.se.Hearts.model.gameComponent.PlayerInterface
import com.google.inject.Inject
import de.htwg.se.Hearts.model.gameComponent.CardInterface

case class Player (name: String, hand:List[CardInterface]= Nil, wonCards: List[CardInterface] = Nil, points: Int = 0) extends PlayerInterface:

    def getHand: List[CardInterface] = hand
    def getPoints: Int = points
    def getName: String = name
    def getWonCards: List[CardInterface] = wonCards
    def setName(newName:String): Player = copy(name = newName)
    def removeCard(card: CardInterface): Player = copy(hand = hand.filterNot(_ == card))

    def addAllCards(cards: List[CardInterface]): Player = copy(hand = cards)

    def addWonCards(cards: List[CardInterface]): Player = copy(wonCards = wonCards ++ cards)

    def addPoints(newPoints: Int): Player = copy(points = points + newPoints, wonCards = List())

    def resetPoints(): PlayerInterface = copy(points = 0)