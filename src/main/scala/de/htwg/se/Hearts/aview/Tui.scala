package de.htwg.se.Hearts.aview

import de.htwg.se.Hearts.model.Game
import scala.io.StdIn.readLine

class Tui {
	val newLine = "\n"
	def getPlayerNumberStateString(): String =
		""

	def getPlayerNamesStateString(): String =
		""

	def getGameplayStateString(): String =
		"Trick:" + newLine +
		Game.trick.trickToString() + newLine +
		Game.currentPlayer.get.name +" please select card to play:" + newLine +
		Game.currentPlayer.get.handToString() + newLine

	def getGameOverStateString(): String =
		""

	def parseUserInput(): Option[Int] =
		readLine().toIntOption




}