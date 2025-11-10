package de.htwg.se.Hearts.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import scala.collection.mutable.ListBuffer

class GameSpec extends AnyWordSpec with Matchers {
  "A Game" should {
    val p1 = Player("Alice")
    val p2 = Player("Bob")
    val card1 = Card(Rank.Two,Suit.Clubs)
    val card2 = Card(Rank.Two,Suit.Diamonds)
    p1.hand += (card1)

    "add player" in{
        val game = Game()
        game.addPlayer(p1)
        game.players should be (ListBuffer[Player](p1))
        game.addPlayer(p2)
        game.players should be (ListBuffer[Player](p1,p2))
    }

    "update current player for first card" in {
        val game = Game()
        game.addPlayer(p1)
        game.addPlayer(p2)
        game.updateCurrentPlayer()
        game.currentPlayer should be (Some(p1))
    }

    "update current player for normal case" in  {
        val game = Game()
        game.addPlayer(p1)
        game.addPlayer(p2)
        game.updateCurrentPlayer()
        game.firstCard = false
        game.updateCurrentPlayer()
        game.currentPlayer should be (Some(p2))

    }

    "update current player when trick is full" in{
        val game = Game()
        game.addPlayer(p1)
        game.addPlayer(p2)
        game.trick.currentWinner = Some(p2)
        game.updateCurrentPlayer()
        game.firstCard = false
        game.updateCurrentPlayer()
        game.trick.cards += (card1,card2)
        game.updateCurrentPlayer()
        game.currentPlayer should be (Some(p2))
    }

    "loop over list of players" in {
        val game = Game()
        game.firstCard = false
        game.addPlayer(p1)
        game.addPlayer(p2)
        game.currentPlayer = Some(p2)
        game.updateCurrentPlayer()
        game.currentPlayer should be (Some(p1))
    }
  }
}
