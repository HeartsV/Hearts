package de.htwg.se.Hearts.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import scala.collection.mutable.ListBuffer

class PlayerSpec extends AnyWordSpec with Matchers {
    "A player" should {
        val p1 = Player(name ="Alice",hand = ListBuffer[Card](Card(Rank.Ace,Suit.Spades),Card(Rank.Three,Suit.Hearts)),ListBuffer[Card]()) 
        Game.firstCard = false
        Game.startWithHearts = false

        "have a name" in {
            p1.name should be ("Alice")
        }

        "have a hand" in {
            p1.hand should be (ListBuffer[Card](Card(Rank.Ace,Suit.Spades),Card(Rank.Three,Suit.Hearts)))
        }

        "be able to play Cards" in {
            p1.playCard(2) should be (false)
            p1.hand should be (ListBuffer[Card](Card(Rank.Ace,Suit.Spades),Card(Rank.Three,Suit.Hearts)))
            Game.trick should be (ListBuffer())
            p1.playCard(1) should be (false)
            p1.hand should be (ListBuffer[Card](Card(Rank.Ace,Suit.Spades),Card(Rank.Three,Suit.Hearts)))
            Game.trick should be (ListBuffer())
            p1.playCard(0) should be (true)
            p1.hand should be (ListBuffer[Card](Card(Rank.Three,Suit.Hearts)))
            Game.trick should be (ListBuffer[Card](Card(Rank.Ace,Suit.Spades)))
            Game.addCard(Card(Rank.Two,Suit.Spades),p1)
            Game.addCard(Card(Rank.Three,Suit.Spades),p1)
            Game.addCard(Card(Rank.Four,Suit.Spades),p1)
        }
    }
   
}
