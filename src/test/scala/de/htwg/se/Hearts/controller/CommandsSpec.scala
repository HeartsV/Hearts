package de.htwg.se.Hearts.controller

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.Hearts.controller.controllerComponent.controllerBase._
import de.htwg.se.Hearts.model.gameComponent.gameBase.Game

class CommandsSpec extends AnyWordSpec with Matchers {
	val game = Game()
	val gameCo = Controller(game)

	"A redoCommand" should {
		"" in {

		}
	}

	"A undoCommand" should {
		"" in {

		}
	}

	"A SetPlayerNumberCommand" should {
		"set the number of players" in {
		val command = SetPlayerNumberCommand(index = Some (3))
		val commandf = SetPlayerNumberCommand(index = Some (5))
		command.setup(gameCo)
		command.storeBackup
		gameCo.processInput(commandf)
		gameCo.game.getPlayerNumber should be (None)
		gameCo.processInput(command)
		gameCo.game.getPlayerNumber should be (Some(3))
		gameCo.undo
		gameCo.game.getPlayerNumber should be (None)
		}
	}

	"A AddPlayerCommand" should {

	}

	"A SetMaxScoreCommand" should {
		"set the max score" in {
		val command = SetMaxScoreCommand(index = Some (3))
		val commandf = SetMaxScoreCommand(index = Some (450))

		command.setup(gameCo)
		command.storeBackup
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
			gameCo.processInput(SetSortingRankCommand())

		}
	}

	"A SetSortingSuitCommand" should {
		"" in {
			gameCo.processInput(SetSortingSuitCommand())

		}
	}

	"A NewCommand" should {
		"" in {
			gameCo.processInput(NewCommand())

		}
	}

	"A AgainCommand" should {
		"" in {
			gameCo.processInput(AgainCommand())

		}
	}

	"A QuitCommand" should {
		"" in {
			gameCo.processInput(QuitCommand())

		}
	}

	"A ExitCommand" should {
		"" in {
			gameCo.processInput(ExitCommand())

		}
	}

	"A RulesCommand" should {
		"" in {
			gameCo.processInput(RulesCommand())

			}
	}

	"A BackCommand" should {
		"" in {
			gameCo.processInput(BackCommand())

		}
	}

	"A ContinueCommand" should {
		"" in {
			gameCo.processInput(ContinueCommand())

		}
	}
	"A PlayCardCommand" should {
		"" in {

		}
	}
}
