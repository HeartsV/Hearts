package de.htwg.se.Hearts.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import scala.collection.mutable.ListBuffer

class GameSpec extends AnyWordSpec with Matchers {
  "A Game" should {
    val currentGame = Game()
    val p1 = Player("Alice",List[Card](),List[Card]())
    val p2 = Player("Dave",List[Card](),List[Card]())

    "set player number" in {
        currentGame.setPlayerNumber(6)
        currentGame.playerNumber should be (None: Option[Int])
        currentGame.setPlayerNumber(4)
        currentGame.playerNumber should be (Some(4))
    }

    "add player" in{
        currentGame.setPlayerNumber(2)
        currentGame.addPlayer(p1)
        currentGame.addPlayer(p2)
        currentGame.players should be (ListBuffer[Player](p1,p2))
    }

    "recognize first card played" in {
        currentGame.setPlayerNumber(2)
        currentGame.addPlayer(Player("Alice",List[Card](),List[Card]()))
        currentGame.addPlayer(Player("Dave",List[Card](),List[Card]()))

    }


    "recoginize first heart card played" in {

    }

    

  }
}
