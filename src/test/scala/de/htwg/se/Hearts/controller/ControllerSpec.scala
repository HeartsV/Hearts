package de.htwg.se.Hearts.controller

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.Hearts.model.*
import scalafx.scene.input.KeyCode.G


class ControllerSpec extends AnyWordSpec with Matchers {
    "A Controller" should {
        val card1 = Card(Rank.Two,Suit.Clubs)
        val card2 = Card(Rank.Two,Suit.Diamonds)
        val card3 = Card(Rank.Ace,Suit.Clubs)
        val card4 = Card(Rank.Ace,Suit.Diamonds)
        val card5 = Card(Rank.Jack,Suit.Hearts)
        val card6 = Card(Rank.Jack,Suit.Clubs)
        val card7 = Card(Rank.Ten,Suit.Clubs)
        val card8 = Card(Rank.Ten,Suit.Diamonds)
        val p1 = Player("Alice",List(card1,card2),List(card5), points = 1)
        val p2 = Player("Dave",List(card3,card4))
        val p3 = Player("Charlie",List(card6, card7))
        val game2PlayersNoHandCards = Game(players = Vector(Player("A"), Player("B")), playerNumber = Some(2))
        val game3Players = Game(playerNumber = Some(3),players = Vector(p1,p2,p3),currentPlayerIndex = Some(0))
        val gameFirstCard = Game(playerNumber = Some(2),players = Vector(p1,p2),currentPlayerIndex = Some(0))
        val gameSecondPlayer = Game(playerNumber = Some(2),players = Vector(p1,p2), currentPlayerIndex = Some(1), firstCard = false)
        val gameNoHearts = Game(playerNumber = Some(2),players = Vector(p1,p2),firstCard = false,currentPlayerIndex = Some(0))
        val gameHearts = Game(playerNumber = Some(2),players = Vector(p1,p2),firstCard = false,startWithHearts = true, currentPlayerIndex = Some(0))
        val gameWithTrick = Game(playerNumber = Some(2),players = Vector(p1,p2),firstCard = false, currentPlayerIndex = Some(0),trickCards = List(card7), highestCard = Some(card7),currentWinnerIndex = Some(0))
        val controllerSecondPlayer = Controller(gameSecondPlayer)
        val controller3Players = Controller(game3Players)
        val controllerHearts = Controller(gameHearts)
        val contollerTrick = Controller(gameWithTrick)
        val gameController = Controller(gameFirstCard)
        val state = GamePlayState(gameController)


        "change state" in {
            gameController.changeState(state)
            gameController.state should be (state)
        }

        "return current winner" in {
            contollerTrick.updateCurrentWinner((1,card3)) should be (Some(1),Some(card3))
            controllerHearts.updateCurrentWinner((0,card3)) should be (Some(0),Some(card3))
            contollerTrick.updateCurrentWinner((1,card2)) should be (Some(0),Some(card7))
        }

        "update current player" in {
            gameController.updateCurrentPlayer should be (0)
            controllerSecondPlayer.updateCurrentPlayer should be (0)
        }

        "output the correct strings for played Cards" in {
            contollerTrick.trickToString should be ("| 10\u2663 |")
            gameController.trickToString should be ("|")
        }

        "complete Trick String for three players" in{
            contollerTrick.completeTrickString should be ("| 10\u2663 |     |")
        }

        "filter the correct Card out" in {
            controller3Players.filterOneCardOut(List(card1, card2)) should be (List(card1))
            controller3Players.filterOneCardOut(List(card2, card1)) should be (List(card1))
            gameController.filterOneCardOut(List(card1, card2)) should be (List(card1, card2))
        }

        "deal the cards correctly" in {
            gameController.dealCards(List(card1, card2), game2PlayersNoHandCards) should be (Vector(Player("A", List(card1)), Player("B", List(card2))))
        }

        "points for cards" in {
            gameController.cardPoints(card5) should be (1)
            gameController.cardPoints(Card(Rank.Queen, Suit.Spades)) should be (13)
            gameController.cardPoints(card1) should be (0)
        }

        "calculate points for a single player" in {
            gameController.pointsForPlayer(p1) should be (1)
        }

        "match players and points to map" in {
            gameController.rawPointsPerPlayer should be (Map((p1, 1),(p2, 0)))
        }

        "applyShootingTheMoon correctly" in {
            gameController.applyShootingTheMoon should be (Map((p1, 0),(p2, 1)))
        }

        "getPlayersWithPoints correctly" in {
            gameController.getPlayersWithPoints should be (List(("Alice", 1), ("Dave", 0)))
        }

        "rankPlayers correctly" in {
            gameController.rankPlayers(List(("Alice", 1), ("Dave", 0), ("Charlie", 0))) should be (List((1, "Dave", 0), (1, "Charlie", 0), (3, "Alice", 1)))
        }

        "get the handstring of current player" in{
            gameController.handToString should be ("|  1  |  2  |\n| 2 \u2666 | 2 \u2663 |")
        }

        "get the correct CurrentPlayerName" in {
            gameController.getCurrentPlayerName should be ("Alice")
        }

        "get keepProcessRunning correctly" in {
            gameController.getKeepProcessRunning should be (true)
        }
    }
}