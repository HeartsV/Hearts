package de.htwg.se.Hearts.controller
import de.htwg.se.Hearts.model.*

trait Strategy {
    def execute(player : Player): Unit
}

class SortByRankStrategy() extends Strategy(){
    def execute(player: Player): Unit =
        player.hand.sortBy(x => x.rank)
}

class  SortBySuitStrategy extends Strategy() {
    def execute(player: Player): Unit = 
        player.hand.sortBy(x => x.suit)
}