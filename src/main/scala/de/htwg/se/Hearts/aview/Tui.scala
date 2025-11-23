package de.htwg.se.Hearts.aview

import de.htwg.se.Hearts.controller.*

import scala.io.StdIn.readLine

import de.htwg.se.Hearts.model.Suit

class Tui(gameController: Controller)  extends Observer{
	val nl = "\n"

	def getMainScreenStateString(): String = "Hearts" + nl + "new Game" + nl + "rules" + nl + "exit"

	def getRulesScreenStateString(): String = "1 point per won hearts card" + nl + "13 points for won Q of Spades" + nl + "lowest points wins"
		
	def getPlayerNumberStateString(): String = "please input a Number of Players between 3 and 4" + nl

	def getPlayerNamesStateString(): String = f"please input the names of the ${gameController.getGame().players.size +1}. player" +nl

	def setMaxScoreStateString(): String = "please enter the score required to win (between 1 and 400)" +nl

	def getGameplayStateString(): String =
		"Trick:" + nl +
		gameController.completeTrickString() + nl +
		gameController.getCurrentPlayerName() +" please select card to play:" + nl +
		gameController.getCurrentPlayerHand() + nl

	def getShowScoreState(): String = "to be implemented"

	def getGameOverStateString(): String =
		"GameOver:" + nl +
		gameController.getGame().players.map(p => s"${p.name}: ${p.wonCards.count(_.suit == Suit.Hearts)}").mkString("",nl,"") + nl

	def update(): Unit =
		gameController.state.getStateString() match
			case "MainScreenState" => print(getMainScreenStateString())
			case "RulesScreenState" => print(getRulesScreenStateString())
			case "GetPlayerNumberState" => print(getPlayerNumberStateString())
			case "GetPlayerNamesState" => print(getPlayerNamesStateString())
			case "SetMaxScoreState" => print(setMaxScoreStateString())
			case "GamePlayState" => print(getGameplayStateString())
			case "ShowScoreState" => print(getShowScoreState())
			case "GameOverState" => print(getGameOverStateString())

	def runGame(): Unit =
		update()
		while (!gameController.checkGameOver())
			gameController.processInput(readLine())
}