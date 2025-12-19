package de.htwg.se.Hearts.controller.LeaderBoardComponent

import de.htwg.se.Hearts.model._

trait LeaderBoardInterface:
  def playersWithPoints(players: Vector[Player]): List[(String, Int)]
  def rankPlayers(players: List[(String, Int)]): List[(Int, String, Int)]