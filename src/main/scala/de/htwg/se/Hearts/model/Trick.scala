package de.htwg.se.Hearts.model

class Trick(val cards: List[Card],val validSuit: Option[Suit],val currentWinner: Option[Player], val firstPlayer: Option[Player]) { // mit options fals wir erst trick erstellen
    def addCard(newCard: Card): Boolean = {
        true
    }

    def checkValid(newCard: Card): Boolean = {
        true
    }

    def updateCurrentWinner(newWinner: Player): Boolean = {
        true
    }

    def clearTrick(): Boolean ={
        true
    }
}
