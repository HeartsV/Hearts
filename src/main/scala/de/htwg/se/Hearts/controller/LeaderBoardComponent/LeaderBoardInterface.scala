package de.htwg.se.Hearts.controller.leaderBoardComponent

import de.htwg.se.Hearts.model.gameComponent.gameBase.*
import de.htwg.se.Hearts.model.gameComponent.PlayerInterface

trait LeaderBoardInterface:
    def playersWithPoints(players: Vector[PlayerInterface]): List[(String, Int)]
    def rankPlayers(players: List[(String, Int)]): List[(Int, String, Int)]