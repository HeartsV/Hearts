package de.htwg.se.Hearts.model

trait Builder:
	def reset: Unit
	def setPlayerNumber(playerNumber: Int): Unit
	def setStartWithHearts(swh: Boolean): Unit
	def setKeepProcessRunning(kpr: Boolean): Unit
	def setFirstCard(fc: Boolean): Unit
	def setPlayers(players: Vector[Player]): Unit
	def setMaxScore(maxScore: Int): Unit
	def setCurrentPlayerIndex(cpi: Int): Unit
	def setTrickCards(trick: List[Card]): Unit
	def setCurrentWinnerAndHighestCard(newWinner: (Player, Card)): Unit
	def setFirstPlayer(firstP: Player): Unit

class GameBuilder(var game: Game = Game()) extends Builder:
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

	def setCurrentWinnerAndHighestCard(newWinner: (Player, Card)): Unit = game = game.copy(currentWinner = Some(newWinner(0)), highestCard = Some(newWinner(1)))
	def setFirstPlayer(firstP: Player): Unit = game = game.copy(firstPlayer = Some(firstP))

	def getGame: Game =
		 val newGame = game
		 reset
		 newGame


