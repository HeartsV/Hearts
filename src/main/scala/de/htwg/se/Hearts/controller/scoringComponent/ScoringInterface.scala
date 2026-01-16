package de.htwg.se.Hearts.controller.scoringComponent
import de.htwg.se.Hearts.model._
import de.htwg.se.Hearts.model.gameComponent.gameBase.Card
import de.htwg.se.Hearts.model.gameComponent.gameBase.Game
import de.htwg.se.Hearts.model.gameComponent.gameBase.Player
import de.htwg.se.Hearts.model.gameComponent.GameInterface
import de.htwg.se.Hearts.model.gameComponent.PlayerInterface
import de.htwg.se.Hearts.model.gameComponent.CardInterface

trait ScoringInterface:
    def cardPoints(card: CardInterface): Int
    def pointsForPlayer(player: PlayerInterface): Int
    def rawPointsPerPlayer(players: Vector[PlayerInterface]): Map[PlayerInterface, Int]
    def applyShootingTheMoon(points: Map[PlayerInterface, Int]): Map[PlayerInterface, Int]
    def addRoundPoints(players: Vector[PlayerInterface]): Vector[PlayerInterface]

    /** Adds points based on the wonCards currently stored on the players in this game. */
    def addPointsToPlayers(game: GameInterface): Vector[PlayerInterface] = addRoundPoints(game.getPlayers)
