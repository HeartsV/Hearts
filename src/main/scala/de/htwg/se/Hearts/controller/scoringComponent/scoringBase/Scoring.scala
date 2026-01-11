package de.htwg.se.Hearts.controller.scoringComponent.scoringBase

import de.htwg.se.Hearts.controller.scoringComponent.ScoringInterface
import de.htwg.se.Hearts.model._
import de.htwg.se.Hearts.model.gameComponent.gameBase.Card
import de.htwg.se.Hearts.model.gameComponent.gameBase.Player
import de.htwg.se.Hearts.model.gameComponent.Suit
import de.htwg.se.Hearts.model.gameComponent.Rank


class HeartsScoring extends ScoringInterface:
    override def cardPoints(card: Card): Int =
        card match
        case Card(_, Suit.Hearts) => 1
        case Card(Rank.Queen, Suit.Spades) => 13
        case _ => 0

    override def pointsForPlayer(player: Player): Int =
        player.wonCards.map(cardPoints).sum

    override def rawPointsPerPlayer(players: Vector[Player]): Map[Player, Int] =
        players.map(p => p -> pointsForPlayer(p)).toMap

    /** If someone took all point cards (26), everyone else gets 26 and that player gets 0. */
    override def applyShootingTheMoon(points: Map[Player, Int]): Map[Player, Int] =
        val nonZero = points.filter { case (_, p) => p > 0 }
        if (nonZero.size == 1 && points.exists { case (_, p) => p == 0 })
            val (moonPlayer, moonPoints) = nonZero.head
            points.map {
                case (p, _) if p == moonPlayer  => p -> 0
                case (p, _)                     => p -> moonPoints
            }
        else
            points

    override def addRoundPoints(players: Vector[Player]): Vector[Player] =
        val raw = rawPointsPerPlayer(players)
        val adjusted = applyShootingTheMoon(raw)
        players.map { p =>
        val delta = adjusted.getOrElse(p, 0)
        p.copy(points = p.points + delta, wonCards = List())
        }