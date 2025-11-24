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
            controller.setkeepProcessRunning(false)
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
                controller.addPointsToPlayers(controller.rawPointsPerPlayer())
                controller.changeState(ShowScoreState(controller))
            else if (controller.getGame().currentPlayer.get.hand.size == 0 && controller.checkGameOver())
                controller.addPointsToPlayers(controller.rawPointsPerPlayer())
                controller.changeState(GameOverState(controller))
            true
        else
            false

    def getStateString(): String = "GamePlayState"
}

class ShowScoreState(controller: Controller) extends State(controller: Controller) {
    def processInput(input: String): Boolean = 
        controller.dealCards(controller.shuffledeck(controller.createDeck()))
        controller.getGame().firstCard = true
        controller.getGame().startWithHearts = false
        controller.updateCurrentPlayer()
        controller.changeState(GamePlayState(controller))
        true

    def getStateString(): String = "ShowScoreState"
}


class GameOverState(controller: Controller) extends State(controller: Controller) {
    def processInput(input: String): Boolean =
        input.toLowerCase().trim match {
            case "new"|"n"|"quit"|"q" =>
                controller.getGame().firstCard = true
                controller.getGame().startWithHearts = false
                controller.getGame().playerNumber = None
                controller.getGame().players.clear()
                controller.getGame().currentPlayer = None
                controller.getGame().maxScore = None
                input.toLowerCase().trim match {
                    case "new"|"n" => 
                        controller.changeState(GetPlayerNumberState(controller))
                    case "quit"|"q" =>
                        controller.changeState(MainScreenState(controller))
                }
                true
            case "again"|"a" =>
                controller.dealCards(controller.shuffledeck(controller.createDeck()))
                controller.getGame().firstCard = true
                controller.getGame().startWithHearts = false
                controller.updateCurrentPlayer()
                for(p <- controller.getGame().players) p.points = 0
                controller.changeState(GamePlayState(controller))
                true
            case "exit"|"e" =>
                controller.setkeepProcessRunning(false)
                true
            case _ =>
                false
        }

    def getStateString(): String = "GameOverState"
}