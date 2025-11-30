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
        val p1 = Player("Alice").addAllCards(List(card1))
        val p2 = Player("Bob")

        "add player" in{
            var game = Game()
            game = game.addPlayer(p1)
            game.players should be (Vector[Player](p1))
            game = game.addPlayer(p2)
            game.players should be (Vector[Player](p1,p2))
        }

        "Add played card to cards" in {
            var game = Game()
            game = game.addCard(card)
            game.trickCards should be (List(card))
        }

        "clear a trick by clearTrick" in {
            var game = Game()
            game = game.clearTrick
            game.trickCards should be (List.empty)
            game.currentWinner should be (None)
            game.firstPlayer should be (None)
            game.highestCard should be (None)
        }

        "output the correct strings for played Cards" in {
            val card3 = Card(Rank.Two, Suit.Diamonds)
            val card4 = Card(Rank.Ten, Suit.Diamonds)
            val card5 = Card(Rank.Three, Suit.Diamonds)
            val card6 = Card(Rank.Queen, Suit.Diamonds)
            var game= Game()
            val gameController = Controller(game)
            gameController.game = gameController.setPlayerNumber(Some(4))
            gameController.trickToString should be ("|")
            gameController.game = gameController.game.addCard(card3)
            gameController.trickToString should be ("| 2 \u2666 |")
            gameController.game = gameController.game.addCard(card4)
            gameController.game.trickCards should equal (List(card3, card4))
            gameController.trickToString should be ("| 2 \u2666 | 10\u2666 |")
            gameController.game = gameController.game.addCard(card5)
            gameController.game.trickCards should equal (List(card3, card4, card5))
            gameController.trickToString should be ("| 2 \u2666 | 10\u2666 | 3 \u2666 |")
            gameController.game = gameController.game.addCard(card6)
            gameController.game.trickCards should equal (List(card3, card4, card5, card6))
            gameController.trickToString should be ("| 2 \u2666 | 10\u2666 | 3 \u2666 | Q \u2666 |")
        }
    }
}
