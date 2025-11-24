package de.htwg.se.Hearts.aview

import de.htwg.se.Hearts.controller.*

import scala.io.StdIn.readLine

import de.htwg.se.Hearts.model.Suit

class Tui(gameController: Controller)  extends Observer{
	val nl = "\n"

	def getMainScreenStateString(): String = "Hearts" + nl + "new Game" + nl + "rules" + nl + "exit" + nl

	def getRulesScreenStateString(): String = "1 point per won hearts card" + nl + "13 points for won Q of Spades" + nl + "lowest points wins"

	def getPlayerNumberStateString(): String = "please input a Number of Players between 3 and 4" + nl

	def getPlayerNamesStateString(): String = f"please input the names of the ${gameController.getGame().players.size +1}. player" +nl

	def setMaxScoreStateString(): String = "please enter the score required to win (between 1 and 400)" +nl

	def getGameplayStateString(): String =
		"Trick:" + nl +
		gameController.completeTrickString() + nl +
		gameController.getCurrentPlayerName() +" please select card to play:" + nl +
		gameController.getCurrentPlayerHand() + nl

	def getShowScoreStateString(): String = "to be implemented" // wrong
	//Idee 1: Ohne sortierung einfach in der reihenfolge in der wir die Player eingetragen haben ausgeben
	//Idee/Möglichkeit 2: Wir sortieren die ausgabe aufsteigend sodass die Plazierung die du gerade hast auch schon mit angegeben werden kann.

	def getGameOverStateString(): String = "to be implemented"
		// Idee: Game Over ausgeben, darunter dann Das Scoreboard und darunter dann die information was man eingeben kann/muss um neuzustarten zu beenden, etc.

	def update(): Unit =
		gameController.state.getStateString() match
			case "MainScreenState" => print(getMainScreenStateString())
			case "RulesScreenState" => print(getRulesScreenStateString())
			case "GetPlayerNumberState" => print(getPlayerNumberStateString())
			case "GetPlayerNamesState" => print(getPlayerNamesStateString())
			case "SetMaxScoreState" => print(setMaxScoreStateString())
			case "GamePlayState" => print(getGameplayStateString())
			case "ShowScoreState" => print(getShowScoreStateString())
			case "GameOverState" => print(getGameOverStateString())

	def runGame(): Unit =
		update()
		while (gameController.getkeepProcessRunning())
			gameController.processInput(readLine())
}