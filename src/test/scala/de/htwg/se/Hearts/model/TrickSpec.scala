package de.htwg.se.Hearts.model

package de.htwg.se.Hearts.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import scala.collection.mutable.ListBuffer

class TrickSpec extends AnyWordSpec with Matchers {
    "A Trick" should {
        val p1 = Player("Alice",ListBuffer[Card](Card(Rank.Two,Suit.Clubs)),ListBuffer[Card]())
        val p2 = Player("Dave",ListBuffer[Card](Card(Rank.Ace,Suit.Clubs)),ListBuffer[Card]())
        Game.firstCard = false

        "check if player has played valid color" in {
            val CurrentTrick = Trick()
            CurrentTrick.addCard(Card(Rank.Two,Suit.Clubs),p1) should be (true)
            CurrentTrick.addCard(Card(Rank.Two,Suit.Diamonds),p2) should be (false)
            CurrentTrick.addCard(Card(Rank.Ace,Suit.Clubs),p2) should be (true)
        }

        "update currentWinner" in {
            val CurrentTrick = Trick()
            CurrentTrick.addCard(Card(Rank.Five,Suit.Diamonds),p1)
            CurrentTrick.highestCard should be (Some(Card(Rank.Five,Suit.Diamonds)))
            CurrentTrick.currentWinner should be (Some(p1))
            CurrentTrick.addCard(Card(Rank.Four,Suit.Diamonds),p2)
            CurrentTrick.highestCard should be (Some(Card(Rank.Five,Suit.Diamonds)))
            CurrentTrick.currentWinner should be (Some(p1))
            CurrentTrick.addCard(Card(Rank.Jack, Suit.Diamonds),p2)
            CurrentTrick.highestCard should be (Some(Card(Rank.Jack,Suit.Diamonds)))
            CurrentTrick.currentWinner should be (Some(p2))
            CurrentTrick.addCard(Card(Rank.Ace, Suit.Hearts),p1)
        }

        "set first player with first card played" in {
            Game.firstCard = true
            val CurrentTrick = Trick()
            CurrentTrick.addCard(Card(Rank.Two,Suit.Spades),p1)
            CurrentTrick.firstPlayer should be (Some(p1))
            Game.firstCard = false
            val CurrentTrick2 = Trick()
            CurrentTrick.addCard(Card(Rank.Five,Suit.Diamonds),p1)
            CurrentTrick.firstPlayer should be (Some(p1))
        }

        "clear Trick when Trick is full" in {
            Game.setPlayerNumber(2)
            val p3 = Player("Alice",ListBuffer[Card](Card(Rank.Two,Suit.Clubs)),ListBuffer[Card]())
            val p4 = Player("Dave",ListBuffer[Card](Card(Rank.Ace,Suit.Clubs)),ListBuffer[Card]())
            Game.addPlayer(p3)
            Game.addPlayer(p4)
            val CurrentTrick = Trick()
            CurrentTrick.addCard(Card(Rank.Three,Suit.Clubs),p3)
            CurrentTrick.clearTrick() should be (false)
            CurrentTrick.addCard(Card(Rank.Five,Suit.Clubs),p4)
            CurrentTrick.cards should be (List[Card]())
            p4.wonCards should be (ListBuffer(Card(Rank.Three,Suit.Clubs),Card(Rank.Five,Suit.Clubs)))
        }
    }
}

