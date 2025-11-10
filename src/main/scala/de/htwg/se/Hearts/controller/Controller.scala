package de.htwg.se.Hearts.controller

import de.htwg.se.Hearts.model.*
import scala.collection.mutable.ListBuffer


class Controller(game: Game) extends Observable() {

    def processInput(input: String): Boolean  = {
        if(game.firstCard == true)
            game.updateCurrentPlayer()
        if(input.toIntOption.exists(index => playCard(index)))
            if(game.firstCard == true) game.firstCard = false
            updateCurrentWinner()
            game.updateCurrentPlayer()
            notifyObservers
            true
        else
            notifyObservers
            false
    }


    def playCard(index : Int) : Boolean = {
        if(game.trick.cards.size == game.players.size)
            game.currentPlayer.get.wonCards.addAll(game.trick.cards)
            game.trick.clearTrick()
        if(game.currentPlayer.get.hand.size > index - 1 && index - 1 >= 0 && addCard(game.currentPlayer.get.hand(index - 1)))
            game.currentPlayer.get.removeCard(index)
            true
        else
            false

    }

    def addCard(newCard: Card): Boolean = {
        if(game.firstCard == true)
            if(newCard  == (Card(Rank.Two,Suit.Clubs)))
                game.trick.addCard(newCard)
                true
            else
                false
        else if(!(game.trick.cards == ListBuffer()))
            if(game.trick.highestCard.exists(card => card.suit == newCard.suit) || !game.trick.highestCard.exists(card => game.currentPlayer.get.hand.exists(_.suit == card.suit)))
                game.trick.addCard(newCard)
                if(newCard.suit == Suit.Hearts)
                    game.startWithHearts = true
                true
            else
                false
        else
            if (newCard.suit == Suit.Hearts && game.startWithHearts == false)
                false
            else
                game.trick.addCard(newCard)
                game.trick.initializeTrick(game.currentPlayer.get,newCard)
                true

    }

    def updateCurrentWinner(): Boolean = {
        if(game.trick.highestCard == None || game.trick.highestCard.exists(card => card.suit == game.trick.cards.last.suit && game.trick.cards.last.rank.compare(card.rank) > 0))
            game.trick.highestCard = Some(game.trick.cards.last)
            game.trick.currentWinner = Some(game.currentPlayer.get)
            true
        else
            false

    }

    def completeTrickString(): String = {
        var string = game.trick.trickToString()
        for(i <- 1 to game.players.size - game.trick.cards.size)
            string += "     |"
        string

    }

    def getCurrentPlayerHand(): String = game.currentPlayer.get.handToString()

    def getCurrentPlayerName(): String = game.currentPlayer.get.name

    def checkGameOver(): Boolean = game.gameOver
}
