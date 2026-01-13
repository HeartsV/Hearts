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
    "A Director" should {
        val card = Card(Rank.Two,Suit.Clubs)
        val card1 = Card(Rank.Two,Suit.Hearts)
        val card2 = Card(Rank.Ace,Suit.Clubs)
        val game = Game(playerNumber = Some(3),players = Vector(Player("Alice",List(card,card1,card2),List(),10)),currentPlayerIndex = Some(0))
        val gamePlayed = Game(Some(3),true,true,false,Vector(Player("Alice",List(card,card1),List(),10)),Some(100),Some(0))
        val builder = GameBuilder()
        val director = Director(builder)

        "copy the game" in {
            director.copyGameState(game)
            builder.game.playerNumber should be (Some(3))
            builder.reset
        }

        "reset for the next game" in {
            director.copyGameState(gamePlayed)
            director.resetForNextGame
            builder.game.players(0).points should be (0)
            builder.game.firstCard should be (true)
            builder.reset
        }

        "move cards form hand to Trick" in {
            director.copyGameState(game)
            director.moveCard(card)
            builder.game.startWithHearts should be (false)
            builder.game.trickCards should be (List(card))
            builder.game.firstCard should be (false)
            builder.game.players(0).hand should be (List(card1,card2))
            builder.game.highestCard should be (Some(card))
            director.moveCard(card1)
            builder.game.startWithHearts should be (true)
            builder.game.trickCards should be (List(card,card1))
            builder.game.firstCard should be (false)
            builder.game.players(0).hand should be (List(card2))
            builder.game.highestCard should be (Some(card))
            director.moveCard(card2)
            builder.game.startWithHearts should be (true)
            builder.game.trickCards should be (List(card,card1,card2))
            builder.game.firstCard should be (false)
            builder.game.players(0).hand should be (List())
            builder.game.highestCard should be (Some(card2))
        }
    }
}