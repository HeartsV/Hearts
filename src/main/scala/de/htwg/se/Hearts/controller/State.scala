package de.htwg.se.Hearts.controller

import de.htwg.se.Hearts.model.*

trait State(controller: Controller) {
    def processInput(input: String):Boolean
    def getStateString():String
}

class MainScreenState(controller: Controller) extends State(controller: Controller){
    def processInput(input: String): Boolean =
        input.toLowerCase().trim match{
        case "newgame"| "n" =>
            controller.changeState(GetPlayerNumberState(controller))
            true
        case "rules"|"r" =>
            controller.changeState(RulesScreenState(controller))
            true
        case "exit"|"e" =>
            //Programm beenden, eventuell nehmen wir das raus?!
            true
        case _ =>
            false
        }

    def getStateString(): String ="MainScreenState"
}

class RulesScreenState(controller: Controller) extends State(controller: Controller){
    def processInput(input: String): Boolean =
        input.trim match {
            case "back" | "b" =>
                controller.changeState(MainScreenState(controller))
                true
            case _ =>
                false
        }

    def getStateString(): String = "RulesScreenState"
}

class GetPlayerNumberState(controller: Controller) extends State(controller: Controller) {
    def processInput(input: String): Boolean =
        if(input.toIntOption.exists(intInput => intInput >= 3 && intInput <= 4))
            controller.setPlayerNumber(input.toInt)
            controller.changeState(GetPlayerNamesState(controller))
            true
        else
            false

    def getStateString(): String = "GetPlayerNumberState"
}

class GetPlayerNamesState(controller: Controller) extends State(controller: Controller) {
    def processInput(input: String): Boolean =
        if(!input.trim.equals(""))
            controller.getGame().addPlayer(Player(input))
        else
            controller.getGame().addPlayer(Player(s"P${controller.getGame().players.size + 1}"))
        if(controller.getGame().players.size == controller.getGame().playerNumber.get)
            controller.dealCards(controller.createDeck())
            controller.updateCurrentPlayer()
            controller.changeState(SetMaxScoreState(controller))
        true

    def getStateString(): String = "GetPlayerNamesState"
}

class SetMaxScoreState(controller: Controller) extends State(controller: Controller) {
    def processInput(input: String): Boolean =
        if(input.toIntOption.exists(intInput => intInput >= 1 ))
            controller.getGame().maxScore = Some(input.toInt)
            controller.changeState(GamePlayState(controller))
            true
        else if(input.trim.equals(""))
            controller.getGame().maxScore = Some(100)
            controller.changeState(GamePlayState(controller))
            true
        else
            false

    def getStateString(): String ="SetMaxScoreState"
}

class GamePlayState(controller: Controller) extends State(controller: Controller) {
    def processInput(input: String):Boolean =
        if(input.toIntOption.exists(index => controller.playCard(index)))
            if(controller.getGame().firstCard == true) controller.getGame().firstCard = false
            controller.updateCurrentWinner()
            controller.updateCurrentPlayer()
            if(controller.getGame().currentPlayer.get.hand.size == 0 && !controller.checkGameOver())
                controller.changeState(ShowScoreState(controller))
            else if (controller.getGame().currentPlayer.get.hand.size == 0 && controller.checkGameOver())
                controller.changeState(GameOverState(controller))
            true
        else
            false

    def getStateString(): String = "GamePlayState"
}

class ShowScoreState(controller: Controller) extends State(controller: Controller) {
    def processInput(input: String): Boolean = ??? //reset cards and go to gameplayState

    def getStateString(): String = "ShowScoreState"
}


class GameOverState(controller: Controller) extends State(controller: Controller) {
    def processInput(input: String): Boolean =
        input.toLowerCase().trim match {
            case "new"|"n" =>
                //Zu den Einstellungen, also wird ein spiel neu gestartet, aber die settings müssen neu angegeben werden
                controller.changeState(GetPlayerNumberState(controller))
                true
            case "again"|"a" =>
                //neues Spiel wird gestartet aber die Settings und Playernamen bleiben gleich
                controller.changeState(GamePlayState(controller))
                true
            case "exit"|"e" =>
                //Wir beenden das programm, eventuell nehmen wir das raus?!
                true
            case "quit"|"q" =>
                //Wir gehen zurück zum MainScreen
                controller.changeState(MainScreenState(controller))
                true
            case _ =>
                false
        }

    def getStateString(): String = "GameOverState"
}