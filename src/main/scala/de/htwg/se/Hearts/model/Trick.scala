package de.htwg.se.Hearts.model

import scala.collection.mutable.ListBuffer
import de.htwg.se.Hearts.model.Game

class Trick {
    val cards: ListBuffer[Card] = ListBuffer()
    var highestCard: Option[Card] = None
    var currentWinner: Option[Player] = None
    var firstPlayer: Option[Player] = None

    def addCard(newCard: Card): Boolean = {
        if(Game.firstCard == true)
            if(newCard  == (Card(Rank.Two,Suit.Clubs)))
                cards += newCard
                firstPlayer = Some(Game.currentPlayer.get)
                Game.firstCard = false
                true
            else
                false
        else if(!(cards == ListBuffer()))
            if(highestCard.exists(card => card.suit == newCard.suit) || !highestCard.exists(card => Game.currentPlayer.get.hand.exists(_.suit == card.suit)))
                cards += newCard
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
                firstPlayer = Some(Game.currentPlayer.get)
                true

    }

    def updateCurrentWinner(): Boolean = {
        if(highestCard == None || highestCard.exists(card => card.suit == cards.last.suit && cards.last.rank.compare(card.rank) > 0))
            highestCard = Some(cards.last)
            currentWinner = Some(Game.currentPlayer.get)
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

    def trickToString(): String = {
        var trickString = "|"
        var amountPlayedCards = 0
        for(element<-cards)
            trickString += (" " + element.toString +  " ")
            trickString += "|"
            amountPlayedCards += 1
        while (amountPlayedCards < Game.playerNumber.get)
            trickString += "     |"
            amountPlayedCards += 1
        trickString
    }
}
