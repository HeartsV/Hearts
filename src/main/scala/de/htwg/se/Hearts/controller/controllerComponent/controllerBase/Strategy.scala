package de.htwg.se.Hearts.controller.controllerComponent.controllerBase
import de.htwg.se.Hearts.model.*
import de.htwg.se.Hearts.model.gameComponent.gameBase.Card
import de.htwg.se.Hearts.model.gameComponent.gameBase.Player

trait Strategy:
    def execute(player: Player): List[Card]

class SortByRankStrategy extends Strategy:
    def execute(player: Player): List[Card] = player.hand.sortBy(_.suit).sortBy(_.rank).toList

class  SortBySuitStrategy extends Strategy:
    def execute(player: Player): List[Card] = player.hand.sortBy(_.rank).sortBy(_.suit).toList
