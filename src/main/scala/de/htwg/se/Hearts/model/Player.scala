package de.htwg.se.Hearts.model

import scala.collection.mutable.ListBuffer

class Player(val name: String) {

    val hand: ListBuffer[Card] = ListBuffer()
    val wonCards:ListBuffer[Card] = ListBuffer()

    def playCard(index : Int) : Boolean = {
        if(hand.size > index && Game.trick.addCard(hand(index)))
            hand.remove(index)
            true
        else
            false
    }

    def handToString(): String =
        var handString = "|"
        for(element<-hand)
            handString += (" " + element.toString +  " ")
            handString += "|"
        handString
}
