package de.htwg.se.Hearts.model

package de.htwg.se.Hearts.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import scala.collection.mutable.ListBuffer
import scalafx.scene.input.KeyCode.C
import _root_.de.htwg.se.Hearts.model.Game.updateCurrentPlayer
import _root_.de.htwg.se.Hearts.model.Game.addPlayer
import _root_.de.htwg.se.Hearts.model.Game.currentPlayer

class TrickSpec extends AnyWordSpec with Matchers {
    "A Trick" should {
        val p1 = Player("Alice")
        val p2 = Player("Dave")
        val p3 = Player("Charlie")
        val p4 = Player("David")
        p1.hand +=(Card(Rank.Two,Suit.Clubs))
        p2.hand +=(Card(Rank.Ace,Suit.Clubs))
        p3.hand +=(Card(Rank.Jack,Suit.Clubs))
        p4.hand +=(Card(Rank.Ten,Suit.Clubs))
        addPlayer(p1)
        addPlayer(p2)
        addPlayer(p3)
        addPlayer(p4)
        Game.currentPlayer = Some(p1)
        Game.firstCard = false


        "check if player has played valid color" in {
            val CurrentTrick = Trick()
            CurrentTrick.addCard(Card(Rank.Two,Suit.Clubs)) should be (true)
            CurrentTrick.updateCurrentWinner()
            CurrentTrick.addCard(Card(Rank.Two,Suit.Diamonds)) should be (false)
            CurrentTrick.addCard(Card(Rank.Ace,Suit.Clubs)) should be (true)
        }

        "update currentWinner" in {
            val CurrentTrick = Trick()
            Game.playerNumber = Some(4)
            Game.currentPlayer = Some(p1)
            CurrentTrick.addCard(Card(Rank.Five,Suit.Diamonds))
            CurrentTrick.updateCurrentWinner()
            updateCurrentPlayer()
            CurrentTrick.highestCard should be (Some(Card(Rank.Five,Suit.Diamonds)))
            CurrentTrick.currentWinner should be (Some(p1))
            CurrentTrick.addCard(Card(Rank.Four,Suit.Diamonds))
            CurrentTrick.updateCurrentWinner()
            updateCurrentPlayer()
            CurrentTrick.highestCard should be (Some(Card(Rank.Five,Suit.Diamonds)))
            CurrentTrick.currentWinner should be (Some(p1))
            CurrentTrick.addCard(Card(Rank.Jack,Suit.Diamonds))
            CurrentTrick.updateCurrentWinner()
            updateCurrentPlayer()
            CurrentTrick.highestCard should be (Some(Card(Rank.Jack,Suit.Diamonds)))
            CurrentTrick.currentWinner should be (Some(p3))

        }

        "set first player with first card played" in {
            Game.firstCard = true
            Game.currentPlayer = Some(p1)
            val CurrentTrick = Trick()
            CurrentTrick.addCard(Card(Rank.Two,Suit.Clubs))
            CurrentTrick.firstPlayer should be (Some(p1))

            Game.firstCard = false
            val CurrentTrick2 = Trick()
            CurrentTrick2.addCard(Card(Rank.Five,Suit.Diamonds))
            CurrentTrick2.firstPlayer should be (Some(p1))
        }

        "check if played card is a heart card and first card of trick before that is allowed" in {
            Game.startWithHearts = false
            val CurrentTrick = Trick ()
            CurrentTrick.addCard(Card(Rank.Two,Suit.Hearts)) should be (false)
            CurrentTrick.cards should be (ListBuffer())
        }

        "clear Trick when Trick is full" in {
            val CurrentTrick = Trick()
            p4.wonCards.clear()
            CurrentTrick.addCard(Card(Rank.Two, Suit.Clubs))
            CurrentTrick.addCard(Card(Rank.Three, Suit.Clubs))
            CurrentTrick.addCard(Card(Rank.Four,Suit.Clubs))
            CurrentTrick.clearTrick() should be (false)
            CurrentTrick.addCard(Card(Rank.Five,Suit.Clubs))
            currentPlayer = Some(p4)
            CurrentTrick.updateCurrentWinner()
            CurrentTrick.clearTrick() should be (true)
            CurrentTrick.cards should be (List[Card]())
            p4.wonCards should be (ListBuffer(Card(Rank.Two, Suit.Clubs),Card(Rank.Three, Suit.Clubs),Card(Rank.Four,Suit.Clubs),Card(Rank.Five,Suit.Clubs)))
        }

        "output the correct strings for played Cards in a Trick with 4 Players" in {
            val CurrentTrick = Trick()
            CurrentTrick.trickToString() should be ("|     |     |     |     |")
            CurrentTrick.addCard(Card(Rank.Two, Suit.Diamonds))
            CurrentTrick.trickToString() should be ("| 2 \u2666 |     |     |     |")
            CurrentTrick.addCard(Card(Rank.Ten, Suit.Diamonds))
            CurrentTrick.trickToString() should be ("| 2 \u2666 | 10\u2666 |     |     |")
            CurrentTrick.addCard(Card(Rank.Three, Suit.Diamonds))
            CurrentTrick.trickToString() should be ("| 2 \u2666 | 10\u2666 | 3 \u2666 |     |")
            CurrentTrick.addCard(Card(Rank.Queen, Suit.Diamonds))
            CurrentTrick.trickToString() should be ("| 2 \u2666 | 10\u2666 | 3 \u2666 | Q \u2666 |")
        }

        "output the correct strings for played Cards in a Trick with 3 Players" in {
            val CurrentTrick = Trick()
            Game.playerNumber = Some(3)
            CurrentTrick.trickToString() should be ("|     |     |     |")
            CurrentTrick.addCard(Card(Rank.Two, Suit.Diamonds))
            CurrentTrick.trickToString() should be ("| 2 \u2666 |     |     |")
            CurrentTrick.addCard(Card(Rank.Ten, Suit.Diamonds))
            CurrentTrick.trickToString() should be ("| 2 \u2666 | 10\u2666 |     |")
            CurrentTrick.addCard(Card(Rank.Three, Suit.Diamonds))
            CurrentTrick.trickToString() should be ("| 2 \u2666 | 10\u2666 | 3 \u2666 |")
        }

        "output the correct strings for played Cards in a Trick with 2 Players" in {
            val CurrentTrick = Trick()
            Game.playerNumber = Some(2)
            CurrentTrick.trickToString() should be ("|     |     |")
            CurrentTrick.addCard(Card(Rank.Two, Suit.Diamonds))
            CurrentTrick.trickToString() should be ("| 2 \u2666 |     |")
            CurrentTrick.addCard(Card(Rank.Ten, Suit.Diamonds))
            CurrentTrick.trickToString() should be ("| 2 \u2666 | 10\u2666 |")
        }

    }
}

