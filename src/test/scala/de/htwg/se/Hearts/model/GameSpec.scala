package de.htwg.se.Hearts.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.Hearts.controller._
import de.htwg.se.Hearts.model._
import de.htwg.se.Hearts.model.gameComponent.gameBase.Card
import de.htwg.se.Hearts.model.gameComponent.gameBase.Game
import de.htwg.se.Hearts.model.gameComponent.gameBase.Player
import de.htwg.se.Hearts.model.gameComponent._
import scala.xml.Node

class GameSpec extends AnyWordSpec with Matchers {
    "A Game" should {
        val card = Card(Rank.Ace,Suit.Clubs)
        val card1 = Card(Rank.Two,Suit.Clubs)
        val card2 = Card(Rank.Two,Suit.Diamonds)
        val p1 = Player("Alice", List(card1),points = 10)
        val p2 = Player("Bob", List(card2))
        val game = Game(players = Vector(p1),trickCards = List(card1),currentPlayerIndex = Some(0),currentWinnerIndex = Some(0), highestCard = Some(card1), maxScore = Some(1))

        "get the number of players" in {
            game.getPlayerNumber should be (None)
        }

        "get value of startWithHearts" in {
            game.getStartWithHearts should be (false)
        }

        "get value of keepProcessRunning" in {
            game.getKeepProcessRunning should be (true)
        }

        "get value of firstCard" in {
            game.getFirstCard should be (true)
        }

        "get vector of players" in {
            game.getPlayers should be (Vector(p1))
        }

        "get value of maxScore" in {
            game.getMaxScore should be (Some(1))
        }

        "get the current player" in {
            game.getCurrentPlayer should be (Some(p1))
        }

        "get value of trickCards" in {
            game.getTrickCards should be (List(card1))
        }

        "get highestCard" in {
            game.getHighestCard should be (Some(card1))
        }

        "get currentWinnerIndex" in {
            game.currentWinnerIndex should be (Some(0))
        }

        "get lastCardPlayed" in {
            game.errorOrlastCardPlayed should be (Left("No Card"))
        }

        "get currentPlayerIndex" in {
            game.currentPlayerIndex should be (Some(0))
        }
    }
    "eitherCardFromXML" should {

        "return Left('<label> fehlt') when the label node is missing" in {
            val parent: Node = <game></game>
            val game = Game()

            game.eitherCardFromXML(parent, "errorOrlastCardPlayed") shouldBe Left("errorOrlastCardPlayed fehlt")
        }

        "return Left('right without <card>') when <right> exists but has no <card>" in {
            val game = Game()
            val parent: Node =
                <game>
                <errorOrlastCardPlayed>
                    <right></right>
                </errorOrlastCardPlayed>
                </game>

            game.eitherCardFromXML(parent, "errorOrlastCardPlayed") shouldBe
                Left("right without <card>")
        }
        "return Left(Empty) when <Left> exists but is empty" in {
            val game = Game()
            val parent: Node =
                <game>
                <errorOrlastCardPlayed>
                    <left></left>
                </errorOrlastCardPlayed>
                </game>

            game.eitherCardFromXML(parent, "errorOrlastCardPlayed") shouldBe
                Left("No Card\n")
        }

        "return Right(Card(...)) when <right><card> is present" in {
            val game = Game()
            val parent: Node =
                <game>
                <errorOrlastCardPlayed>
                    <right>
                    <card>
                        <rank>Ace</rank>
                        <suit>Hearts</suit>
                    </card>
                    </right>
                </errorOrlastCardPlayed>
                </game>

            val res = game.eitherCardFromXML(parent, "errorOrlastCardPlayed")
            res.isRight shouldBe true
        }
    }

    "gameFromXML" should {

    "use default values when boolean tags are missing (covers reqBool case None => default)" in {
      val xml =
        <game>
          <players></players>
          <trickcards></trickcards>
          {}
        </game>

      val g = Game().gameFromXML(xml)

      g.getStartWithHearts shouldBe false
      g.getKeepProcessRunning shouldBe true
      g.getFirstCard shouldBe true
    }

        "use default values when boolean tags exist but are empty/whitespace (also covers reqBool None branch due to filter(_.nonEmpty))" in {
            val xml =
                <game>
                <players></players>
                <trickcards></trickcards>
                <startWithHearts>   </startWithHearts>
                <keepProcessRunning></keepProcessRunning>
                <firstCard> </firstCard>
                </game>

            val g = Game().gameFromXML(xml)

            g.getStartWithHearts shouldBe false
            g.getKeepProcessRunning shouldBe true
            g.getFirstCard shouldBe true
        }
    }
    
    "optCardToXml" should {

        "return an empty <card> element when Option is None (covers case None branch)" in {
            val game = Game()

            val xml = game.optCardToXml(None)

            xml.label shouldBe "card"
            xml.child.isEmpty shouldBe true
            xml.text shouldBe ""
        }
    }

    "playerFromXML" should {

        "parse name, hand cards, wonCards and points correctly" in {
            val game = Game()

            val xml =
                <player>
                <name>Alice</name>
                <hand>
                    <card><rank>Ace</rank><suit>Hearts</suit></card>
                    <card><rank>King</rank><suit>Spades</suit></card>
                </hand>
                <wonCards>
                    <card><rank>Two</rank><suit>Clubs</suit></card>
                </wonCards>
                <points>42</points>
                </player>

            val p = game.playerFromXML(xml)

            p.name shouldBe "Alice"
            p.hand.size shouldBe 2
            p.wonCards.size shouldBe 1
            p.points shouldBe 42
        }

        "return empty lists when hand/wonCards are missing" in {
            val game = Game()

            val xml =
                <player>
                <name>Bob</name>
                <points>0</points>
                </player>

            val p = game.playerFromXML(xml)

            p.name shouldBe "Bob"
            p.hand shouldBe Nil
            p.wonCards shouldBe Nil
            p.points shouldBe 0
        }
    }
}