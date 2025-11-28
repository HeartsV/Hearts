package de.htwg.se.Hearts.controller

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.Hearts.model.*
import scala.collection.mutable.ListBuffer

class StateSpec extends AnyWordSpec with Matchers{

    "A State" should {
        val card1 = Card(Rank.Two,Suit.Clubs)
        val card2 = Card(Rank.Two,Suit.Diamonds)
        val card3 = Card(Rank.Ace,Suit.Clubs)
        val card4 = Card(Rank.Ace,Suit.Diamonds)
        val card5 = Card(Rank.Jack,Suit.Hearts)
        val card6 = Card(Rank.Jack,Suit.Clubs)
        val card7 = Card(Rank.Ten,Suit.Clubs)
        val card8 = Card(Rank.Ten,Suit.Diamonds)

        "process input for MainScreenState correctly" in {
            val game = Game()
            val gameController = Controller(game)
            gameController.state = MainScreenState(gameController)
            gameController.processInput("n") should be (true)
            gameController.state = MainScreenState(gameController)
            gameController.processInput("r") should be (true)
            gameController.state = MainScreenState(gameController)
            gameController.processInput("e") should be (true)
            gameController.processInput("") should be (false)
            gameController.state.getStateString() should be ("MainScreenState")
        }
        "process input for RuleScreenState correctly" in {
            val game = Game()
            val gameController = Controller(game)
            gameController.state = RulesScreenState(gameController)
            gameController.processInput("") should be (false)
            gameController.state.getStateString() should be ("RulesScreenState")
            gameController.processInput("b") should be (true)
            gameController.state.getStateString() should be ("MainScreenState")
        }

        "process input for GetPlayerNumberState correctly" in {
            val game = Game()
            val gameController = Controller(game)
            gameController.state = GetPlayerNumberState(gameController)
            gameController.processInput("2") should be (false)
            gameController.processInput("5") should be (false)
            gameController.state.getStateString() should be ("GetPlayerNumberState")
            game.playerNumber should be (None)
            gameController.processInput("3") should be (true)
            game.playerNumber should be (Some(3))
            gameController.state.getStateString() should be ("GetPlayerNamesState")
        }

        "process input for GetPlayerNamesState correctly" in {
            val game = Game()
            val gameController = Controller(game)
            gameController.state = GetPlayerNamesState(gameController)
            game.playerNumber = Some(3)
            gameController.processInput("Alice") should be (true)
            game.players(0).name should be ("Alice")
            gameController.state.getStateString() should be ("GetPlayerNamesState")
            gameController.processInput("") should be (true)
            game.players(1).name should be ("P2")
            gameController.state.getStateString() should be ("GetPlayerNamesState")
            gameController.processInput("     ") should be (true)
            game.players(2).name should be ("P3")
            game.players.size should equal (game.playerNumber.get)
            gameController.state.getStateString() should be ("SetMaxScoreState")

        }

        "process input for SetMaxScoreState correctly" in {
            val game = Game()
            val gameController = Controller(game)
            gameController.state = SetMaxScoreState(gameController)
            gameController.processInput("0") should be (false)
            gameController.state.getStateString() should be ("SetMaxScoreState")
            gameController.processInput("") should be (true)
            game.maxScore should be (Some(100))
            gameController.state.getStateString() should be ("GamePlayState")
            gameController.state = SetMaxScoreState(gameController)
            gameController.processInput("1") should be (true)
            game.maxScore should be (Some(1))
            gameController.state.getStateString() should be ("GamePlayState")
            gameController.state = SetMaxScoreState(gameController)
            gameController.processInput("100") should be (true)
            game.maxScore should be (Some(100))
            gameController.state.getStateString() should be ("GamePlayState")
        }

        "process input for GamePlayState correctly" in {
            val p1 = Player("Alice")
            val p2 = Player("Bob")
            val p3 = Player("Charlie")
            val game = Game()
            game.maxScore = Some(100)
            game.addPlayer(p1)
            game.addPlayer(p2)
            game.addPlayer(p3)
            game.playerNumber = Some(3)
            game.currentPlayer = Some(p1)
            val gameController = Controller(game)
            gameController.state = GamePlayState(gameController)
            p1.hand ++= List(card1)
            p2.hand ++= List(card1)
            p3.hand ++= List(card1)
            gameController.processInput("a") should be (false)
            gameController.processInput("1") should be (true)
            game.firstCard should be (false)
            gameController.processInput("1") should be (true)
            gameController.processInput("1") should be (true)
        }

        "be able to change sortingStrategies" in {
            val game = Game()
            val gameController = Controller(game)
            val p1 = Player("Alice")
            p1.hand ++= List(card1, card2, card3, card4, card5, card6, card7, card8)
            gameController.state = GamePlayState(gameController)
            game.currentPlayer = Some(p1)
            gameController.processInput("suit")
            val sortedHand = List(card5, card2, card8, card4, card1, card7, card6, card3)
            gameController.sortingStrategy.execute(game.currentPlayer.get) should equal (sortedHand)

        }

        "process input for GamePlayState correctly when someone reaches max score" in {
            val p1 = Player("Alice")
            val p2 = Player("Bob")
            val p3 = Player("Charlie")
            val game = Game()
            game.maxScore = Some(0)
            game.addPlayer(p1)
            game.addPlayer(p2)
            game.addPlayer(p3)
            game.playerNumber = Some(3)
            game.currentPlayer = Some(p1)
            val gameController = Controller(game)
            gameController.state = GamePlayState(gameController)
            p1.hand ++= List(card1)
            p2.hand ++= List(card1)
            p3.hand ++= List(card1)
            gameController.processInput("a") should be (false)
            gameController.processInput("1") should be (true)
            game.firstCard should be (false)
            gameController.processInput("1") should be (true)
            gameController.processInput("1") should be (true)
        }

        "process input for ShowScoreState correctly" in {
            val p1 = Player("Alice")
            val p2 = Player("Bob")
            val p3 = Player("Charlie")
            val game = Game()
            game.firstCard = false
            game.startWithHearts = true
            game.maxScore = Some(100)
            game.addPlayer(p1)
            game.addPlayer(p2)
            game.addPlayer(p3)
            game.playerNumber = Some(3)
            game.currentPlayer = Some(p1)
            val gameController = Controller(game)
            gameController.state = ShowScoreState(gameController)
            gameController.state.getStateString() should be ("ShowScoreState")
            gameController.processInput("a")
            gameController.state.getStateString() should be ("GamePlayState")
            game.firstCard should be (true)
            game.startWithHearts should be (false)
            game.players(0).hand.size should be (17)

        }

        "process input for GameOverState correctly" in {
            val game = Game()
            val gameController = Controller(game)
            gameController.state = GameOverState(gameController)
            gameController.processInput("") should be (false)
            gameController.state.getStateString() should be ("GameOverState")
            gameController.processInput("e") should be (true)
            game.keepProcessRunning should be (false)
        }

        "process again input for GameOverState" in {
            val p1 = Player("Alice")
            val p2 = Player("Bob")
            val p3 = Player("Charlie")
            p1.points = 20
            val game = Game()
            game.firstCard = false
            game.startWithHearts = true
            game.maxScore = Some(100)
            game.addPlayer(p1)
            game.addPlayer(p2)
            game.addPlayer(p3)
            game.playerNumber = Some(3)
            game.currentPlayer = Some(p1)
            val gameController = Controller(game)
            gameController.state = GameOverState(gameController)
            gameController.processInput("a")
            gameController.state.getStateString() should be ("GamePlayState")
            game.firstCard should be (true)
            game.startWithHearts should be (false)
            game.players(0).hand.size should be (17)
            p1.points should be (0)
        }
        "procss new input for GameOverState" in {
            val p1 = Player("Alice")
            val p2 = Player("Bob")
            val p3 = Player("Charlie")
            p1.points = 20
            val game = Game()
            game.firstCard = false
            game.startWithHearts = true
            game.maxScore = Some(100)
            game.addPlayer(p1)
            game.addPlayer(p2)
            game.addPlayer(p3)
            game.playerNumber = Some(3)
            game.currentPlayer = Some(p1)
            val gameController = Controller(game)
            gameController.state = GameOverState(gameController)
            gameController.processInput("n")
            game.firstCard should be (true)
            game.startWithHearts should be (false)
            game.players should be (ListBuffer[Player]())
            game.playerNumber should be (None)
            game.maxScore should be (None)
            game.currentPlayer should be (None)
            gameController.state.getStateString() should be ("GetPlayerNumberState")
        }
        "process quit input for GameOverState" in {
            val p1 = Player("Alice")
            val p2 = Player("Bob")
            val p3 = Player("Charlie")
            p1.points = 20
            val game = Game()
            game.firstCard = false
            game.startWithHearts = true
            game.maxScore = Some(100)
            game.addPlayer(p1)
            game.addPlayer(p2)
            game.addPlayer(p3)
            game.playerNumber = Some(3)
            game.currentPlayer = Some(p1)
            val gameController = Controller(game)
            gameController.state = GameOverState(gameController)
            gameController.processInput("q")
            game.firstCard should be (true)
            game.startWithHearts should be (false)
            game.players should be (ListBuffer[Player]())
            game.playerNumber should be (None)
            game.maxScore should be (None)
            game.currentPlayer should be (None)
            gameController.state.getStateString() should be ("MainScreenState")
        }
    }
}