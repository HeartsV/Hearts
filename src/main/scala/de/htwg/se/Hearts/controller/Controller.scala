package de.htwg.se.Hearts.controller

import de.htwg.se.Hearts.model.*
import scala.collection.mutable.ListBuffer
import scala.compiletime.ops.boolean


class Controller(var game: Game) extends Observable():

    var state:State = MainScreenState(this)
    var sortingStrategy:Strategy = SortByRankStrategy()

    def processInput(input: String): Boolean =
        /*if(state.processInput(input))
            notifyObservers
            true
        else
            notifyObservers
            false*/
        game = state.processInput(input)
        notifyObservers
        true


    def changeState(newState:State): Unit = state = newState

    def cardAllowed(index: Int): Boolean =
        val h = sortingStrategy.execute(game.getCurrentPlayer.get)
        if(h.size > index - 1 && index - 1 >= 0 && addCard(h(index - 1)))
            true
        else
            false

    def playCard(index: Int): Player =
        val h = sortingStrategy.execute(game.getCurrentPlayer.get)
        if(game.trickCards.size == game.players.size)
            game.getCurrentPlayer.get.addWonCards(game.trickCards)
            game.clearTrick
            

    def addCard(newCard: Card): Game =
        if(game.firstCard == true)
            if(newCard  == (Card(Rank.Two,Suit.Clubs)))
                game.addCard(newCard)
            else
                game
        else if(!(game.trickCards == ListBuffer()))
            if(game.highestCard.exists(card => card.suit == newCard.suit) || !game.highestCard.exists(card => game.getCurrentPlayer.get.hand.exists(_.suit == card.suit)))
                if(newCard.suit == Suit.Hearts)
                    game.setStartWithHearts(true).addCard(newCard)
                else
                    game.addCard(newCard)
            else
                game
        else
            if (newCard.suit == Suit.Hearts && game.startWithHearts == false)
                game
            else
                game.addCard(newCard).setTrick(game.getCurrentPlayer.get,newCard).setFirstPlayer(game.getCurrentPlayer.get)



    def updateCurrentWinner(currentPlayer: Player, newCard: Card): Game =
        if(game.highestCard == None || game.highestCard.exists(card => card.suit == game.trickCards.last.suit && game.trickCards.last.rank.compare(card.rank) > 0))
            game.setTrick(currentPlayer, newCard)
        else
            game

    def trickToString: String =
        if (game.trickCards.nonEmpty) game.trickCards.map(card => s" $card ").mkString("|", "|", "|")
        else "|"

    def completeTrickString(): String = trickToString + "     |" * (game.players.size - game.trickCards.size)

    def updateCurrentPlayer(): Game =
    if (game.firstCard == true)
        game.setCurrentPlayerIndex(game.players.indexWhere(_.hand.contains(Card(Rank.Two,Suit.Clubs))))
    else if(game.players.size == game.trickCards.size)
        game.setCurrentPlayerIndex(game.players.indexOf(game.getCurrentWinner))
    else if(game.getCurrentPlayerIndex + 1 == game.players.size)
        game.setCurrentPlayerIndex(0)
    else
        game.setCurrentPlayerIndex(game.getCurrentPlayerIndex + 1)

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

    def dealCards(deck: List[Card]): Game =
        val newdeck = filterOneCardOut(deck)
        val handlist = deck.grouped(newdeck.size/game.playerNumber.get).toList
        //for (i <- 0 to game.playerNumber.get - 1) game.players(i).addAllCards(handlist(i))
        game.copy(players = (game.players.zip(handlist).map { case (player, newCards) => player.copy(hand = newCards)}).toVector)

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

    def addPointsToPlayers(raw: Map[Player, Int]): Game =
        game.copy(players = (raw.map{ case (player, newPoints) => player.copy(points = player.points + newPoints)}).toVector)

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
        val h = sortingStrategy.execute(game.getCurrentPlayer.get)
        (1 to h.size).map(index => s"  $index".padTo(5, ' ')).mkString("|", "|", "|") + "\n" +
        h.map(card => s" $card ").mkString("|", "|", "|")

    def getCurrentPlayerName(): String = game.getCurrentPlayer.get.name

    def checkGameOver(): Boolean =
        val maxScorePoints = game.players.map(p => p -> p.points).toMap
        if(maxScorePoints.exists {case (_, point) => point >= game.maxScore.get})
            true
        else
            false

    def getPlayerNumber: Option[Int] = game.playerNumber

    def setPlayerNumber(number: Int): Game = game.copy(playerNumber = Some(number))

    def getkeepProcessRunning: Boolean = game.keepProcessRunning

    def setkeepProcessRunning(a: Boolean): Unit = game = game.copy(keepProcessRunning = a)

    def setStrategy(strategy:Strategy): Unit = this.sortingStrategy = strategy

    def executeStrategy(): Unit = sortingStrategy.execute(game.getCurrentPlayer.get)
