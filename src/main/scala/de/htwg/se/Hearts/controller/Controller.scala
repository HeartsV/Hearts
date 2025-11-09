package de.htwg.se.Hearts.controller

import de.htwg.se.Hearts.model.Game
import de.htwg.se.Hearts.aview.*


class Controller extends Observable() {

    def processInput(input: String): Unit  = {
        if(Game.firstCard == true)
            Game.updateCurrentPlayer()
        if(input.toIntOption.exists(index => Game.currentPlayer.get.playCard(index)))
            Game.trick.updateCurrentWinner()
            Game.updateCurrentPlayer()
            Game.trick.clearTrick()
        notifyObservers
    }

}
