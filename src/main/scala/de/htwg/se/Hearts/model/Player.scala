package de.htwg.se.Hearts.model

import scala.collection.mutable.ListBuffer

class Player(val name: String) {

    val hand: ListBuffer[Card] = ListBuffer()
    val wonCards:ListBuffer[Card] = ListBuffer()

    def removeCard(index : Int) : Boolean = {
        hand.remove(index-1)
        true
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
