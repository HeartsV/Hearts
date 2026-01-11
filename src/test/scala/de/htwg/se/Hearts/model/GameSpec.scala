package de.htwg.se.Hearts.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.Hearts.controller._
import de.htwg.se.Hearts.model._
import de.htwg.se.Hearts.model.gameComponent.gameBase.Card
import de.htwg.se.Hearts.model.gameComponent.gameBase.Game
import de.htwg.se.Hearts.model.gameComponent.gameBase.Player
import de.htwg.se.Hearts.model.gameComponent._

class GameSpec extends AnyWordSpec with Matchers {
    "A Game" should {
        val card = Card(Rank.Ace,Suit.Clubs)
        val card1 = Card(Rank.Two,Suit.Clubs)
        val card2 = Card(Rank.Two,Suit.Diamonds)
        val p1 = Player("Alice", List(card1),points = 10)
        val p2 = Player("Bob", List(card2))
        val game = Game(players = Vector(p1),trickCards = List(card1),currentPlayerIndex = Some(0),currentWinnerIndex = Some(0), highestCard = Some(card1), maxScore = Some(1))

        "get the number of players" in {
            game.getPlayerNumber should be (None)
        }

        "get value of startWithHearts" in {
            game.getStartWithHearts should be (false)
        }

        "get value of keepProcessRunning" in {
            game.getKeepProcessRunning should be (true)
        }

        "get value of firstCard" in {
            game.getFirstCard should be (true)
        }

        "get vector of players" in {
            game.getPlayers should be (Vector(p1))
        }

        "get value of maxScore" in {
            game.getMaxScore should be (Some(1))
        }

        "get the current player" in {
            game.getCurrentPlayer should be (Some(p1))
        }

        "get value of trickCards" in {
            game.getTrickCards should be (List(card1))
        }

        "get highestCard" in {
            game.getHighestCard should be (Some(card1))
        }

        "get currentWinnerIndex" in {
            game.currentWinnerIndex should be (Some(0))
        }

        "get lastCardPlayed" in {
            game.lastCardPlayed should be (Left("No Card"))
        }

        "get currentPlayerIndex" in {
            game.currentPlayerIndex should be (Some(0))
        }
    }
}