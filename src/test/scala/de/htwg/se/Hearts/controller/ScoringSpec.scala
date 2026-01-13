package de.htwg.se.Hearts.controller

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

import de.htwg.se.Hearts.controller.scoringComponent.ScoringInterface
import de.htwg.se.Hearts.controller.scoringComponent.scoringBase.HeartsScoring
import de.htwg.se.Hearts.model.gameComponent.gameBase._
import de.htwg.se.Hearts.model.gameComponent.Rank
import de.htwg.se.Hearts.model.gameComponent.Suit

class ScoringSpec extends AnyWordSpec with Matchers{
	val card1 = Card(Rank.Two,Suit.Clubs)
	val card2 = Card(Rank.Two,Suit.Diamonds)
	val card3 = Card(Rank.Ace,Suit.Clubs)
	val card4 = Card(Rank.Ace,Suit.Diamonds)
	val card5 = Card(Rank.Jack,Suit.Hearts)
	val p1 = Player("Alice",List(card1,card2),wonCards = List(card5), points = 1)
	val p2 = Player("Dave",List(card3,card4),wonCards = List())
	val p3 = Player("David",List(card3,card4),wonCards = List(card5))
	val game = Game(playerNumber = Some(3), players = Vector(p1, p2, p3))
	val scor = HeartsScoring()
	"A scoring" should {
		"points for cards" in {
			scor.cardPoints(card5) should be (1)
			scor.cardPoints(Card(Rank.Queen, Suit.Spades)) should be (13)
			scor.cardPoints(card1) should be (0)
			}

		"calculate points for a single player" in {
			scor.pointsForPlayer(p1) should be (1)
		}

		"match players and points to map" in {
			scor.rawPointsPerPlayer(Vector(p1,p2)) should be (Map((p1, 1),(p2, 0)))
		}

		"applyShootingTheMoon correctly" in {
            scor.applyShootingTheMoon(Map((p1, 1),(p2, 0))) should be (Map((p1, 0),(p2, 1)))
            scor.applyShootingTheMoon(Map((p1, 1),(p2, 1))) should be (Map((p1, 1),(p2, 1)))
        }

		"addRoundPoints to players from parameter" in {
			val Vector(r1, r2) = scor.addRoundPoints(Vector(p1, p2))
			val Vector(o1, o2, o3) = scor.addRoundPoints(Vector(p1, p2, p3))
			r1.points shouldBe p1.points
			r2.points shouldBe (p2.points + 1)
			o1.points shouldBe (p1.points + 1)
			o2.points shouldBe p2.points
			o3.points shouldBe (p3.points + 1)
			r1.wonCards shouldBe Nil
			r2.wonCards shouldBe Nil
			o1.wonCards shouldBe Nil
			o2.wonCards shouldBe Nil
			o3.wonCards shouldBe Nil

		}
	}
	"A ScoringInterface" should {
		"addPointsToPlayer" in {
			val Vector(o1, o2, o3) = scor.addPointsToPlayers(game)

						o1.points shouldBe (p1.points + 1)
			o2.points shouldBe p2.points
			o3.points shouldBe (p3.points + 1)
			o1.wonCards shouldBe Nil
			o2.wonCards shouldBe Nil
			o3.wonCards shouldBe Nil
		}
	}
}
