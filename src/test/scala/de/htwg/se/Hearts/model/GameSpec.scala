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
    p1.hand ++= List(card1)

    "add player" in{
        val game = Game()
        game.addPlayer(p1)
        game.players should be (ListBuffer[Player](p1))
        game.addPlayer(p2)
        game.players should be (ListBuffer[Player](p1,p2))
    }
  }
}
