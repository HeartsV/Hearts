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
        game.addPlayer(p1)
        game.addPlayer(p2)
        game.addPlayer(p3)
        game.addPlayer(p4)
        p1.hand +=(Card(Rank.Two,Suit.Clubs))
        p2.hand +=(Card(Rank.Ace,Suit.Clubs))
        p3.hand +=(Card(Rank.Jack,Suit.Clubs))
        p4.hand +=(Card(Rank.Ten,Suit.Clubs))

        "get the correct getMainScreenStateString" in {
            tui.getMainScreenStateString() should be ("Hearts\nnew Game\nrules\nexit\n")
        }

        "get the correct getRulesScreenStateString" in {
            tui.getRulesScreenStateString() should be ("1 point per won hearts card\n13 points for won Q of Spades\nlowest points wins")
        }

        "get the correct getPlayerNumberStateString" in {
            tui.getPlayerNumberStateString() should be ("please input a Number of Players between 3 and 4\n")
        }

        "get the correct getPlayerNamesStateString" in {
            tui.getPlayerNamesStateString() should be ("please input the names of the 5. player\n")
        }

        "get the correct setMaxScoreStateString" in {
            tui.setMaxScoreStateString() should be ("please enter the score required to win (between 1 and 400)\n")
        }

        "get the correct GameplayStateString" in {
            gameController.updateCurrentPlayer()
            tui.getGameplayStateString() should be ("Trick:\n|     |     |     |     |\nAlice please select card to play:\n|  1  |\n| 2 ♣ |\n")
            gameController.playCard(1)
            game.firstCard = false
            gameController.updateCurrentPlayer()
            tui.getGameplayStateString() should be ("Trick:\n| 2 ♣ |     |     |     |\nDave please select card to play:\n|  1  |\n| A ♣ |\n")
            gameController.playCard(2)
            tui.getGameplayStateString() should be ("Trick:\n| 2 ♣ |     |     |     |\nDave please select card to play:\n|  1  |\n| A ♣ |\n")
        }

        "get the correct getShowScoreStateString" in {

        }

        "get the correct getGameOverStateString" in {
           
        }
    }

}
