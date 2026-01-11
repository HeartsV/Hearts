package de.htwg.se.Hearts.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.Hearts.model.gameComponent.gameBase.Card
import de.htwg.se.Hearts.model.gameComponent._

class CardSpec extends AnyWordSpec with Matchers {

	"A Card" should {
		val card1 = Card(Rank.Two, Suit.Clubs)

		val Deck : List[Card] =
			for
				suit <- Suit.values.toList
				rank <- Rank.values.toList
			yield Card(rank, suit)


		"have rank the correct Ranks" in {
			Deck(0).toString should include ("2")
			Deck(1).toString should include ("3")
			Deck(2).toString should include ("4")
			Deck(3).toString should include ("5")
			Deck(4).toString should include ("6")
			Deck(5).toString should include ("7")
			Deck(6).toString should include ("8")
			Deck(7).toString should include ("9")
			Deck(8).toString should include ("10")
			Deck(9).toString should include ("J")
			Deck(10).toString should include ("Q")
			Deck(11).toString should include ("K")
			Deck(12).toString should include ("A")
		}

		"be able to compare Cards by rank" in{
			Deck(0).compare(Deck(1)) should be < 0
			Deck(1).compare(Deck(0)) should be > 0
			Deck(0).compare(Deck(13)) should be (0)
		}

		"have the correct Suit" in {
			Deck(0).toString should include ("\u2665")
			Deck(13).toString should include ("\u2660")
			Deck(26).toString should include ("\u2666")
			Deck(39).toString should include ("\u2663")
		}

		"be able to compare Suits" in {
			Deck(0).suit.compare(Deck(1).suit) should be (0)
			Deck(0).suit.compare(Deck(13).suit) should be < 0
		}

		"filename" in {
			Deck(0).suit.fileName should be ("hearts")
			Deck(13).suit.fileName should be ("spades")
			Deck(26).suit.fileName should be ("diamonds")
			Deck(39).suit.fileName should be ("clubs")
		}

		"create the correct cardname of png" in {
			card1.pngName should be ("2_of_clubs.png")
		}

		"get the correct Suit" in {
			card1.getSuit should be (Suit.Clubs)
		}

		"get the correct Rank" in {
			card1.getRank should be (Rank.Two)
		}
	}
}
