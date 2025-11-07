package de.htwg.se.Hearts.controller

import de.htwg.se.Hearts.model.Game
import de.htwg.se.Hearts.view.*

class GameController() {
    Tui()

    def rungame() = {
        while(!Game.gameOver == false){
            print()
        }
    }

}
