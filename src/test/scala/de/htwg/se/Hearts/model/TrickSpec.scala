package de.htwg.se.Hearts.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import scala.collection.mutable.ListBuffer

class TrickSpec extends AnyWordSpec with Matchers {
    "A Trick" should {
        val p1 = Player(name ="Alice",hand = ListBuffer[Card](Card(rank = Rank.Two,suit = Suit.Clubs)),ListBuffer[Card]()) 
        val p2 = Player(name ="Dave",hand = ListBuffer[Card](Card(rank = Rank.Ace,suit = Suit.Clubs)),ListBuffer[Card]()) 
        
        
        "check if played card is valid card for first card" in { 
            Trick.addCard(Card(rank = Rank.Ten,suit = Suit.Clubs),p1) should be (false)
            Game.firstCard should be (true)
            Trick.cards should be (ListBuffer())
            Trick.addCard(Card(rank = Rank.Two,suit = Suit.Spades),p1) should be (true)
            Game.firstCard should be (false)
            Trick.cards should be(ListBuffer(Card(rank = Rank.Two,suit = Suit.Spades)))
        }

        

        "check if played card is a heart card and first card of trick before that is allowed" in {
            Trick.cards.clear()
            Game.firstCard = false
            Trick.addCard(Card(rank = Rank.Two,suit = Suit.Hearts),p1) should be (false)
            Trick.cards should be (ListBuffer())
            Trick.addCard(Card(rank = Rank.Two,suit = Suit.Clubs),p2) should be (true)
            Trick.cards should contain (Card(rank = Rank.Two,suit = Suit.Clubs))
        }

        

        "update start with heart after heart is played" in {
            Game.firstCard = false
            Trick.cards.clear()
            Trick.addCard(Card(rank = Rank.Two,suit = Suit.Clubs),p1) should be (true)
            Trick.addCard(Card(rank = Rank.Two,suit = Suit.Hearts),p2) should be (true)
            Game.startWithHearts should be (true)
        }

        

        "add played card" in {
            Trick.cards.clear()
            Trick.addCard(Card(rank = Rank.Ten,suit = Suit.Clubs),p1)
            Trick.cards should contain (Card(rank = Rank.Ten,suit = Suit.Clubs))
        }

        

        "check if highest card of played color" in {
            
        }

        

        "update currentWinner" in {
            Trick.cards.clear()
            Trick.addCard(Card(rank = Rank.Ace,suit = Suit.Clubs),p1)
            Trick.currentWinner should be (p1)
        }

        

        "clear when trick full" in {
            Trick.cards.clear()
            Trick.addCard(Card(rank = Rank.Three,suit = Suit.Clubs),p1)
            Trick.cards should be (List[Card]()) 
        }



    }
}
