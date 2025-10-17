package de.htwg.se.Hearts
import de.htwg.se.Hearts.model.Card
import de.htwg.se.Hearts.model.Suit
import de.htwg.se.Hearts.model.Rank

object Main {
  def main(args: Array[String]): Unit = {
    val Hand = List(Card(rank = Rank.Ten,suit = Suit.Hearts),Card(rank = Rank.Six,suit = Suit.Spades),Card(rank = Rank.King,suit = Suit.Clubs))
    println("Please select card to play: ")
    for(element <- 1 until Hand.size + 1){
        printf("|  %d".padTo(7,' '),element)
    }
    println("|")
    for(element<-Hand)
    {
        printf("| %s ",element)
    }
    println("|")
  }
}