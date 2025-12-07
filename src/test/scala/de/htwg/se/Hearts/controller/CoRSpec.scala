package de.htwg.se.Hearts.controller
import de.htwg.se.Hearts.model._
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class CoRSpec extends AnyWordSpec with Matchers {
    "A CoR" should {
        val card1 = Card(Rank.Two,Suit.Clubs)
        val card2 = Card(Rank.Two,Suit.Diamonds)
        val card3 = Card(Rank.Ace,Suit.Clubs)
        val card4 = Card(Rank.Ace,Suit.Diamonds)
        val card5 = Card(Rank.Jack,Suit.Hearts)
        val card6 = Card(Rank.Jack,Suit.Hearts)
        val card7 = Card(Rank.Ten,Suit.Clubs)
        val card8 = Card(Rank.Ten,Suit.Diamonds)
        val p1 = Player("Alice",List(card1,card2),List(card5))
        val p2 = Player("Dave",List(card3,card4,card5))
        val p3 = Player("Charlie", List(card5, card6))
        val gameFirstCard = Game(playerNumber = Some(2),players = Vector(p1,p2),currentPlayerIndex = Some(0))
        val gameNoHearts = Game(playerNumber = Some(2),players = Vector(p1,p2),firstCard = false,currentPlayerIndex = Some(0))
        val gameHearts = Game(playerNumber = Some(2),players = Vector(p1,p2),firstCard = false,startWithHearts = true,currentPlayerIndex = Some(0))
        val gameWithTrick = Game(playerNumber = Some(2),players = Vector(p1,p2),firstCard = false, currentPlayerIndex = Some(0),trickCards = List(card7))

        "check if card allowed for first card in game" in {
            ChainOfResponsibility.validateMove(gameFirstCard, p1.hand, 2) should be (Left("Index: 3 was out of bounds!\n"))
            ChainOfResponsibility.validateMove(gameFirstCard, p1.hand, 1) should be (Left("First trick must start with 2 \u2663!\n"))
            ChainOfResponsibility.validateMove(gameFirstCard, p1.hand, 0) should be (Right(card1))
        }

        "check if card allowed for cards after the first" in {
            ChainOfResponsibility.validateMove(gameWithTrick, p1.hand, 1) should be (Left("You have at least one card with Suit \u2663! You must follow this Suit!\n"))
            ChainOfResponsibility.validateMove(gameWithTrick, p1.hand, 0) should be (Right(card1))
        }

        "check if heart allowed when hearts are broken" in {
            ChainOfResponsibility.validateMove(gameHearts, p2.hand, 2) should be (Right(card5))
            ChainOfResponsibility.validateMove(gameNoHearts, p3.hand, 0) should be(Right(card5))
            ChainOfResponsibility.validateMove(gameNoHearts, p2.hand, 2) should be(Left("You cannot play \u2665 until they are broken or you have only \u2665s\n"))
        }

    }
}
