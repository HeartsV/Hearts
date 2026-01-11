package de.htwg.se.Hearts.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.Hearts.model.gameComponent.gameBase.{Director, GameBuilder}
import de.htwg.se.Hearts.model.gameComponent._
import de.htwg.se.Hearts.model.gameComponent.gameBase._

class BuilderSpec extends AnyWordSpec with Matchers {
	val card1 = Card(Rank.Two,Suit.Clubs)
	val card2 = Card(Rank.Two,Suit.Diamonds)
	val card3 = Card(Rank.Ace,Suit.Clubs)
	val card4 = Card(Rank.Ace,Suit.Diamonds)
	val card5 = Card(Rank.Jack,Suit.Hearts)
	val card6 = Card(Rank.Jack,Suit.Hearts)
	val card7 = Card(Rank.Ten,Suit.Clubs)
	val card8 = Card(Rank.Ten,Suit.Diamonds)
	val p1 = Player("Alice",List(card1,card2),List(card5), 10)
	val p2 = Player("Dave",List(card3,card4), points = 10)
	val gameFirstCard = Game(playerNumber = Some(2),players = Vector(p1,p2),currentPlayerIndex = Some(0))
	val gameHearts = Game(playerNumber = Some(2),players = Vector(p1,p2),firstCard = false,startWithHearts = true,currentPlayerIndex = Some(0))

	"A builder" should {

		"reset itself" in {
			val builder = GameBuilder(gameFirstCard)
			builder.reset
			builder.game should be (Game())
		}

		"setPlayerNumber of game" in {
			val builder = GameBuilder()
			builder.setPlayerNumber(Some(3))
			builder.game.playerNumber should be (Some(3))
		}

		"setStartWithHearts to boolean" in {
			val builder = GameBuilder()
			builder.setStartWithHearts(true)
			builder.game.startWithHearts should be (true)
		}

		"setKeepProcessRunning to boolean" in {
			val builder = GameBuilder()
			builder.setKeepProcessRunning(false)
			builder.game.keepProcessRunning should be (false)
		}

		"setFirstCard to boolean" in {
			val builder = GameBuilder()
			builder.setFirstCard(false)
			builder.game.firstCard should be (false)
		}

		"setPlayers" in {
			val builder = GameBuilder()
			builder.setPlayers(Vector(p1))
			builder.game.players should be (Vector(p1))
		}

		"updatePlayer at given index" in {
			val builder = GameBuilder(gameFirstCard)
			builder.updatePlayer(0, p2)
			builder.game.players should be (Vector(p2, p2))
		}

		"setMaxScore" in {
			val builder = GameBuilder()
			builder.setMaxScore(Some(1))
			builder.game.maxScore should be (Some(1))
		}

		"setCurrentPlayerIndex" in {
			val builder = GameBuilder()
			builder.setCurrentPlayerIndex(Some(0))
			builder.game.currentPlayerIndex should be (Some(0))
		}

		"setTrickCards" in {
			val builder = GameBuilder()
			builder.setTrickCards(List(card3,card4))
			builder.game.trickCards should be (List(card3,card4))
		}

		"addCard to trickCards" in {
			val builder = GameBuilder()
			builder.addCard(card2)
			builder.game.trickCards should be (List(card2))
		}

		"setCurrentWinnerAndHighestCard" in {
			val builder = GameBuilder()
			builder.setCurrentWinnerAndHighestCard((Some(0),Some(card8)))
			builder.game.currentWinnerIndex should be (Some(0))
			builder.game.highestCard should be (Some(card8))
		}

		"setLastPlayedCard" in {
			val builder = GameBuilder()
			builder.setLastPlayedCard(Right(card8))
			builder.game.lastCardPlayed should be (Right(card8))
		}

		"getGame" in {
			val builder = GameBuilder()
			builder.getGame should be (Game())

		}
	}

	"A Director" should {
		"resetForNextGame" in {
			val builder = GameBuilder(gameHearts)
			val director = Director()
			director.resetForNextGame(builder)
			builder.game.startWithHearts should be (false)
			builder.game.firstCard should be (true)
			builder.game.players(0).points should be (0)
			builder.game.players(1).points should be (0)
		}
	}
}