package de.htwg.se.Hearts.controller

import org.scalatest.BeforeAndAfterEach
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.Hearts.controller.controllerComponent.controllerBase._
import de.htwg.se.Hearts.model.gameComponent.gameBase._
import de.htwg.se.Hearts.model.gameComponent._
import java.io.File

class CommandsSpec extends AnyWordSpec with Matchers with BeforeAndAfterEach{
	private val saveFile = new File("hearts.json")
	override def beforeEach(): Unit =
		if saveFile.exists() then saveFile.delete()

	val card1 = Card(Rank.Two,Suit.Diamonds)
	val card2 = Card(Rank.Five,Suit.Diamonds)
	val card3 = Card(Rank.Eight,Suit.Hearts)
	val p1 = Player("Alice", List(card1,card2),List(card3),5)
	val p2 = Player("Bob", List(card1,card2),List(card3),5)
	val p3 = Player("Charlie", List(card1,card2),List(card3),5)
	val game = Game()
	val gameCo = Controller(game)
	val gameSetup = Game(Some(3),true,true,false,Vector(p1,p2,p3),Some(100),Some(0),List(),None,None,Left("No Card"))
	val gameCoSetup = Controller(gameSetup)
	val gameOver = Game(Some(3),true,true,false,Vector(Player("Alice",List(card1)),Player("Bob"),Player("Charlie")),Some(100),Some(0),List(),None,None,Left("No Card"))
	val gameOverCo = Controller(gameOver)
	val done = Controller(gameOver.copy(players = Vector(Player("Alice",List(card1),List(),120),Player("Bob"),Player("Charlie"))))

	"A undoCommand" should {
		"" in {
			val command = SetPlayerNumberCommand(index = Some (3))
			val undoCommand = UndoCommand()
			gameCo.processInput(command)
			gameCo.processInput(undoCommand)
			gameCo.game.getPlayerNumber should be (None)
			undoCommand.undoStep
			gameCo.game.getPlayerNumber should be (Some(3))
			command.undoStep
		}
	}

	"A redoCommand" should {
		"" in {
			val command = SetPlayerNumberCommand(index = Some (3))
			val undoCommand = UndoCommand()
			val redoCommand = RedoCommand()
			gameCo.processInput(command)
			gameCo.processInput(undoCommand)
			gameCo.processInput(redoCommand)
			gameCo.game.getPlayerNumber should be (Some(3))
			redoCommand.undoStep
			gameCo.game.getPlayerNumber should be (None)

		}
	}
	"A PlayCardCommand" should {
		"Play card and be undone" in {
			gameCoSetup.processInput(PlayCardCommand(index = Some(1)))
			gameCoSetup.game.getTrickCards should be (List(card1))
			gameCoSetup.processInput(PlayCardCommand(index = Some(1)))
			gameCoSetup.processInput(PlayCardCommand(index = Some(1)))
			gameCoSetup.processInput(PlayCardCommand(index = None))
			gameCoSetup.game.getErrorOrLastCardPlayed should be (Left("Index was not a number!\n"))
			gameCoSetup.undo
			gameCoSetup.undo
			gameCoSetup.game.getTrickCards should be (List(card1,card1))
			gameCoSetup.undo
			gameCoSetup.undo
		}
		"Play the last card and go to ShowScores" in {
			gameOverCo.processInput(PlayCardCommand(index = Some(1)))
			gameOverCo.state shouldBe a [ShowScoreState]
			gameOverCo.undo
			gameOverCo.state shouldBe a [MainScreenState]
		}
		"Play the last card and go to GameOver" in {
			done.processInput(PlayCardCommand(index = Some(1)))
			done.state shouldBe a [GameOverState]
			done.undo
			gameOverCo.state shouldBe a [MainScreenState]
		}
	}

	"A SetPlayerNumberCommand" should {
		"set the number of players" in {
			val command = SetPlayerNumberCommand(index = Some (3))
			val commandf = SetPlayerNumberCommand(index = Some (5))
			gameCo.processInput(commandf)
			gameCo.game.getPlayerNumber should be (None)
			gameCo.processInput(command)
			gameCo.game.getPlayerNumber should be (Some(3))
			gameCo.undo
			gameCo.game.getPlayerNumber should be (None)
		}
	}

	"A AddPlayerCommand" should {
		"Add a player" in {
		gameCo.processInput(SetPlayerNumberCommand(index = Some(3)))
		val command = AddPlayerCommand(name ="Alice")
		val commandNoText = AddPlayerCommand(name = "")
		gameCo.processInput(command)
		gameCo.game.getPlayers(0).getName should be ("Alice")
		gameCo.processInput(commandNoText)
		gameCo.game.getPlayers(1).getName should be ("P2")
		gameCo.processInput(command)
		gameCo.state shouldBe a [SetMaxScoreState]
		gameCo.undo
		gameCo.undo
		gameCo.undo
		gameCo.undo
		}
	}

	"A SetMaxScoreCommand" should {
		"set the max score" in {
		val command = SetMaxScoreCommand(index = Some (3))
		val commandf = SetMaxScoreCommand(index = Some (450))

		gameCo.processInput(commandf)
		gameCo.game.getMaxScore should be (Some(100))
		gameCo.undo
		gameCo.game.getMaxScore should be (None)
		gameCo.processInput(command)
		gameCo.game.getMaxScore should be (Some(3))
		gameCo.undo
		gameCo.game.getMaxScore should be (None)
		}
	}

	"A SetSortingRankCommand" should {
		"set sorting to rank" in {
			val command = SetSortingRankCommand()
			gameCo.processInput(SetSortingSuitCommand())
			gameCo.processInput(command)
			gameCo.sortingStrategy shouldBe a [SortByRankStrategy]
			command.undoStep
		}
	}

	"A SetSortingSuitCommand" should {
		"" in {
			val command = SetSortingSuitCommand()
			gameCo.processInput(command)
			gameCo.sortingStrategy shouldBe a [SortBySuitStrategy]
			command.undoStep
		}
	}

	"A NewCommand" should {
		"" in {
			val command = NewCommand()
			gameCo.processInput(command)
			gameCo.state shouldBe a [GetPlayerNumberState]
			gameCo.undo
			gameCo.state shouldBe a [MainScreenState]

		}
	}

	"A AgainCommand" should {
		"" in {
			gameCoSetup.processInput(AgainCommand())
			gameCoSetup.state shouldBe a [GamePlayState]
			gameCoSetup.game.getPlayers(0).getHand.size should be (17)
			gameCoSetup.undo
			gameCoSetup.state shouldBe a [MainScreenState]
			gameCoSetup.game.getPlayers(0).getHand.size should be (2)
		}
	}

	"A QuitCommand" should {
		"" in {
			val command = NewCommand()
			val command2 = QuitCommand()
			gameCo.processInput(command)
			gameCo.processInput(command2)
			gameCo.state shouldBe a [MainScreenState]
			gameCo.undo
			gameCo.state shouldBe a [GetPlayerNumberState]
			gameCo.undo
		}
	}

	"A ExitCommand" should {
		"" in {
			gameCo.processInput(ExitCommand())
			gameCo.game.getKeepProcessRunning should be (false)
			gameCo.undo
			gameCo.game.getKeepProcessRunning should be (true)

		}
	}

	"A RulesCommand" should {
		"" in {
			val command = RulesCommand()
			gameCo.processInput(command)
			gameCo.state shouldBe a [RulesScreenState]
			command.undoStep
			gameCo.state shouldBe a [MainScreenState]

		}
	}

	"A BackCommand" should {
		"" in {
			val command = BackCommand()
			gameCo.processInput(command)
			gameCo.state shouldBe a [MainScreenState]
			gameCoSetup.processInput(command)
			gameCoSetup.state shouldBe a [GamePlayState]
			command.undoStep
			gameCo.state shouldBe a [MainScreenState]
		}
	}

	"A ContinueCommand" should {
		"" in {
			val command = ContinueCommand()
			gameCoSetup.processInput(command)
			gameCoSetup.state shouldBe a [GamePlayState]
			gameCoSetup.game.getPlayers(0).getHand.size should be (17)
			gameCoSetup.undo
			gameCoSetup.state shouldBe a [MainScreenState]
			gameCoSetup.game.getPlayers(0).getHand.size should be (2)

		}
	}
	"A SaveCommand" should {
		val gameLS = Game()
		val gameCoLS = Controller(gameLS)
		import de.htwg.se.Hearts.model.fileIOComponent.fileIOJSONImpl._
		def deleteFile(saveFile: File): Unit =
			if (saveFile.exists()) then
				saveFile.delete()
		"set error message to Game was saved! if successfully saved" in {
			val cmd = SaveCommand()
			cmd.setup(gameCoLS)
			cmd.storeBackup
			cmd.execute
		}
	}

	"A LoadCommand" should {
		gameCo.game = game
		import de.htwg.se.Hearts.model.fileIOComponent.fileIOJSONImpl._
		def deleteFile(saveFile: File): Unit =
			if (saveFile.exists()) then
				saveFile.delete()

		"set error message to Game loaded! if successfully saved" in {
			val saveFile = new File("hearts.json")
			val saveFile2 = new File("hearts.xml")
			while FileIO().saveExists do
				deleteFile(saveFile)
				deleteFile(saveFile2)
			val cmd = LoadCommand()
			cmd.setup(gameCo)
			cmd.storeBackup
			cmd.execute

		}

		"set error message if no save exists" in {
			val saveFile = new File("hearts.json")
			val saveFile2 = new File("hearts.xml")
			while FileIO().saveExists do
				deleteFile(saveFile)
				deleteFile(saveFile2)
			val cmd = LoadCommand()
			cmd.setup(gameCo)
			cmd.storeBackup
			cmd.execute

			gameCo.getGame.getErrorOrLastCardPlayed match
				case Left(msg) => msg.trim shouldBe "No game saved!"
				case Right(_)   => fail("Expected Left(message)")

			gameCo.state = MainScreenState(gameCo)
			gameCo.processInput(cmd)
			gameCo.state shouldBe a [MainScreenState]
			cmd.undoStep
			gameCo.state shouldBe a [MainScreenState]
		}
	}
}
