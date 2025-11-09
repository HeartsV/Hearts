package de.htwg.se.Hearts.model

import scala.collection.mutable.ListBuffer
import de.htwg.se.Hearts.model.Game

class Trick {
    val cards: ListBuffer[Card] = ListBuffer()
    var highestCard: Option[Card] = None
    var currentWinner: Option[Player] = None
    var firstPlayer: Option[Player] = None

    def addCard(newCard: Card): Boolean = { 
        cards += newCard
        true
    }

    def initializeTrick(player: Player, card: Card) = {
        highestCard = Some(card)
        firstPlayer = Some(player)
        currentWinner = Some(player)
    }

    def clearTrick(): Boolean ={
        cards.clear()
        highestCard = None
        currentWinner = None
        firstPlayer = None
        true
    }

    def trickToString(): String = {
        var trickString = "|"
        for(element<-cards)
            trickString += (" " + element.toString +  " ")
            trickString += "|"
        trickString
    }
}
