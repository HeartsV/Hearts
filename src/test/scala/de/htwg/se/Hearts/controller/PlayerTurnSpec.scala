package de.htwg.se.Hearts.controller

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

import de.htwg.se.Hearts.controller.playerTurnComponent.PlayerTurnInterface
import de.htwg.se.Hearts.controller.playerTurnComponent.playerTurnBase._
import de.htwg.se.Hearts.model.gameComponent._
import de.htwg.se.Hearts.model.gameComponent.gameBase.Game
import de.htwg.se.Hearts.model.gameComponent.gameBase.Card
import de.htwg.se.Hearts.model.gameComponent.gameBase.Player
import de.htwg.se.Hearts.model.gameComponent.Suit
import de.htwg.se.Hearts.model.gameComponent.Rank
import scalafx.scene.input.KeyCode.G
import scalafx.scene.input.KeyCode.GameA

class PlayerTurnSpec extends AnyWordSpec with Matchers {
	"A PlayerTurn" should {
		val card1 = Card(Rank.Two,Suit.Clubs)
		val card2 = Card(Rank.Two,Suit.Diamonds)
		val card3 = Card(Rank.Ace,Suit.Clubs)
		val card4 = Card(Rank.Ace,Suit.Diamonds)
		val card5 = Card(Rank.Jack,Suit.Hearts)
		val card6 = Card(Rank.Jack,Suit.Clubs)
		val card7 = Card(Rank.Ten,Suit.Clubs)
		val card8 = Card(Rank.Ten,Suit.Diamonds)
		val p1 = Player("Alice",List(card1,card2),List(card5))
		val p2 = Player("Dave",List(card3,card4,card5))
		val p3 = Player("Charlie", List(card5, card5))
		val gameStart = Game(playerNumber = Some(3), players = Vector(p1, p2, p3), currentPlayerIndex = None)
		val gameFullTrick = Game(playerNumber = Some(3), players = Vector(p1, p2, p3), trickCards = List(card1, card2, card3), currentPlayerIndex = Some(2), currentWinnerIndex = Some(1), firstCard = false)
		val gameCicleBack = Game(playerNumber = Some(3), players = Vector(p1, p2, p3), trickCards = List(card7), currentPlayerIndex = Some(2), firstCard = false, currentWinnerIndex = Some(2), highestCard = Some(card7))
		val gameNormal = Game(playerNumber = Some(3), players = Vector(p1, p2, p3), currentPlayerIndex = Some(1), firstCard = false)
		val turn = PlayerTurn()
		"nextPlayerIndex" should {
			"update PlayerIndex to Player with the 2 \u2663" in {
				turn.nextPlayerIndex(gameStart) should be (0)
			}
			"update PlayerIndex to winner of last trick when the last trick was full" in {
				turn.nextPlayerIndex(gameFullTrick) should be (1)
			}

			"set PlayerIndex to first Player in Vector when the end is reached" in {
				turn.nextPlayerIndex(gameCicleBack) should be (0)
			}

			"update PlayerIndex to next Player" in {
				turn.nextPlayerIndex(gameNormal) should be (2)
			}
		}

		"updateCurrentWinner" should {
			"return the winner from parameter newWinner when HighestCard is None" in {
				turn.updateCurrentWinner((0, card1), gameStart) should be (Some(0), Some(card1))
			}

			"return the winner when new Card is the same Suit and higher than the currentHighestCard " in {
				turn.updateCurrentWinner((1, card3), gameCicleBack) should be (Some(1), Some(card3))
			}

			"return the CurrentWinner when the new Card is not the same Suit or not higher than the currentHighestCard" in {
				turn.updateCurrentWinner((0, card1), gameCicleBack) should be (Some(2), Some(card7))
			}
		}
	}
}
