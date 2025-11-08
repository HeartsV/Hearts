package de.htwg.se.Hearts.aview
import de.htwg.se.Hearts.model.*
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import _root_.de.htwg.se.Hearts.aview.Tui


class TuiSpec extends AnyWordSpec with Matchers {
	"A Tui" should {
        Game.playerNumber = Some(4)
        Game.trick.clearTrick()
        Game.players.clear()
        val p1 = Player("Alice")
        val p2 = Player("Dave")
        val p3 = Player("Charlie")
        val p4 = Player("David")
        Game.addPlayer(p1)
        Game.addPlayer(p2)
        Game.addPlayer(p3)
        Game.addPlayer(p4)
        Game.currentPlayer = Some(p1)
        p1.hand +=(Card(Rank.Two,Suit.Clubs))
        p2.hand +=(Card(Rank.Ace,Suit.Clubs))
        p3.hand +=(Card(Rank.Jack,Suit.Clubs))
        p4.hand +=(Card(Rank.Ten,Suit.Clubs))

        "get the correct GameplayStateString" in {
            val currentTui = Tui()

            currentTui.getGameplayStateString() should be ("Trick:\n|     |     |     |     |\nAlice please select card to play:\n|  1  |\n| 2 ♣ |\n")
            Game.players(0).playCard(1)
            Game.updateCurrentPlayer()
            currentTui.getGameplayStateString() should be ("Trick:\n| 2 ♣ |     |     |     |\nDave please select card to play:\n|  1  |\n| A ♣ |\n")
            Game.players(1).playCard(2)
            currentTui.getGameplayStateString() should be ("Trick:\n| 2 ♣ |     |     |     |\nDave please select card to play:\n|  1  |\n| A ♣ |\n")
            }
    }
}
