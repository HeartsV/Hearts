package de.htwg.se.Hearts.aview

import de.htwg.se.Hearts.controller.controllerComponent.ControllerInterface
import de.htwg.se.Hearts.controller.controllerComponent.controllerBase._
import de.htwg.se.Hearts.util._

import scala.io.StdIn.readLine
import de.htwg.se.Hearts.controller.controllerComponent.controllerBase.BackCommand

class Tui(gameController: ControllerInterface)  extends Observer:

	def trickToString: String =
		if (gameController.getTrickCards.nonEmpty) gameController.getTrickCards.map(card => s" $card ").mkString("|", "|", "|")
		else "|"

	def completeTrickString: String = trickToString + "     |" * (gameController.getPlayerSize - gameController.getTrickCards.size)

	def handToString: String =
		val h = gameController.getPlayerHand
		(1 to h.size).map(index => s"  $index".padTo(5, ' ')).mkString("|", "|", "|") + "\n" +
		h.map(card => s" $card ").mkString("|", "|", "|")

	def getMainScreenStateString: String =
		"Hearts" + "\n\n" +
		"Please enter:" + "\n" +
		"- n or new for a new Game" + "\n" +
		"- ru or rules for the rules" + "\n" +
		"- e or exit to end the program" + "\n"

	def getRulesScreenStateString: String =
		"\n\n" +"Rules:" + "\n\n" +
		"Hearts is a trick-taking card game played with 3-4 players, each playing individually." + "\n" +
		"With 4 players, the game uses all 52 cards of a standard deck." + "\n" +
		"With 3 players, one card is removed and the remaining 51 cards are used." + "\n" +
		"Each suit contains cards from Two (lowest) to Ace (highest), giving 13 cards per suit." + "\n" +
		"There is no trump suit." + "\n\n\n" +
		"Objective:" + "\n\n" +
		"The goal of the game is to collect as few penalty points as possible." + "\n" +
		"You receive penalty points for taking tricks that contain any Heart cards or the Queen of Spades." + "\n" +
		"The game ends when a player reaches or exceeds the maximum score (the standard is 100 points)." + "\n" +
		"The player with the lowest total score is the winner." + "\n\n\n" +
		"Dealing:" + "\n\n" +
		"This rule applies only with 4 players!!" + "\n" +
		"Cards are dealt clockwise, one at a time, until each player has 13 cards." + "\n" +
		"Players may only see their own hands." + "\n" +
		"With 3 players, one card is removed from the deck, and the remaining 51 cards are dealt evenly." + "\n" +
		"There is no passing in a 3-player game." + "\n\n\n" +
		"Passing Cards:" + "\n\n" +
		"After all cards have been dealt (4-player game only), the passing phase begins." + "\n" +
		"Each player selects three cards from their hand and passes them face-down to another player." + "\n\n" +
		"The passing rotation works as follows:" + "\n" +
		"- 1st round: Pass to the player on your left" + "\n" +
		"- 2nd round: Pass to the player on your right" + "\n" +
		"- 3rd round: Pass to the player across from you" + "\n" +
		"- 4th round: No passing:" + "\n" +
		"- Then the cycle repeats for the rest of the game." + "\n\n\n" +
		"How the Game Is Played:" + "\n\n" +
		"The player holding the Two of Clubs leads the first trick." + "\n" +
		"All players must follow suit if possible. If they cannot, they may play another suit under certain conditions." + "\n" +
		"The player who plays the highest card of the led suit wins the trick and leads the next one." + "\n\n" +
		"There are restrictions when playing Hearts or the Queen of Spades:" + "\n" +
		"- In the first trick, you may not play a Heart or the Queen of Spades, even if you cannot follow suit." + "\n" +
		"- You cannot lead Hearts until Hearts have been broken." + "\n" +
		"- Hearts are broken when a player who cannot follow suit plays a Heart, or when the Queen of Spades has been played." + "\n" +
		"- Exception: A player with only Hearts may lead Hearts at any time." + "\n\n" +
		"A round ends after all 13 tricks have been played." + "\n\n\n" +
		"Scoring:" + "\n\n" +
		"Each Heart is worth 1 penalty point, and the Queen of Spades is worth 13 points." + "\n" +
		"There are 26 total penalty points in each round in a 4-player game." + "\n" +
		"It can differ in a 3-player game since one card is removed." + "\n\n" +
		"If a player captures all penalty cards (all Hearts and the Queen of Spades), they score 0 points," + "\n" +
		"and each opponent scores 26 points instead. This is called Shooting the Moon." + "\n\n" +
		"The game ends when a player reaches 100 points (or another agreed limit)." + "\n" +
		"The player with the lowest score wins." + "\n\n" +
		"While playing you can enter redo to redo last step and undo to undo last step" + "\n" +
		"Enter 'back' or 'b' to return to the main menu." + "\n"

	def getPlayerNumberStateString: String = "please input a Number of Players between 3 and 4" + "\n"

	def getPlayerNamesStateString: String = f"please input the names of the ${gameController.getPlayerSize + 1}. player" + "\n"

	def setMaxScoreStateString: String = "please enter the score required to win (between 1 and 400)" + "\n"

	def getGameplayStateString: String =
		val lastCard = gameController.getLastCardPlayed match
			case Left("No Card") => ""
			case Left(error) => error + "\n"
			case Right(card) => card.toString + " played" + "\n"

		"Trick:" + "\n" +
		completeTrickString + "\n" +
		lastCard +
		gameController.getCurrentPlayerName +" please select card to play:" + "\n" +
		handToString + "\n"

	def getShowScoreStateString: String =
		"Scoreboard:\n" +
		(for((r, n, p) <- gameController.rankPlayers(gameController.getPlayersWithPoints)) yield (""+ r + ". " + n + 	": " + p)+ "\n").mkString +
		"Press any button to continue\n"

	def getGameOverStateString: String =
		"GAMEOVER\n\n" +
		"Scoreboard:\n" +
		(for((r, n, p) <- gameController.rankPlayers(gameController.getPlayersWithPoints)) yield (""+ r + ". " + n + 	": " + p)+ "\n").mkString +
		"Please enter:" + "\n" +
		"- n or new for a new Game" + "\n" +
		"- a or again for playing again" + "\n" +
		"- q or quit to go back to Mainmenu" + "\n" +
		"- e or exit to end the program" + "\n"

	def update: Unit =
		gameController.passStateString match
			case "MainScreenState" => print(getMainScreenStateString)
			case "RulesScreenState" => print(getRulesScreenStateString)
			case "GetPlayerNumberState" => print(getPlayerNumberStateString)
			case "GetPlayerNamesState" => print(getPlayerNamesStateString)
			case "SetMaxScoreState" => print(setMaxScoreStateString)
			case "GamePlayState" => print(getGameplayStateString)
			case "ShowScoreState" => print(getShowScoreStateString)
			case "GameOverState" => print(getGameOverStateString)

	def commandFor(state: String, input: String): Option[Command] =
		state match
			case "MainScreenState" =>
				input match
					case "new" | "n"   => Some(NewCommand())
					case "rules" | "ru"=> Some(RulesCommand())
					case "exit" | "e"  => Some(ExitCommand())
					case _             => None

			case "RulesScreenState" =>
				input match
					case "back" | "b"  => Some(BackCommand())
					case _             => None

			case "GetPlayerNumberState" =>
				input match
					case "undo" => Some(UndoCommand())
					case "redo" => Some(RedoCommand())
					case _      => Some(SetPlayerNumberCommand(index = input.toIntOption))

			case "GetPlayerNamesState" =>
				input match
					case "undo" => Some(UndoCommand())
					case "redo" => Some(RedoCommand())
					case _      => Some(AddPlayerCommand(name = input))

			case "SetMaxScoreState" =>
				input match
					case "undo" => Some(UndoCommand())
					case "redo" => Some(RedoCommand())
					case _      => Some(SetMaxScoreCommand(index = input.toIntOption))

			case "GamePlayState" =>
				input match
					case "suit" | "s" => Some(SetSortingSuitCommand())
					case "rank" | "r" => Some(SetSortingRankCommand())
					case "undo"       => Some(UndoCommand())
					case "redo"       => Some(RedoCommand())
					case _            => Some(PlayCardCommand(index = input.toIntOption))

			case "ShowScoreState" =>
				input match
					case "undo" => Some(UndoCommand())
					case "redo" => Some(RedoCommand())
					case _      => Some(ContinueCommand())

			case "GameOverState" =>
				input match
					case "new" | "n"   => Some(NewCommand())
					case "again" | "a" => Some(AgainCommand())
					case "quit" | "q"  => Some(QuitCommand())
					case "exit" | "e"  => Some(ExitCommand())
					case "undo"        => Some(UndoCommand())
					case "redo"        => Some(RedoCommand())
					case _             => None
			case _ => None


	def runGame: Unit =
		update
		while (gameController.getKeepProcessRunning)
			val input = readLine
			commandFor(gameController.passStateString, input).foreach(gameController.processInput)
			/*
			gameController.passStateString match
				case "MainScreenState" => input match
					case "new" | "n" =>
						gameController.processInput(NewCommand())
					case "rules" | "ru" =>
						gameController.processInput(RulesCommand())
					case "exit" | "e" =>
						gameController.processInput(ExitCommand())
					case _ => update

				case "RulesScreenState" =>
					input match
						case "back" | "b" =>
							gameController.processInput(BackCommand())
						case _ => update

				case "GetPlayerNumberState" =>
					input match
						case "undo" =>
							gameController.processInput(UndoCommand())
						case "redo" =>
							gameController.processInput(RedoCommand())
						case _ =>
							gameController.processInput(SetPlayerNumberCommand(index = input.toIntOption))

				case "GetPlayerNamesState" =>
					input match
						case "undo" =>
							gameController.processInput(UndoCommand())
						case "redo" =>
							gameController.processInput(RedoCommand())
						case _ =>
							gameController.processInput(AddPlayerCommand(name = input))

				case "SetMaxScoreState" =>
					input match
						case "undo" =>
							gameController.processInput(UndoCommand())
						case "redo" =>
							gameController.processInput(RedoCommand())
						case _ =>
							gameController.processInput(SetMaxScoreCommand(index = input.toIntOption))

				case "GamePlayState" =>
					input match
						case "suit" | "s" =>
							gameController.processInput(SetSortingSuitCommand())
						case "rank" | "r" =>
							gameController.processInput(SetSortingRankCommand())
						case "undo" =>
							gameController.processInput(UndoCommand())
						case "redo" =>
							gameController.processInput(RedoCommand())
						case _ =>
							gameController.processInput(PlayCardCommand(index = input.toIntOption))

				case "ShowScoreState" =>
					input match
						case "undo" =>
							gameController.processInput(UndoCommand())
						case "redo" =>
							gameController.processInput(RedoCommand())
						case _ =>
							gameController.processInput(ContinueCommand())

				case "GameOverState" =>
					input match
						case "new" | "n"=>
							gameController.processInput(NewCommand())
						case "again" | "a" =>
							gameController.processInput(AgainCommand())
						case "quit" | "q" =>
							gameController.processInput(QuitCommand())
						case "exit" | "e" =>
							gameController.processInput(ExitCommand())
						case "undo" =>
							gameController.processInput(UndoCommand())
						case "redo" =>
							gameController.processInput(RedoCommand())
						case _ => update*/

