package de.htwg.se.Hearts.controller.LeaderBoardComponent.LeaderBoardBase

import de.htwg.se.Hearts.controller.LeaderBoardComponent.LeaderBoardInterface

import de.htwg.se.Hearts.model._

class LeaderBoard extends LeaderBoardInterface:
  override def playersWithPoints(players: Vector[Player]): List[(String, Int)] =
    players.toList.map(p => (p.name, p.points))

  override def rankPlayers(players: List[(String, Int)]): List[(Int, String, Int)] =
    val sorted = players.sortBy(_._2)
    var lastPoints = Int.MinValue
    var lastRank = 0
    var index = 0
    sorted.map { case (name, points) =>
      index += 1
      if points != lastPoints then
        lastRank = index
        lastPoints = points
      (lastRank, name, points)
    }
