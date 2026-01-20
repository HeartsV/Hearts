package de.htwg.se.Hearts.aview

import de.htwg.se.Hearts.controller.controllerComponent.ControllerInterface
import de.htwg.se.Hearts.util._

import scalafx._
import scalafx.application.JFXApp3
import scalafx.application.Platform
import scalafx.application.{JFXApp3, Platform}
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout._
import scalafx.geometry.Insets
import scalafx.Includes._
import scalafx.scene.shape.StrokeLineCap.Butt
import de.htwg.se.Hearts.controller.controllerComponent.controllerBase._
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.Alert

class Gui(gameController: ControllerInterface) extends JFXApp3 with Observer:

	val rootBorderPane = new BorderPane()
	override def start(): Unit =
		stage = new JFXApp3.PrimaryStage:
			title = "Hearts"
			scene = new Scene(1000, 700):
				root = rootBorderPane
		update

	override def update: Unit =
		Platform.runLater {
			gameController.passStateString match
				case "MainScreenState" => showMainScreenState()
				case "RulesScreenState" => showRulesScreenState()
				case "GetPlayerNumberState" => showPlayerNumberState()
				case "GetPlayerNamesState" => showPlayerNamesState()
				case "SetMaxScoreState" => showMaxScoreState()
				case "GamePlayState" => showGameplayState()
				case "ShowScoreState" => showShowScoreState()
				case "GameOverState" => showGameOverState()
		}

	lazy val undoButton = new Button("Undo"):
		onAction = _ => gameController.processInput(UndoCommand())
	lazy val redoButton = new Button("Redo"):
		onAction = _ => gameController.processInput(RedoCommand())
	lazy val saveButton = new Button("Save"):
		onAction = _ => gameController.processInput(SaveCommand())
	lazy val loadButton = new Button("Load"):
		onAction = _ => gameController.processInput(LoadCommand())
	lazy val newGameButton = new Button("New Game"):
		onAction = _ => gameController.processInput(NewCommand())
	lazy val rulesButton = new Button("Rules"):
		onAction = _ => gameController.processInput(RulesCommand())
	lazy val exitButton = new Button("Exit"):
		onAction = _ =>
			gameController.processInput(ExitCommand())
			Platform.exit()
	lazy val againButton = new Button("Play again with same settings"):
		onAction = _ => gameController.processInput(AgainCommand())
	lazy val quitButton = new Button("Quit to Main Menu"):
		onAction = _ => gameController.processInput(QuitCommand())
	lazy val continueButton = new Button("Continue"):
		onAction = _ => gameController.processInput(ContinueCommand())
	lazy val backButton = new Button("Back"):
		onAction = _ => gameController.processInput(BackCommand())
	lazy val suitSortButton = new Button("Sort by: suit"):
		onAction = _ => gameController.processInput(SetSortingSuitCommand())
	lazy val rankSortButton = new Button("Sort by: rank"):
		onAction = _ => gameController.processInput(SetSortingRankCommand())
	lazy val scoreBox = new VBox()


	lazy val undoBox = new HBox:
		children = Seq(undoButton, redoButton, fileIOBox)

	lazy val fileIOBox = new HBox:
		children = Seq(saveButton, loadButton)

	def renderTrick(imageUrls: List[String]): HBox =
		val trickBox = new HBox()

		imageUrls.foreach {
			url =>
				val iv = new ImageView(new Image(url))
				trickBox.children.add(iv)
		}
		trickBox

	def renderHand(imageUrls: List[String]): HBox =
		val handBox = new HBox()
		imageUrls.zipWithIndex.foreach {
			case (url, index) =>
				val iv = new ImageView(new Image(url)) {
					onMouseClicked = _ => gameController.processInput(PlayCardCommand(index = Some(index + 1)))
				}
				handBox.children.add(iv)
		}
		handBox

	def renderScoreBoard: Unit =
		scoreBox.children.clear()

		gameController.rankPlayers(gameController.getPlayersWithPoints).foreach(
			(r, n, p) =>
				val label = new Label(""+ r + ". " + n + 	": " + p)
				scoreBox.children.add(label)
			)

	def showMainScreenState(): Unit =
		val topBox = new VBox:
			children = Seq(Label(gameController.errorOrLastCardPlayedToString), Label("Hearts"))

		val centerBox = new VBox:
			children = Seq(Label("Main Menu"), loadButton, newGameButton, rulesButton, exitButton)
		rootBorderPane.top = topBox
		rootBorderPane.center = centerBox
		rootBorderPane.bottom = null


	def showRulesScreenState(): Unit =
		val text =
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

		val ruleText = new TextArea(text):
			editable = false

		rootBorderPane.top = new Label("Rules")
		rootBorderPane.center = ruleText
		rootBorderPane.bottom = backButton

	def showPlayerNumberState(): Unit =
		val topBox = new VBox:
			children = Seq(Label(gameController.errorOrLastCardPlayedToString), Label("Setup"))

		val textField = new TextField():
			onAction = _ =>
				gameController.processInput(SetPlayerNumberCommand(index = text().toIntOption))
				text = ""
		Platform.runLater {textField.requestFocus()}

		val centerBox = new VBox:
			children = Seq(Label("please enter a number:"), textField)

		rootBorderPane.top = topBox
		rootBorderPane.center = centerBox
		rootBorderPane.bottom = undoBox


	def showPlayerNamesState(): Unit =
		val topBox = new VBox:
			children = Seq(Label(gameController.errorOrLastCardPlayedToString), Label("Setup"))

		val textField = new TextField():
			onAction = _ =>
				gameController.processInput(AddPlayerCommand(name = text()))
				text = ""
		Platform.runLater {textField.requestFocus()}

		val centerBox = new VBox:
			children = Seq(Label(f"please input the names of the ${gameController.getPlayerSize + 1}. player"), textField)

		rootBorderPane.top = topBox
		rootBorderPane.center = centerBox
		rootBorderPane.bottom = undoBox

	def showMaxScoreState(): Unit =
		val topBox = new VBox:
			children = Seq(Label(gameController.errorOrLastCardPlayedToString), Label("Setup"))

		val textField = new TextField():
			onAction = _ => gameController.processInput(SetMaxScoreCommand(index = text().toIntOption))
				text = ""
		Platform.runLater {textField.requestFocus()}

		val centerBox = new VBox:
			children = Seq(
				Label("please enter the score required to win (between 1 and 400)"),
				textField
			)

		rootBorderPane.top = topBox
		rootBorderPane.center = centerBox
		rootBorderPane.bottom = undoBox

	def showGameplayState(): Unit =

		val buttonBox = new HBox:
			children = Seq(
				suitSortButton,
				rankSortButton,
				exitButton,
				rulesButton
			)

		val topBox = new HBox:
			children = Seq(
				Label(""),
				buttonBox,
				undoBox
			)

		val centerBox = new VBox:
			children = Seq(
				Label("Trick:"),
				renderTrick(gameController.cardsPathList(gameController.getTrickCards))
			)



		val bottomBox = new VBox:
			val pHand = gameController.getCurrentPlayerName
			children = Seq(
				Label(gameController.errorOrLastCardPlayedToString),
				Label(pHand + "'s hand:"),
				renderHand(gameController.cardsPathList(gameController.getPlayerHand))
			)

		rootBorderPane.top = topBox
		rootBorderPane.center = centerBox
		rootBorderPane.bottom = bottomBox

	def showShowScoreState(): Unit =
		renderScoreBoard

		rootBorderPane.top = Label("Scoreboard:")
		rootBorderPane.center = scoreBox
		rootBorderPane.bottom = new HBox:
			children = Seq(continueButton, Label(gameController.errorOrLastCardPlayedToString), undoBox)


	def showGameOverState(): Unit =
		renderScoreBoard
		val centerBox = new VBox:
			children = Seq(Label(gameController.errorOrLastCardPlayedToString), scoreBox)
		val bottomBox = new HBox:
			children = Seq(
				newGameButton,
				againButton,
				quitButton,
				exitButton,
				undoBox
			)

		rootBorderPane.top = Label("GAME OVER")
		rootBorderPane.center = centerBox
		rootBorderPane.bottom = bottomBox
