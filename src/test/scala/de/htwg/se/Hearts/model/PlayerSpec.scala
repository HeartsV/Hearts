package de.htwg.se.Hearts.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import scala.collection.mutable.ListBuffer

class PlayerSpec extends AnyWordSpec with Matchers {
    "A player" should {
        val card1 = Card(Rank.Ace,Suit.Spades)
        val card2 = Card(Rank.Ten,Suit.Hearts)

        "have a name" in {
            val p1 = Player("Alice")
            p1.name should be ("Alice")
        }

        "have a hand" in {
            val p1 = Player("Alice").addAllCards(List(card1,card2))
            p1.hand should be (List[Card](card1,card2))
        }

        "be able to remove cards" in {
            val p1 = Player("Alice").addAllCards(List(card1,card2))
            p1.removeCard(card1)
            p1.hand should be (List[Card](card2))
        }
    }

}
