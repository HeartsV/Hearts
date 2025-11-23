package de.htwg.se.Hearts.controller

import de.htwg.se.Hearts.model.*

trait State(controller: Controller) {
    def processInput(input: String):Boolean
    def getStateString():String
}

class TitleScreenState(controller: Controller) extends State(controller: Controller){
    def processInput(input: String): Boolean =
        
        true

    def getStateString(): String = "TitleScreenState"
}
class MainScreenState(controller: Controller) extends State(controller: Controller){
    def processInput(input: String): Boolean =
        input.toLowerCase().trim match{
        case "newgame"| "n" =>
            //ein spiel wird gestartet, bzw. man kommt in die settings für ein neues game
            controller.changeState(GetPlayerNumberState(controller))
            true
        case "rules"|"r" =>
            //Regeln, Punkte vergabe, ...
            true
        case "exit"|"e" =>
            //Programm beenden, eventuell nehmen wir das raus?!
            true
        case _ =>
            false
        }

    def getStateString(): String ="TitleScreenState"
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
        if(!input.equals(""))
            controller.getGame().addPlayer(Player(input))
            true
        else if(input.equals(""))
            controller.getGame().addPlayer(Player(s"P${controller.getGame().players.size + 1}"))
            true
        else
            false

    def getStateString(): String = "GetPlayerNamesState"
}

class SetMaxScoreState(controller: Controller) extends State(controller: Controller) {
    def processInput(input: String): Boolean =
        if(input.toIntOption.exists(intInput => intInput >= 1 ))
            controller.getGame().maxScore = Some(input.toInt)
            true
        else if(input.trim.equals(""))
            controller.getGame().maxScore = Some(100)
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
            true
        else
            false
    
    def getStateString(): String = "GamePlayState"
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