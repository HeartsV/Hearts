package de.htwg.se.Hearts.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import scala.collection.mutable.ListBuffer

class PlayerSpec extends AnyWordSpec with Matchers {
    "A player" should {
        val card1 = Card(Rank.Ace,Suit.Spades)
        val card2 = Card(Rank.Ten,Suit.Hearts)

        "have a name" in {
            val p1 = Player(name ="Alice")
            p1.name should be ("Alice")
        }

        "have a hand" in {
            val p1 = Player(name ="Alice")
            p1.hand ++= List(card1,card2)
            p1.hand should be (ListBuffer[Card](card1,card2))
        }


        "output handstring" in {
            val p1 = Player(name ="Alice")
            p1.hand ++= List(card1,card2)
            p1.handToString() should be ("|  1  |  2  |\n| A \u2660 | 10\u2665 |")
        }

        "be able to remove cards" in {
            val p1 = Player(name ="Alice")
            p1.hand ++= List(card1,card2)
            p1.removeCard(1) should be (true)
            p1.hand should be (ListBuffer[Card](card2))

        }

    }

}
