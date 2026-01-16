package de.htwg.se.Hearts.controller.playerTurnComponent

import de.htwg.se.Hearts.model._
import de.htwg.se.Hearts.model.gameComponent.gameBase.Card
import de.htwg.se.Hearts.model.gameComponent.gameBase.Game
import de.htwg.se.Hearts.model.gameComponent.GameInterface
import de.htwg.se.Hearts.model.gameComponent.CardInterface

trait PlayerTurnInterface:
    def nextPlayerIndex(game: GameInterface): Int
    def updateCurrentWinner(newWinner: (Int, CardInterface), game: GameInterface): (Option[Int], Option[CardInterface])