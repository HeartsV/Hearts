package de.htwg.se.Hearts.aview

import de.htwg.se.Hearts.controller.controllerComponent.ControllerInterface
import de.htwg.se.Hearts.util._

import scalafx._
import scalafx.application.JFXApp3
import scalafx.application.Platform
import scalafx.application.{JFXApp3, Platform}
import scalafx.scene.Scene
import scalafx.scene.control.*
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout.*
import scalafx.geometry.Insets
import scalafx.Includes.*
import scalafx.scene.shape.StrokeLineCap.Butt

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

	lazy val newGameButton = new Button("New Game"):
		onAction = _ => gameController.processInput("n")
	lazy val rulesButton = new Button("Rules"):
		onAction = _ => gameController.processInput("rules")
	lazy val exitButton = new Button("Exit"):
		onAction = _ =>
			gameController.processInput("exit")
			Platform.exit()
	lazy val againButton = new Button("Play again with same settings"):
		onAction = _ => gameController.processInput("a")
	lazy val quitButton = new Button("Quit to Main Menu"):
		onAction = _ => gameController.processInput("q")
	lazy val textField = new TextField():
		onAction = _ =>
			gameController.processInput(text())
			text = ""
	lazy val backButton = new Button("Back"):
		onAction = _ => gameController.processInput("b")
	lazy val suitSortButton = new Button("Sort by: suit"):
		onAction = _ => gameController.processInput("suit")
	lazy val rankSortButton = new Button("Sort by: rank"):
		onAction = _ => gameController.processInput("rank")
	lazy val scoreBox = new VBox()

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
					onMouseClicked = _ => gameController.processInput("" + (index + 1))
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
		val centerBox = new VBox:
			children = Seq(
				Label("Main Menu"),
				newGameButton,
				rulesButton,
				exitButton
			)
		rootBorderPane.top = new VBox:
			children = new Label("Hearts")
		rootBorderPane.center = centerBox
		rootBorderPane.bottom = null


	def showRulesScreenState(): Unit =
		val text = "a, b, c"
		val ruleText = new TextArea(text):
			editable = false

		rootBorderPane.top = new Label("Rules")
		rootBorderPane.center = ruleText
		rootBorderPane.bottom = backButton

	def showPlayerNumberState(): Unit =

		val centerBox = new VBox:
			children = Seq(
				Label("please enter a number:"),
				textField
			)

		rootBorderPane.top = Label("Setup")
		rootBorderPane.center = centerBox
		rootBorderPane.bottom = null


	def showPlayerNamesState(): Unit =

		val centerBox = new VBox:
			children = Seq(
				Label(f"please input the names of the ${gameController.getPlayerSize + 1}. player"),
				textField
			)

		rootBorderPane.top = Label("Setup")
		rootBorderPane.center = centerBox
		rootBorderPane.bottom = null

	def showMaxScoreState(): Unit =

		val centerBox = new VBox:
			children = Seq(
				Label("please enter the score required to win (between 1 and 400)"),
				textField
			)

		rootBorderPane.top = Label("Setup")
		rootBorderPane.center = centerBox
		rootBorderPane.bottom = null

	def showGameplayState(): Unit =

		val buttonBox = new HBox:
			children = Seq(
				suitSortButton,
				rankSortButton,
				exitButton,
				rulesButton
			)

		val topBox = new VBox:
			children = Seq(
				Label(""),
				buttonBox
			)

		val centerBox = new VBox:
			children = Seq(
				Label("Trick:"),
				renderTrick(gameController.cardsPathList(gameController.getTrickCards))
			)



		val bottomBox = new VBox:
			val lastCard = gameController.getLastCardPlayed match
				case Left(error) => error
				case Right(card) => card.toString + " played"
			val pHand = gameController.getCurrentPlayerName
			children = Seq(
				Label(lastCard),
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
		rootBorderPane.bottom =
			new Button("Continue"):
				onAction = _ => gameController.processInput("a")

	def showGameOverState(): Unit =
		renderScoreBoard

		val bottomBox = new HBox:
			children = Seq(
				newGameButton,
				againButton,
				quitButton,
				exitButton
			)

		rootBorderPane.top = Label("GAME OVER")
		rootBorderPane.center = scoreBox
		rootBorderPane.bottom = bottomBox
