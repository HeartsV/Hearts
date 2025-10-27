package de.htwg.se.Hearts.model

import scala.collection.mutable.ListBuffer
import de.htwg.se.Hearts.model.Game

class Trick {
    val cards: ListBuffer[Card] = ListBuffer()
    var highestCard: Option[Card] = None
    var currentWinner: Option[Player] = None
    var firstPlayer: Option[Player] = None

    def addCard(newCard: Card, currentPlayer: Player): Boolean = {
        if(Game.firstCard == true)
            if(newCard  == (Card(Rank.Two,Suit.Spades)))
                cards += newCard
                firstPlayer = Some(currentPlayer)
                updateCurrentWinner(newCard,currentPlayer)
                Game.firstCard = false
                true
            else
                false
        else if(!(cards == ListBuffer()))
            if(highestCard.exists(card => card.suit == newCard.suit) || !highestCard.exists(card => currentPlayer.hand.exists(_.suit == card.suit)))
                cards += newCard
                updateCurrentWinner(newCard,currentPlayer)
                clearTrick()
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
                firstPlayer = Some(currentPlayer)
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
        if(Game.playerNumber.exists(pnuber => cards.size == pnuber))
            currentWinner.foreach(_.wonCards ++= cards)
            cards.clear()
            highestCard = None
            currentWinner = None
            firstPlayer = None
            true
        else
            false
    }
}
