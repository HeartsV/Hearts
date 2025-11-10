package de.htwg.se.Hearts.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers


class TrickSpec extends AnyWordSpec with Matchers {
    "A Trick" should {
        val card = Card(Rank.Ace,Suit.Clubs)
        val player = Player("Alice")

        "Add played card to cards" in {
            val trick = Trick()
            trick.addCard(card) should be (true)
            trick.cards should be (List(card))
        }

        "initialise new trick with firstcard firstplayer and CurrentWinner" in {
            val trick = Trick()
            trick.initializeTrick(player,card)
            trick.currentWinner should be (Some(player))
            trick.firstPlayer should be (Some(player))
            trick.highestCard should be (Some(card))
        }

        "be cleared by clear Trick" in {
            val trick = Trick()
            trick.initializeTrick(player,card)
            trick.clearTrick() should be (true)
            trick.currentWinner should be (None)
            trick.firstPlayer should be (None)
            trick.highestCard should be (None)
        }

        "output the correct strings for played Cards" in {
            val CurrentTrick = Trick()
            CurrentTrick.trickToString() should be ("|")
            CurrentTrick.addCard(Card(Rank.Two, Suit.Diamonds))
            CurrentTrick.trickToString() should be ("| 2 \u2666 |")
            CurrentTrick.addCard(Card(Rank.Ten, Suit.Diamonds))
            CurrentTrick.trickToString() should be ("| 2 \u2666 | 10\u2666 |")
            CurrentTrick.addCard(Card(Rank.Three, Suit.Diamonds))
            CurrentTrick.trickToString() should be ("| 2 \u2666 | 10\u2666 | 3 \u2666 |")
            CurrentTrick.addCard(Card(Rank.Queen, Suit.Diamonds))
            CurrentTrick.trickToString() should be ("| 2 \u2666 | 10\u2666 | 3 \u2666 | Q \u2666 |")
        }



        

    }
}

