package de.htwg.se.Hearts.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import scala.collection.mutable.ListBuffer
import _root_.de.htwg.se.Hearts.model.Game.updateCurrentPlayer

class GameSpec extends AnyWordSpec with Matchers {
  "A Game" should {
    val p1 = Player("Alice")
    val p2 = Player("Bob")
    p2.hand += (Card(Rank.Eight,Suit.Clubs))
    val p3 = Player("Charlie")
    val p4 = Player("Dave")
    p4.hand += (Card(Rank.Two,Suit.Clubs))
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
        updateCurrentPlayer()
        Game.trick.addCard(Card(Rank.Ten,Suit.Clubs)) should be (false)
        Game.firstCard should be (true)
        Game.trick.cards should be (ListBuffer())
        Game.trick.addCard(Card(Rank.Two,Suit.Clubs)) should be (true)
        Game.firstCard should be (false)
        Game.trick.cards should be(ListBuffer(Card(Rank.Two,Suit.Clubs)))
        Game.trick.addCard(Card(Rank.Three,Suit.Clubs))
        Game.trick.addCard(Card(Rank.Four,Suit.Clubs))
        Game.trick.addCard(Card(Rank.Five,Suit.Clubs))
    }

    "update current player when trick is full" in{
        Game.trick.updateCurrentWinner()
        updateCurrentPlayer()
        Game.currentPlayer should be (Some(p4))
        Game.trick.clearTrick()
    }

    "update current player" in {
        Game.firstCard = true
        Game.updateCurrentPlayer()
        Game.currentPlayer should be (Some(p4))
        Game.firstCard = false
        Game.updateCurrentPlayer()
        Game.currentPlayer should be (Some(p1))
        Game.updateCurrentPlayer()
        Game.currentPlayer should be (Some(p2))
        Game.updateCurrentPlayer()
        Game.currentPlayer should be (Some(p3))
        Game.updateCurrentPlayer()
        Game.currentPlayer should be (Some(p4))
    }

    "update start with hearts when heart gets played" in {
        Game.trick.addCard(Card(Rank.Two,Suit.Clubs))
        Game.trick.addCard(Card(Rank.Three,Suit.Clubs))
        Game.trick.addCard(Card(Rank.Four,Suit.Hearts))
        Game.startWithHearts should be (true)
        Game.trick.addCard(Card(Rank.Five,Suit.Clubs))
        Game.trick.clearTrick()
    }
  }
}
