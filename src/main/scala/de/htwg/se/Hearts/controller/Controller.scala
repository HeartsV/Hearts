package de.htwg.se.Hearts.controller

import de.htwg.se.Hearts.model._

class Controller(var game: Game) extends Observable():

    var state:State = MainScreenState(this)
    var sortingStrategy:Strategy = SortByRankStrategy()

    def processInput(input: String): Game =
        /*if(state.processInput(input))
            notifyObservers
            true
        else
            notifyObservers
            false*/
        notifyObservers
        state.processInput(input)

    def changeState(newState:State): Unit = state = newState

    /*def cardAllowed(index: Int): Boolean =
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
        */

    def cardAllowed(index: Int): Boolean =
        game.getCurrentPlayer.exists { player =>
            val sortedHand = sortingStrategy.execute(player)
            val realIndex  = index - 1

            if realIndex < 0 || realIndex >= sortedHand.size then
                false
            else
                val card = sortedHand(realIndex)
                ChainOfResponsibility.validateMove(game, player, card)
        }

    def playCard(index: Int): Game =
        if !cardAllowed(index) then game
        else
            val currentPlayerIndex = game.getCurrentPlayerIndex
            val currentPlayer = game.getCurrentPlayer.get
            val sortedHand = sortingStrategy.execute(currentPlayer)
            val realIndex = index - 1

            if realIndex < 0 || realIndex >= sortedHand.size then game
            else
                val card = sortedHand(realIndex)
                val updatedPlayer = currentPlayer.removeCard(card)
                var g: Game = game.updatePlayer(currentPlayerIndex, updatedPlayer)

                // 4. Karte in den Stich legen
                g = g.addCard(card)

                // 5. Gewinner / höchste Karte im aktuellen Stich aktualisieren
                g = updateCurrentWinner(updatedPlayer, card)

                // 6. firstCard-Flag ausschalten (nach der ersten jemals gespielten Karte)
                if g.firstCard then g = g.setFirstCard(false)

                // 7. Hearts "brechen", falls gerade zum ersten Mal Herz ausgespielt wurde
                if card.suit == Suit.Hearts && !g.startWithHearts then g = g.setStartWithHearts(true)

                // 8. Wenn Stich voll ist (jeder Spieler hat genau eine Karte gelegt)
                if g.trickCards.size == g.players.size then
                    val winner = g.getCurrentWinner.get // sollte gesetzt sein
                    val winnerIndex = g.players.indexOf(winner)

                    val winnerWithCards = winner.addWonCards(g.trickCards)

                    // Gewinner updaten und Stich leeren
                    g = g.updatePlayer(winnerIndex, winnerWithCards).clearTrick

                    // Nach einem vollen Stich: aktueller Spieler ist der Gewinner
                    // (falls du das so modelliert hast)
                    // Wenn du dafür eine Game-Methode hast, kannst du die nutzen:
                    // g = g.setCurrentPlayerIndex(winnerIndex)

                // 9. Nächsten Spieler drannehmen
                g = updateCurrentPlayer()

                // 10. neuen Game-State im Controller speichern
                game = g
                game

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
        game.copy(players = (raw.map{ case (player, newPoints) => player.addPoints(newPoints)}).toVector)

    def getPlayersWithPoints(): List[(String, Int)] =
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

