package de.htwg.se.Hearts.util
import de.htwg.se.Hearts.controller.controllerComponent.ControllerInterface
import de.htwg.se.Hearts.controller.controllerComponent.controllerBase.{Controller,State}
import de.htwg.se.Hearts.model.gameComponent.GameInterface
import com.google.inject.Injector
import com.google.inject.Guice
import de.htwg.se.Hearts.HeartsModule


trait Command:
    val injector: Injector = Guice.createInjector(HeartsModule())
    def setup(gameController:Controller):Unit
    def storeBackup: Unit
    def undoStep: Unit
    def execute: Boolean