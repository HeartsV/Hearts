package de.htwg.se.Hearts.model.fileIOComponent.fileIOJSONImpl

import de.htwg.se.Hearts.model.fileIOComponent.fileIOInterface
import de.htwg.se.Hearts.model.gameComponent.GameInterface
import de.htwg.se.Hearts.controller.controllerComponent.controllerBase.State
import de.htwg.se.Hearts.controller.controllerComponent.controllerBase.Controller

class fileIO extends fileIOInterface:
	def load(controller: Controller): GameInterface = controller.game
	def save(game: GameInterface, state: State): Unit = ???
