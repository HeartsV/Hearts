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

  def addPlayer(player: Player): Boolean = {
    players.addOne(player)
    true
  }

  def updateCurrentPlayer(): Unit = {
    if (firstCard == true)
      currentPlayer = players.find(_.hand.contains(Card(Rank.Two,Suit.Clubs)))
    else if(players.size == trick.cards.size)
      currentPlayer = trick.currentWinner
    else if(players.indexOf(currentPlayer.get) + 1 == players.size)
      currentPlayer = Some(players(0))
    else
      currentPlayer = Some(players((players.indexOf(currentPlayer.get) + 1)))
  }
}
