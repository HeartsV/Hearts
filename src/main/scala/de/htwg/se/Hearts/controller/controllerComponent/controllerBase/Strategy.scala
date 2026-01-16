package de.htwg.se.Hearts.controller.controllerComponent.controllerBase
import de.htwg.se.Hearts.model.*
import de.htwg.se.Hearts.model.gameComponent.gameBase.Card
import de.htwg.se.Hearts.model.gameComponent.gameBase.Player
import de.htwg.se.Hearts.model.gameComponent.PlayerInterface
import de.htwg.se.Hearts.model.gameComponent.CardInterface

trait Strategy:
    def execute(player: PlayerInterface): List[CardInterface]

class SortByRankStrategy extends Strategy:
    def execute(player: PlayerInterface): List[CardInterface] = player.getHand.sortBy(_.getSuit).sortBy(_.getRank).toList

class  SortBySuitStrategy extends Strategy:
    def execute(player: PlayerInterface): List[CardInterface] = player.getHand.sortBy(_.getRank).sortBy(_.getSuit).toList
