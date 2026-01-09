package de.htwg.se.Hearts.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import scala.collection.mutable.ListBuffer
import de.htwg.se.Hearts.model.gameComponent.gameBase.Card
import de.htwg.se.Hearts.model.gameComponent.gameBase.Player
import de.htwg.se.Hearts.model.gameComponent._

class PlayerSpec extends AnyWordSpec with Matchers {
    "A player" should {
        val card1 = Card(Rank.Ace,Suit.Spades)
        val card2 = Card(Rank.Ten,Suit.Hearts)
        val card3 = Card(Rank.Ace,Suit.Hearts)
        val player = Player(name = "Alice",hand = List(card1),wonCards = List(card3))

        "have a name" in {
            player.name should be ("Alice")
        }

        "have a hand" in {
            player.hand should be (List[Card](card1))
        }

        "be able to remove cards" in {
            player.removeCard(card1) should be (Player(name = "Alice",hand = List(),wonCards = List(card3)))
        }

        "be able to add cards" in {
            player.addAllCards(List(card2)) should be (Player(name = "Alice",hand = List(card1,card2),wonCards = List(card3)))
        }

        "be able to add Won Cards" in {
            player.addWonCards(List(card2)) should be (Player(name = "Alice",hand = List(card1),wonCards = List(card3,card2)))
        }

        "be able to add points" in {
            player.addPoints(10) should be (Player(name = "Alice",hand = List(card1),wonCards = List(card3),points = 10))
        }
    }

}
