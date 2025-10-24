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
                Trick.playedSuit = Some(newCard.suit)
                Game.firstCard = false
                true
            else
                false
        else if(!(Trick.cards == ListBuffer()))
            if(playedSuit.contains(newCard.suit) || !playedSuit.exists(suit => currentPlayer.hand.exists(_.suit == suit)))
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
                Trick.playedSuit = Some(newCard.suit)
                true

    }

    def updateCurrentWinner(playedCard: Card, currentPlayer: Player): Boolean = {
        if(Trick.playedSuit.contains(playedCard.suit))
            false
        else
            true
    }

    def clearTrick(): Boolean ={
        true
    }
}
