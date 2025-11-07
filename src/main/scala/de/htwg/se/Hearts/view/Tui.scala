package de.htwg.se.Hearts.view

import de.htwg.se.Hearts.model.Game
import scala.io.StdIn.readLine

object Tui {
	def getPlayerNumberStateString(): String =
		""

	def getPlayerNamesStateString(): String =
		""

	def getGameplayStateString(): String =
		Game.currentPlayer.get.name +
		" please select card to play:\n" +
		Game.trick.trickToString() + "\n" +
		Game.currentPlayer.get.handToString()

	def getGameOverStateString(): String =
		""

	def parseUserInput(): Option[Int] =
		readLine().toIntOption




}