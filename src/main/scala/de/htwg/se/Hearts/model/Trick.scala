package de.htwg.se.Hearts.model

import scala.collection.mutable.ListBuffer
import de.htwg.se.Hearts.model.Game.startWithHearts

class Trick {
    val cards: ListBuffer[Card] = ListBuffer()
    var highestCard: Option[Card] = None
    var currentWinner: Option[Player] = None
    var firstPlayer: Option[Player] = None

    def addCard(newCard: Card, currentPlayer: Player): Boolean = {
        if(Game.firstCard == true)
            if(newCard  == (Card(Rank.Two,Suit.Spades)))
                cards += newCard

                updateCurrentWinner(newCard,currentPlayer)
                Game.firstCard = false
                true
            else
                false
        else if(!(cards == ListBuffer()))
            if(highestCard.exists(card => card.suit == newCard.suit) || !highestCard.exists(card => currentPlayer.hand.exists(_.suit == card.suit)))
                cards += newCard
                updateCurrentWinner(newCard,currentPlayer)
                if(newCard.suit == Suit.Hearts)
                    Game.startWithHearts = true
                true
            else
                false
        else
            if (newCard.suit == Suit.Hearts && Game.startWithHearts == false)
                false
            else
                cards += newCard

                updateCurrentWinner(newCard,currentPlayer)
                true

    }

    def updateCurrentWinner(playedCard: Card, currentPlayer: Player): Boolean = {
        if(highestCard == None || highestCard.exists(card => card.suit == playedCard.suit && playedCard.rank.compare(card.rank) > 0))
            highestCard = Some(playedCard)
            currentWinner = Some(currentPlayer)
            true
        else
            false

    }

    def clearTrick(): Boolean ={
        true
    }
}
