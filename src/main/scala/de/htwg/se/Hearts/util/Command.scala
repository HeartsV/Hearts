package de.htwg.se.Hearts.util
import de.htwg.se.Hearts.controller.controllerComponent.ControllerInterface
import de.htwg.se.Hearts.model.gameComponent.GameInterface


trait Command:
    def undoStep: Unit
    def doStep: Unit
    def redoStep: Unit




