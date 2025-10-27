package de.htwg.se.Hearts.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import scala.collection.mutable.ListBuffer

class GameSpec extends AnyWordSpec with Matchers {
  "A Game" should {
    val p1 = Player("Alice",ListBuffer[Card](),ListBuffer[Card]())
    val p2 = Player("Bob",ListBuffer[Card](Card(Rank.Eight,Suit.Clubs)),ListBuffer[Card]())
    val p3 = Player("Charlie",ListBuffer[Card](),ListBuffer[Card]())
    val p4 = Player("Dave",ListBuffer[Card](),ListBuffer[Card]())
    Game.firstCard = true
    Game.startWithHearts = false

    "set player number" in {
        Game.setPlayerNumber(6)
        Game.playerNumber should be (None: Option[Int])
        Game.setPlayerNumber(4)
        Game.playerNumber should be (Some(4))
    }

    "add player" in{
        Game.addPlayer(p1)
        Game.addPlayer(p2)
        Game.addPlayer(p3)
        Game.addPlayer(p4)
        Game.players should be (ListBuffer[Player](p1,p2,p3,p4))
    }

    "check if played card is valid card for first card" in {
        Game.trick.addCard(Card(Rank.Ten,Suit.Clubs),p1) should be (false)
        Game.firstCard should be (true)
        Game.trick.cards should be (ListBuffer())
        Game.trick.addCard(Card(Rank.Two,Suit.Spades),p1) should be (true)
        Game.firstCard should be (false)
        Game.trick.cards should be(ListBuffer(Card(Rank.Two,Suit.Spades)))
        Game.trick.addCard(Card(Rank.Three,Suit.Spades),p2)
        Game.trick.addCard(Card(Rank.Four,Suit.Spades),p3)
        Game.trick.addCard(Card(Rank.Five,Suit.Spades),p4)
    }

    "check if played card is a heart card and first card of trick before that is allowed" in {
        Game.trick.addCard(Card(Rank.Two,Suit.Hearts),p1) should be (false)
        Game.trick.cards should be (ListBuffer())
    }

    "update start with heart after heart is played" in {
        Game.trick.addCard(Card(Rank.Two,Suit.Diamonds),p1) should be (true)
        Game.trick.addCard(Card(Rank.Two,Suit.Hearts),p2) should be (true)
        Game.startWithHearts should be (true)
        Game.trick.addCard(Card(Rank.Three,Suit.Diamonds),p3) should be (true)
        Game.trick.addCard(Card(Rank.Four,Suit.Diamonds),p4) should be (true)
    }

    "check if player has played valid color" in {
        Game.trick.addCard(Card(Rank.Two,Suit.Clubs),p1) should be (true)
        Game.trick.addCard(Card(Rank.Two,Suit.Diamonds),p2) should be (false)
        Game.trick.addCard(Card(Rank.Ace,Suit.Clubs),p2) should be (true)
        Game.trick.addCard(Card(Rank.Three,Suit.Diamonds),p3) should be (true)
        Game.trick.addCard(Card(Rank.Four,Suit.Diamonds),p4) should be (true)
    }

    "update currentWinner" in {
        Game.trick.addCard(Card(Rank.Five,Suit.Diamonds),p1) should be (true)
        Game.trick.highestCard should be (Some(Card(Rank.Five,Suit.Diamonds)))
        Game.trick.currentWinner should be (Some(p1))
        Game.trick.addCard(Card(Rank.Four,Suit.Diamonds),p2)
        Game.trick.highestCard should be (Some(Card(Rank.Five,Suit.Diamonds)))
        Game.trick.currentWinner should be (Some(p1))
        Game.trick.addCard(Card(Rank.Jack, Suit.Spades),p3)
        Game.trick.highestCard should be (Some(Card(Rank.Five,Suit.Diamonds)))
        Game.trick.currentWinner should be (Some(p1))
        Game.trick.addCard(Card(Rank.Ace, Suit.Hearts),p4)

        Game.trick.addCard(Card(Rank.Five,Suit.Diamonds),p1) should be (true)
        Game.trick.highestCard should be (Some(Card(Rank.Five,Suit.Diamonds)))
        Game.trick.currentWinner should be (Some(p1))
        Game.trick.addCard(Card(Rank.Jack, Suit.Diamonds),p2)
        Game.trick.highestCard should be (Some(Card(Rank.Jack,Suit.Diamonds)))
        Game.trick.currentWinner should be (Some(p2))
        Game.trick.addCard(Card(Rank.Ace,Suit.Spades),p3)
        Game.trick.highestCard should be (Some(Card(Rank.Jack,Suit.Diamonds)))
        Game.trick.currentWinner should be (Some(p2))
        Game.trick.addCard(Card(Rank.Ace, Suit.Hearts),p4)
    }

    "clear Trick when Trick is full" in {
        Game.trick.addCard(Card(Rank.Two,Suit.Clubs),p1)
        Game.trick.addCard(Card(Rank.Three,Suit.Clubs),p2)
        Game.trick.addCard(Card(Rank.Five,Suit.Clubs),p3)
        Game.trick.clearTrick() should be (false)
        Game.trick.addCard(Card(Rank.Four,Suit.Clubs),p4)
        Game.trick.cards should be (List[Card]())
        Game.trick.highestCard should be (None)
        Game.trick.currentWinner should be (None)
        Game.trick.firstPlayer should be (None)
        p3.wonCards should be (ListBuffer(Card(Rank.Two,Suit.Clubs),Card(Rank.Three,Suit.Clubs),Card(Rank.Five,Suit.Clubs),Card(Rank.Four,Suit.Clubs)))
    }
  }
}
