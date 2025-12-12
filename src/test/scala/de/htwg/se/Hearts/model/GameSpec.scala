package de.htwg.se.Hearts.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.Hearts.controller._
import de.htwg.se.Hearts.model._

class GameSpec extends AnyWordSpec with Matchers {
    "A Game" should {
        val card = Card(Rank.Ace,Suit.Clubs)
        val card1 = Card(Rank.Two,Suit.Clubs)
        val card2 = Card(Rank.Two,Suit.Diamonds)
        val p1 = Player("Alice", List(card1),points = 10)
        val p2 = Player("Bob", List(card2))
        val game = Game(players = Vector(p1),trickCards = List(card1),currentPlayerIndex = Some(0),currentWinner = Some(p1))

        "get the current player" in {
            game.getCurrentPlayer should be (Some(p1))
        }
    }
}