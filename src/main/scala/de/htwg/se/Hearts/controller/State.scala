package de.htwg.se.Hearts.controller

trait State(controller: Controller) {
    def processInput(input:String):Boolean
}

class GetPlayerNumberState(controller:Controller) extends State(controller:Controller) {
    def processInput(input: String): Boolean =
        if(input.toIntOption.exists(intInput => 3 >= intInput && intInput <= 4))
            controller.SetPlayerNumber(input.toInt)
            controller.changeState(GetPlayerNamesState(controller))
            true
        else
            false

}

class GetPlayerNamesState(controller:Controller) extends State(controller:Controller) {
    def processInput(input: String): Boolean = ???
}

class SetMaxScoreState(controller:Controller) extends State(controller:Controller) {
    def processInput(input: String): Boolean = ???
}

class GamePlayState(controller: Controller) extends State(controller:Controller) {
    def processInput(input:String):Boolean =
        if(input.toIntOption.exists(index => controller.playCard(index)))
            if(controller.getGame().firstCard == true) controller.getGame().firstCard = false
            controller.updateCurrentWinner()
            controller.updateCurrentPlayer()
            true
        else
            false
}

class GameOverState(controller:Controller) extends State(controller:Controller) {
    def processInput(input: String): Boolean = ???
}