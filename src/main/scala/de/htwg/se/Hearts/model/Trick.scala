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
            if(newCard  == (Card(Rank.Two,Suit.Clubs)))
                Trick.cards += newCard
                Game.firstCard = false
                true
            else
                false
        else if(!(Trick.cards == ListBuffer()))
            if( newCard.suit == playedSuit || !(currentPlayer.hand.exists(_.suit == playedSuit)))
                Trick.cards += newCard
                if(newCard.suit == Suit.Hearts)
                    Game.startWithHearts = true
                true
            else
                false
        else
            if (newCard.suit == Suit.Hearts && Game.startWithHearts == false)
                false
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
