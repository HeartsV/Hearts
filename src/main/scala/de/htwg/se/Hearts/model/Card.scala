package de.htwg.se.Hearts.model

import scala.math.Ordered

case class Card(rank: Rank, suit: Suit){
  override def toString: String = s"${rank.toString.padTo(2, ' ')}${suit.toString}"
  def compare(that: Card): Int = this.rank.compare(that.rank)
}



