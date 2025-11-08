package de.htwg.se.Hearts.model

import scala.collection.mutable.ListBuffer

class Player(val name: String) {

    val hand: ListBuffer[Card] = ListBuffer()
    val wonCards:ListBuffer[Card] = ListBuffer()

    def playCard(index : Int) : Boolean = {
        if(hand.size > index-1 && Game.trick.addCard(hand(index-1)))
            hand.remove(index-1)
            true
        else
            false
    }

    def handToString(): String =
        var handString = "|"

        for(i <- 1 to hand.size )
            handString += ("  " + i.toString().padTo(2, ' ') +  " ")
            handString += "|"
        handString += "\n|"

        for(element<-hand)
            handString += (" " + element.toString +  " ")
            handString += "|"
        handString
}
