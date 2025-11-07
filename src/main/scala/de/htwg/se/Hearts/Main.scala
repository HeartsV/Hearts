package de.htwg.se.Hearts
import de.htwg.se.Hearts.model.*
import de.htwg.se.Hearts.controller.*
object Main {
  def main(args: Array[String]): Unit = {
    Game.playerNumber = Some(4)
    val p1 = Player("Alice")
    val p2 = Player("Dave")
    val p3 = Player("Charlie")
    val p4 = Player("David")
    p1.hand +=(Card(Rank.Two,Suit.Clubs))
    p2.hand +=(Card(Rank.Ace,Suit.Clubs))
    p3.hand +=(Card(Rank.Jack,Suit.Clubs))
    p4.hand +=(Card(Rank.Ten,Suit.Clubs))
    Game.addPlayer(p1)
    Game.addPlayer(p2)
    Game.addPlayer(p3)
    Game.addPlayer(p4)
    val gameCo = Controller()
    gameCo.rungame()
  }
}