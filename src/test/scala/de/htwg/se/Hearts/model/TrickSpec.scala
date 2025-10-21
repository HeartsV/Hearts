package de.htwg.se.Hearts.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class TrickSpec extends AnyWordSpec with Matchers {
    "A Trick" should {
        val p1 = Player(name ="Alice",hand = List[Card](Card(rank = Rank.Two,suit = Suit.Clubs)),List[Card]()) 
        val p2 = Player(name ="Dave",hand = List[Card](Card(rank = Rank.Ace,suit = Suit.Clubs)),List[Card]()) 
        val currentTrick = Trick(List[Card](),None: Option[Suit] ,None: Option[Player] ,None: Option[Player])
        

        "add played card" in {
            currentTrick.addCard(Card(rank = Rank.Ten,suit = Suit.Clubs),p1)
            currentTrick.cards should contain (Card(rank = Rank.Ten,suit = Suit.Clubs))
        }

        
        "check if played card is valid card" in { 
            
            

        }

        "check if highest card of played color" in {
            
        }

        "update currentWinner" in {
            currentTrick.addCard(Card(rank = Rank.Ace,suit = Suit.Clubs),p1)
            currentTrick.currentWinner should be (p1)

        }

        "clear when trick full" in {
            currentTrick.addCard(Card(rank = Rank.Three,suit = Suit.Clubs),p1)
            currentTrick.cards should be (List[Card]()) 

        }


    }
}
