package de.htwg.se.Hearts.model.fileIOComponent.fileIOJSONImpl

import de.htwg.se.Hearts.model.fileIOComponent.fileIOInterface
import de.htwg.se.Hearts.model.gameComponent.GameInterface
import de.htwg.se.Hearts.controller.controllerComponent.controllerBase.State

class fileIO extends fileIOInterface:
	def load: (GameInterface, State) = ???
	def save(game: GameInterface, state: State): Unit = ???
