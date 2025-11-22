package de.htwg.se.Hearts.controller

trait  State(controller: Controller) {
    def processInput(input:String):Boolean


}


class GamePlayState(controller: Controller) extends State(controller:Controller) {
    def processInput(input:String):Boolean = {
        if(input.toIntOption.exists(index => controller.playCard(index)))
            if(controller.getGame().firstCard == true) controller.getGame().firstCard = false
            controller.updateCurrentWinner()
            controller.updateCurrentPlayer()
            controller.notifyObservers
            true
        else
            controller.notifyObservers
            false

    }
}