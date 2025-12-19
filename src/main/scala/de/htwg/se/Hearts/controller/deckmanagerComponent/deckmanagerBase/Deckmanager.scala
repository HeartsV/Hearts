package de.htwg.se.Hearts.controller.deckmanagerComponent.deckmanagerBase

import de.htwg.se.Hearts.controller.deckmanagerComponent.DeckmanagerInterface
import de.htwg.se.Hearts.model._

class Deckmanager extends DeckmanagerInterface:

  override def createDeck: List[Card] =
    for
      suit <- Suit.values.toList
      rank <- Rank.values.toList
    yield Card(rank, suit)

  override def shuffle(deck: List[Card]): List[Card] =
    util.Random().shuffle(deck)

  /** Hearts with 3 players typically removes 1 card so hands are equal-sized. */
  private def filterOneCardOut(deck: List[Card], game: Game): List[Card] =
    if game.playerNumber.contains(3) then
      // Keep 2♣ in the deck; remove a different single card to keep equal hands.
      if deck.head == Card(Rank.Two, Suit.Clubs) then deck.filterNot(_ == deck(1))
      else deck.tail
    else deck

  override def deal(deck: List[Card], game: Game): Vector[Player] =
    val normalized = filterOneCardOut(deck, game)
    val handSize = normalized.size / game.playerNumber.get
    val hands = normalized.grouped(handSize).toList
    (game.players.zip(hands).map { case (p, cards) => p.copy(hand = cards) }).toVector
