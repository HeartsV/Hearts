package de.htwg.se.Hearts.controller.playerTurnComponent.playerTurnBase

import de.htwg.se.Hearts.model._
import de.htwg.se.Hearts.controller.playerTurnComponent.PlayerTurnInterface
import de.htwg.se.Hearts.model.gameComponent.gameBase.Card
import de.htwg.se.Hearts.model.gameComponent.gameBase.Game
import de.htwg.se.Hearts.model.gameComponent.Suit
import de.htwg.se.Hearts.model.gameComponent.Rank
import de.htwg.se.Hearts.model.gameComponent.GameInterface

class PlayerTurn extends PlayerTurnInterface:
    override def nextPlayerIndex(game: GameInterface): Int =
        if game.getFirstCard then
        game.getPlayers.indexWhere(_.hand.contains(
            Card(Rank.Two, Suit.Clubs)
        ))
        else if game.getPlayerNumber.get == game.getTrickCards.size then game.getCurrentWinnerIndex.get
        else if game.getCurrentPlayerIndex.get + 1 == game.getPlayerNumber.get then 0
        else game.getCurrentPlayerIndex.get + 1

    override def updateCurrentWinner(newWinner: (Int, Card), game: GameInterface): (Option[Int], Option[Card]) =
        game.getHighestCard match
        case None => (Some(newWinner._1), Some(newWinner._2))
        case Some(currentHighest) =>
            val leadSuit = game.getTrickCards.headOption.map(_.suit)
            val challenger = newWinner._2
            val sameSuitAsLead = leadSuit.contains(challenger.suit) && leadSuit.contains(currentHighest.suit)

            if sameSuitAsLead && challenger.rank.compare(currentHighest.rank) > 0 then
            (Some(newWinner._1), Some(challenger))
            else
            (game.getCurrentWinnerIndex, game.getHighestCard)
