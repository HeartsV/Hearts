package de.htwg.se.Hearts.model

import scala.compiletime.ops.boolean
import org.scalactic.Bool
import scala.collection.mutable.ListBuffer


class Game() {
  var startWithHearts: Boolean = false
  var firstCard: Boolean = true
  var playerNumber: Option[Int] = None
  val players: ListBuffer[Player] = ListBuffer.empty[Player]

  def setPlayerNumber(number: Int): Boolean = {
    if (number > 1 && number < 5){
        playerNumber = Some(number)
        true
    } else {
        false
    }
  }

  def addPlayer(player: Player): Boolean ={
    players.addOne(player)
    true
  }

}
