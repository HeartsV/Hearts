package de.htwg.se.Hearts
import de.htwg.se.Hearts.model.Card
import de.htwg.se.Hearts.model.Suit
import de.htwg.se.Hearts.model.Rank

object Main {
  def main(args: Array[String]): Unit = {
    val Hand = List(Card(rank = Rank.Five,suit = Suit.Hearts),Card(rank = Rank.Six,suit = Suit.Spades))
    println("Please select card to play: ")
    println("|  1  |  2  |")
    for(element<-Hand)
        {
            printf("| %s ",element)
        }
    println("|")
  }
}