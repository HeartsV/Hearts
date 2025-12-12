
package de.htwg.se.Hearts.aview

import de.htwg.se.Hearts.controller.*
import de.htwg.se.Hearts.model.*

import scalafx.application.{JFXApp3, Platform}
import scalafx.scene.Scene
import scalafx.scene.control.*
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout.*
import scalafx.geometry.Insets
import scalafx.Includes.*

// ScalaFX GUI + Observer to the event-based controller
object GuiApp extends JFXApp3 with Observer:

  // Will be injected from Main
  var gameController: Controller = _

  def init(controller: Controller): Unit =
    this.gameController = controller
    gameController.add(this)

  // === Observer callback ===
  override def update: Unit =
    Platform.runLater {
      if stage != null then
        updateUi()
    }

  private val rootBorderPane = new BorderPane()

  override def start(): Unit =
    stage = new JFXApp3.PrimaryStage:
      title = "Hearts - GUI"
      scene = new Scene(1000, 700):
        root = rootBorderPane

    updateUi()

  // === State dispatcher ===
  private def updateUi(): Unit =
    val state = gameController.state.getStateString
    state match
      case "MainScreenState"      => showMainScreen()
      case "RulesScreenState"     => showRulesScreen()
      case "GetPlayerNumberState" => showPlayerNumberScreen()
      case "GetPlayerNamesState"  => showPlayerNamesScreen()
      case "SetMaxScoreState"     => showSetMaxScoreScreen()
      case "GamePlayState"        => showGamePlayScreen()
      case "ShowScoreState"       => showScoreScreen()
      case "GameOverState"        => showGameOverScreen()
      case other =>
        rootBorderPane.center = new Label(s"Unknown state: $other")

  // === helpers ===

  private def simpleHeader(text: String): VBox =
    new VBox:
      spacing = 10
      padding = Insets(20)
      children = Seq(
        new Label("Hearts"):
          style = "-fx-font-size: 28px; -fx-font-weight: bold;",
        new Label(text):
          style = "-fx-font-size: 16px;"
      )

  private def bottomBackButton(label: String, command: String): Unit =
    rootBorderPane.bottom = new HBox:
      spacing = 10
      padding = Insets(10)
      children = Seq(
        new Button(label):
          onAction = _ => gameController.processInput(command)
      )

  // ==== CARD IMAGE MAPPING WITH YOUR ENUMS ====

  // Uses your Rank/Suit enums + Suit.fileName
  // -> "2_of_clubs.png", "jack_of_hearts.png", etc.
  private def cardToImageName(card: Card): String =
    val rankStr = card.rank match
      case Rank.Jack  => "jack"
      case Rank.Queen => "queen"
      case Rank.King  => "king"
      case Rank.Ace   => "ace"
      case other      => other.toString // "2","3",..."10"

    val suitStr = card.suit.fileName    // "hearts","spades","diamonds","clubs"

    s"${rankStr}_of_${suitStr}.png"

  // Loads /cards/<filename> (put PNGs in src/main/resources/cards/)
  private def imageViewForCard(card: Card, indexInHand: Int): ImageView =
    val fileName = cardToImageName(card)
    val stream   = getClass.getResourceAsStream(s"/cards/$fileName")

    val img =
      if stream == null then
        // Fallback: use some generic back.png if specific card is missing
        new Image(getClass.getResourceAsStream("/cards/back.png"))
      else
        new Image(stream)

    new ImageView(img):
      fitWidth = 80
      preserveRatio = true
      smooth = true
      cache = true
      // Click card -> send index into hand as input to controller
      onMouseClicked = _ =>
        gameController.processInput(indexInHand.toString)

  // === Screens ===

  private def showMainScreen(): Unit =
    val newGameButton = new Button("New Game"):
      maxWidth = Double.MaxValue
      onAction = _ => gameController.processInput("n")

    val rulesButton = new Button("Rules"):
      maxWidth = Double.MaxValue
      onAction = _ => gameController.processInput("r")

    val exitButton = new Button("Exit"):
      maxWidth = Double.MaxValue
      onAction = _ => gameController.processInput("e")

    val centerBox = new VBox:
      spacing = 15
      padding = Insets(20)
      children = Seq(
        new Label("Main Menu"):
          style = "-fx-font-size: 22px; -fx-font-weight: bold;",
        newGameButton,
        rulesButton,
        exitButton
      )

    rootBorderPane.top = simpleHeader("Welcome to Hearts")
    rootBorderPane.center = centerBox
    rootBorderPane.bottom = null

  private def showRulesScreen(): Unit =
    val rulesText =
      """Hearts – Rules (short)
        |
        |• 3–4 players, everyone for themselves.
        |• Avoid hearts and the Queen of Spades.
        |• Hearts = 1 point, Q♠ = 13 points.
        |• Game ends when someone reaches max score; lowest score wins.
        |
        |See TUI for full rules.
        |""".stripMargin

    val textArea = new TextArea:
      text = rulesText
      wrapText = true
      editable = false

    rootBorderPane.top = simpleHeader("Rules")
    rootBorderPane.center = textArea
    bottomBackButton("Back to Main Menu", "back") // or "b" if that's what you use

  private def showPlayerNumberScreen(): Unit =
    val infoLabel = new Label("Please input a number of players between 3 and 4:")

    val inputField = new TextField:
      promptText = "3 or 4"

    val confirmButton = new Button("OK"):
      onAction = _ =>
        val value = inputField.text.value.trim
        if value.nonEmpty then
          gameController.processInput(value)

    val centerBox = new VBox:
      spacing = 10
      padding = Insets(20)
      children = Seq(infoLabel, inputField, confirmButton)

    rootBorderPane.top = simpleHeader("Player Count")
    rootBorderPane.center = centerBox
    rootBorderPane.bottom = null

  private def showPlayerNamesScreen(): Unit =
    val infoLabel = new Label(
      s"Please input the name of player ${gameController.game.players.size + 1}:"
    )

    val inputField = new TextField:
      promptText = "Player name"

    val confirmButton = new Button("OK"):
      onAction = _ =>
        val name = inputField.text.value.trim
        if name.nonEmpty then
          gameController.processInput(name)

    val centerBox = new VBox:
      spacing = 10
      padding = Insets(20)
      children = Seq(infoLabel, inputField, confirmButton)

    rootBorderPane.top = simpleHeader("Player Names")
    rootBorderPane.center = centerBox
    rootBorderPane.bottom = null

  private def showSetMaxScoreScreen(): Unit =
    val infoLabel = new Label("Please enter the score required to win (between 1 and 400):")

    val inputField = new TextField:
      promptText = "e.g. 100"

    val confirmButton = new Button("OK"):
      onAction = _ =>
        val value = inputField.text.value.trim
        if value.nonEmpty then
          gameController.processInput(value)

    val centerBox = new VBox:
      spacing = 10
      padding = Insets(20)
      children = Seq(infoLabel, inputField, confirmButton)

    rootBorderPane.top = simpleHeader("Max Score")
    rootBorderPane.center = centerBox
    rootBorderPane.bottom = null

  private def showGamePlayScreen(): Unit =
    // Uses the controller's getHand: List[Card]
    val hand: List[Card] = gameController.getHand

    val currentPlayerLabel = new Label(
      s"${gameController.getCurrentPlayerName}, please select a card to play:"
    ):
      style = "-fx-font-size: 18px; -fx-font-weight: bold;"

    val trickLabel = new Label("Current trick:")
    val trickTextArea = new TextArea:
      text = gameController.completeTrickString
      wrapText = true
      editable = false
      prefRowCount = 4

    val handBox = new HBox:
      spacing = 10
      padding = Insets(10)
      children = hand.zipWithIndex.map { case (card, idx) =>
        imageViewForCard(card, idx)
      }

    val centerBox = new VBox:
      spacing = 15
      padding = Insets(20)
      children = Seq(
        currentPlayerLabel,
        trickLabel,
        trickTextArea,
        new Label("Your hand:"),
        handBox
      )

    rootBorderPane.top = simpleHeader("Gameplay")
    rootBorderPane.center = centerBox
    rootBorderPane.bottom = null

  private def showScoreScreen(): Unit =
    val scoreLines =
      for (rank, name, points) <- gameController.rankPlayers(gameController.getPlayersWithPoints)
      yield s"$rank. $name: $points"

    val textArea = new TextArea:
      text = "Scoreboard:\n" + scoreLines.mkString("\n") + "\n\nPress any button to continue."
      editable = false
      wrapText = true

    val continueButton = new Button("Continue"):
      onAction = _ => gameController.processInput(" ")

    val centerBox = new VBox:
      spacing = 10
      padding = Insets(20)
      children = Seq(textArea, continueButton)

    rootBorderPane.top = simpleHeader("Scoreboard")
    rootBorderPane.center = centerBox
    rootBorderPane.bottom = null

  private def showGameOverScreen(): Unit =
    val scoreLines =
      for (rank, name, points) <- gameController.rankPlayers(gameController.getPlayersWithPoints)
      yield s"$rank. $name: $points"

    val textArea = new TextArea:
      text =
        "GAME OVER\n\nScoreboard:\n" +
          scoreLines.mkString("\n") +
          "\n\nPlease choose:\n" +
          "- n or new for a new Game\n" +
          "- a or again for playing again\n" +
          "- q or quit to go back to Mainmenu\n" +
          "- e or exit to end the program\n"
      editable = false
      wrapText = true

    val btnNew   = new Button("New Game (n)") { onAction = _ => gameController.processInput("n") }
    val btnAgain = new Button("Again (a)")    { onAction = _ => gameController.processInput("a") }
    val btnQuit  = new Button("Quit (q)")     { onAction = _ => gameController.processInput("q") }
    val btnExit  = new Button("Exit (e)")     { onAction = _ => gameController.processInput("e") }

    val buttons = new HBox:
      spacing = 10
      children = Seq(btnNew, btnAgain, btnQuit, btnExit)

    val centerBox = new VBox:
      spacing = 10
      padding = Insets(20)
      children = Seq(textArea, buttons)

    rootBorderPane.top = simpleHeader("Game Over")
    rootBorderPane.center = centerBox
    rootBorderPane.bottom = null

