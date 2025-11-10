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
		gameController.completeTrickString() + newLine +
		gameController.getCurrentPlayerName() +" please select card to play:" + newLine +
		gameController.getCurrentPlayerHand() + newLine

	def getGameOverStateString(): String =
		""


	def update(): Unit = print(getGameplayStateString())


	def runGame(): Unit =
		//gameController.processInput("-1")
		while (!gameController.checkGameOver())
			gameController.processInput(readLine())


}