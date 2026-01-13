package de.htwg.se.Hearts.controller

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.Hearts.controller.controllerComponent.controllerBase._
import de.htwg.se.Hearts.model.gameComponent.gameBase.Game

class CommandsSpec extends AnyWordSpec with Matchers {
	val game = Game()
	val gameCo = Controller(game)

	"A redoCommand" should {
		"" in {}
	}

	"A undoCommand" should {

	}

	"A SetPlayerNumberCommand" should {
		"set the number of players"in {
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

	}

	"A SetSortingRankCommand" should {

	}

	"A SetSortingSuitCommand" should {

	}

	"A NewCommand" should {

	}

	"A AgainCommand" should {

	}

	"A QuitCommand" should {

	}

	"A ExitCommand" should {

	}

	"A RulesCommand" should {

	}

	"A BackCommand" should {

	}

	"A ContinueCommand" should {

	}
	"A PlayCardCommand" should {

	}
}
