package de.htwg.se.Hearts.model
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.Hearts.model.gameComponent.gameBase.Game
import de.htwg.se.Hearts.model.gameComponent.gameBase.GameBuilder
import de.htwg.se.Hearts.model.gameComponent.gameBase.Director
import de.htwg.se.Hearts.model.gameComponent.gameBase.Card
import de.htwg.se.Hearts.model.gameComponent.GameInterface
import de.htwg.se.Hearts.model.gameComponent.gameBase.Player
import de.htwg.se.Hearts.model.gameComponent.Rank
import de.htwg.se.Hearts.model.gameComponent.Suit

class DirectorSpec extends AnyWordSpec with Matchers {
    /*
    Das war im BuilderSpec drin, deswegen habe ich es mal raus und hier rüber aber es kann glaub ganz weg!
    "A Director" should {
		"resetForNextGame" in {
			val builder = GameBuilder(gameHearts)
			val director = Director(builder)
			director.resetForNextGame
			builder.game.startWithHearts should be (false)
			builder.game.firstCard should be (true)
			builder.game.players(0).points should be (0)
			builder.game.players(1).points should be (0)
	}
    */

    "A Director" should {
        val card = Card(Rank.Two,Suit.Clubs)
        val card1 = Card(Rank.Two,Suit.Hearts)
        val card2 = Card(Rank.Ace,Suit.Clubs)
        val game = Game(playerNumber = Some(3),players = Vector(Player("Alice",List(card,card1,card2),List(),10)),currentPlayerIndex = Some(0))
        val gamePlayed = Game(Some(3),true,true,false,Vector(Player("Alice",List(card,card1),List(),10)),Some(100),Some(0))
        val director = Director()

        "copy the game" in {
            director.copyGameState(game)
            director.getBuilder.getPlayerNumber should be (3)
            director.getBuilder.reset
        }

        "reset for the next game" in {
            director.copyGameState(gamePlayed)
            director.resetForNextGame
            director.getBuilder.getPlayers(0).getPoints should be (0)
            director.getBuilder.getFirstCard should be (true)
            director.getBuilder.reset
        }

        "move cards form hand to Trick" in {
            director.copyGameState(game)
            director.moveCard(card)
            director.getBuilder.getStartWithHearts should be (false)
            director.getBuilder.getTrickCards should be (List(card))
            director.getBuilder.getFirstCard should be (false)
            director.getBuilder.getPlayers(0).getHand should be (List(card1,card2))
            director.getBuilder.getHighestCard should be (Some(card))
            director.moveCard(card1)
            director.getBuilder.getStartWithHearts should be (true)
            director.getBuilder.getTrickCards should be (List(card,card1))
            director.getBuilder.getFirstCard should be (false)
            director.getBuilder.getPlayers(0).getHand should be (List(card2))
            director.getBuilder.getHighestCard should be (Some(card))
            director.moveCard(card2)
            director.getBuilder.getStartWithHearts should be (true)
            director.getBuilder.getTrickCards should be (List(card,card1,card2))
            director.getBuilder.getFirstCard should be (false)
            director.getBuilder.getPlayers(0).getHand should be (List())
            director.getBuilder.getHighestCard should be (Some(card2))
        }
    }
}