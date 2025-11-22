package de.htwg.se.Hearts.model

import scala.compiletime.ops.boolean
import scala.collection.mutable.ListBuffer


class Game () {
  var startWithHearts: Boolean = false
  var gameOver: Boolean = false
  var firstCard: Boolean = true
  val players: ListBuffer[Player] = ListBuffer.empty[Player]
  var currentPlayer: Option[Player] = None
  val trick = Trick()

  def addPlayer(player: Player): Boolean =
    players.addOne(player)
    true
}
