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
        val builder = GameBuilder(controller.game)
        if(input.toIntOption.exists(intInput => intInput >= 3 && intInput <= 4))
            controller.changeState(GetPlayerNamesState(controller))
            builder.setPlayerNumber(input.toInt)
            builder.getGame
        else
            builder.getGame

    def getStateString: String = "GetPlayerNumberState"

class GetPlayerNamesState(controller: Controller) extends State(controller: Controller):
    def processInput(input: String): Game =
        val builder = GameBuilder(controller.game)
        if(!input.trim.equals(""))
            builder.setPlayers(controller.game.players :+ Player(input))
        else
            builder.setPlayers(controller.game.players :+ Player(s"P${controller.game.players.size + 1}"))
        if(builder.game.players.size == builder.game.playerNumber.get)
            builder.setPlayers(controller.dealCards(controller.shuffledeck(controller.createDeck)))
            controller.changeState(SetMaxScoreState(controller))
        builder.getGame

    def getStateString: String = "GetPlayerNamesState"

class SetMaxScoreState(controller: Controller) extends State(controller: Controller):
    def processInput(input: String): Game =
        val builder = GameBuilder(controller.game)
        if(input.toIntOption.exists(intInput => intInput >= 1 && intInput <= 400))
            controller.changeState(GamePlayState(controller))
            builder.setMaxScore(input.toInt)
        else if(input.trim.equals(""))
            controller.changeState(GamePlayState(controller))
            builder.setMaxScore(100)
        builder.getGame

    def getStateString: String ="SetMaxScoreState"

class GamePlayState(controller: Controller) extends State(controller: Controller):
    def processInput(input: String): Game =
        val builder = GameBuilder(controller.game)
        builder.setPlayers(controller.executeStrategy)
        input.trim.toLowerCase match
            case "suit" | "s" =>
                controller.setStrategy(SortBySuitStrategy())
                builder.getGame
            case "rank" | "r" =>
                controller.setStrategy(SortByRankStrategy())
                builder.getGame
            case "rules"|"ru" =>
                controller.changeState(RulesScreenState(controller))
                builder.getGame
            case "exit"|"e" =>
                controller.setKeepProcessRunning(false)
                builder.getGame
            case _ =>
                if(!input.toIntOption.equals(None))
                    controller.game = controller.playCard(input.toInt-1)
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
        val builder = GameBuilder(controller.game)
        builder.setPlayers(controller.dealCards(controller.shuffledeck(controller.createDeck)))
        builder.setFirstCard(true)
        builder.setStartWithHearts(false)
        controller.changeState(GamePlayState(controller))
        builder.getGame

    def getStateString: String = "ShowScoreState"

class GameOverState(controller: Controller) extends State(controller: Controller):
    def processInput(input: String): Game =
        val builder = GameBuilder(controller.game)
        input.toLowerCase().trim match
            case "new"|"n"|"quit"|"q" =>
                builder.reset
                input.toLowerCase().trim match
                    case "new"|"n" =>
                        controller.changeState(GetPlayerNumberState(controller))
                    case "quit"|"q" =>
                        controller.changeState(MainScreenState(controller))
                builder.getGame
            case "again"|"a" =>
                controller.dealCards(controller.shuffledeck(controller.createDeck))
                controller.changeState(GamePlayState(controller))
                val director = Director()
                director.resetForNextGame(builder)
                builder.getGame
            case "exit"|"e" =>
                builder.setKeepProcessRunning(false)
                builder.getGame
            case _ =>
                builder.getGame

    def getStateString: String = "GameOverState"
