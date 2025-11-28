package de.htwg.se.Hearts.controller

import de.htwg.se.Hearts.model.*

trait State(controller: Controller) {
    def processInput(input: String): Game
    def getStateString(): String
}

class MainScreenState(controller: Controller) extends State(controller: Controller){
    def processInput(input: String): Game =
        input.toLowerCase().trim match{
        case "new"| "n" =>
            controller.changeState(GetPlayerNumberState(controller))
            controller.game
        case "rules"|"r" =>
            controller.changeState(RulesScreenState(controller))
            controller.game
        case "exit"|"e" =>
            controller.setkeepProcessRunning(false)
            controller.game
        case _ =>
            controller.game
        }

    def getStateString(): String ="MainScreenState"
}

class RulesScreenState(controller: Controller) extends State(controller: Controller){
    def processInput(input: String): Game =
        input.trim match {
            case "back" | "b" =>
                controller.changeState(MainScreenState(controller))
                controller.game
            case _ =>
                controller.game
        }

    def getStateString(): String = "RulesScreenState"
}

class GetPlayerNumberState(controller: Controller) extends State(controller: Controller) {
    def processInput(input: String): Game =
        if(input.toIntOption.exists(intInput => intInput >= 3 && intInput <= 4))
            controller.changeState(GetPlayerNamesState(controller))
            controller.setPlayerNumber(input.toInt)
        else
            controller.game

    def getStateString(): String = "GetPlayerNumberState"
}

class GetPlayerNamesState(controller: Controller) extends State(controller: Controller) {
    def processInput(input: String): Game =
        if(!input.trim.equals(""))
            controller.game.addPlayer(Player(input))
        else
            controller.game.addPlayer(Player(s"P${controller.game.players.size + 1}"))
        if(controller.game.players.size == controller.game.playerNumber.get)
            controller.game = controller.dealCards(controller.shuffledeck(controller.createDeck()))
            controller.changeState(SetMaxScoreState(controller))
            controller.updateCurrentPlayer()
        else
            controller.game
    def getStateString(): String = "GetPlayerNamesState"
}

class SetMaxScoreState(controller: Controller) extends State(controller: Controller) {
    def processInput(input: String): Game =
        if(input.toIntOption.exists(intInput => intInput >= 1 ))
            controller.changeState(GamePlayState(controller))
            controller.game.setMaxScore(input.toInt)
        else if(input.trim.equals(""))
            controller.changeState(GamePlayState(controller))
            controller.game.setMaxScore(100)
        else
            controller.game

    def getStateString(): String ="SetMaxScoreState"
}

class GamePlayState(controller: Controller) extends State(controller: Controller) {
    def processInput(input: String): Game =
        controller.executeStrategy()
        input.trim.toLowerCase() match {
            case "suit" | "s" => controller.setStrategy(SortBySuitStrategy()); controller.game
            case "rank" | "r" => controller.setStrategy(SortByRankStrategy()); controller.game
            case _ =>
                if(input.toIntOption.exists(index => controller.cardAllowed(index)))
                    if(controller.game.firstCard == true) controller.game = controller.game.setFirstCard(false)
                    controller.game = controller.updateCurrentWinner(controller.game.getCurrentPlayer.get, controller.game.getCurrentPlayer.get.hand(input.toInt-1))
                    controller.playCard(input.toInt-1)
                    controller.updateCurrentPlayer()
                    if(controller.game.getCurrentPlayer.get.hand.size == 0 && !controller.checkGameOver())
                        controller.addPointsToPlayers(controller.rawPointsPerPlayer())
                        controller.changeState(ShowScoreState(controller))
                    else if (controller.game.getCurrentPlayer.get.hand.size == 0 && controller.checkGameOver())
                        controller.addPointsToPlayers(controller.rawPointsPerPlayer())
                        controller.changeState(GameOverState(controller))
                    true
                else
                    controller.game
        }


    def getStateString(): String = "GamePlayState"
}

class ShowScoreState(controller: Controller) extends State(controller: Controller) {
    def processInput(input: String): Game =
        controller.dealCards(controller.shuffledeck(controller.createDeck()))
        controller.game.firstCard = true
        controller.game.startWithHearts = false
        controller.updateCurrentPlayer()
        controller.changeState(GamePlayState(controller))
        true

    def getStateString(): String = "ShowScoreState"
}


class GameOverState(controller: Controller) extends State(controller: Controller) {
    def processInput(input: String): Game =
        input.toLowerCase().trim match {
            case "new"|"n"|"quit"|"q" =>
                controller.game.firstCard = true
                controller.game.startWithHearts = false
                controller.game.playerNumber = None
                controller.game.players.clear()
                controller.game.currentPlayer = None
                controller.game.maxScore = None
                input.toLowerCase().trim match {
                    case "new"|"n" =>
                        controller.changeState(GetPlayerNumberState(controller))
                    case "quit"|"q" =>
                        controller.changeState(MainScreenState(controller))
                }
                true
            case "again"|"a" =>
                controller.dealCards(controller.shuffledeck(controller.createDeck()))
                controller.game.firstCard = true
                controller.game.startWithHearts = false
                controller.updateCurrentPlayer()
                for(p <- controller.game.players) p.points = 0
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