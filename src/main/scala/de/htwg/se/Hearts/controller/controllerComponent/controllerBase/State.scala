package de.htwg.se.Hearts.controller.controllerComponent.controllerBase

import de.htwg.se.Hearts.model.gameComponent.BuilderInterface
import de.htwg.se.Hearts.model.gameComponent.gameBase.GameBuilder
import de.htwg.se.Hearts.model.gameComponent.gameBase.{Director, GameBuilder}
import de.htwg.se.Hearts.model.gameComponent.CoRInterface
import de.htwg.se.Hearts.model.gameComponent.gameBase.ChainOfResponsibility
import de.htwg.se.Hearts.model.gameComponent.GameInterface
import de.htwg.se.Hearts.model.gameComponent.gameBase.Game
import de.htwg.se.Hearts.model.gameComponent.PlayerInterface
import de.htwg.se.Hearts.model.gameComponent.gameBase.Player
import de.htwg.se.Hearts.model.gameComponent.Suit
import de.htwg.se.Hearts.model.gameComponent.Rank

trait State(controller: Controller):
    def processInput(input: String): Game
    def getStateString: String

class MainScreenState(controller: Controller) extends State(controller: Controller):
    def processInput(input: String): Game =
        val builder = GameBuilder(controller.game)
        input.toLowerCase.trim match
        case "new" | "n" =>
            controller.changeState(GetPlayerNumberState(controller))
            controller.game
        case "rules" | "r" =>
            controller.changeState(RulesScreenState(controller))
            controller.game
        case "exit" | "e" =>
            builder.setKeepProcessRunning(false)
            builder.getGame
        case _ =>
            controller.game

    def getStateString: String = "MainScreenState"

class RulesScreenState(controller: Controller) extends State(controller: Controller):
    def processInput(input: String): Game =
        input.trim match
        case "back" | "b" =>
            if controller.game.players.equals(Vector.empty) then controller.changeState(MainScreenState(controller))
            else controller.changeState(GamePlayState(controller))
            controller.game
        case _ =>
            controller.game

    def getStateString: String = "RulesScreenState"

class GetPlayerNumberState(controller: Controller) extends State(controller: Controller):
    def processInput(input: String): Game =
        val builder = GameBuilder(controller.game)
        if input.toIntOption.exists(intInput => intInput >= 3 && intInput <= 4) then
            controller.changeState(GetPlayerNamesState(controller))
            builder.setPlayerNumber(input.toInt)
            builder.getGame
        else
            builder.getGame

    def getStateString: String = "GetPlayerNumberState"

class GetPlayerNamesState(controller: Controller) extends State(controller: Controller):
    def processInput(input: String): Game =
        val builder = GameBuilder(controller.game)

        if !input.trim.equals("") then
            builder.setPlayers(builder.game.players :+ Player(input))
        else
            builder.setPlayers(builder.game.players :+ Player(s"P${builder.game.players.size + 1}"))

        if builder.game.players.size == builder.game.playerNumber.get then
            builder.setPlayers(controller.dealNewRound(builder.game))
            controller.changeState(SetMaxScoreState(controller))

        builder.getGame

    def getStateString: String = "GetPlayerNamesState"

class SetMaxScoreState(controller: Controller) extends State(controller: Controller):
    def processInput(input: String): Game =
        val builder = GameBuilder(controller.game)

        if input.toIntOption.exists(intInput => intInput >= 1 && intInput <= 400) then
            controller.changeState(GamePlayState(controller))
            builder.setMaxScore(input.toInt)
        else if input.trim.equals("") then
            controller.changeState(GamePlayState(controller))
            builder.setMaxScore(100)
        builder.setCurrentPlayerIndex(controller.turnService.nextPlayerIndex(builder.game))
        builder.getGame

    def getStateString: String = "SetMaxScoreState"

class GamePlayState(controller: Controller) extends State(controller: Controller):
    val cOR: CoRInterface = ChainOfResponsibility()

    def processInput(input: String): Game = ???


    def getStateString: String = "GamePlayState"

class ShowScoreState(controller: Controller) extends State(controller: Controller):
    def processInput(input: String): Game =
        val builder = GameBuilder(controller.game)
        builder.setPlayers(controller.dealNewRound(builder.game))
        builder.setFirstCard(true)
        builder.setStartWithHearts(false)

        controller.changeState(GamePlayState(controller))
        builder.getGame

    def getStateString: String = "ShowScoreState"

class GameOverState(controller: Controller) extends State(controller: Controller):
    def processInput(input: String): Game =
        val builder = GameBuilder(controller.game)

        input.toLowerCase().trim match
        case "new" | "n" | "quit" | "q" =>
            builder.reset
            input.toLowerCase().trim match
            case "new" | "n"  => controller.changeState(GetPlayerNumberState(controller))
            case "quit" | "q" => controller.changeState(MainScreenState(controller))
            builder.getGame

        case "again" | "a" =>
            builder.setPlayers(controller.dealNewRound(builder.game))
            val director = Director()
            director.resetForNextGame(builder)
            controller.changeState(GamePlayState(controller))
            builder.getGame

        case "exit" | "e" =>
            builder.setKeepProcessRunning(false)
            builder.getGame

        case _ =>
            builder.getGame

    def getStateString: String = "GameOverState"
