package de.htwg.se.Hearts.model

package de.htwg.se.Hearts.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import scala.collection.mutable.ListBuffer
import scalafx.scene.input.KeyCode.C
import _root_.de.htwg.se.Hearts.model.Game.updateCurrentPlayer
import _root_.de.htwg.se.Hearts.model.Game.addPlayer

class TrickSpec extends AnyWordSpec with Matchers {
    "A Trick" should {
        val p1 = Player("Alice",ListBuffer[Card](Card(Rank.Two,Suit.Clubs)),ListBuffer[Card]())
        val p2 = Player("Dave",ListBuffer[Card](Card(Rank.Ace,Suit.Clubs)),ListBuffer[Card]())
        val p3 = Player("Charlie",ListBuffer[Card](Card(Rank.Jack,Suit.Clubs)),ListBuffer[Card]())
        val p4 = Player("David",ListBuffer[Card](Card(Rank.Ten,Suit.Clubs)),ListBuffer[Card]())
        addPlayer(p1)
        addPlayer(p2)
        addPlayer(p3)
        addPlayer(p4)
        Game.currentPlayer = Some(p1)
        Game.firstCard = false


        "check if player has played valid color" in {
            val CurrentTrick = Trick()
            CurrentTrick.addCard(Card(Rank.Two,Suit.Clubs),p1) should be (true)
            CurrentTrick.updateCurrentWinner()
            CurrentTrick.addCard(Card(Rank.Two,Suit.Diamonds),p2) should be (false)
            CurrentTrick.addCard(Card(Rank.Ace,Suit.Clubs),p2) should be (true)
        }

        "update currentWinner" in {
            val CurrentTrick = Trick()
            Game.playerNumber = Some(4)
            Game.currentPlayer = Some(p1)
            CurrentTrick.addCard(Card(Rank.Five,Suit.Diamonds), p1)
            CurrentTrick.updateCurrentWinner()
            updateCurrentPlayer()
            CurrentTrick.highestCard should be (Some(Card(Rank.Five,Suit.Diamonds)))
            CurrentTrick.currentWinner should be (Some(p1))
            CurrentTrick.addCard(Card(Rank.Four,Suit.Diamonds), p2)
            CurrentTrick.updateCurrentWinner()
            updateCurrentPlayer()
            CurrentTrick.highestCard should be (Some(Card(Rank.Five,Suit.Diamonds)))
            CurrentTrick.currentWinner should be (Some(p1))
            CurrentTrick.addCard(Card(Rank.Jack,Suit.Diamonds), p3)
            CurrentTrick.updateCurrentWinner()
            updateCurrentPlayer()
            CurrentTrick.highestCard should be (Some(Card(Rank.Jack,Suit.Diamonds)))
            CurrentTrick.currentWinner should be (Some(p3))

        }

        "set first player with first card played" in {
            Game.firstCard = true
            val CurrentTrick = Trick()
            CurrentTrick.addCard(Card(Rank.Two,Suit.Clubs),p1)
            CurrentTrick.firstPlayer should be (Some(p1))

            Game.firstCard = false
            val CurrentTrick2 = Trick()
            CurrentTrick2.addCard(Card(Rank.Five,Suit.Diamonds),p1)
            CurrentTrick2.firstPlayer should be (Some(p1))
        }

        "check if played card is a heart card and first card of trick before that is allowed" in {
            Game.startWithHearts = false
            val CurrentTrick = Trick ()
            CurrentTrick.addCard(Card(Rank.Two,Suit.Hearts),p1) should be (false)
            CurrentTrick.cards should be (ListBuffer())
        }

        "clear Trick when Trick is full" in {
            val CurrentTrick = Trick()
            p4.wonCards.clear()
            CurrentTrick.addCard(Card(Rank.Two, Suit.Clubs), p1)
            CurrentTrick.addCard(Card(Rank.Three, Suit.Clubs), p2)
            CurrentTrick.addCard(Card(Rank.Four,Suit.Clubs),p3)
            CurrentTrick.clearTrick() should be (false)
            CurrentTrick.addCard(Card(Rank.Five,Suit.Clubs),p4)
            CurrentTrick.updateCurrentWinner()
            CurrentTrick.clearTrick() should be (true)
            CurrentTrick.cards should be (List[Card]())
            p4.wonCards should be (ListBuffer(Card(Rank.Two, Suit.Clubs),Card(Rank.Three, Suit.Clubs),Card(Rank.Four,Suit.Clubs),Card(Rank.Five,Suit.Clubs)))
        }
    }
}

