package de.htwg.se.Hearts.model.fileIOComponent

import de.htwg.se.Hearts.model.gameComponent.GameInterface
import de.htwg.se.Hearts.controller.controllerComponent.controllerBase.State
import de.htwg.se.Hearts.controller.controllerComponent.ControllerInterface
import de.htwg.se.Hearts.controller.controllerComponent.controllerBase.Controller

trait fileIOInterface {
	def load(controller: Controller): GameInterface
	def save(game: GameInterface, state: State): Unit
}
