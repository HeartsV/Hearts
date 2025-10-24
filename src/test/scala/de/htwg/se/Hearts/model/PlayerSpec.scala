package de.htwg.se.Hearts.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import scala.collection.mutable.ListBuffer

class PlayerSpec extends AnyWordSpec with Matchers {
    "A player" should {
        val p1 = Player(name ="Alice",hand = ListBuffer[Card](Card(rank = Rank.Ace,suit = Suit.Spades)),ListBuffer[Card]()) 

        "have a name" in {
            p1.name should be ("Alice")
        }

        "have a hand" in {
            p1.hand should be (List[Card](Card(rank = Rank.Ace,suit = Suit.Spades)))
        }
    }
   
}
