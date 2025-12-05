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
        val p1 = Player("Alice",List(card1,card2),List(card5))
        val p2 = Player("Dave",List(card3,card4))
        val gamefirstCard = Game(playerNumber = Some(2),players = Vector(p1,p2),currentPlayerIndex = Some(0))
        val gameNoHearts = Game(playerNumber = Some(2),players = Vector(p1,p2),firstCard = false,currentPlayerIndex = Some(0))
        val gameHearts = Game(playerNumber = Some(2),players = Vector(p1,p2),firstCard = false,startWithHearts = true, currentPlayerIndex = Some(0))

        "check if card allowed" in {
            val gameController = Controller(game = gameHearts)
            gameController.cardAllowed(3) should be (false)
            gameController.cardAllowed(1) should be (true)
        }

        "check if card is played" in {
            val gameController = Controller(game = gameHearts)
            gameController.playCard(0).trickCards should be (List(card1))
            gameController.playCard(0).players should be (Vector(Player("Alice", List(card2), List(card5)),p2))
            gameController.playCard(5) should be (Game(playerNumber = Some(2),players = Vector(p1,p2),firstCard = false, startWithHearts = true, currentPlayerIndex = Some(0)))
        }

        /*"play cards only if input is valid" in{
            val p1 = Player("Alice")
            val p2 = Player("Dave")
            val game = Game()
            val gameController = Controller(game)
            gameController.game = gameController.game.setFirstCard(false)
            gameController.game = gameController.game.addPlayer(p1)
            p1.addAllCards(List(card1,card2))
            gameController.game = gameController.game.setCurrentPlayerIndex(Some(gameController.game.players.indexWhere(p => p.equals(p1))))
        }

        "add and remove cards for players on first card" in{
            val game = Game()
            val gameController = Controller(game)
        }

        "add and remove cards for players and trick when card allowed" in{
            val p1 = Player("Alice")
            val p2 = Player("Dave")
            val game = Game()
            val gameController = Controller(game)
            gameController.game = gameController.game.addPlayer(p1)
            gameController.game = gameController.game.addPlayer(p2)
            p1.addAllCards(List(card1,card2))
            p2.addAllCards(List(card3,card4))
            gameController.game = gameController.game.setFirstCard(false)
            gameController.game = gameController.game.setCurrentPlayerIndex(None)
            gameController.game.addCard(card1)
            gameController.updateCurrentPlayer
            gameController.game.addCard(card4)
            gameController.game.addCard(card3)
        }

        "add and remove cards for players and trick when card is hearts" in{
            val p1 = Player("Alice")
            val p2 = Player("Dave")
            val game = Game()
            val gameController = Controller(game)
            gameController.game = gameController.game.addPlayer(p1)
            gameController.game = gameController.game.addPlayer(p2)
            p1.addAllCards(List(card1,card5))
            p2.addAllCards(List(card5,card6))
            gameController.game = gameController.game.setFirstCard(false)
            gameController.game = gameController.game.setCurrentPlayerIndex(Some(gameController.game.players.indexWhere(p => p.equals(p1))))
            gameController.game.addCard(card5)
            gameController.game.addCard(card1)
            gameController.updateCurrentPlayer
            gameController.game.startWithHearts should be (false)
            gameController.game.addCard(card6)
            gameController.game.startWithHearts should be (true)
        }

        "update current winner in trick" in{
            val p1 = Player("Alice")
            val p2 = Player("Dave")
            val game = Game()
            val gameController = Controller(game)
            gameController.game = gameController.game.addPlayer(p1)
            gameController.game = gameController.game.addPlayer(p2)
            p1.addAllCards(List(card1,card5))
            p2.addAllCards(List(card3,card6))
            gameController.updateCurrentPlayer
            gameController.game.addCard(card1)
            gameController.game = gameController.game.setFirstCard(false)
            gameController.game.currentWinner should be (Some(p1))
            gameController.updateCurrentPlayer
            gameController.game.addCard(card3)
            gameController.game.currentWinner should be (Some(p2))
            gameController.updateCurrentPlayer
        }

        "not update current winner when unnecessary" in {
            val p1 = Player("Alice")
            val p2 = Player("Dave")
            val game = Game()
            val gameController = Controller(game)
            gameController.game = gameController.game.addPlayer(p1)
            gameController.game = gameController.game.addPlayer(p2)
            p1.addAllCards(List(card3,card5))
            p2.addAllCards(List(card1,card6))
            gameController.game = gameController.game.setCurrentPlayerIndex(Some(gameController.game.players.indexWhere(p => p.equals(p1))))
            gameController.game = gameController.game.setFirstCard(false)
            gameController.game.addCard(card3)
            gameController.game.currentWinner should be (Some(p1))
            gameController.game.addCard(card1)
            gameController.game.currentWinner should be (Some(p1))
        }

        "complete Trick String for three players" in{
            val p1 = Player("Alice")
            val p2 = Player("Dave")
            val p3 = Player("Charlie")
            val game = Game()
            val gameController = Controller(game)
            gameController.game = gameController.game.setFirstCard(false)
            gameController.game = gameController.game.setCurrentPlayerIndex(Some(gameController.game.players.indexWhere(p => p.equals(p1))))
            gameController.game = gameController.game.addPlayer(p1)
            gameController.game = gameController.game.addPlayer(p2)
            gameController.game = gameController.game.addPlayer(p3)
            gameController.completeTrickString should be ("|     |     |     |")
            gameController.game.addCard(Card(Rank.Two, Suit.Diamonds))
            gameController.completeTrickString should be ("| 2 \u2666 |     |     |")
            gameController.game.addCard(Card(Rank.Ten, Suit.Diamonds))
            gameController.completeTrickString should be ("| 2 \u2666 | 10\u2666 |     |")
            gameController.game.addCard(Card(Rank.Three, Suit.Diamonds))
            gameController.completeTrickString should be ("| 2 \u2666 | 10\u2666 | 3 \u2666 |")
        }

        "complete Trick String for four players" in {
            val p1 = Player("Alice")
            val p2 = Player("Dave")
            val p3 = Player("Charlie")
            val p4 = Player("David")
            val game = Game()
            val gameController = Controller(game)
            gameController.game = gameController.game.setFirstCard(false)
            gameController.game = gameController.game.setCurrentPlayerIndex(Some(gameController.game.players.indexWhere(p => p.equals(p1))))
            gameController.game = gameController.game.addPlayer(p1)
            gameController.game = gameController.game.addPlayer(p2)
            gameController.game = gameController.game.addPlayer(p3)
            gameController.game = gameController.game.addPlayer(p4)
            gameController.completeTrickString should be ("|     |     |     |     |")
            gameController.game.addCard(Card(Rank.Two, Suit.Diamonds))
            gameController.completeTrickString should be ("| 2 \u2666 |     |     |     |")
            gameController.game.addCard(Card(Rank.Ten, Suit.Diamonds))
            gameController.completeTrickString should be ("| 2 \u2666 | 10\u2666 |     |     |")
            gameController.game.addCard(Card(Rank.Three, Suit.Diamonds))
            gameController.completeTrickString should be ("| 2 \u2666 | 10\u2666 | 3 \u2666 |     |")
            gameController.game.addCard(Card(Rank.Queen, Suit.Diamonds))
            gameController.completeTrickString should be ("| 2 \u2666 | 10\u2666 | 3 \u2666 | Q \u2666 |")
        }

        "give won cards to the correct player" in {
            val p1 = Player("Alice")
            val p2 = Player("Dave")
            val game = Game()
            val gameController = Controller(game)
            gameController.game = gameController.game.addPlayer(p1)
            gameController.game = gameController.game.addPlayer(p2)
            p1.addAllCards(List(card1,card2))
            p2.addAllCards(List(card3,card4))
            gameController.game = gameController.game.setCurrentPlayerIndex(Some(gameController.game.players.indexWhere(p => p.equals(p1))))
            gameController.game = gameController.game.setFirstCard(false)
            gameController.playCard(1)
            gameController.playCard(1)
            gameController.game.getCurrentPlayer should be (Some(p2))
            gameController.playCard(1)
            gameController.game.players(1).wonCards should be (List(card2,card4))
        }

        "get the handstring of current player" in{
            val game = Game()
            val gameController = Controller(game)
            val p1 = Player("Alice")
            gameController.game = gameController.game.setCurrentPlayerIndex(Some(gameController.game.players.indexWhere(p => p.equals(p1))))
            p1.addAllCards(List(card1,card2))
            gameController.handToString should be ("|  1  |  2  |\n| 2 \u2666 | 2 \u2663 |")
        }
        "get the name of current player" in{
            val game = Game()
            val gameController = Controller(game)
            val p1 = Player("Alice")
            gameController.game = gameController.game.setCurrentPlayerIndex(Some(gameController.game.players.indexWhere(p => p.equals(p1))))
            gameController.getCurrentPlayerName should be ("Alice")
        }

        "update current player for first card" in {
            val game = Game()
            val gameController = Controller(game)
            val p1 = Player("Alice")
            val p2 = Player("Dave")
            p1.addAllCards(List(card1,card6))
            gameController.game = gameController.game.addPlayer(p1)
            gameController.game = gameController.game.addPlayer(p2)
            p2.addAllCards(List(card3,card5))
            gameController.game.getCurrentPlayer should be (Some(p1))

        }

        "update current player for normal case" in  {
            val game = Game()
            val gameController = Controller(game)
            val p1 = Player("Alice")
            val p2 = Player("Dave")
            gameController.game = gameController.game.addPlayer(p1)
            gameController.game = gameController.game.addPlayer(p2)
            p1.addAllCards(List(card1,card6))
            p2.addAllCards(List(card3,card5))
            gameController.updateCurrentPlayer
            gameController.game = gameController.game.setFirstCard(false)
            gameController.updateCurrentPlayer
            gameController.game.getCurrentPlayer should be (Some(p2))
        }

        "update current player when trick is full" in{
            val game = Game()
            val gameController = Controller(game)
            val p1 = Player("Alice").addAllCards(List(card1,card6))
            val p2 = Player("Dave").addAllCards(List(card3,card5))
            gameController.game = gameController.game.addPlayer(p1)
            gameController.game = gameController.game.addPlayer(p2)
            gameController.game.setFirstCard(false)
            gameController.game = gameController.game.copy(trickCards = List(card1,card2))
            gameController.game = gameController.updateCurrentPlayer
            gameController.game.getCurrentPlayer should be (Some(p2))
        }

        "loop over list of players" in {
            val game = Game()
            val gameController = Controller(game)
            val p1 = Player("Alice")
            val p2 = Player("Dave")
            gameController.game = gameController.game.setFirstCard(false)
            gameController.game = gameController.game.addPlayer(p1)
            gameController.game = gameController.game.addPlayer(p2)
            p1.addAllCards(List(card1,card6))
            p2.addAllCards(List(card3,card5))
            gameController.game = gameController.game.setCurrentPlayerIndex(Some(gameController.game.players.indexWhere(p => p.equals(p2))))
            gameController.game = gameController.updateCurrentPlayer
            gameController.game.getCurrentPlayer should be (Some(p1))
        }

        "check if deck is shuffled" in {
            val game = Game()
            val gameController = Controller(game)
            val deck = gameController.createDeck
            gameController.shuffledeck(deck) should not equal (deck)
        }

        "check if playerNumber is 3 or (playerNumber is 3 and 2 \u2663 is the first Card in the deck) one Card gets filtered out, but not the 2 \u2663" in {
            val game = Game()
            val gameController = Controller(game)
            gameController.game = gameController.setPlayerNumber(Some(3))
            val deck = List[Card](card1, card2, card3, card4, card5, card6, card7, card8)
            val deck2 = List[Card](card4, card7, card5, card1, card3, card2, card8, card6)
            val newdeck = gameController.filterOneCardOut(deck)
            newdeck should contain (Card(Rank.Two,Suit.Clubs))
            newdeck.size should be (7)
            val newdeck2 = gameController.filterOneCardOut(deck2)
            newdeck2 should contain (Card(Rank.Two,Suit.Clubs))
            newdeck2.size should be (7)
        }

        "check if playerNumber is 4 no Card gets filtered out" in {
            val game = Game()
            val gameController = Controller(game)
            gameController.game = gameController.setPlayerNumber(Some(4))
            val deck = gameController.createDeck
            val newdeck = gameController.filterOneCardOut(deck)
            newdeck should equal (deck)
        }

        "deal the correct amount of cards for 3 Players" in {
            val game = Game()
            val gameController = Controller(game)
            gameController.game = gameController.setPlayerNumber(Some(3))
            val p1 = Player("Alice")
            val p2 = Player("Dave")
            val p3 = Player("Charlie")
            gameController.game = gameController.game.addPlayer(p1)
            gameController.game = gameController.game.addPlayer(p2)
            gameController.game = gameController.game.addPlayer(p3)
            gameController.game = gameController.dealCards(gameController.createDeck)
            p1.hand.size should be (17)
            p2.hand.size should be (17)
            p3.hand.size should be (17)
        }

        "deal the correct cards for 4 Players" in {
            val game = Game()
            val gameController = Controller(game)
            gameController.game = gameController.setPlayerNumber(Some(4))
            val p1 = Player("Alice")
            val p2 = Player("Dave")
            val p3 = Player("Charlie")
            val p4 = Player("David")
            gameController.game = gameController.game.addPlayer(p1)
            gameController.game = gameController.game.addPlayer(p2)
            gameController.game = gameController.game.addPlayer(p3)
            gameController.game = gameController.game.addPlayer(p4)
            gameController.dealCards(gameController.filterOneCardOut(gameController.createDeck))
            p1.hand.size should be (13)
            p2.hand.size should be (13)
            p3.hand.size should be (13)
            p4.hand.size should be (13)
        }

        "check if pointsForPlayer are calculated correctly" in {
            val game = Game()
            val gameController = Controller(game)
            val p1 = Player("Alice")
            val l1: List[Card] = List(Card(Rank.Ace, Suit.Hearts), Card(Rank.Four,Suit.Clubs), Card(Rank.Queen, Suit.Spades))
            p1.addWonCards(l1)
            gameController.pointsForPlayer(p1) should be (14)
        }

        "check if rawPointsPerPlayer are calculated correctly" in {
            val game = Game()
            val gameController = Controller(game)
            val l1: List[Card] = List(Card(Rank.Ace, Suit.Hearts), Card(Rank.Four,Suit.Clubs), Card(Rank.Queen, Suit.Spades))
            val l2: List[Card] = List(Card(Rank.Ace, Suit.Hearts), Card(Rank.Four,Suit.Clubs), Card(Rank.Queen, Suit.Diamonds))
            val p1 = Player("Alice").addWonCards(l1)
            val p2 = Player("Dave").addWonCards(l2)
            gameController.game = gameController.game.addPlayer(p1)
            gameController.game = gameController.game.addPlayer(p2)
            val m1: Map[Player, Int] = Map((p1, 14), (p2, 1))
            gameController.rawPointsPerPlayer should equal (m1)
        }

        "check if applyShootingTheMoon gets applied if only one Player has Points" in {
            val game = Game()
            val gameController = Controller(game)
            val p1 = Player("Alice").addWonCards(List(Card(Rank.Two, Suit.Hearts), Card(Rank.Five, Suit.Hearts), Card(Rank.Queen, Suit.Spades)))
            val p2 = Player("Dave").addWonCards(List(Card(Rank.Four, Suit.Clubs), Card(Rank.Seven, Suit.Spades), Card(Rank.Ten, Suit.Diamonds)))
            val p3 = Player("Charlie").addWonCards(List(Card(Rank.Eight, Suit.Diamonds), Card(Rank.Jack, Suit.Clubs), Card(Rank.Three, Suit.Spades)))
            val p4 = Player("David").addWonCards(List(Card(Rank.Six, Suit.Clubs), Card(Rank.King, Suit.Diamonds), Card(Rank.Nine, Suit.Spades)))
            gameController.game = gameController.game.addPlayer(p1)
            gameController.game = gameController.game.addPlayer(p2)
            gameController.game = gameController.game.addPlayer(p3)
            gameController.game = gameController.game.addPlayer(p4)
            val m1: Map[Player, Int] = Map((p1, 0), (p2, 15), (p3, 15), (p4, 15))
            gameController.applyShootingTheMoon should equal (m1)
        }

        "check if applyShootingTheMoon is not applied when two Player get Points" in {
            val game = Game()
            val gameController = Controller(game)
            val p1 = Player("Alice")
            val p2 = Player("Dave")
            val p3 = Player("Charlie")
            val p4 = Player("David")
            p1.addWonCards(List(Card(Rank.Two, Suit.Hearts), Card(Rank.Five, Suit.Hearts), Card(Rank.Queen, Suit.Spades)))
            p2.addWonCards(List(Card(Rank.Four, Suit.Hearts), Card(Rank.Seven, Suit.Spades), Card(Rank.Ten, Suit.Diamonds)))
            p3.addWonCards(List(Card(Rank.Eight, Suit.Diamonds), Card(Rank.Jack, Suit.Clubs), Card(Rank.Three, Suit.Spades)))
            p4.addWonCards(List(Card(Rank.Six, Suit.Clubs), Card(Rank.King, Suit.Diamonds), Card(Rank.Nine, Suit.Spades)))
            gameController.game = gameController.game.addPlayer(p1)
            gameController.game = gameController.game.addPlayer(p2)
            gameController.game = gameController.game.addPlayer(p3)
            gameController.game = gameController.game.addPlayer(p4)
            val m1: Map[Player, Int] = Map((p1, 15), (p2, 1), (p3, 0), (p4, 0))
            gameController.applyShootingTheMoon should equal (m1)
        }

        "check if addPointsToPlayers adds the correct amount of Points to the correct Player and clears their wonCards List" in {
            val game = Game()
            val gameController = Controller(game)
            gameController.game = gameController.setPlayerNumber(Some(4))
            val p1 = Player("Alice").addWonCards(List(Card(Rank.Two, Suit.Hearts), Card(Rank.Five, Suit.Hearts), Card(Rank.Queen, Suit.Spades)))
            val p2 = Player("Dave").addWonCards(List(Card(Rank.Four, Suit.Hearts), Card(Rank.Seven, Suit.Spades), Card(Rank.Ten, Suit.Diamonds)))
            val p3 = Player("Charlie").addWonCards(List(Card(Rank.Eight, Suit.Diamonds), Card(Rank.Jack, Suit.Clubs), Card(Rank.Three, Suit.Spades)))
            val p4 = Player("David").addWonCards(List(Card(Rank.Six, Suit.Clubs), Card(Rank.King, Suit.Diamonds), Card(Rank.Nine, Suit.Spades)))
            gameController.game = gameController.game.addPlayer(p1)
            gameController.game = gameController.game.addPlayer(p2)
            gameController.game = gameController.game.addPlayer(p3)
            gameController.game = gameController.game.addPlayer(p4)
            gameController.addPointsToPlayers
            p1.points should be (15)
            p1.wonCards should be (empty)
            p2.points should be (1)
            p2.wonCards should be (empty)
            p3.points should be (0)
            p3.wonCards should be (empty)
            p4.points should be (0)
            p4.wonCards should be (empty)
        }

        "check if playerAndPointsToList gets the player names and their points and puts them into tuples" in {
            val game = Game()
            val gameController= Controller(game)
            gameController.setPlayerNumber(Some(4))
            val p1 = Player("Alice").addPoints(30)
            val p2 = Player("Dave").addPoints(20)
            val p3 = Player("Charlie").addPoints(20)
            val p4 = Player("David").addPoints(10)
            gameController.game = gameController.game.addPlayer(p1)
            gameController.game = gameController.game.addPlayer(p2)
            gameController.game = gameController.game.addPlayer(p3)
            gameController.game = gameController.game.addPlayer(p4)
            val a = List((p1.name,p1.points),(p2.name,p2.points),(p3.name,p3.points),(p4.name,p4.points))
            gameController.getPlayersWithPoints should be (a)
        }
        " sort and rank Players by Points" in {
            val game = Game()
            val gameController= Controller(game)
            gameController.setPlayerNumber(Some(4))
            val p1 = Player("Alice").addPoints(30)
            val p2 = Player("Dave").addPoints(20)
            val p3 = Player("Charlie").addPoints(20)
            val p4 = Player("David").addPoints(10)
            gameController.game = gameController.game.addPlayer(p1)
            gameController.game = gameController.game.addPlayer(p2)
            gameController.game = gameController.game.addPlayer(p3)
            gameController.game = gameController.game.addPlayer(p4)
            val b = List((1,p4.name,p4.points),(2,p2.name,p2.points),(2,p3.name,p3.points),(4,p1.name,p1.points))
            gameController.rankPlayers(gameController.getPlayersWithPoints) should be (b)
        }
        /*
        "output handstring" in {
            val p1 = Player("Alice")
            val game = Game()
            val gameController= Controller(game)
            p1.addAllCards(List(card1,card2)
            gameController.game = gameController.game.addPlayer(p1)
            gameController.game = gameController.game.setCurrentPlayerIndex(Some(gameController.game = gameController.game.players.indexWhere(p => p.equals(p1))))
            gameController.handToString() should be ("|  1  |  2  |\n| A \u2660 | 10\u2665 |")
        }
            */

        "check if the game is over" in{
            val game = Game()
            val p1 = Player("Alice")
            val gameController = Controller(game)
            gameController.game = gameController.game.setCurrentPlayerIndex(Some(0))
            gameController.game = gameController.game.addPlayer(p1)
            gameController.game = gameController.game.setMaxScore(Some(1))
            gameController.checkGameOver should be (false)
            gameController.game = gameController.game.updatePlayer(0, gameController.game.getCurrentPlayer.get.addPoints(1))
            gameController.checkGameOver should be (true)
        }

        "check if getPlayerNumber gets a playerNumber" in {
            val game = Game()
            val gameController = Controller(game)
            gameController.game = gameController.setPlayerNumber(Some(2))
            gameController.getPlayerNumber should be (Some(2))
        }

        "check if setPlayerNumber sets the playerNumber" in {
            val game = Game()
            val gameController = Controller(game)
            gameController.setPlayerNumber(Some(3))
            gameController.game.playerNumber should be (Some(3))
        }

        "be able to change states" in {
            val game = Game()
            val gameController = Controller(game)
            val state = GetPlayerNumberState(gameController)
            gameController.changeState(state)
            gameController.state should be(state)
        }

        "be able to get and set keepProcessRunning" in {
            val game = Game()
            val gameController = Controller(game)
            gameController.getKeepProcessRunning should be (true)
            gameController.setKeepProcessRunning(false)
            gameController.getKeepProcessRunning should be (false)
        }
        */
    }
}
