package de.htwg.se.Hearts.controller.leaderBoardComponent.leaderBoardBase

import de.htwg.se.Hearts.controller.leaderBoardComponent.LeaderBoardInterface

import de.htwg.se.Hearts.model.gameComponent.gameBase.*
import de.htwg.se.Hearts.model.gameComponent.PlayerInterface

class LeaderBoard extends LeaderBoardInterface:
    override def playersWithPoints(players: Vector[PlayerInterface]): List[(String, Int)] =
        players.toList.map(p => (p.getName, p.getPoints))

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
