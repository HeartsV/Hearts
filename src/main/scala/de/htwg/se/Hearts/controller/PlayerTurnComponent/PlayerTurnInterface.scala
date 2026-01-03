package de.htwg.se.Hearts.controller.playerTurnComponent

import de.htwg.se.Hearts.model._
import de.htwg.se.Hearts.model.gameComponent.gameBase.Card
import de.htwg.se.Hearts.model.gameComponent.gameBase.Game
import de.htwg.se.Hearts.model.gameComponent.GameInterface

trait PlayerTurnInterface:
    def nextPlayerIndex(game: GameInterface): Int
    def updateCurrentWinner(newWinner: (Int, Card), game: GameInterface): (Option[Int], Option[Card])