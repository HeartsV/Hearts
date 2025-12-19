package de.htwg.se.Hearts.controller.scoringComponent
import de.htwg.se.Hearts.model._
import de.htwg.se.Hearts.model.gameComponent.gameBase.Card
import de.htwg.se.Hearts.model.gameComponent.gameBase.Game
import de.htwg.se.Hearts.model.gameComponent.gameBase.Player

trait ScoringInterface:
  def cardPoints(card: Card): Int
  def pointsForPlayer(player: Player): Int
  def rawPointsPerPlayer(players: Vector[Player]): Map[Player, Int]
  def applyShootingTheMoon(points: Map[Player, Int]): Map[Player, Int]
  def addRoundPoints(players: Vector[Player]): Vector[Player]

  /** Adds points based on the wonCards currently stored on the players in this game. */
  def addPointsToPlayers(game: Game): Vector[Player] =
    addRoundPoints(game.players)
