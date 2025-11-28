package de.htwg.se.Hearts.controller

import de.htwg.se.Hearts.model.*
import scala.collection.mutable.ListBuffer
import scala.compiletime.ops.boolean


class Controller(game: Game) extends Observable() {

    var state:State = MainScreenState(this)
    var sortingStrategy:Strategy = SortByRankStrategy()

    def processInput(input: String): Boolean =
        if(state.processInput(input))
            notifyObservers
            true
        else
            notifyObservers
            false

    def changeState(newState:State): Unit = state = newState

    def playCard(index: Int): Boolean =
        if(game.trick.cards.size == game.players.size)
            game.currentPlayer.get.wonCards.addAll(game.trick.cards)
            game.trick.clearTrick()
        if(game.currentPlayer.get.hand.size > index - 1 && index - 1 >= 0 && addCard(game.currentPlayer.get.hand(index - 1)))
            val h = sortingStrategy.execute(game.currentPlayer.get)
            game.currentPlayer.get.removeCard(h(index-1))
            true
        else
            false

    def addCard(newCard: Card): Boolean =
        if(game.firstCard == true)
            if(newCard  == (Card(Rank.Two,Suit.Clubs)))
                game.trick.addCard(newCard)
                true
            else
                false
        else if(!(game.trick.cards == ListBuffer()))
            if(game.trick.highestCard.exists(card => card.suit == newCard.suit) || !game.trick.highestCard.exists(card => game.currentPlayer.get.hand.exists(_.suit == card.suit)))
                game.trick.addCard(newCard)
                if(newCard.suit == Suit.Hearts)
                    game.startWithHearts = true
                true
            else
                false
        else
            if (newCard.suit == Suit.Hearts && game.startWithHearts == false)
                false
            else
                game.trick.addCard(newCard)
                game.trick.initializeTrick(game.currentPlayer.get,newCard)
                true

    def updateCurrentWinner(): Boolean =
        if(game.trick.highestCard == None || game.trick.highestCard.exists(card => card.suit == game.trick.cards.last.suit && game.trick.cards.last.rank.compare(card.rank) > 0))
            game.trick.highestCard = Some(game.trick.cards.last)
            game.trick.currentWinner = Some(game.currentPlayer.get)
            true
        else
            false

    def completeTrickString(): String = game.trick.trickToString()  + "     |" * (game.players.size - game.trick.cards.size)

    def updateCurrentPlayer(): Boolean =
    if (game.firstCard == true)
        game.currentPlayer = game.players.find(_.hand.contains(Card(Rank.Two,Suit.Clubs)))
        true
    else if(game.players.size == game.trick.cards.size)
        game.currentPlayer = game.trick.currentWinner
        true
    else if(game.players.indexOf(game.currentPlayer.get) + 1 == game.players.size)
        game.currentPlayer = Some(game.players(0))
        true
    else
        game.currentPlayer = Some(game.players((game.players.indexOf(game.currentPlayer.get) + 1)))
        true

    def createDeck(): List[Card] =
        for
            suit <- Suit.values.toList
            rank <- Rank.values.toList
        yield Card(rank, suit)

    def shuffledeck(deck: List[Card]): List[Card] = util.Random().shuffle(deck)

    def filterOneCardOut(deck: List[Card]): List[Card] =
        if(deck.head == Card(Rank.Two,Suit.Clubs) && game.playerNumber.get == 3)
            val newdeck = deck.filterNot(_ == deck(1))
            newdeck
        else if(game.playerNumber.get == 3)
            val newdeck = deck.filterNot(_ == deck.head)
            newdeck
        else
            deck

    def dealCards(deck: List[Card]): Boolean =
        val newdeck = filterOneCardOut(deck)
        val handlist = deck.grouped(newdeck.size/game.playerNumber.get).toList
        for (i <- 0 to game.playerNumber.get - 1) game.players(i).addAllCards(handlist(i))
        true

    def cardPoints(card: Card): Int =
        card match
            case Card(_, Suit.Hearts) => 1
            case Card(Rank.Queen, Suit.Spades) => 13
            case _ => 0

    def pointsForPlayer(player: Player): Int = player.wonCards.map(cardPoints).sum

    def rawPointsPerPlayer(): Map[Player, Int] = game.players.map(p => p -> pointsForPlayer(p)).toMap

    def applyShootingTheMoon(points: Map[Player, Int]): Map[Player, Int] =
        val nonZero = points.filter { case (_, p) => p > 0 }
        if (nonZero.size == 1 && points.exists { case (_, p) => p == 0 })
            val (moonPlayer, moonPoints) = nonZero.head
            points.map {
                case (p, _) if p == moonPlayer  => p -> 0
                case (p, _)                     => p -> moonPoints
            }
        else
            points

    def addPointsToPlayers(raw: Map[Player, Int]): Unit =
        for (p <- game.players)
            p.points += applyShootingTheMoon(raw).getOrElse(p, 0)
            p.wonCards.clear()

    def getPlayersWithPoints(): List[(String, Int)] =
        for {
            n <- game.players.toList
        } yield (n.name, n.points)

    def rankPlayers(players: List[(String, Int)]): List[(Int, String, Int)] =
        val sorted = players.sortBy(_._2)
        var lastPoints = -1
        var lastRank = 0
        var index = 0
        sorted.map { case (name, points) =>
            index += 1
            if (points != lastPoints)
                lastRank = index
                lastPoints = points
            (lastRank, name, points)
        }

    def handToString(): String =
        val h = sortingStrategy.execute(game.currentPlayer.get)
        (1 to h.size).map(index => s"  $index".padTo(5, ' ')).mkString("|", "|", "|") + "\n" +
        h.map(card => s" $card ").mkString("|", "|", "|")

    def getCurrentPlayerName(): String = game.currentPlayer.get.name

    def checkGameOver(): Boolean =
        val maxScorePoints = game.players.map(p => p -> p.points).toMap
        if(maxScorePoints.exists {case (_, point) => point >= game.maxScore.get})
            true
        else
            false

    def getGame(): Game = game

    def getPlayerNumber(): Option[Int] = game.playerNumber

    def setPlayerNumber(number: Int): Unit = game.playerNumber = Some(number)

    def getkeepProcessRunning(): Boolean = game.keepProcessRunning

    def setkeepProcessRunning(a:Boolean): Unit = game.keepProcessRunning = a

    def setStrategy(strategy:Strategy): Unit = this.sortingStrategy = strategy

    def executeStrategy(): Unit = sortingStrategy.execute(game.currentPlayer.get)
}
