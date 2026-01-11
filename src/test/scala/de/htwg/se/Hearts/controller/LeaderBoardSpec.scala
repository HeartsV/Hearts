package de.htwg.se.Hearts.controller

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

import de.htwg.se.Hearts.controller.leaderBoardComponent.leaderBoardBase.LeaderBoard

class LeaderBoardSpec extends AnyWordSpec with Matchers:
	"A LeaderBoard" should {
		val lead = LeaderBoard()
		 "rankPlayers correctly" in {
            lead.rankPlayers(List(("Alice", 1), ("Dave", 0), ("Charlie", 0))) should be (List((1, "Dave", 0), (1, "Charlie", 0), (3, "Alice", 1)))
        }
	}
