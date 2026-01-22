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
import play.api.libs.json.JsValue

trait State(controller: Controller):
    def getStateString: String
    def stateFromXml(node: scala.xml.NodeSeq, controller: Controller): Unit = stateFromString(node.text, controller)
    def stateFromString(stateString: String, controller: Controller): Unit =
        controller.state = stateString.trim match
            case "MainScreenState"      => new MainScreenState(controller)
            case "RulesScreenState"     => new RulesScreenState(controller)
            case "GetPlayerNumberState" => new GetPlayerNumberState(controller)
            case "GetPlayerNamesState"  => new GetPlayerNamesState(controller)
            case "SetMaxScoreState"     => new SetMaxScoreState(controller)
            case "GamePlayState"        => new GamePlayState(controller)
            case "ShowScoreState"       => new ShowScoreState(controller)
            case "GameOverState"        => new GameOverState(controller)
            case other => new MainScreenState(controller)


case class MainScreenState(controller: Controller) extends State(controller: Controller):
    def getStateString: String = "MainScreenState"

case class RulesScreenState(controller: Controller) extends State(controller: Controller):
    def getStateString: String = "RulesScreenState"

case class GetPlayerNumberState(controller: Controller) extends State(controller: Controller):
    def getStateString: String = "GetPlayerNumberState"

case class GetPlayerNamesState(controller: Controller) extends State(controller: Controller):
    def getStateString: String = "GetPlayerNamesState"

case class SetMaxScoreState(controller: Controller) extends State(controller: Controller):
    def getStateString: String = "SetMaxScoreState"

case class GamePlayState(controller: Controller) extends State(controller: Controller):
    def getStateString: String = "GamePlayState"

case class ShowScoreState(controller: Controller) extends State(controller: Controller):
    def getStateString: String = "ShowScoreState"

case class GameOverState(controller: Controller) extends State(controller: Controller):
    def getStateString: String = "GameOverState"
