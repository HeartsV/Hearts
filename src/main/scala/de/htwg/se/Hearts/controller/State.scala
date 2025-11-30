package de.htwg.se.Hearts.controller

import de.htwg.se.Hearts.model.*

trait State(controller: Controller):
    def processInput(input: String): Game
    def getStateString: String

class MainScreenState(controller: Controller) extends State(controller: Controller):
    def processInput(input: String): Game =
        input.toLowerCase.trim match{
        case "new"| "n" =>
            controller.changeState(GetPlayerNumberState(controller))
            controller.game
        case "rules"|"r" =>
            controller.changeState(RulesScreenState(controller))
            controller.game
        case "exit"|"e" =>
            controller.setKeepProcessRunning(false)
            controller.game
        case _ =>
            controller.game
        }

    def getStateString: String ="MainScreenState"

class RulesScreenState(controller: Controller) extends State(controller: Controller):
    def processInput(input: String): Game =
        input.trim match {
            case "back" | "b" =>
                if(controller.game.players.equals(Vector.empty)) controller.changeState(MainScreenState(controller))
                else controller.changeState(GamePlayState(controller))
                controller.game
            case _ =>
                controller.game
        }

    def getStateString: String = "RulesScreenState"

class GetPlayerNumberState(controller: Controller) extends State(controller: Controller):
    def processInput(input: String): Game =
        if(input.toIntOption.exists(intInput => intInput >= 3 && intInput <= 4))
            controller.changeState(GetPlayerNamesState(controller))
            controller.setPlayerNumber(input.toInt)
        else
            controller.game

    def getStateString: String = "GetPlayerNumberState"

class GetPlayerNamesState(controller: Controller) extends State(controller: Controller):
    def processInput(input: String): Game =
        if(!input.trim.equals(""))
            controller.game = controller.game.addPlayer(Player(input))
        else
            controller.game =controller.game.addPlayer(Player(s"P${controller.game.players.size + 1}"))
        if(controller.game.players.size == controller.game.playerNumber.get)
            controller.game = controller.dealCards(controller.shuffledeck(controller.createDeck))
            controller.changeState(SetMaxScoreState(controller))
            controller.game = controller.updateCurrentPlayer
        controller.game

    def getStateString: String = "GetPlayerNamesState"

class SetMaxScoreState(controller: Controller) extends State(controller: Controller):
    def processInput(input: String): Game =
        if(input.toIntOption.exists(intInput => intInput >= 1 && intInput <= 400))
            controller.changeState(GamePlayState(controller))
            controller.game.setMaxScore(input.toInt)
        else if(input.trim.equals(""))
            controller.changeState(GamePlayState(controller))
            controller.game.setMaxScore(100)
        else
            controller.game

    def getStateString: String ="SetMaxScoreState"

class GamePlayState(controller: Controller) extends State(controller: Controller):
    def processInput(input: String): Game =
        controller.game = controller.executeStrategy
        input.trim.toLowerCase match
            case "suit" | "s" =>
                controller.setStrategy(SortBySuitStrategy())
                controller.game
            case "rank" | "r" =>
                controller.setStrategy(SortByRankStrategy())
                controller.game
            case "rules"|"ru" =>
                controller.changeState(RulesScreenState(controller))
                controller.game
            case "exit"|"e" =>
                controller.setKeepProcessRunning(false)
                controller.game
            case _ =>
                if(!input.toIntOption.equals(None))
                    controller.playCard(input.toInt-1)
                    if(controller.game.getCurrentPlayer.get.hand.size == 0 && !controller.checkGameOver)
                        controller.changeState(ShowScoreState(controller))
                    if (controller.game.getCurrentPlayer.get.hand.size == 0 && controller.checkGameOver)
                        controller.changeState(GameOverState(controller))
                    controller.addPointsToPlayers
                else
                    controller.game

    def getStateString: String = "GamePlayState"

class ShowScoreState(controller: Controller) extends State(controller: Controller):
    def processInput(input: String): Game =
        controller.dealCards(controller.shuffledeck(controller.createDeck))
        controller.game.setFirstCard(true)
        controller.game.setStartWithHearts(false)
        controller.changeState(GamePlayState(controller))
        controller.updateCurrentPlayer

    def getStateString: String = "ShowScoreState"

class GameOverState(controller: Controller) extends State(controller: Controller):
    def processInput(input: String): Game =
        input.toLowerCase().trim match
            case "new"|"n"|"quit"|"q" =>
                controller.game.resetForNewGame
                controller.game = controller.game.copy(players = Vector.empty)
                input.toLowerCase().trim match
                    case "new"|"n" =>
                        controller.changeState(GetPlayerNumberState(controller))
                    case "quit"|"q" =>
                        controller.changeState(MainScreenState(controller))
                controller.game
            case "again"|"a" =>
                controller.dealCards(controller.shuffledeck(controller.createDeck))
                controller.changeState(GamePlayState(controller))
                controller.game.resetForNewGame
            case "exit"|"e" =>
                controller.setKeepProcessRunning(false)
                controller.game
            case _ =>
                controller.game

    def getStateString: String = "GameOverState"
