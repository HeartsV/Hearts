package de.htwg.se.Hearts.model

import scala.collection.mutable.ListBuffer

class Game () {
    var playerNumber: Option[Int] = None
    var startWithHearts: Boolean = false
    var gameOver: Boolean = false
    var firstCard: Boolean = true
    val players: ListBuffer[Player] = ListBuffer.empty[Player]
    var maxScore: Option[Int] = None
    var currentPlayer: Option[Player] = None
    val trick = Trick()

    def addPlayer(player: Player): Boolean =
        players.addOne(player)
        true

    /*def addPlayer(newPlayerName: String): Boolean =
        players.addOne(Player(newPlayerName))
        true
    Das wäre meine neue/geänderte Funktion*/
}
