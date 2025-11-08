package de.htwg.se.Hearts.controller

import de.htwg.se.Hearts.model.Game
import de.htwg.se.Hearts.aview.*
import de.htwg.se.Hearts.model.Game.updateCurrentPlayer

class Controller extends Observable() {
    val tui = Tui()

    def rungame() = {
        updateCurrentPlayer()
        while(!Game.gameOver){
            notifyObservers
            if(tui.parseUserInput().exists(index => Game.currentPlayer.get.playCard(index)))
                Game.trick.updateCurrentWinner()
                updateCurrentPlayer()
                Game.trick.clearTrick()
        }
    }
}
