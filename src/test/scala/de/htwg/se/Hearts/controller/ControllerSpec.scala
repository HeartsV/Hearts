package de.htwg.se.Hearts.controller

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.Hearts.model.*

class ControllerSpec extends AnyWordSpec with Matchers {
    "A Controller" should {
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

        "play cards only if input is valid" in{
            val p1 = Player("Alice")
            val p2 = Player("Dave")
            val game = Game()
            game.firstCard = false
            game.addPlayer(p1)
            p1.hand ++= List(card1,card2)
            game.currentPlayer = Some(p1)
            val gameController = Controller(game)
            gameController.playCard(3) should be (false)
            gameController.playCard(1) should be (true)
        }

        "add and remove cards for players on first card" in{
            val game = Game()
            val gameController = Controller(game)
            gameController.addCard(card2) should be (false)
            gameController.addCard(card1) should be (true)
        }

        "add and remove cards for players and trick when card allowed" in{
            val p1 = Player("Alice")
            val p2 = Player("Dave")
            val game = Game()
            game.addPlayer(p1)
            game.addPlayer(p2)
            p1.hand ++= List(card1,card2)
            p2.hand ++= List(card3,card4)
            game.firstCard = false
            game.currentPlayer = Some(p1)
            val gameController = Controller(game)
            gameController.addCard(card1) should be (true)
            gameController.updateCurrentPlayer()
            gameController.addCard(card4) should be (false)
            gameController.addCard(card3) should be (true)
        }

        "add and remove cards for players and trick when card is hearts" in{
            val p1 = Player("Alice")
            val p2 = Player("Dave")
            val game = Game()
            game.addPlayer(p1)
            game.addPlayer(p2)
            p1.hand ++= List(card1,card5)
            p2.hand ++= List(card5,card6)
            game.firstCard = false
            game.currentPlayer = Some(p1)
            val gameController = Controller(game)
            gameController.addCard(card5) should be (false)
            gameController.addCard(card1) should be (true)
            gameController.updateCurrentPlayer()
            game.startWithHearts should be (false)
            gameController.addCard(card6) should be (true)
            game.startWithHearts should be (true)
        }

        "update current winner in trick" in{
            val p1 = Player("Alice")
            val p2 = Player("Dave")
            val game = Game()
            val gameController = Controller(game)
            game.addPlayer(p1)
            game.addPlayer(p2)
            p1.hand ++= List(card1,card5)
            p2.hand ++= List(card3,card6)
            gameController.updateCurrentPlayer()
            gameController.addCard(card1)
            gameController.updateCurrentWinner() should be (true)
            game.firstCard = false
            game.trick.currentWinner should be (Some(p1))
            gameController.updateCurrentPlayer()
            gameController.addCard(card3)
            gameController.updateCurrentWinner() should be (true)
            game.trick.currentWinner should be (Some(p2))
            gameController.updateCurrentPlayer()
        }

        "not update current winner when unnecessary" in {
            val p1 = Player("Alice")
            val p2 = Player("Dave")
            val game = Game()
            game.addPlayer(p1)
            game.addPlayer(p2)
            p1.hand ++= List(card3,card5)
            p2.hand ++= List(card1,card6)
            game.currentPlayer = Some(p1)
            game.firstCard = false
            val gameController = Controller(game)
            gameController.addCard(card3)
            gameController.updateCurrentWinner()
            gameController.updateCurrentPlayer()
            gameController.addCard(card1)
            gameController.updateCurrentWinner() should be (false)
        }

        "complete Trick String for three players" in{
            val p1 = Player("Alice")
            val p2 = Player("Dave")
            val p3 = Player("Charlie")
            val game = Game()
            game.firstCard = false
            game.currentPlayer = Some(p1)
            val gameController = Controller(game)
            game.addPlayer(p1)
            game.addPlayer(p2)
            game.addPlayer(p3)
            gameController.completeTrickString() should be ("|     |     |     |")
            gameController.addCard(Card(Rank.Two, Suit.Diamonds))
            gameController.completeTrickString() should be ("| 2 \u2666 |     |     |")
            gameController.addCard(Card(Rank.Ten, Suit.Diamonds))
            gameController.completeTrickString() should be ("| 2 \u2666 | 10\u2666 |     |")
            gameController.addCard(Card(Rank.Three, Suit.Diamonds))
            gameController.completeTrickString() should be ("| 2 \u2666 | 10\u2666 | 3 \u2666 |")
        }

        "complete Trick String for four players" in {
            val p1 = Player("Alice")
            val p2 = Player("Dave")
            val p3 = Player("Charlie")
            val p4 = Player("David")
            val game = Game()
            game.firstCard = false
            game.currentPlayer = Some(p1)
            val gameController = Controller(game)
            game.addPlayer(p1)
            game.addPlayer(p2)
            game.addPlayer(p3)
            game.addPlayer(p4)
            gameController.completeTrickString() should be ("|     |     |     |     |")
            gameController.addCard(Card(Rank.Two, Suit.Diamonds))
            gameController.completeTrickString() should be ("| 2 \u2666 |     |     |     |")
            gameController.addCard(Card(Rank.Ten, Suit.Diamonds))
            gameController.completeTrickString() should be ("| 2 \u2666 | 10\u2666 |     |     |")
            gameController.addCard(Card(Rank.Three, Suit.Diamonds))
            gameController.completeTrickString() should be ("| 2 \u2666 | 10\u2666 | 3 \u2666 |     |")
            gameController.addCard(Card(Rank.Queen, Suit.Diamonds))
            gameController.completeTrickString() should be ("| 2 \u2666 | 10\u2666 | 3 \u2666 | Q \u2666 |")
        }

        "give won cards to the correct player" in {
            val p1 = Player("Alice")
            val p2 = Player("Dave")
            val game = Game()
            game.addPlayer(p1)
            game.addPlayer(p2)
            p1.hand ++= List(card1,card2)
            p2.hand ++= List(card3,card4)
            game.currentPlayer = Some(p1)
            game.firstCard = false
            val gameController = Controller(game)
            gameController.playCard(1)
            gameController.updateCurrentWinner()
            gameController.updateCurrentPlayer()
            gameController.playCard(1)
            gameController.updateCurrentWinner()
            gameController.updateCurrentPlayer()
            game.currentPlayer should be (Some(p2))
            gameController.playCard(1)
            game.players(1).wonCards should be (List(card1,card3))

        }

        "get the handstring of current player" in{
            val game = Game()
            val gameController = Controller(game)
            val p1 = Player(name = "Alice")
            game.currentPlayer = Some(p1)
            p1.hand ++= List(card1,card2)
            gameController.getCurrentPlayerHand() should be ("|  1  |  2  |\n| 2 \u2663 | 2 \u2666 |")
        }
        "get the name of current player" in{
            val game = Game()
            val gameController = Controller(game)
            val p1 = Player(name = "Alice")
            game.currentPlayer = Some(p1)
            gameController.getCurrentPlayerName() should be ("Alice")
        }

        "update current player for first card" in {
            val game = Game()
            val gameController = Controller(game)
            val p1 = Player("Alice")
            val p2 = Player("Dave")
            p1.hand ++= List(card1,card6)
            game.addPlayer(p1)
            game.addPlayer(p2)
            p2.hand ++= List(card3,card5)
            gameController.updateCurrentPlayer() should be (true)
            game.currentPlayer should be (Some(p1))
            game
        }

        "update current player for normal case" in  {
            val game = Game()
            val gameController = Controller(game)
            val p1 = Player("Alice")
            val p2 = Player("Dave")
            game.addPlayer(p1)
            game.addPlayer(p2)
            p1.hand ++= List(card1,card6)
            p2.hand ++= List(card3,card5)
            gameController.updateCurrentPlayer()
            game.firstCard = false
            gameController.updateCurrentPlayer()
            game.currentPlayer should be (Some(p2))
        }

        "update current player when trick is full" in{
            val game = Game()
            val gameController = Controller(game)
            val p1 = Player("Alice")
            val p2 = Player("Dave")
            game.addPlayer(p1)
            game.addPlayer(p2)
            p1.hand ++= List(card1,card6)
            p2.hand ++= List(card3,card5)
            game.trick.currentWinner = Some(p2)
            gameController.updateCurrentPlayer()
            game.firstCard = false
            gameController.updateCurrentPlayer()
            game.trick.cards ++= List(card1,card2)
            gameController.updateCurrentPlayer()
            game.currentPlayer should be (Some(p2))
        }

        "loop over list of players" in {
            val game = Game()
            val gameController = Controller(game)
            val p1 = Player("Alice")
            val p2 = Player("Dave")
            game.firstCard = false
            game.addPlayer(p1)
            game.addPlayer(p2)
            p1.hand ++= List(card1,card6)
            p2.hand ++= List(card3,card5)
            game.currentPlayer = Some(p2)
            gameController.updateCurrentPlayer()
            game.currentPlayer should be (Some(p1))
        }

        "check if the game is over" in{

        }
    }
}
