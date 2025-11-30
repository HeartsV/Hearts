package de.htwg.se.Hearts.controller

import de.htwg.se.Hearts.model._

class Controller(var game: Game) extends Observable:

    var state: State = MainScreenState(this)
    var sortingStrategy: Strategy = SortByRankStrategy()

    def processInput(input: String): Game =
        game = state.processInput(input)
        notifyObservers
        game

    def changeState(newState:State): Unit = state = newState

    def cardAllowed(index: Int): Boolean =
        game.getCurrentPlayer.exists { player =>
            val sortedHand = sortingStrategy.execute(player)
            val realIndex = index - 1
            if (realIndex < 0 || realIndex >= sortedHand.size)
                false
            else
                val card = sortedHand(realIndex)
                ChainOfResponsibility.validateMove(game, player, card)
        }

    def playCard(index: Int): Game =
        if (!cardAllowed(index))
            game
        else
            val currentPlayerIndex = game.getCurrentPlayerIndex
            val currentPlayer = game.getCurrentPlayer.get
            val sortedHand = sortingStrategy.execute(currentPlayer)
            val realIndex = index - 1
            if (realIndex < 0 || realIndex >= sortedHand.size)
                game
            else
                val card = sortedHand(realIndex)
                val updatedPlayer = currentPlayer.removeCard(card)
                var newGame = game.updatePlayer(currentPlayerIndex, updatedPlayer)
                newGame = newGame.addCard(card)
                newGame = updateCurrentWinner(updatedPlayer, card)
                if (newGame.firstCard)
                    newGame = newGame.setFirstCard(false)
                if ((card.suit == Suit.Hearts || card.equals(Card(Rank.Queen, Suit.Spades))) && !newGame.startWithHearts)
                    newGame = newGame.setStartWithHearts(true)
                if (newGame.trickCards.size == newGame.playerNumber.get)
                    val winner = newGame.getCurrentWinner.get
                    val winnerIndex = newGame.players.indexOf(winner)
                    val winnerWithHand = winner.addWonCards(newGame.trickCards)
                    newGame = newGame.updatePlayer(winnerIndex, winnerWithHand).clearTrick
                newGame = updateCurrentPlayer
                newGame

    def updateCurrentWinner(currentPlayer: Player, newCard: Card): Game =
        if (game.highestCard == None || game.highestCard.exists(card => card.suit == game.trickCards.last.suit && game.trickCards.last.rank.compare(card.rank) > 0))
            game.setTrick(currentPlayer, newCard)
        else
            game

    def trickToString: String =
        if (game.trickCards.nonEmpty) game.trickCards.map(card => s" $card ").mkString("|", "|", "|")
        else "|"

    def completeTrickString: String = trickToString + "     |" * (game.players.size - game.trickCards.size)

    def updateCurrentPlayer: Game =
    if (game.firstCard == true)
        game.setCurrentPlayerIndex(Some(game.players.indexWhere(_.hand.contains(Card(Rank.Two,Suit.Clubs)))))
    else if (game.players.size == game.trickCards.size)
        game.setCurrentPlayerIndex(Some(game.players.indexOf(game.getCurrentWinner)))
    else if (game.getCurrentPlayerIndex + 1 == game.players.size)
        game.setCurrentPlayerIndex(Some(0))
    else
        game.setCurrentPlayerIndex(Some(game.getCurrentPlayerIndex + 1))

    def createDeck: List[Card] =
        for
            suit <- Suit.values.toList
            rank <- Rank.values.toList
        yield Card(rank, suit)

    def shuffledeck(deck: List[Card]): List[Card] = util.Random().shuffle(deck)

    def filterOneCardOut(deck: List[Card]): List[Card] =
        if (deck.head == Card(Rank.Two,Suit.Clubs) && game.playerNumber.get == 3)
            deck.filterNot(_ == deck(1))
        else if (game.playerNumber.get == 3)
            deck.filterNot(_ == deck.head)
        else
            deck

    def dealCards(deck: List[Card]): Game =
        val newdeck = filterOneCardOut(deck)
        val handlist = deck.grouped(newdeck.size/game.playerNumber.get).toList
        game.copy(players = (game.players.zip(handlist).map { case (player, newCards) => player.copy(hand = newCards)}).toVector)

    def cardPoints(card: Card): Int =
        card match
            case Card(_, Suit.Hearts) => 1
            case Card(Rank.Queen, Suit.Spades) => 13
            case _ => 0

    def pointsForPlayer(player: Player): Int = player.wonCards.map(cardPoints).sum

    def rawPointsPerPlayer: Map[Player, Int] = game.players.map(p => p -> pointsForPlayer(p)).toMap

    def applyShootingTheMoon: Map[Player, Int] =
        val points = rawPointsPerPlayer
        val nonZero = points.filter { case (_, p) => p > 0 }
        if (nonZero.size == 1 && points.exists { case (_, p) => p == 0 })
            val (moonPlayer, moonPoints) = nonZero.head
            points.map {
                case (p, _) if p == moonPlayer  => p -> 0
                case (p, _)                     => p -> moonPoints
            }
        else
            points

    def addPointsToPlayers: Game =
        game.copy(players = (applyShootingTheMoon.map{ case (player, newPoints) => player.addPoints(newPoints)}).toVector)

    def getPlayersWithPoints: List[(String, Int)] =
        for {
            n <- game.players.toList
        } yield (n.name, n.points)

    def rankPlayers(players: List[(String, Int)]): List[(Int, String, Int)] =
        val sorted = players.sortBy(_._2)
        var lastPoints = -1
        var lastRank = 0
        var index = 0
        sorted.map {
            case (name, points) =>
                index += 1
                if (points != lastPoints)
                    lastRank = index
                    lastPoints = points
                (lastRank, name, points)
        }

    def handToString: String =
        val h = sortingStrategy.execute(game.getCurrentPlayer.get)
        (1 to h.size).map(index => s"  $index".padTo(5, ' ')).mkString("|", "|", "|") + "\n" +
        h.map(card => s" $card ").mkString("|", "|", "|")

    def getCurrentPlayerName: String = game.getCurrentPlayer.get.name

    def checkGameOver: Boolean =
        val maxScorePoints = game.players.map(p => p -> p.points).toMap
        if(maxScorePoints.exists {case (_, point) => point >= game.maxScore.get})
            true
        else
            false

    def getPlayerNumber: Option[Int] = game.playerNumber

    def setPlayerNumber(number: Option[Int]): Game = game.copy(playerNumber = number)

    def getKeepProcessRunning: Boolean = game.keepProcessRunning

    def setKeepProcessRunning(a: Boolean): Unit = game = game.copy(keepProcessRunning = a)

    def setStrategy(strategy:Strategy): Unit = this.sortingStrategy = strategy

    def executeStrategy: Game = game.copy(players = game.players.map(player => player.copy(hand = sortingStrategy.execute(player))))
    //falls ich das nur für einen spieler machen will
    /*def executeStrategy: Game =
        val i = game.getCurrentPlayerIndex
        val p = game.players(i)
        val sorted = sortingStrategy.execute(p)
        game.updatePlayer(i, p.copy(hand = sorted))*/

