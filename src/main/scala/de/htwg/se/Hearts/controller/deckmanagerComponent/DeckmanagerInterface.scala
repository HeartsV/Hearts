package de.htwg.se.Hearts.controller.deckmanagerComponent
import de.htwg.se.Hearts.model._

trait DeckmanagerInterface {
  def createDeck: List[Card]
  def shuffle(deck: List[Card]): List[Card]
  def deal(deck: List[Card], game: Game): Vector[Player]
}
