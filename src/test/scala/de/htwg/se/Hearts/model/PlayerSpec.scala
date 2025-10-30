package de.htwg.se.Hearts.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import scala.collection.mutable.ListBuffer

class PlayerSpec extends AnyWordSpec with Matchers {
    "A player" should {
        val p1 = Player(name ="Alice")
        p1.hand += (Card(Rank.Ace,Suit.Spades),Card(Rank.Ten,Suit.Hearts))
        Game.firstCard = false
        Game.startWithHearts = false
        Game.trick.cards.clear()
        Game.trick.highestCard

        "have a name" in {
            p1.name should be ("Alice")
        }

        "have a hand" in {
            p1.hand should be (ListBuffer[Card](Card(Rank.Ace,Suit.Spades),Card(Rank.Ten,Suit.Hearts)))
        }

        "output handstring" in {
            p1.handToString() should be ("| A \u2660 | 10\u2665 |")
        }

        "be able to play Cards" in {
            p1.playCard(2) should be (false)
            p1.hand should be (ListBuffer[Card](Card(Rank.Ace,Suit.Spades),Card(Rank.Ten,Suit.Hearts)))
            Game.trick.cards should be (ListBuffer())
            p1.playCard(1) should be (false)
            p1.hand should be (ListBuffer[Card](Card(Rank.Ace,Suit.Spades),Card(Rank.Ten,Suit.Hearts)))
            Game.trick.cards should be (ListBuffer())
            p1.playCard(0) should be (true)
            p1.hand should be (ListBuffer[Card](Card(Rank.Ten,Suit.Hearts)))
            Game.trick.cards should be (ListBuffer[Card](Card(Rank.Ace,Suit.Spades)))
            Game.trick.addCard(Card(Rank.Two,Suit.Spades))
            Game.trick.addCard(Card(Rank.Three,Suit.Spades))
            Game.trick.addCard(Card(Rank.Four,Suit.Spades))
        }
    }

}
