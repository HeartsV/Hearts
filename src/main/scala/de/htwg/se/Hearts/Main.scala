package de.htwg.se.Hearts
import de.htwg.se.Hearts.model.*
import de.htwg.se.Hearts.controller.*
import de.htwg.se.Hearts.aview.*
object Main {
  def main(args: Array[String]): Unit = {
    val game = Game()
    val gameCo = Controller(game)
    val gameTui = Tui(gameCo)
    gameCo.add(gameTui)
    gameTui.runGame()
  }
}