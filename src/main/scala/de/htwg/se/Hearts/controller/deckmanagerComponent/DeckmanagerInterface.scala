package de.htwg.se.Hearts.controller.deckmanagerComponent
import de.htwg.se.Hearts.model._
import de.htwg.se.Hearts.model.gameComponent.gameBase.Card
import de.htwg.se.Hearts.model.gameComponent.gameBase.Game
import de.htwg.se.Hearts.model.gameComponent.gameBase.Player

trait DeckmanagerInterface {
  def createDeck: List[Card]
  def shuffle(deck: List[Card]): List[Card]
  def deal(deck: List[Card], game: Game): Vector[Player]
}
