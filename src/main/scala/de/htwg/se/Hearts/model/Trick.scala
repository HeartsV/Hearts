package de.htwg.se.Hearts.model

import scala.collection.mutable.ListBuffer
import de.htwg.se.Hearts.model.Game.startWithHearts

object Trick {
    val cards: ListBuffer[Card] = ListBuffer()
    var highestCard: Option[Card] = None
    var currentWinner: Option[Player] = None
    var firstPlayer: Option[Player] = None

    def addCard(newCard: Card, currentPlayer: Player): Boolean = {
        if(Game.firstCard == true)
            if(newCard  == (Card(Rank.Two,Suit.Clubs)))
                Trick.cards += newCard
                Trick.highestCard = Some(newCard)
                Game.firstCard = false
                true
            else
                false
        else if(!(Trick.cards == ListBuffer()))
            if(highestCard.exists(card => card.suit == newCard.suit) || !highestCard.exists(card => currentPlayer.hand.exists(_.suit == card.suit)))
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
                Trick.highestCard = Some(newCard)
                true

    }

    def updateCurrentWinner(playedCard: Card, currentPlayer: Player): Boolean = {
        if(Trick.highestCard.exists(card => card.suit == playedCard.suit))
            false
        else
            true
    }

    def clearTrick(): Boolean ={
        true
    }
}
