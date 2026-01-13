package de.htwg.se.Hearts.util
import de.htwg.se.Hearts.controller.controllerComponent.ControllerInterface
import de.htwg.se.Hearts.controller.controllerComponent.controllerBase.{Controller,State}
import de.htwg.se.Hearts.model.gameComponent.GameInterface


trait Command:
    def setup(gameController:Controller):Unit
    def storeBackup: Unit
    def undoStep: Unit
    def execute: Boolean