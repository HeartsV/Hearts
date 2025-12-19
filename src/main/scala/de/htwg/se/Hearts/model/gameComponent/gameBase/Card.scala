package de.htwg.se.Hearts.model.gameComponent.gameBase

import scala.math.Ordered
import de.htwg.se.Hearts.model.gameComponent.CardInterface

case class Card(rank: Rank, suit: Suit) extends CardInterface:
    override def toString: String = s"${rank.toString.padTo(2, ' ')}${suit.toString}"
    def compare(that: Card): Int = this.rank.compare(that.rank)
    def pngName: String = s"${this.rank.toString}_of_${this.suit.fileName}.png"
    def getRank: Rank = rank
    def getSuit: Suit = suit




