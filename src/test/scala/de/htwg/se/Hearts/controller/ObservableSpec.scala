package de.htwg.se.Hearts.controller

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class ObservableSpec extends AnyWordSpec with Matchers  {
    "A observalble" should {
        "be able to add,remove and update to observer" in {
            val observer = ObserverObject()
            val observalble = ObservalbleObject()
            observalble.add(observer)
            observalble.notifyObservers
            observer.localCheck should be (1)
            observalble.remove(observer)
            observalble.notifyObservers
            observer.localCheck should be (1)
            observalble.add(observer)
            observalble.notifyObservers
            observer.localCheck should be (2)

        }
    }
}

class ObservalbleObject extends Observable() {
    def triggerupdate(): Unit = {
        notifyObservers
    }

}

class ObserverObject extends Observer(){
    var localCheck = 0
    def update(): Unit =  localCheck += 1
}

