package de.htwg.se.Hearts.controller.playerTurnComponent.playerTurnBase

import de.htwg.se.Hearts.model._
import de.htwg.se.Hearts.controller.playerTurnComponent.PlayerTurnInterface
import de.htwg.se.Hearts.model.gameComponent.gameBase.Card
import de.htwg.se.Hearts.model.gameComponent.gameBase.Game
import de.htwg.se.Hearts.model.gameComponent.Suit
import de.htwg.se.Hearts.model.gameComponent.Rank

class PlayerTurn extends PlayerTurnInterface:
    override def nextPlayerIndex(game: Game): Int =
        if game.firstCard then
        game.players.indexWhere(_.hand.contains(
            Card(Rank.Two, Suit.Clubs)
        ))
        else if game.playerNumber.get == game.trickCards.size then
        game.currentWinnerIndex.get
        else if game.currentPlayerIndex.get + 1 == game.playerNumber.get then 0
        else game.currentPlayerIndex.get + 1

    override def updateCurrentWinner(newWinner: (Int, Card), game: Game): (Option[Int], Option[Card]) =
        game.highestCard match
        case None => (Some(newWinner._1), Some(newWinner._2))
        case Some(currentHighest) =>
            val leadSuit = game.trickCards.headOption.map(_.suit)
            val challenger = newWinner._2
            val sameSuitAsLead =
            leadSuit.contains(challenger.suit) && leadSuit.contains(currentHighest.suit)

            if sameSuitAsLead && challenger.rank.compare(currentHighest.rank) > 0 then
            (Some(newWinner._1), Some(challenger))
            else
            (game.currentWinnerIndex, game.highestCard)
