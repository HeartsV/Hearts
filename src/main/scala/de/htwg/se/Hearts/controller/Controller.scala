package de.htwg.se.Hearts.controller

import de.htwg.se.Hearts.model._

class Controller(var game: Game) extends Observable:

    var state: State = MainScreenState(this)
    var sortingStrategy: Strategy = SortByRankStrategy()

    def processInput(input: String): Game =
        game = state.processInput(input)
        game.lastCardPlayed match
            case Right(card) =>
                val builder = GameBuilder(game)
                builder.setCurrentPlayerIndex(updateCurrentPlayer)
                game = builder.getGame
            case _ =>
        notifyObservers
        game

    def changeState(newState:State): Unit = state = newState

    def updateCurrentWinner(newWinner: (Player, Card)): (Player, Card) =
        if (game.highestCard == None || game.highestCard.exists(card => card.suit == game.trickCards.last.suit && game.trickCards.last.rank.compare(card.rank) > 0))
            newWinner
        else
            (game.currentWinner.get, game.highestCard.get)

    def updateCurrentPlayer: Int =
    if (game.firstCard == true)
        game.players.indexWhere(_.hand.contains(Card(Rank.Two,Suit.Clubs)))
    else if (game.players.size == game.trickCards.size)
        game.players.indexOf(game.currentWinner)
    else if (game.currentPlayerIndex.get + 1 == game.players.size)
        0
    else
        game.currentPlayerIndex.get + 1

    def trickToString: String =
        if (game.trickCards.nonEmpty) game.trickCards.map(card => s" $card ").mkString("|", "|", "|")
        else "|"

    def completeTrickString: String = trickToString + "     |" * (game.players.size - game.trickCards.size)

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

    def dealCards(deck: List[Card]): Vector[Player] =
        val newdeck = filterOneCardOut(deck)
        val handlist = deck.grouped(newdeck.size/game.playerNumber.get).toList
        (game.players.zip(handlist).map { case (player, newCards) => player.copy(hand = newCards)}).toVector

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

    def addPointsToPlayers: Vector[Player] =
        (applyShootingTheMoon.map{ case (player, newPoints) => player.addPoints(newPoints)}).toVector

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

    def executeStrategy: Vector[Player] = game.players.map(player => player.copy(hand = sortingStrategy.execute(player)))
    //falls ich das nur für einen spieler machen will
    /*def executeStrategy: Game =
        val i = game.getCurrentPlayerIndex
        val p = game.players(i)
        val sorted = sortingStrategy.execute(p)
        game.updatePlayer(i, p.copy(hand = sorted))*/

