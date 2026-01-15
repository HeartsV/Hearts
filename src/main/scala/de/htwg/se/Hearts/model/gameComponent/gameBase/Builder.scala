package de.htwg.se.Hearts.model.gameComponent.gameBase

import de.htwg.se.Hearts.model.gameComponent.BuilderInterface
import de.htwg.se.Hearts.model.gameComponent._

class Director(builder: GameBuilder) extends DirectorInterface:

	def resetForNextGame: Unit =
		builder.setPlayers(builder.game.getPlayers.map(_.copy(points = 0)))
		builder.setFirstCard(true)
		builder.setStartWithHearts(false)
		builder.setTrickCards(List.empty)
		builder.setCurrentWinnerAndHighestCard(None, None)
		builder.setLastPlayedCard(Left("No Card"))

	def copyGameState(gameState: GameInterface): Unit =
		builder.setPlayerNumber(gameState.getPlayerNumber)
		builder.setStartWithHearts(gameState.getStartWithHearts)
		builder.setKeepProcessRunning(gameState.getKeepProcessRunning)
		builder.setFirstCard(gameState.getFirstCard)
		builder.setPlayers(gameState.getPlayers)
		builder.setMaxScore(gameState.getMaxScore)
		builder.setCurrentPlayerIndex(gameState.getCurrentPlayerIndex)
		builder.setTrickCards(gameState.getTrickCards)
		builder.setCurrentWinnerAndHighestCard(gameState.getCurrentWinnerIndex,gameState.getHighestCard)
		builder.setLastPlayedCard(gameState.getLastCardPlayed)


	def moveCard(playedCard:Card): Unit =
		builder.setPlayers(
			builder.getPlayers.updated(
				builder.getCurrentPlayerIndex.get,
				builder.getCurrentPlayer.get.removeCard(playedCard)
			)
		)
		builder.addCard(playedCard)
		if builder.getFirstCard then builder.setFirstCard(false)
		if (playedCard.suit == Suit.Hearts || playedCard == Card(Rank.Queen, Suit.Spades))
			builder.setStartWithHearts(true)
		if (builder.game.highestCard.forall(highest => playedCard.suit == highest.suit && playedCard.rank > highest.rank)) {
			builder.setCurrentWinnerAndHighestCard(builder.getCurrentPlayerIndex, Some(playedCard))
}

class GameBuilder(var game:Game = Game()) extends BuilderInterface:
	def reset: Unit = game = Game()
	def setPlayerNumber(playerNumber: Option[Int]): Unit = game = game.copy(playerNumber = playerNumber)
	def setStartWithHearts(swh: Boolean): Unit = game = game.copy(startWithHearts = swh)
	def setKeepProcessRunning(kpr: Boolean): Unit = game = game.copy(keepProcessRunning = kpr)
	def setFirstCard(fc: Boolean): Unit = game = game.copy(firstCard = fc)
	def setPlayers(players: Vector[Player]): Unit = game = game.copy(players = players)
	def updatePlayer(index: Int, updatedPlayer: Player): Unit = game = game.copy(players = game.players.updated(index, updatedPlayer))
	def setMaxScore(score: Option[Int]): Unit = game = game.copy(maxScore = score)
	def setCurrentPlayerIndex(cpi: Option[Int]): Unit = game = game.copy(currentPlayerIndex = cpi)
	def setTrickCards(trick: List[Card]): Unit = game = game.copy(trickCards = trick)
	def addCard(card: Card): Unit = game = game.copy(trickCards = game.trickCards :+ card)
	def setCurrentWinnerAndHighestCard(newWinner: (Option[Int], Option[Card])): Unit = game = game.copy(currentWinnerIndex = newWinner(0), highestCard = newWinner(1))
	def setLastPlayedCard(card: Either[String, Card]): Unit = game = game.copy(lastCardPlayed = card)
	def getCopy: GameInterface = game
	def getTrickSize: Int = game.trickCards.size
	def getPlayerNumber: Int = game.playerNumber.get
	def getPlayers: Vector[Player] = game.players
	def getCurrentPlayer: Option[Player] = game.getCurrentPlayer
	def getCurrentPlayerIndex: Option[Int] = game.currentPlayerIndex
	def getFirstCard: Boolean = game.firstCard
	def getTrickCards: List[Card] = game.trickCards
	def getCurrentWinnerIndex: Option[Int] = game.currentWinnerIndex
	def getStartWithHearts: Boolean = game.startWithHearts
	def getGame: GameInterface =
		 val newGame = game
		 reset
		 newGame