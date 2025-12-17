package de.htwg.se.Hearts.util

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class ObservableSpec extends AnyWordSpec with Matchers  {
    "A observalble" should {
        "be able to add subscribers" in {
            val observer = ObserverObject()
            val observalble = ObservalbleObject()
            observalble.add(observer)
            observalble.subscribers should be (Vector(observer))
        }

        "be able to remove subscribers" in{
            val observer = ObserverObject()
            val observalble = ObservalbleObject()
            observalble.add(observer)
            observalble.remove(observer)
            observalble.subscribers should be (Vector())
        }

        "be able to notify observers" in {
            val observer = ObserverObject()
            val observalble = ObservalbleObject()
            observalble.add(observer)
            observalble.notifyObservers
            observer.localCheck should be (1)
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
    def update: Unit =  localCheck += 1
}

