package de.htwg.se.Hearts.controller

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

import de.htwg.se.Hearts.controller.scoringComponent.ScoringInterface
import de.htwg.se.Hearts.controller.scoringComponent.scoringBase.HeartsScoring
import de.htwg.se.Hearts.model.gameComponent.gameBase._
import de.htwg.se.Hearts.model.gameComponent.Rank
import de.htwg.se.Hearts.model.gameComponent.Suit

class ScoringSpec extends AnyWordSpec with Matchers{
	"A scoring" should {
		val card1 = Card(Rank.Two,Suit.Clubs)
		val card2 = Card(Rank.Two,Suit.Diamonds)
		val card3 = Card(Rank.Ace,Suit.Clubs)
		val card4 = Card(Rank.Ace,Suit.Diamonds)
		val card5 = Card(Rank.Jack,Suit.Hearts)
		val p1 = Player("Alice",List(card1,card2),List(card5), points = 1)
		val p2 = Player("Dave",List(card3,card4))

		"points for cards" in {
			val scor = HeartsScoring()
			scor.cardPoints(card5) should be (1)
			scor.cardPoints(Card(Rank.Queen, Suit.Spades)) should be (13)
			scor.cardPoints(card1) should be (0)
			}

		"calculate points for a single player" in {
			val scor = HeartsScoring()
			scor.pointsForPlayer(p1) should be (1)
		}

		"match players and points to map" in {
			val scor = HeartsScoring()
			scor.rawPointsPerPlayer(Vector(p1,p2)) should be (Map((p1, 1),(p2, 0)))
		}

		"applyShootingTheMoon correctly" in {
			val scor = HeartsScoring()
			scor.applyShootingTheMoon(Map((p1, 1),(p2, 0))) should be (Map((p1, 0),(p2, 1)))
		}
	}
}
