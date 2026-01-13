package de.htwg.se.Hearts.controller

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.Hearts.model.gameComponent.gameBase.*
import de.htwg.se.Hearts.controller.controllerComponent.controllerBase._
import de.htwg.se.Hearts.model.gameComponent._

class StrategySpec extends AnyWordSpec with Matchers {

	"A Strategy" should {
		val strat1 = SortByRankStrategy()
		val strat2 = SortBySuitStrategy()
		val card1 = Card(Rank.Two,Suit.Clubs)
		val card2 = Card(Rank.Two,Suit.Diamonds)
		val card3 = Card(Rank.Ace,Suit.Clubs)
		val card4 = Card(Rank.Ace,Suit.Diamonds)
		val card5 = Card(Rank.Jack,Suit.Hearts)
		val card6 = Card(Rank.Jack,Suit.Clubs)
		val card7 = Card(Rank.Ten,Suit.Clubs)
		val card8 = Card(Rank.Ten,Suit.Diamonds)
		val p1 = Player("Alice", List(card1, card2, card3, card4), List(card5))

		"sort cards by rank" in {
			strat1.execute(p1) should be (List(card2, card1, card4, card3))
		}

		"sort cards by suit" in {
			strat2.execute(p1) should be (List(card2, card4, card1, card3))
		}
	}
}
