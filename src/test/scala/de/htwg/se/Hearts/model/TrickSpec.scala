package de.htwg.se.Hearts.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class TrickSpec extends AnyWordSpec with Matchers {
    "A Trick" should {
        val p1 = Player(name ="Alice",hand = List[Card](Card(rank = Rank.Ace,suit = Suit.Spades)),List[Card]()) 
        val currentTrick = Trick(List[Card](),None: Option[Suit] ,Some(p1) ,Some(p1))
        

        "add played card" in {
            currentTrick.addCard(Card(rank = Rank.Ten,suit = Suit.Clubs))
            currentTrick.cards should contain (Card(rank = Rank.Ten,suit = Suit.Clubs))
        }

        
        "check if played card is highest valid card" in { // highest valid oder erlaubt wegen regeln ?
            

        }

        "update currentWinner" in {
            currentTrick.addCard(Card(rank = Rank.Ace,suit = Suit.Clubs)) // sollten wir hier den Current player übergeben?
            currentTrick.currentWinner should be (p1)

        }

        "clear when trick full" in {
            currentTrick.addCard(Card(rank = Rank.Three,suit = Suit.Clubs))
            currentTrick.cards should be (List[Card]()) 

        }


    }
}
