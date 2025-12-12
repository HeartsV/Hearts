package de.htwg.se.Hearts
import de.htwg.se.Hearts.model._
import de.htwg.se.Hearts.controller._
import de.htwg.se.Hearts.aview._
object Main:
  def main(args: Array[String]): Unit =
    val game = Game()
    val gameCo = Controller(game)
    val gameTui = Tui(gameCo)
    gameCo.add(gameTui)
    gameTui.runGame