package de.htwg.se.Hearts.controller

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.Hearts.model._

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
        val p1 = Player("Alice", List(card1, card2, card3, card4), List(card5))
        val p2 = Player("Dave",List(card3,card4,card5))
        val p3 = Player("Charlie",List(card6,card7, card8))
        val gameFirstCard = Game(playerNumber = Some(2),players = Vector(p1,p2),currentPlayerIndex = Some(0))
        val gameNoHearts = Game(playerNumber = Some(2),players = Vector(p1,p2),firstCard = false,currentPlayerIndex = Some(0))
        val gameHearts = Game(playerNumber = Some(2),players = Vector(p1,p2),firstCard = false,startWithHearts = true,currentPlayerIndex = Some(0))
        val gameWithTrick = Game(playerNumber = Some(2),players = Vector(p1,p2),firstCard = false, currentPlayerIndex = Some(0),trickCards = List(card7))
        val gameController = Controller(game = Game())

        "process input for MainScreenState correctly" in {
            gameController.state = MainScreenState(gameController)
            gameController.processInput("n")
            gameController.state.getStateString should be ("GetPlayerNumberState")
            gameController.state = MainScreenState(gameController)
            gameController.processInput("r")
            gameController.state.getStateString should be ("RulesScreenState")
            gameController.state = MainScreenState(gameController)
            gameController.processInput("e")
            gameController.game.keepProcessRunning should be (false)
            gameController.processInput("")
            gameController.state.getStateString should be ("MainScreenState")
        }

        "process input for RuleScreenState correctly" in {
            val game = Game()
            val gameController = Controller(game)
            gameController.state = RulesScreenState(gameController)
            gameController.processInput("")
            gameController.state.getStateString should be ("RulesScreenState")
            gameController.processInput("b")
            gameController.state.getStateString should be ("MainScreenState")
            gameController.game = game.copy(players = Vector(p1, p2, p3))
            gameController.state = RulesScreenState(gameController)
            gameController.processInput("b")
            gameController.state.getStateString should be ("GamePlayState")
        }

        "process input for GetPlayerNumberState correctly" in {
            val game = Game()
            val gameController = Controller(game)
            gameController.state = GetPlayerNumberState(gameController)
            gameController.processInput("2")
            gameController.state.getStateString should be ("GetPlayerNumberState")
            gameController.processInput("5")
            gameController.state.getStateString should be ("GetPlayerNumberState")
            gameController.game.playerNumber should be (None)
            gameController.processInput("3")
            gameController.game.playerNumber should be (Some(3))
            gameController.state.getStateString should be ("GetPlayerNamesState")
        }

        "process input for GetPlayerNamesState correctly" in {
            val game = Game(playerNumber = Some(3))
            val gameController = Controller(game)
            gameController.state = GetPlayerNamesState(gameController)
            gameController.processInput("Alice")
            gameController.game.players(0).name should be ("Alice")
            gameController.state.getStateString should be ("GetPlayerNamesState")
            gameController.processInput("")
            gameController.game.players(1).name should be ("P2")
            gameController.state.getStateString should be ("GetPlayerNamesState")
            gameController.processInput("     ")
            gameController.game.players(2).name should be ("P3")
            gameController.game.players.size should equal (game.playerNumber.get)
            gameController.state.getStateString should be ("SetMaxScoreState")
        }

        "process input for SetMaxScoreState correctly" in {
            val game = Game()
            val gameController = Controller(game)
            gameController.state = SetMaxScoreState(gameController)
            gameController.processInput("0")
            gameController.state.getStateString should be ("SetMaxScoreState")
            gameController.processInput("401")
            gameController.state.getStateString should be ("SetMaxScoreState")
            gameController.processInput("")
            gameController.game.maxScore should be (Some(100))
            gameController.state.getStateString should be ("GamePlayState")
            gameController.state = SetMaxScoreState(gameController)
            gameController.processInput("1")
            gameController.game.maxScore should be (Some(1))
            gameController.state.getStateString should be ("GamePlayState")
            gameController.state = SetMaxScoreState(gameController)
            gameController.processInput("100")
            gameController.game.maxScore should be (Some(100))
            gameController.state.getStateString should be ("GamePlayState")
        }

        "process input for GamePlayState correctly" in {
            val game = Game(Some(3), false, true, true, Vector(p1, p2, p3), Some(100), Some(0), List())
            val gameController = Controller(game)
            gameController.state = GamePlayState(gameController)
            gameController.processInput("a")
            gameController.game.firstCard should be (true)
            gameController.processInput("1")
            gameController.game.firstCard should be (false)
            gameController.processInput("1")
            gameController.processInput("1")
            gameController.game.trickCards.size should be (3)
        }

        "be able to change sortingStrategies" in {
            val p1 = Player("Alice",(List(card1, card2, card3, card4, card5, card6, card7, card8)))
            val game = Game(players = Vector(p1), currentPlayerIndex = Some(0))
            val gameController = Controller(game)
            gameController.state = GamePlayState(gameController)
            gameController.processInput("suit")
            val sortedSuit = List(card5, card2, card8, card4, card1, card7, card6, card3)
            gameController.sortingStrategy.execute(gameController.game.getCurrentPlayer.get) should equal (sortedSuit)
            gameController.processInput("rank")
            val sortedRank = List(card2, card1, card8, card7, card5, card6, card4, card3)
            gameController.sortingStrategy.execute(gameController.game.getCurrentPlayer.get) should equal (sortedRank)
        }

        "process input for GamePlayState correctly when someone reaches max score" in {
            val game = Game(maxScore = Some(0), players = Vector(p1, p2, p3), playerNumber = Some(3), currentPlayerIndex = Some(0))
            val gameController = Controller(game)
            gameController.state = GamePlayState(gameController)
            gameController.processInput("a") should be (game)
            gameController.processInput("1") should be (Game(maxScore = Some(0), players = Vector(p1, p2, p3), playerNumber = Some(3), currentPlayerIndex = Some(0), firstCard = false))
            gameController.processInput("1")
            gameController.processInput("1").trickCards.size should be (3)
        }

        "process input for ShowScoreState correctly" in {
            val game = Game(players = Vector(p1, p2, p3), firstCard = false, startWithHearts = true, maxScore = Some(100), playerNumber = Some(3), currentPlayerIndex = Some(0))
            val gameController = Controller(game)
            gameController.state = ShowScoreState(gameController)
            gameController.state.getStateString should be ("ShowScoreState")
            gameController.processInput("a")
            gameController.state.getStateString should be ("GamePlayState")
            gameController.game.firstCard should be (true)
            gameController.game.startWithHearts should be (false)
            gameController.game.players(0).hand.size should be (17)

        }

        "process input for GameOverState correctly" in {
            val game = Game()
            val gameController = Controller(game)
            gameController.state = GameOverState(gameController)
            gameController.processInput("")
            gameController.state.getStateString should be ("GameOverState")
            gameController.processInput("e")
            gameController.game.keepProcessRunning should be (false)
        }

        "process again input for GameOverState" in {
            val game = Game(players = Vector(p1, p2, p3), firstCard = false, startWithHearts = true, maxScore = Some(100), playerNumber = Some(3), currentPlayerIndex = Some(0))
            val gameController = Controller(game)
            gameController.state = GameOverState(gameController)
            gameController.processInput("a")
            gameController.state.getStateString should be ("GamePlayState")
            gameController.game.firstCard should be (true)
            gameController.game.startWithHearts should be (false)
            gameController.game.players(0).hand.size should be (17)
            p1.points should be (0)
        }
        "procss new input for GameOverState" in {
            val game = Game(players = Vector(p1, p2, p3), firstCard = false, startWithHearts = true, maxScore = Some(100), playerNumber = Some(3), currentPlayerIndex = Some(0))
            val gameController = Controller(game)
            gameController.state = GameOverState(gameController)
            gameController.processInput("n")
            gameController.game.firstCard should be (true)
            gameController.game.startWithHearts should be (false)
            gameController.game.players should be (Vector.empty)
            gameController.game.playerNumber should be (None)
            gameController.game.maxScore should be (None)
            gameController.game.currentPlayerIndex should be (None)
            gameController.state.getStateString should be ("GetPlayerNumberState")
        }
        "process quit input for GameOverState" in {
            val game = Game(players = Vector(p1, p2, p3), firstCard = false, startWithHearts = true, maxScore = Some(100), playerNumber = Some(3), currentPlayerIndex = Some(0))
            val gameController = Controller(game)
            gameController.state = GameOverState(gameController)
            gameController.processInput("q")
            gameController.game.firstCard should be (true)
            gameController.game.startWithHearts should be (false)
            gameController.game.players should be (Vector.empty)
            gameController.game.playerNumber should be (None)
            gameController.game.maxScore should be (None)
            gameController.game.currentPlayerIndex should be (None)
            gameController.state.getStateString should be ("MainScreenState")
        }
    }
}