package de.htwg.se.Hearts.aview

import de.htwg.se.Hearts.controller

import scala.io.StdIn.readLine
import de.htwg.se.Hearts.controller.Observer
import de.htwg.se.Hearts.controller.Controller

class Tui(gameController: Controller)  extends Observer{
	val nl = "\n"
	def getPlayerNumberStateString(): String =
		""

	def getPlayerNamesStateString(): String =
		""

	def getGameplayStateString(): String =
		"Trick:" + nl +
		gameController.completeTrickString() + nl +
		gameController.getCurrentPlayerName() +" please select card to play:" + nl +
		gameController.getCurrentPlayerHand() + nl

	def getGameOverStateString(): String =
		""


	def update(): Unit = print(getGameplayStateString())


	def runGame(): Unit =
		gameController.updateCurrentPlayer()
		update()
		while (!gameController.checkGameOver())
			gameController.processInput(readLine())
}