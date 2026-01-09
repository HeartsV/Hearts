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
    def getStateString: String

class MainScreenState(controller: Controller) extends State(controller: Controller):
    def getStateString: String = "MainScreenState"

class RulesScreenState(controller: Controller) extends State(controller: Controller):
    def getStateString: String = "RulesScreenState"

class GetPlayerNumberState(controller: Controller) extends State(controller: Controller):
    def getStateString: String = "GetPlayerNumberState"

class GetPlayerNamesState(controller: Controller) extends State(controller: Controller):
    def getStateString: String = "GetPlayerNamesState"

class SetMaxScoreState(controller: Controller) extends State(controller: Controller):
    def getStateString: String = "SetMaxScoreState"

class GamePlayState(controller: Controller) extends State(controller: Controller):
    val cOR: CoRInterface = ChainOfResponsibility()
    def getStateString: String = "GamePlayState"

class ShowScoreState(controller: Controller) extends State(controller: Controller):
    def getStateString: String = "ShowScoreState"

class GameOverState(controller: Controller) extends State(controller: Controller):
    def getStateString: String = "GameOverState"
