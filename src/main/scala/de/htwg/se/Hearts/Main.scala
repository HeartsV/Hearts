package de.htwg.se.Hearts

import de.htwg.se.Hearts.model.gameComponent.gameBase.*
import de.htwg.se.Hearts.controller.controllerComponent.controllerBase.*
import de.htwg.se.Hearts.controller._
import de.htwg.se.Hearts.util._
import de.htwg.se.Hearts.aview._
object Main{
    def main(args: Array[String]): Unit = {
        val game = Game()
        val gameCo = Controller(game)
        val gameTui = Tui(gameCo)
        gameCo.add(gameTui)

        val guiThread = new Thread(() =>
            val gameGui = Gui(gameCo)
            gameCo.add(gameGui)
            gameGui.main(Array.empty[String])   // start ScalaFX app
            )
            guiThread.setDaemon(true)
            guiThread.start()

        gameTui.runGame
    }
}