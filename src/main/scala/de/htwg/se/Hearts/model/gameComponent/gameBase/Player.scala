package de.htwg.se.Hearts.model.gameComponent.gameBase

import de.htwg.se.Hearts.model.gameComponent.PlayerInterface
import de.htwg.se.Hearts.model.gameComponent.Rank
import de.htwg.se.Hearts.model.gameComponent.Suit
import scala.xml.Elem

case class Player(name: String, hand:List[Card]= Nil, wonCards: List[Card] = Nil, points: Int = 0) extends PlayerInterface:

    def removeCard(card: Card): Player = copy(hand = hand.filterNot(_ == card))

    def addAllCards(cards: List[Card]): Player = copy(hand = hand ++ cards)

    def addWonCards(cards: List[Card]): Player = copy(wonCards = wonCards ++ cards)

    def addPoints(newPoints: Int): Player = copy(points = points + newPoints)

    def playerToXML: Elem =
        <player>
            <name>{name}</name>
            <hand>{hand.map(c => c.cardToXML)}</hand>
            <wonCards>{wonCards.map(c => c.cardToXML)}</wonCards>
            <points>{points}</points>
        </player>

    
