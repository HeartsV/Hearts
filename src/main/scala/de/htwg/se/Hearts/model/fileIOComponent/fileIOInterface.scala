package de.htwg.se.Hearts.model.fileIOComponent

import de.htwg.se.Hearts.model.gameComponent.GameInterface
import de.htwg.se.Hearts.controller.controllerComponent.controllerBase.State

trait fileIOInterface {
	def load: (GameInterface, State)
	def save(game: GameInterface, state: State): Unit
}
