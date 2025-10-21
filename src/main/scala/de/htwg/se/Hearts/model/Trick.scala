package de.htwg.se.Hearts.model

import scala.collection.mutable.ListBuffer
import de.htwg.se.Hearts.model.Game.startWithHearts

object Trick {
    val cards: ListBuffer[Card] = ListBuffer()
    var playedSuit: Option[Suit] = None
    var currentWinner: Option[Player] = None
    var firstPlayer: Option[Player] = None

    def addCard(newCard: Card, currentPlayer: Player): Boolean = {
        if(Game.firstCard == true)
            if(newCard  == (Card(Rank.Two,Suit.Spades)))
                Trick.cards += newCard
                Game.firstCard = false
                true
            else
                false
        else if(Game.startWithHearts == false) 
            if(!(Trick.cards == ListBuffer()))
                Trick.cards += newCard
                if(newCard.suit == Suit.Hearts)
                    Game.startWithHearts = true
                true
            else
                if(newCard.suit == Suit.Hearts)
                    false
                else
                    Trick.cards += newCard
                    true
        else
            Trick.cards += newCard
            true
        
    }

    def updateCurrentWinner(newWinner: Player): Boolean = {
        true
    }

    def clearTrick(): Boolean ={
        true
    }
}
