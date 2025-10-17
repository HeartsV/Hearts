package de.htwg.se.Hearts.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class CardSpec extends AnyWordSpec with Matchers {

	"A Card" should {
		val card1 = Card(rank = Rank.Ten, suit = Suit.Hearts)
		val card2 = Card(rank = Rank.Ace, suit = Suit.Clubs)
		val card2string = card2.toString

		"have rank and suit" in {
			card1.rank should be (Rank.Ten)
			card1.suit should be (Suit.Hearts)
		}
		"generate the correct string" in {
			card2string should include ("\u2663")
			card2string should include ("A")
		}
	}
}
