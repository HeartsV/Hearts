package de.htwg.se.Hearts.model.gameComponent.gameBase

import de.htwg.se.Hearts.model.gameComponent.BuilderInterface
import de.htwg.se.Hearts.model.gameComponent._

class Director() extends DirectorInterface():

	val builder = GameBuilder()
	def getBuilder: BuilderInterface = builder
	def resetForNextGame: Unit =
		builder.setPlayers(builder.getPlayers.map(_.resetPoints()))
		builder.setFirstCard(true)
		builder.setStartWithHearts(false)
		builder.setTrickCards(List.empty)
		builder.setCurrentWinnerAndHighestCard(None, None)
		builder.setErrorOrLastPlayedCard(Left("No Card"))

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
		builder.setErrorOrLastPlayedCard(gameState.getErrorOrLastCardPlayed)


	def moveCard(playedCard:CardInterface): Unit =
		builder.setPlayers(
			builder.getPlayers.updated(
				builder.getCurrentPlayerIndex.get,
				builder.getCurrentPlayer.get.removeCard(playedCard)
			)
		)
		builder.addCard(playedCard)
		if builder.getFirstCard then builder.setFirstCard(false)
		if (playedCard.getSuit == Suit.Hearts || playedCard == Card(Rank.Queen, Suit.Spades))
			builder.setStartWithHearts(true)
		if (builder.getHighestCard.forall(highest => playedCard.getSuit == highest.getSuit && playedCard.getRank > highest.getRank)) {
			builder.setCurrentWinnerAndHighestCard(builder.getCurrentPlayerIndex, Some(playedCard))
}

class GameBuilder() extends BuilderInterface:
	var game:Game = Game()
	def reset: Unit = game = Game()
	def setPlayerNumber(playerNumber: Option[Int]): Unit = game = game.copy(playerNumber = playerNumber)
	def setStartWithHearts(swh: Boolean): Unit = game = game.copy(startWithHearts = swh)
	def setKeepProcessRunning(kpr: Boolean): Unit = game = game.copy(keepProcessRunning = kpr)
	def setFirstCard(fc: Boolean): Unit = game = game.copy(firstCard = fc)
	def setPlayers(players: Vector[PlayerInterface]): Unit = game = game.copy(players = players)
	def updatePlayer(index: Int, updatedPlayer: Player): Unit = game = game.copy(players = game.players.updated(index, updatedPlayer))
	def setMaxScore(score: Option[Int]): Unit = game = game.copy(maxScore = score)
	def setCurrentPlayerIndex(cpi: Option[Int]): Unit = game = game.copy(currentPlayerIndex = cpi)
	def setTrickCards(trick: List[CardInterface]): Unit = game = game.copy(trickCards = trick)
	def addCard(card: CardInterface): Unit = game = game.copy(trickCards = game.trickCards :+ card)
	def setCurrentWinnerAndHighestCard(newWinner: (Option[Int], Option[CardInterface])): Unit = game = game.copy(currentWinnerIndex = newWinner(0), highestCard = newWinner(1))
	def setErrorOrLastPlayedCard(card: Either[String, CardInterface]): Unit = game = game.copy(errorOrlastCardPlayed = card)
	def getCopy: GameInterface = game
	def getTrickSize: Int = game.trickCards.size
	def getPlayerNumber: Int = game.playerNumber.get
	def getPlayers: Vector[PlayerInterface] = game.players
	def getCurrentPlayer: Option[PlayerInterface] = game.getCurrentPlayer
	def getCurrentPlayerIndex: Option[Int] = game.currentPlayerIndex
	def getFirstCard: Boolean = game.firstCard
	def getTrickCards: List[CardInterface] = game.trickCards
	def getCurrentWinnerIndex: Option[Int] = game.currentWinnerIndex
	def getStartWithHearts: Boolean = game.startWithHearts
	def getHighestCard: Option[CardInterface] = game.highestCard
	def getGame: GameInterface =
		 val newGame = game
		 reset
		 newGame