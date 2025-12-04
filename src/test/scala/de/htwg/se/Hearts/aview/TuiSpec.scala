package de.htwg.se.Hearts.aview
import de.htwg.se.Hearts.model.*
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import _root_.de.htwg.se.Hearts.aview.Tui
import de.htwg.se.Hearts.controller.Controller


class TuiSpec extends AnyWordSpec with Matchers {
	"A Tui" should {
        val game = Game()
        val gameController = Controller(game)
        val tui = Tui(gameController)
        val p1 = Player("Alice")
        val p2 = Player("Dave")
        val p3 = Player("Charlie")
        val p4 = Player("David")
        p1.addPoints(30)
        p2.addPoints(20)
        p3.addPoints(20)
        p4.addPoints(10)
        game.addPlayer(p1)
        game.addPlayer(p2)
        game.addPlayer(p3)
        game.addPlayer(p4)
        p1.addAllCards(List(Card(Rank.Two,Suit.Clubs)))
        p2.addAllCards(List(Card(Rank.Ace,Suit.Clubs)))
        p3.addAllCards(List(Card(Rank.Jack,Suit.Clubs)))
        p4.addAllCards(List(Card(Rank.Ten,Suit.Clubs)))

        "get the correct getMainScreenStateString" in {
            val expected ="Hearts" + "\n\n" +
                "Please enter:" + "\n" +
                "- n or new for a new Game" + "\n" +
                "- r or rules for the rules" + "\n" +
                "- e or exit to end the program" + "\n"
                tui.getMainScreenStateString should be (expected)
        }/*

        "get the correct getRulesScreenStateString" in {
            val expected =
                "\n\n" +"Rules:" + "\n\n" +
                "Hearts is a trick-taking card game played with 3-4 players, each playing individually." + "\n" +
                "With 4 players, the game uses all 52 cards of a standard deck." + "\n" +
                "With 3 players, one card is removed and the remaining 51 cards are used." + "\n" +
                "Each suit contains cards from Two (lowest) to Ace (highest), giving 13 cards per suit." + "\n" +
                "There is no trump suit." + "\n\n\n" +
                "Objective:" + "\n\n" +
                "The goal of the game is to collect as few penalty points as possible." + "\n" +
                "You receive penalty points for taking tricks that contain any Heart cards or the Queen of Spades." + "\n" +
                "The game ends when a player reaches or exceeds the maximum score (the standard is 100 points)." + "\n" +
                "The player with the lowest total score is the winner." + "\n\n\n" +
                "Dealing:" + "\n\n" +
                "This rule applies only with 4 players!!" + "\n" +
                "Cards are dealt clockwise, one at a time, until each player has 13 cards." + "\n" +
                "Players may only see their own hands." + "\n" +
                "With 3 players, one card is removed from the deck, and the remaining 51 cards are dealt evenly." + "\n" +
                "There is no passing in a 3-player game." + "\n\n\n" +
                "Passing Cards:" + "\n\n" +
                "After all cards have been dealt (4-player game only), the passing phase begins." + "\n" +
                "Each player selects three cards from their hand and passes them face-down to another player." + "\n\n" +
                "The passing rotation works as follows:" + "\n" +
                "- 1st round: Pass to the player on your left" + "\n" +
                "- 2nd round: Pass to the player on your right" + "\n" +
                "- 3rd round: Pass to the player across from you" + "\n" +
                "- 4th round: No passing:" + "\n" +
                "- Then the cycle repeats for the rest of the game." + "\n\n\n" +
                "How the Game Is Played:" + "\n\n" +
                "The player holding the Two of Clubs leads the first trick." + "\n" +
                "All players must follow suit if possible. If they cannot, they may play another suit under certain conditions." + "\n" +
                "The player who plays the highest card of the led suit wins the trick and leads the next one." + "\n\n" +
                "There are restrictions when playing Hearts or the Queen of Spades:" + "\n" +
                "- In the first trick, you may not play a Heart or the Queen of Spades, even if you cannot follow suit." + "\n" +
                "- You cannot lead Hearts until Hearts have been broken." + "\n" +
                "- Hearts are broken when a player who cannot follow suit plays a Heart, or when the Queen of Spades has been played." + "\n" +
                "- Exception: A player with only Hearts may lead Hearts at any time." + "\n\n" +
                "A round ends after all 13 tricks have been played." + "\n\n\n" +
                "Scoring:" + "\n\n" +
                "Each Heart is worth 1 penalty point, and the Queen of Spades is worth 13 points." + "\n" +
                "There are 26 total penalty points in each round in a 4-player game." + "\n" +
                "It can differ in a 3-player game since one card is removed." + "\n\n" +
                "If a player captures all penalty cards (all Hearts and the Queen of Spades), they score 0 points," + "\n" +
                "and each opponent scores 26 points instead. This is called Shooting the Moon." + "\n\n" +
                "The game ends when a player reaches 100 points (or another agreed limit)." + "\n" +
                "The player with the lowest score wins." + "\n\n" +
                "Enter 'back' or 'b' to return to the main menu." + "\n"
            tui.getRulesScreenStateString should be (expected)
        }

        "get the correct getPlayerNumberStateString" in {
            tui.getPlayerNumberStateString should be ("please input a Number of Players between 3 and 4\n")
        }

        "get the correct getPlayerNamesStateString" in {
            tui.getPlayerNamesStateString should be ("please input the names of the 5. player\n")
        }

        "get the correct setMaxScoreStateString" in {
            tui.setMaxScoreStateString should be ("please enter the score required to win (between 1 and 400)\n")
        }

        "get the correct GameplayStateString" in {
            gameController.updateCurrentPlayer
            tui.getGameplayStateString should be ("Trick:\n|     |     |     |     |\nAlice please select card to play:\n|  1  |\n| 2 ♣ |\n")
            gameController.playCard(1)
            game.setFirstCard(false)
            gameController.updateCurrentPlayer
            tui.getGameplayStateString should be ("Trick:\n| 2 ♣ |     |     |     |\nDave please select card to play:\n|  1  |\n| A ♣ |\n")
            gameController.playCard(2)
            tui.getGameplayStateString should be ("Trick:\n| 2 ♣ |     |     |     |\nDave please select card to play:\n|  1  |\n| A ♣ |\n")
        }

        "get the correct getShowScoreStateString" in {
            tui.getShowScoreStateString should be ("Scoreboard:\n1. David: 10\n2. Dave: 20\n2. Charlie: 20\n4. Alice: 30\nPress any button to continue\n")

        }

        "get the correct getGameOverStateString" in {
            tui.getGameOverStateString should be (
                "GAMEOVER\n\nScoreboard:\n1. David: 10\n2. Dave: 20\n2. Charlie: 20\n4. Alice: 30\n" +
                "Please enter:" + "\n" +
                "- n or new for a new Game" + "\n" +
                "- a or again for playing again" + "\n" +
                "- q or quit to go back to Mainmenu" + "\n" +
                "- e or exit to end the program" + "\n")
        }*/
    }

}
