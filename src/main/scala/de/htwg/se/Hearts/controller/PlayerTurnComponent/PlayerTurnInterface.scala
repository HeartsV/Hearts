package de.htwg.se.Hearts.controller.PlayerTurnComponent

import de.htwg.se.Hearts.model._

trait PlayerTurnInterface:
  def nextPlayerIndex(game: Game): Int
  def updateCurrentWinner(newWinner: (Int, Card), game: Game): (Option[Int], Option[Card])