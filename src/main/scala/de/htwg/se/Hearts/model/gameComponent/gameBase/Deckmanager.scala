package de.htwg.se.Hearts.model.gameComponent.gameBase

import de.htwg.se.Hearts.model.gameComponent.DeckManagerInterface
import de.htwg.se.Hearts.model.gameComponent.Suit
import de.htwg.se.Hearts.model.gameComponent.Rank
import de.htwg.se.Hearts.model.gameComponent.GameInterface
import de.htwg.se.Hearts.model.gameComponent.PlayerInterface
import de.htwg.se.Hearts.model.gameComponent.CardInterface
import de.htwg.se.Hearts.model.gameComponent.gameBase.Card
import de.htwg.se.Hearts.model.gameComponent.gameBase.Game
import de.htwg.se.Hearts.model.gameComponent.gameBase.Player

class DeckManager extends DeckManagerInterface:

    override def createDeck: List[Card] =
        for
            suit <- Suit.values.toList
            rank <- Rank.values.toList
        yield Card(rank, suit)

    override def shuffle(deck: List[Card]): List[Card] =
        util.Random().shuffle(deck)


    override def filterOneCardOut(deck: List[Card], game: GameInterface): List[Card] =
        if game.getPlayerNumber.contains(3) then
            if deck.head == Card(Rank.Two, Suit.Clubs) then deck.filterNot(_ == deck(1))
            else deck.tail
        else deck

    override def deal(deck: List[Card], game: GameInterface): Vector[Player] =
        val normalized = filterOneCardOut(deck, game)
        val handSize = normalized.size / game.getPlayerNumber.get
        val hands = normalized.grouped(handSize).toList
        (game.getPlayers.zip(hands).map { case (p, cards) => p.addAllCards(cards) }).toVector
