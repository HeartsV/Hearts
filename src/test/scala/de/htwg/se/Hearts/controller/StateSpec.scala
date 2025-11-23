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

        "process input for TitleScreen correctly" in {
            val game = Game()
            val gameController = Controller(game)
            gameController.state = TitleScreen(gameController)
            gameController.processInput("") should be (true)
        }
        "process input for MainScreen correctly" in {
            val game = Game()
            val gameController = Controller(game)
            gameController.state = GetPlayerNumberState(gameController)
            gameController.processInput("n") should be (true)
            gameController.processInput("r") should be (true)
            gameController.processInput("e") should be (true)
            gameController.processInput("") should be (false)
        }
        "process input for TitleScreen correctly" in {
            val game = Game()
            val gameController = Controller(game)
            gameController.state = TitleScreen(gameController)
            gameController.processInput("") should be (false)
            gameController.state should be (RulesScreen(gameController))
            gameController.processInput("b") should be (true)
            gameController.state should be (MainScreen(gameController))
        }

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
            val game = Game()
            val gameController = Controller(game)
            gameController.state = GetPlayerNamesState(gameController)
            game.playerNumber = Some(3)
            gameController.processInput("Alice") should be (true)
            game.players should contain (Player("Alice"))
            gameController.processInput("") should be (true)
            game.players should contain (Player("P2"))
            gameController.processInput("     ") should be (true)
            game.players should contain (Player("P3"))
            game.players.size should equal (game.playerNumber.get)
        }

        "process input for SetMaxScoreState correctly" in {
            val game = Game()
            val gameController = Controller(game)
            gameController.state = SetMaxScoreState(gameController)
            gameController.processInput("0") should be (false)
            gameController.processInput("") should be (true)
            game.maxScore should be (Some(100))
            gameController.processInput("1") should be (true)
            game.maxScore should be (Some(1))
            gameController.processInput("100") should be (true)
            game.maxScore should be (Some(100))
        }

        "process input for GamePlayState correctly" in {
            val p1 = Player("Alice")
            val p2 = Player("Dave")
            val game = Game()
            game.addPlayer(p1)
            game.currentPlayer = Some(p1)
            val gameController = Controller(game)
            gameController.state = GamePlayState(gameController)
            p1.hand ++= List(card1,card2)
            gameController.processInput("a") should be (false)
            gameController.processInput("1") should be (true)
            game.firstCard should be (false)
            gameController.processInput("1")
        }

        "process input for GameOverState correctly" in {
            val game = Game()
            val gameController = Controller(game)
            gameController.state = GameOverState(gameController)
            gameController.processInput("a") should be (true)
            gameController.state should be (GamePlayState(gameController))
            gameController.state = GameOverState(gameController)
            gameController.processInput("n") should be (true)
            gameController.state should be (GetPlayerNumberState(gameController))
            gameController.state = GameOverState(gameController)
            gameController.processInput("q") should be (true)
            gameController.state should be (MainScreen(gameController))
            gameController.state = GameOverState(gameController)
            gameController.processInput("e") should be (true)
            gameController.state = GameOverState(gameController)
            gameController.processInput("") should be (false)
            gameController.state should be (GameOverState(gameController))
        }
    }

}