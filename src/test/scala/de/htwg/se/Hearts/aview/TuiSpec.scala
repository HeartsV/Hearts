package de.htwg.se.Hearts.aview
import de.htwg.se.Hearts.model.gameComponent.gameBase.*
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import _root_.de.htwg.se.Hearts.aview.Tui
import de.htwg.se.Hearts.controller.controllerComponent.controllerBase._
import de.htwg.se.Hearts.model.gameComponent._
import org.scalatest.OptionValues.convertOptionToValuable


class TuiSpec extends AnyWordSpec with Matchers:
	"A Tui" should {

        val card1 = Card(Rank.Two,Suit.Clubs)
        val card2 = Card(Rank.Two,Suit.Diamonds)
        val card3 = Card(Rank.Ace,Suit.Clubs)
        val card4 = Card(Rank.Ace,Suit.Diamonds)
        val card5 = Card(Rank.Jack,Suit.Hearts)
        val card6 = Card(Rank.Jack,Suit.Clubs)
        val card7 = Card(Rank.Ten,Suit.Clubs)
        val card8 = Card(Rank.Ten,Suit.Diamonds)
        val p1 = Player("Alice", List(Card(Rank.Two,Suit.Clubs)), points = 30)
        val p2 = Player("Dave", List(Card(Rank.Ace,Suit.Clubs)), points = 20)
        val p3 = Player("Charlie", List(Card(Rank.Jack,Suit.Clubs)), points = 20)
        val p4 = Player("David", List(Card(Rank.Ten,Suit.Clubs)), points = 10)
        val pl1 = Player("Alice",List(card1,card2),List(card5), points = 1)
        val pl2 = Player("Dave",List(card3,card4))
        val game = Game(players = Vector(p1, p2, p3, p4), playerNumber = Some(4), currentPlayerIndex = Some(0))
        val gameLCPRight = Game(players = Vector(p1, p2, p3, p4), playerNumber = Some(4), currentPlayerIndex = Some(0), lastCardPlayed = Right(card1))
        val gameLCPLeftError = Game(players = Vector(p1, p2, p3, p4), playerNumber = Some(4), currentPlayerIndex = Some(0), lastCardPlayed = Left("Cards sorted by rank"))
        val controllerLCPRight = Controller(gameLCPRight)
        val controllerLCPLeft = Controller(gameLCPLeftError)
        val gameFirstCard = Game(playerNumber = Some(2),players = Vector(pl1,pl2),currentPlayerIndex = Some(0))
        val gameWithTrick = Game(playerNumber = Some(2),players = Vector(pl1,pl2),firstCard = false, currentPlayerIndex = Some(0),
                                trickCards = List(card7), highestCard = Some(card7),currentWinnerIndex = Some(0))
        val gameController = Controller(game)
        val contollerTrick = Controller(gameWithTrick)
        val gameCo = Controller(gameFirstCard)
        val tui = Tui(gameController)
        val tuiFullTrick = Tui(contollerTrick)
        val tui2 = Tui(gameCo)
        val tuiRight = Tui(controllerLCPRight)
        val tuiLeft = Tui(controllerLCPLeft)

        "output the correct strings for played Cards" in {
            tuiFullTrick.trickToString should be ("| 10\u2663 |")
            tui.trickToString should be ("|")
        }

        "complete Trick String for three players" in{
            tuiFullTrick.completeTrickString should be ("| 10\u2663 |     |")
        }

        "get the handstring of current player" in{
            tui2.handToString should be ("|  1  |  2  |\n| 2 \u2666 | 2 \u2663 |")
        }

        "get the correct getMainScreenStateString" in {
            val expected ="Hearts" + "\n\n" +
                "Please enter:" + "\n" +
                "- n or new for a new Game" + "\n" +
                "- ru or rules for the rules" + "\n" +
                "- e or exit to end the program" + "\n"
                tui.getMainScreenStateString should be (expected)
        }

        "get the correct getRulesScreenStateString" in {
            val expected =
                "\n\n" +"Rules:" + "\n\n" +
                "Hearts is a trick-taking card game played with 3-4 players, each playing individually." + "\n" +
                "With 4 players, the game uses all 52 cards of a standard deck." + "\n" +
                "With 3 players, one card is removed and the remaining 51 cards are used." + "\n" +
                "Each suit contains cards from Two (lowest) to Ace (highest), giving 13 cards per suit." + "\n" +
                "There is no trump suit." + "\n\n\n" +
                "Objective:" + "\n\n" +
                "The goal of the game is to collect as few penalty points as possible." + "\n" +
                "You receive penalty points for taking tricks that contain any Heart cards or the Queen of Spades." + "\n" +
                "The game ends when a player reaches or exceeds the maximum score (the standard is 100 points)." + "\n" +
                "The player with the lowest total score is the winner." + "\n\n\n" +
                "Dealing:" + "\n\n" +
                "This rule applies only with 4 players!!" + "\n" +
                "Cards are dealt clockwise, one at a time, until each player has 13 cards." + "\n" +
                "Players may only see their own hands." + "\n" +
                "With 3 players, one card is removed from the deck, and the remaining 51 cards are dealt evenly." + "\n" +
                "There is no passing in a 3-player game." + "\n\n\n" +
                "Passing Cards:" + "\n\n" +
                "After all cards have been dealt (4-player game only), the passing phase begins." + "\n" +
                "Each player selects three cards from their hand and passes them face-down to another player." + "\n\n" +
                "The passing rotation works as follows:" + "\n" +
                "- 1st round: Pass to the player on your left" + "\n" +
                "- 2nd round: Pass to the player on your right" + "\n" +
                "- 3rd round: Pass to the player across from you" + "\n" +
                "- 4th round: No passing:" + "\n" +
                "- Then the cycle repeats for the rest of the game." + "\n\n\n" +
                "How the Game Is Played:" + "\n\n" +
                "The player holding the Two of Clubs leads the first trick." + "\n" +
                "All players must follow suit if possible. If they cannot, they may play another suit under certain conditions." + "\n" +
                "The player who plays the highest card of the led suit wins the trick and leads the next one." + "\n\n" +
                "There are restrictions when playing Hearts or the Queen of Spades:" + "\n" +
                "- In the first trick, you may not play a Heart or the Queen of Spades, even if you cannot follow suit." + "\n" +
                "- You cannot lead Hearts until Hearts have been broken." + "\n" +
                "- Hearts are broken when a player who cannot follow suit plays a Heart, or when the Queen of Spades has been played." + "\n" +
                "- Exception: A player with only Hearts may lead Hearts at any time." + "\n\n" +
                "A round ends after all 13 tricks have been played." + "\n\n\n" +
                "Scoring:" + "\n\n" +
                "Each Heart is worth 1 penalty point, and the Queen of Spades is worth 13 points." + "\n" +
                "There are 26 total penalty points in each round in a 4-player game." + "\n" +
                "It can differ in a 3-player game since one card is removed." + "\n\n" +
                "If a player captures all penalty cards (all Hearts and the Queen of Spades), they score 0 points," + "\n" +
                "and each opponent scores 26 points instead. This is called Shooting the Moon." + "\n\n" +
                "The game ends when a player reaches 100 points (or another agreed limit)." + "\n" +
                "The player with the lowest score wins." + "\n\n" +
                "While playing you can enter redo to redo last step and undo to undo last step" + "\n" +
                "Enter 'back' or 'b' to return to the main menu." + "\n"
            tui.getRulesScreenStateString should be (expected)
        }

        "get the correct getPlayerNumberStateString" in {
            tui.getPlayerNumberStateString should be ("please input a Number of Players between 3 and 4\n")
        }

        "get the correct getPlayerNamesStateString" in {
            tui.getPlayerNamesStateString should be ("please input the names of the 5. player\n")
        }

        "get the correct setMaxScoreStateString" in {
            tui.setMaxScoreStateString should be ("please enter the score required to win (between 1 and 400)\n")
        }

        "get the correct GameplayStateString" in {
            tui.getGameplayStateString should be ("Trick:\n|     |     |     |     |\nAlice please select card to play:\n|  1  |\n| 2 ♣ |\n")
        }

        "get the correct getShowScoreStateString" in {
            tui.getShowScoreStateString should be ("Scoreboard:\n1. David: 10\n2. Dave: 20\n2. Charlie: 20\n4. Alice: 30\nPress any button to continue\n")
        }

        "get the correct getGameOverStateString" in {
            tui.getGameOverStateString should be (
                "GAMEOVER\n\nScoreboard:\n1. David: 10\n2. Dave: 20\n2. Charlie: 20\n4. Alice: 30\n" +
                "Please enter:" + "\n" +
                "- n or new for a new Game" + "\n" +
                "- a or again for playing again" + "\n" +
                "- q or quit to go back to Mainmenu" + "\n" +
                "- e or exit to end the program" + "\n")
        }

        "get the correct gameplaystatestring in error and right case" in {
            tuiLeft.getGameplayStateString should include ("Cards sorted by rank" + "\n")
            tuiRight.getGameplayStateString should include ("2 ♣ played" + "\n")
        }

        "commandFor" should {

            "return NewCommand for MainScreenState + 'new'" in {
                val tui = new Tui(gameCo)
                val cmd = tui.commandFor("MainScreenState", "new")
                cmd.value shouldBe a [NewCommand]
            }

            "return NewCommand for MainScreenState + 'n'" in {
                val tui = new Tui(gameCo)
                val cmd = tui.commandFor("MainScreenState", "n")
                cmd.value shouldBe a [NewCommand]
            }

            "return RulesCommand for MainScreenState + 'ru'" in {
                val tui = new Tui(gameCo)
                val cmd = tui.commandFor("MainScreenState", "ru")
                cmd.value shouldBe a [RulesCommand]
            }

            "return ExitCommand for MainScreenState + 'e'" in {
                val tui = new Tui(gameCo)
                val cmd = tui.commandFor("MainScreenState", "e")
                cmd.value shouldBe a [ExitCommand]
            }

            "return None for MainScreenState + unknown input" in {
                val tui = new Tui(gameCo)
                tui.commandFor("MainScreenState", "xyz") shouldBe None
            }

            "return BackCommand for RulesScreenState + 'back'" in {
                val tui = new Tui(gameCo)
                val cmd = tui.commandFor("RulesScreenState", "back")
                cmd.value shouldBe a [BackCommand]
            }

            "return BackCommand for RulesScreenState + 'b'" in {
                val tui = new Tui(gameCo)
                val cmd = tui.commandFor("RulesScreenState", "b")
                cmd.value shouldBe a [BackCommand]
            }

            "return None for RulesScreenState + unknown input" in {
                val tui = new Tui(gameCo)
                tui.commandFor("RulesScreenState", "xyz") shouldBe None
            }

            "return UndoCommand for GetPlayerNumberState + 'undo'" in {
                val tui = new Tui(gameCo)
                val cmd = tui.commandFor("GetPlayerNumberState", "undo")
                cmd.value shouldBe a [UndoCommand]
            }

            "return RedoCommand for GetPlayerNumberState + 'redo'" in {
                val tui = new Tui(gameCo)
                val cmd = tui.commandFor("GetPlayerNumberState", "redo")
                cmd.value shouldBe a [RedoCommand]
            }

            "return SetPlayerNumberCommand for GetPlayerNumberState + number input" in {
                val tui = new Tui(gameCo)
                val cmd = tui.commandFor("GetPlayerNumberState", "4")
                cmd.value shouldBe a [SetPlayerNumberCommand]
            }

            "return UndoCommand for GetPlayerNamesState + 'undo'" in {
                val tui = new Tui(gameCo)
                val cmd = tui.commandFor("GetPlayerNamesState", "undo")
                cmd.value shouldBe a [UndoCommand]
            }

            "return RedoCommand for GetPlayerNamesState + 'redo'" in {
                val tui = new Tui(gameCo)
                val cmd = tui.commandFor("GetPlayerNamesState", "redo")
                cmd.value shouldBe a [RedoCommand]
            }

            "return AddPlayerCommand for GetPlayerNamesState + any name" in {
                val tui = new Tui(gameCo)
                val cmd = tui.commandFor("GetPlayerNamesState", "Alice")
                cmd.value shouldBe a [AddPlayerCommand]
            }

            "return UndoCommand for SetMaxScoreState + 'undo'" in {
                val tui = new Tui(gameCo)
                val cmd = tui.commandFor("SetMaxScoreState", "undo")
                cmd.value shouldBe a [UndoCommand]
            }

            "return RedoCommand for SetMaxScoreState + 'redo'" in {
                val tui = new Tui(gameCo)
                val cmd = tui.commandFor("SetMaxScoreState", "redo")
                cmd.value shouldBe a [RedoCommand]
            }

            "return SetMaxScoreCommand for SetMaxScoreState + score input" in {
                val tui = new Tui(gameCo)
                val cmd = tui.commandFor("SetMaxScoreState", "100")
                cmd.value shouldBe a [SetMaxScoreCommand]
            }

            "return SetSortingSuitCommand for GamePlayState + 's'" in {
                val tui = new Tui(gameCo)
                val cmd = tui.commandFor("GamePlayState", "s")
                cmd.value shouldBe a [SetSortingSuitCommand]
            }

            "return SetSortingRankCommand for GamePlayState + 'r'" in {
                val tui = new Tui(gameCo)
                val cmd = tui.commandFor("GamePlayState", "r")
                cmd.value shouldBe a [SetSortingRankCommand]
            }

            "return UndoCommand for GamePlayState + 'undo'" in {
                val tui = new Tui(gameCo)
                val cmd = tui.commandFor("GamePlayState", "undo")
                cmd.value shouldBe a [UndoCommand]
            }

            "return RedoCommand for GamePlayState + 'redo'" in {
                val tui = new Tui(gameCo)
                val cmd = tui.commandFor("GamePlayState", "redo")
                cmd.value shouldBe a [RedoCommand]
            }

            "return PlayCardCommand for GamePlayState + card index input" in {
                val tui = new Tui(gameCo)
                val cmd = tui.commandFor("GamePlayState", "2")
                cmd.value shouldBe a [PlayCardCommand]
            }

            "return UndoCommand for ShowScoreState + 'undo'" in {
                val tui = new Tui(gameCo)
                val cmd = tui.commandFor("ShowScoreState", "undo")
                cmd.value shouldBe a [UndoCommand]
            }

            "return RedoCommand for ShowScoreState + 'redo'" in {
                val tui = new Tui(gameCo)
                val cmd = tui.commandFor("ShowScoreState", "redo")
                cmd.value shouldBe a [RedoCommand]
            }

            "return ContinueCommand for ShowScoreState + any input" in {
                val tui = new Tui(gameCo)
                val cmd = tui.commandFor("ShowScoreState", "whatever")
                cmd.value shouldBe a [ContinueCommand]
            }

            "return NewCommand for GameOverState + 'new'" in {
                val tui = new Tui(gameCo)
                val cmd = tui.commandFor("GameOverState", "new")
                cmd.value shouldBe a [NewCommand]
            }

            "return NewCommand for GameOverState + 'n'" in {
                val tui = new Tui(gameCo)
                val cmd = tui.commandFor("GameOverState", "n")
                cmd.value shouldBe a [NewCommand]
            }

            "return AgainCommand for GameOverState + 'a'" in {
                val tui = new Tui(gameCo)
                val cmd = tui.commandFor("GameOverState", "a")
                cmd.value shouldBe a [AgainCommand]
            }

            "return QuitCommand for GameOverState + 'q'" in {
                val tui = new Tui(gameCo)
                val cmd = tui.commandFor("GameOverState", "q")
                cmd.value shouldBe a [QuitCommand]
            }

            "return ExitCommand for GameOverState + 'e'" in {
                val tui = new Tui(gameCo)
                val cmd = tui.commandFor("GameOverState", "e")
                cmd.value shouldBe a [ExitCommand]
            }

            "return UndoCommand for GameOverState + 'undo'" in {
                val tui = new Tui(gameCo)
                val cmd = tui.commandFor("GameOverState", "undo")
                cmd.value shouldBe a [UndoCommand]
            }

            "return RedoCommand for GameOverState + 'redo'" in {
                val tui = new Tui(gameCo)
                val cmd = tui.commandFor("GameOverState", "redo")
                cmd.value shouldBe a [RedoCommand]
            }

            "return None for GameOverState + unknown input" in {
                val tui = new Tui(gameCo)
                tui.commandFor("GameOverState", "xyz") shouldBe None
            }

            "return None for unknown state" in {
                val tui = new Tui(gameCo)
                tui.commandFor("SomeWeirdState", "new") shouldBe None
            }
        }

    }
