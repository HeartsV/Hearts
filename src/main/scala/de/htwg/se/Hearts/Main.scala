package de.htwg.se.Hearts
import de.htwg.se.Hearts.model.*
import de.htwg.se.Hearts.controller.*
import de.htwg.se.Hearts.aview.*
object Main {
  def main(args: Array[String]): Unit = {
    val p1 = Player("Alice")
    val p2 = Player("Dave")
    val p3 = Player("Charlie")
    val p4 = Player("David")
    p1.hand ++=List((Card(Rank.Two,Suit.Clubs)),(Card(Rank.Two,Suit.Diamonds)))
    p2.hand ++=List((Card(Rank.Ace,Suit.Clubs)),(Card(Rank.Ace,Suit.Diamonds)))
    p3.hand ++=List((Card(Rank.Jack,Suit.Clubs)),(Card(Rank.Jack,Suit.Diamonds)))
    p4.hand ++=List((Card(Rank.Ten,Suit.Clubs)),(Card(Rank.Ten,Suit.Diamonds)))
    val game = Game()
    game.addPlayer(p1)
    game.addPlayer(p2)
    game.addPlayer(p3)
    game.addPlayer(p4)
    val gameCo = Controller(game)
    val gameTui = Tui(gameCo)
    gameCo.add(gameTui)
    gameTui.runGame()
  }
}