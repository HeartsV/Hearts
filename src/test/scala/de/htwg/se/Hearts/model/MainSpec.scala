package de.htwg.se.Hearts.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class MainSpec extends AnyWordSpec with Matchers {

  "Addition" should {

    "work" in {
        val a = 2 + 2
        a should be (4)
    }
  }
}