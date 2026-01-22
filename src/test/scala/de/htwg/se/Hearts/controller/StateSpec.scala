package de.htwg.se.Hearts.controller

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.Hearts.model.gameComponent.gameBase.*
import de.htwg.se.Hearts.controller.controllerComponent.controllerBase._
import de.htwg.se.Hearts.model.gameComponent._

class StateSpec extends AnyWordSpec with Matchers{

    "A State" should {
        val gameCo = Controller(Game())
        "get the correct string for each state" in {
            MainScreenState(gameCo).getStateString should be ("MainScreenState")
            RulesScreenState(gameCo).getStateString should be ("RulesScreenState")
            GetPlayerNumberState(gameCo).getStateString should be ("GetPlayerNumberState")
            GetPlayerNamesState(gameCo).getStateString should be ("GetPlayerNamesState")
            SetMaxScoreState(gameCo).getStateString should be ("SetMaxScoreState")
            GamePlayState(gameCo).getStateString should be ("GamePlayState")
            ShowScoreState(gameCo).getStateString should be ("ShowScoreState")
            GameOverState(gameCo).getStateString should be ("GameOverState")
        }
        "create the correct state out of the string" in {
            gameCo.state.stateFromString("MainScreenState", gameCo)
            gameCo.state shouldBe a [MainScreenState]
            gameCo.state.stateFromString("RulesScreenState", gameCo)
            gameCo.state shouldBe a [RulesScreenState]
            gameCo.state.stateFromString("GetPlayerNumberState", gameCo)
            gameCo.state shouldBe a [GetPlayerNumberState]
            gameCo.state.stateFromString("GetPlayerNamesState", gameCo)
            gameCo.state shouldBe a [GetPlayerNamesState]
            gameCo.state.stateFromString("SetMaxScoreState", gameCo)
            gameCo.state shouldBe a [SetMaxScoreState]
            gameCo.state.stateFromString("GamePlayState", gameCo)
            gameCo.state shouldBe a [GamePlayState]
            gameCo.state.stateFromString("ShowScoreState", gameCo)
            gameCo.state shouldBe a [ShowScoreState]
            gameCo.state.stateFromString("GameOverState", gameCo)
            gameCo.state shouldBe a [GameOverState]
            gameCo.state.stateFromString("", gameCo)
            gameCo.state shouldBe a [MainScreenState]
        }
    }
}