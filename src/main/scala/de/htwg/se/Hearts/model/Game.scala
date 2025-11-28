/*package de.htwg.se.Hearts.model

import scala.collection.mutable.ListBuffer

class Game ():
    var playerNumber: Option[Int] = None
    var startWithHearts: Boolean = false
    var keepProcessRunning: Boolean = true
    var firstCard: Boolean = true
    val players: ListBuffer[Player] = ListBuffer.empty[Player]
    var maxScore: Option[Int] = None
    var currentPlayer: Option[Player] = None
    val trick = Trick()

    def addPlayer(newPlayerName: Player): Boolean =
        players.addOne(newPlayerName)
        true*/

package de.htwg.se.Hearts.model

case class Game(
    playerNumber: Option[Int] = None,
    startWithHearts: Boolean = false,
    keepProcessRunning: Boolean = true,
    firstCard: Boolean = true,
    players: Vector[Player] = Vector.empty,
    maxScore: Option[Int] = None,
    currentPlayerIndex: Option[Int] = None,
    trick: Trick = Trick()):

  def addPlayer(newPlayer: Player): Game =
    val newPlayers = players :+ newPlayer
    copy(players = newPlayers, playerNumber = Some(newPlayers.size))

  def setMaxScore(score: Int): Game = copy(maxScore = Some(score))

  def setCurrentPlayerIndex(index: Int): Game = copy(currentPlayerIndex = Some(index))

  def getCurrentPlayer: Option[Player] = currentPlayerIndex.flatMap(players.lift)

  def updatePlayer(index: Int, updatedPlayer: Player): Game = copy(players = players.updated(index, updatedPlayer))

  def resetForNewGame: Game =
    val resetPlayers = players.map(_.copy(points = 0))
    copy(players = resetPlayers, firstCard = true, startWithHearts = false)
