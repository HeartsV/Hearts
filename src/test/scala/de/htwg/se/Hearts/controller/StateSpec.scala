package de.htwg.se.Hearts.controller

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.Hearts.model.*

class StateSpec extends AnyWordSpec with Matchers{

    "A State" should {
        val card1 = Card(Rank.Two,Suit.Clubs)
        val card2 = Card(Rank.Two,Suit.Diamonds)
        val card3 = Card(Rank.Ace,Suit.Clubs)
        val card4 = Card(Rank.Ace,Suit.Diamonds)
        val card5 = Card(Rank.Jack,Suit.Hearts)
        val card6 = Card(Rank.Jack,Suit.Hearts)
        val card7 = Card(Rank.Ten,Suit.Clubs)
        val card8 = Card(Rank.Ten,Suit.Diamonds)

        "process input for GetPlayerNumberState correctly" in {
            val game = Game()
            val gameController = Controller(game)
            gameController.state = GetPlayerNumberState(gameController)
            gameController.processInput("2") should be (false)
            gameController.processInput("5") should be (false)
            game.playerNumber should be (None)
            gameController.processInput("3") should be (true)
            game.playerNumber should be (Some(3))
        }

        "process input for GetPlayerNamesState correctly" in {

        }

        "process input for SetMaxScoreState correctly" in {

        }

        "process input for GamePlayState correctly" in {
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

        "process input for GameOverState correctly" in {}
    }

}