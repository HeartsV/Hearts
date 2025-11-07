package de.htwg.se.Hearts.view

import de.htwg.se.Hearts.model.Game

class Tui {
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

	def parseUserInput(): Int =
		6



}