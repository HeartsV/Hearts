package de.htwg.se.Hearts.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import scala.collection.mutable.ListBuffer

class TrickSpec extends AnyWordSpec with Matchers {
    "A Trick" should {
        val p1 = Player("Alice",ListBuffer[Card](Card(Rank.Two,Suit.Clubs)),ListBuffer[Card]())
        val p2 = Player("Dave",ListBuffer[Card](Card(Rank.Ace,Suit.Clubs)),ListBuffer[Card]())
        val CurrentTrick = Trick()
        Game.firstCard = false

        "check if played card is valid card for first card" in {
            Game.firstCard = true
            CurrentTrick.addCard(Card(Rank.Ten,Suit.Clubs),p1) should be (false)
            Game.firstCard should be (true)
            CurrentTrick.cards should be (ListBuffer())
            CurrentTrick.addCard(Card(Rank.Two,Suit.Clubs),p1) should be (true)
            Game.firstCard should be (false)
            CurrentTrick.cards should be(ListBuffer(Card(Rank.Two,Suit.Clubs)))
        }



        "check if played card is a heart card and first card of trick before that is allowed" in {
            CurrentTrick.addCard(Card(Rank.Two,Suit.Hearts),p1) should be (false)
            CurrentTrick.cards should be (ListBuffer())
            CurrentTrick.addCard(Card(Rank.Two,Suit.Clubs),p2) should be (true)
            CurrentTrick.cards should contain (Card(Rank.Two,Suit.Clubs))
        }



        "update start with heart after heart is played" in {
            CurrentTrick.addCard(Card(Rank.Two,Suit.Diamonds),p1) should be (true)
            CurrentTrick.addCard(Card(Rank.Two,Suit.Hearts),p2) should be (true)
            Game.startWithHearts should be (true)
        }

        "check if player has played valid color" in {
            CurrentTrick.addCard(Card(Rank.Two,Suit.Clubs),p1) should be (true)
            CurrentTrick.addCard(Card(Rank.Two,Suit.Diamonds),p2) should be (false)
            CurrentTrick.addCard(Card(Rank.Ace,Suit.Clubs),p2) should be (true)
        }

        "update currentWinner" in {
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
            CurrentTrick.highestCard should be (Some(Card(Rank.Jack,Suit.Diamonds)))
            CurrentTrick.currentWinner should be (Some(p2))
        }



        /*"clear when trick full" in {
            Trick.cards.clear()
            Trick.addCard(Card(Rank.Three,Suit.Clubs),p1)
            Trick.cards should be (List[Card]())
        }*/



    }
}
