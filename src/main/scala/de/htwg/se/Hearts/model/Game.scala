package de.htwg.se.Hearts.model

import scala.compiletime.ops.boolean
import org.scalactic.Bool
import scala.collection.mutable.ListBuffer


object  Game {
  var startWithHearts: Boolean = false
  var firstCard: Boolean = true
  var playerNumber: Option[Int] = None
  val players: ListBuffer[Player] = ListBuffer.empty[Player]
  val trick: ListBuffer[Card] = ListBuffer()
  var highestCard: Option[Card] = None
  var currentWinner: Option[Player] = None
  var firstPlayer: Option[Player] = None

  def setPlayerNumber(number: Int): Boolean = {
    if (number > 1 && number < 5){
        playerNumber = Some(number)
        true
    } else {
        false
    }
  }

  def addPlayer(player: Player): Boolean ={
    players.addOne(player)
    true
  }

  def addCard(newCard: Card, currentPlayer: Player): Boolean = {
      if(Game.firstCard == true)
          if(newCard  == (Card(Rank.Two,Suit.Spades)))
              trick += newCard
              firstPlayer = Some(currentPlayer)
              updateCurrentWinner(newCard,currentPlayer)
              Game.firstCard = false
              true
          else
              false
      else if(!(trick == ListBuffer()))
          if(highestCard.exists(card => card.suit == newCard.suit) || !highestCard.exists(card => currentPlayer.hand.exists(_.suit == card.suit)))
              trick += newCard
              updateCurrentWinner(newCard,currentPlayer)
              clearTrick()
              if(newCard.suit == Suit.Hearts)
                  Game.startWithHearts = true
              true
          else
              false
      else
          if (newCard.suit == Suit.Hearts && Game.startWithHearts == false)
              false
          else
              trick += newCard
              firstPlayer = Some(currentPlayer)
              updateCurrentWinner(newCard,currentPlayer)
              true

  }

  def updateCurrentWinner(playedCard: Card, currentPlayer: Player): Boolean = {
      if(highestCard == None || highestCard.exists(card => card.suit == playedCard.suit && playedCard.rank.compare(card.rank) > 0))
          highestCard = Some(playedCard)
          currentWinner = Some(currentPlayer)
          true
      else
          false

  }

  def clearTrick(): Boolean ={
      if(Game.playerNumber.exists(pnumber => trick.size == pnumber))
          currentWinner.foreach(_.wonCards ++= trick)
          trick.clear()
          highestCard= None
          currentWinner= None
          firstPlayer = None
          true
      else
          false
  }

}
