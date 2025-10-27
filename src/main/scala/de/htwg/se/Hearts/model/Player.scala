package de.htwg.se.Hearts.model

import scala.collection.mutable.ListBuffer

class Player(val name: String,val hand: ListBuffer[Card],val wonCards:ListBuffer[Card]) {

    def playCard(index : Int) : Boolean = {
        if(hand.size > index && Game.trick.addCard(hand(index),this))
            hand.remove(index)
            true
        else
            false
    }


}
