package de.htwg.se.Hearts.controller

import de.htwg.se.Hearts.model.Game
import de.htwg.se.Hearts.view.*
import de.htwg.se.Hearts.view.Tui.getGameplayStateString
import de.htwg.se.Hearts.model.Game.updateCurrentPlayer

class GameController() {


    def rungame() = {
        updateCurrentPlayer()
        while(!Game.gameOver){
            print(getGameplayStateString())
            if(Tui.parseUserInput().exists(index => Game.currentPlayer.get.playCard(index)))
                Game.trick.updateCurrentWinner()
                updateCurrentPlayer()
                Game.trick.clearTrick()
        }
    }

}
