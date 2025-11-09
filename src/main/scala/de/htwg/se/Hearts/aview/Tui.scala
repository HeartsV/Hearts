package de.htwg.se.Hearts.aview

import de.htwg.se.Hearts.model.Game
import de.htwg.se.Hearts.controller

import scala.io.StdIn.readLine
import de.htwg.se.Hearts.controller.Observer
import de.htwg.se.Hearts.controller.Controller

class Tui(gameController: Controller)  extends Observer{
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


	def update(): Unit =
		print(getGameplayStateString())


	def runGame(): Unit =
		gameController.processInput("999")
		while (!Game.gameOver)
			gameController.processInput(readLine())


}