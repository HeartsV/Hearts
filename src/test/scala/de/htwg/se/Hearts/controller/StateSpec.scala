package de.htwg.se.Hearts.controller

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.Hearts.model.*

class StateSpec extends AnyWordSpec with Matchers{
	"A GamePlayState" should {
        val card1 = Card(Rank.Two,Suit.Clubs)
        val card2 = Card(Rank.Two,Suit.Diamonds)
        val card3 = Card(Rank.Ace,Suit.Clubs)
        val card4 = Card(Rank.Ace,Suit.Diamonds)
        val card5 = Card(Rank.Jack,Suit.Hearts)
        val card6 = Card(Rank.Jack,Suit.Hearts)
        val card7 = Card(Rank.Ten,Suit.Clubs)
        val card8 = Card(Rank.Ten,Suit.Diamonds)

        "process input correctly" in {
            val p1 = Player("Alice")
            val p2 = Player("Dave")
            val game = Game()
            game.addPlayer(p1)
            game.currentPlayer = Some(p1)
            val gameController = Controller(game)
            p1.hand ++= List(card1,card2)
            gameController.processInput("a") should be (false)
            gameController.processInput("1") should be (true)
            game.firstCard should be (false)
            gameController.processInput("1")
        }
    }
}