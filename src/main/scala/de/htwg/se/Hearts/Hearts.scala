package de.htwg.se.Hearts

import de.htwg.se.Hearts.model.gameComponent.gameBase.*
import de.htwg.se.Hearts.controller.controllerComponent.controllerBase.*
import de.htwg.se.Hearts.controller._
import de.htwg.se.Hearts.util._
import de.htwg.se.Hearts.aview._
import com.google.inject.{Guice, Injector}
import de.htwg.se.Hearts.controller.controllerComponent.ControllerInterface

object Main{
    def main(args: Array[String]): Unit = {
        val game = Game()
        val gameCo: ControllerInterface = Controller(game)
        val gameTui = Tui(gameCo)
        gameCo.add(gameTui)
        gameTui.runGame
    }
}