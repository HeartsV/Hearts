package de.htwg.se.Hearts.model.gameComponent.gameBase

import de.htwg.se.Hearts.model.gameComponent.BuilderInterface
import de.htwg.se.Hearts.model.gameComponent.GameInterface

class Director:

	def resetForNextGame(builder: GameBuilder): Unit =
		builder.setPlayers(builder.game.getPlayers.map(_.copy(points = 0)))
		builder.setFirstCard(true)
		builder.setStartWithHearts(false)

class GameBuilder(var game:Game = Game()) extends BuilderInterface(game:Game):
	def reset: Unit = game = Game()
	def setPlayerNumber(playerNumber: Int): Unit = game = game.copy(playerNumber = Some(playerNumber))
	def setStartWithHearts(swh: Boolean): Unit = game = game.copy(startWithHearts = swh)
	def setKeepProcessRunning(kpr: Boolean): Unit = game = game.copy(keepProcessRunning = kpr)
	def setFirstCard(fc: Boolean): Unit = game = game.copy(firstCard = fc)
	def setPlayers(players: Vector[Player]): Unit = game = game.copy(players = players)

	def updatePlayer(index: Int, updatedPlayer: Player): Unit = game = game.copy(players = game.players.updated(index, updatedPlayer))
	def setMaxScore(score: Int): Unit = game = game.copy(maxScore = Some(score))
	def setCurrentPlayerIndex(cpi: Int): Unit = game = game.copy(currentPlayerIndex = Some(cpi))
	def setTrickCards(trick: List[Card]): Unit = game = game.copy(trickCards = trick)

	def addCard(card: Card): Unit = game = game.copy(trickCards = game.trickCards :+ card)

	def setCurrentWinnerAndHighestCard(newWinner: (Option[Int], Option[Card])): Unit = game = game.copy(currentWinnerIndex = newWinner(0), highestCard = newWinner(1))

	def setLastPlayedCard(card: Either[String, Card]): Unit = game = game.copy(lastCardPlayed = card)

	def getGame: Game =
		 val newGame = game
		 reset
		 newGame


