package de.htwg.se.Hearts.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

import de.htwg.se.Hearts.model.gameComponent.Suit
import de.htwg.se.Hearts.model.gameComponent.Rank
import de.htwg.se.Hearts.model.gameComponent.DeckManagerInterface
import de.htwg.se.Hearts.model.gameComponent.gameBase._

class DeckmanagerSpec extends AnyWordSpec with Matchers:

    "A Deckmanager" should {
        val manager = DeckManager()
        val card1 = Card(Rank.Two,Suit.Clubs)
        val card2 = Card(Rank.Two,Suit.Diamonds)
        val card3 = Card(Rank.Ace,Suit.Clubs)
        val card4 = Card(Rank.Ace,Suit.Diamonds)
        val card5 = Card(Rank.Jack,Suit.Hearts)
        val card6 = Card(Rank.Jack,Suit.Clubs)
        val card7 = Card(Rank.Ten,Suit.Clubs)
        val card8 = Card(Rank.Ten,Suit.Diamonds)
        val game3Players = Game(playerNumber = Some(3))
        val game4Players = Game(playerNumber = Some(4))
        val game2Players = Game(playerNumber = Some(2),players = Vector(Player("A"), Player("B")))


        " create a deck when function createDeck is called" should {

            "create a deck with all combinations of ranks and suits" in {
                val deck = manager.createDeck
                deck.size shouldBe (Suit.values.size * Rank.values.size)
                for {
                    suit <- Suit.values
                    rank <- Rank.values
                }
                deck should contain (Card(rank, suit))
            }
            "not contain duplicate cards" in {
                val deck = manager.createDeck
                deck.distinct.size shouldBe deck.size
            }
        }

        "shuffle" should {
                "return a deck with the same cards and not introduce duplicates or remove cards" in {
                val originalDeck = manager.createDeck
                val shuffledDeck = manager.shuffle(originalDeck)

                shuffledDeck.size shouldBe originalDeck.size

                shuffledDeck.toSet shouldBe originalDeck.toSet

                shuffledDeck.distinct.size shouldBe shuffledDeck.size
                }
        }

        "filterOneCardOut" should{
            "filter the correct Card out" in {
                manager.filterOneCardOut(List(card1, card2), game3Players) should be (List(card1))
                manager.filterOneCardOut(List(card2, card1),game3Players) should be (List(card1))
                manager.filterOneCardOut(List(card1, card2), game4Players) should be (List(card1, card2))
            }
        }

        "deal" should {
            "deal the cards correctly" in {
                manager.deal(List(card1, card2), game2Players) should be (Vector(Player("A", List(card1)), Player("B", List(card2))))
            }
        }
}
