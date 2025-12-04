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

        "add player" in {
            game.addPlayer(p2).players should be (Vector(p1,p2))
        }

        "add played card to cards" in {
            game.addCard(card).trickCards should be (List(card1,card))
        }

        "clear the Trick" in {
            game.clearTrick.trickCards should be (List())
        }

        "setup new trick" in {
            game.setTrick(p1,card) should be (Game(players = Vector(p1),trickCards = List(card1),highestCard = Some(card),currentWinner = Some(p1),currentPlayerIndex = Some(0)))
        }

        "set the first player" in {
            game.setFirstPlayer(p1).firstPlayer should be (Some(p1))
        }

        "set the scor required to win" in {
            game.setMaxScore(100).maxScore should be (Some(100))
        }

        "set the current player index" in {
            game.setCurrentPlayerIndex(1).currentPlayerIndex should be (Some(1))
        }

        "get the current player" in {
            game.getCurrentPlayer should be (Some(p1))
        }

        "set start with hearts" in {
            game.setStartWithHearts(true).startWithHearts should be (true)
        }

        "set first card" in {
            game.setFirstCard(false).firstCard should be (false)
        }

        "update a player" in {
            game.updatePlayer(0,p2).players should be (Vector(p2))
        }
        


        

    /*    "add player" in{
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
        */
    }
}
