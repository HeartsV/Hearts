package de.htwg.se.Hearts.controller

import de.htwg.se.Hearts.model.*
import scala.collection.mutable.ListBuffer
import scala.compiletime.ops.boolean


class Controller(game: Game) extends Observable() {

    var state:State = MainScreenState(this)

    def processInput(input: String): Boolean =
        if(state.processInput(input))
            notifyObservers
            true
        else
            notifyObservers
            false


    def changeState(newState:State): Unit = state = newState

    def playCard(index: Int): Boolean =
        if(game.trick.cards.size == game.players.size)
            game.currentPlayer.get.wonCards.addAll(game.trick.cards)
            game.trick.clearTrick()
        if(game.currentPlayer.get.hand.size > index - 1 && index - 1 >= 0 && addCard(game.currentPlayer.get.hand(index - 1)))
            game.currentPlayer.get.removeCard(index)
            true
        else
            false

    def addCard(newCard: Card): Boolean =
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

    def updateCurrentWinner(): Boolean =
        if(game.trick.highestCard == None || game.trick.highestCard.exists(card => card.suit == game.trick.cards.last.suit && game.trick.cards.last.rank.compare(card.rank) > 0))
            game.trick.highestCard = Some(game.trick.cards.last)
            game.trick.currentWinner = Some(game.currentPlayer.get)
            true
        else
            false

    def completeTrickString(): String = game.trick.trickToString()  + "     |" * (game.players.size - game.trick.cards.size)

    def updateCurrentPlayer(): Boolean =
    if (game.firstCard == true)
        game.currentPlayer = game.players.find(_.hand.contains(Card(Rank.Two,Suit.Clubs)))
        true
    else if(game.players.size == game.trick.cards.size)
        game.currentPlayer = game.trick.currentWinner
        true
    else if(game.players.indexOf(game.currentPlayer.get) + 1 == game.players.size)
        game.currentPlayer = Some(game.players(0))
        true
    else
        game.currentPlayer = Some(game.players((game.players.indexOf(game.currentPlayer.get) + 1)))
        true

    def createDeck(): List[Card] =
        for
            suit <- Suit.values.toList
            rank <- Rank.values.toList
        yield Card(rank, suit)

    def dealCards(): Boolean =
        var deck: List[Card] = createDeck()
        deck = util.Random().shuffle(deck)
        if(game.playerNumber.get == 3)
            if(deck(1) == Card(Rank.Two,Suit.Clubs))
                deck = deck.filterNot(_ == deck(2))
            else
                deck = deck.filterNot(_ == deck(1))
        val handlist = deck.grouped(deck.size/game.playerNumber.get).toList
        for (i <- 0 to game.playerNumber.get - 1) game.players(i).addAllCards(handlist(i))
        true


    def getCurrentPlayerHand(): String = game.currentPlayer.get.handToString()

    def getCurrentPlayerName(): String = game.currentPlayer.get.name

    def checkGameOver(): Boolean = game.gameOver

    def getGame(): Game = game

    def getPlayerNumber(): Option[Int] = game.playerNumber

    def setPlayerNumber(number: Int): Unit = game.playerNumber = Some(number)
}
