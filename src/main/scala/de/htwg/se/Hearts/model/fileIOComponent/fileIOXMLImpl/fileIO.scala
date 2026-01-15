package de.htwg.se.Hearts.model.fileIOComponent.fileXMLImpl


import scala.xml._
import de.htwg.se.Hearts.model.fileIOComponent.fileIOInterface
import de.htwg.se.Hearts.model.gameComponent.GameInterface
import de.htwg.se.Hearts.controller.controllerComponent.controllerBase._
import de.htwg.se.Hearts.controller.controllerComponent.controllerBase.State
import com.google.inject.Injector
import com.google.inject.Guice
import de.htwg.se.Hearts.HeartsModule
import de.htwg.se.Hearts.controller.controllerComponent.ControllerInterface

class fileIO extends fileIOInterface:
	def load(controller: Controller): (GameInterface, State) =
		val xml = scala.xml.XML.loadFile("hearts.xml")

		val game: GameInterface = gameFromXml(xml \ "game")   // dein Game-Code
		val state = stateFromXml(xml, controller)

		(game, state)

	def stateFromXml(node: scala.xml.Node, controller: Controller): State =
		val stateString = (node \ "state").text.trim
		stateString match
			case "MainScreenState"       => new MainScreenState(controller)
			case "RulesScreenState"      => new RulesScreenState(controller)
			case "GetPlayerNumberState"  => new GetPlayerNumberState(controller)
			case "GetPlayerNamesState"   => new GetPlayerNamesState(controller)
			case "SetMaxScoreState"      => new SetMaxScoreState(controller)
			case "GamePlayState"         => new GamePlayState(controller)
			case "ShowScoreState"        => new ShowScoreState(controller)
			case "GameOverState"         => new GameOverState(controller)
			case other =>
				throw new RuntimeException(s"Unknown state in XML: $other")


	def save(game: GameInterface, state: State): Unit = saveString(game, state)

	def gameAndStateToXML(game: GameInterface, state: State): Elem =
		<gameSave>
			<state>{state.getStateString}</state>
			<game>
				<playNumber>{ game.getPlayerNumber }</playNumber>
				<startWithHearts>{ game.getStartWithHearts}</startWithHearts>
				<keepProcessRunning>{game.getKeepProcessRunning}</keepProcessRunning>
				<firstCard>{game.getFirstCard}</firstCard>
				<players>{game.getPlayers.map(p => p.playerToXML)}</players>
				<maxScore>{game.getMaxScore}</maxScore>
				<currentPlayerIndex>{game.getCurrentPlayerIndex}</currentPlayerIndex>
				<trickCards>{game.getTrickCards.map(tc => tc.cardToXML)}</trickCards>
				<highestCard>{game.getHighestCard}</highestCard>
				<currentWinnerIndex>{game.getCurrentWinnerIndex}</currentWinnerIndex>
				<lastCardPlayed>{game.getLastCardPlayed}</lastCardPlayed>
			</game>
		</gameSave>

	//def saveXML(game: GameInterface, state: State): Unit = scala.xml.XML.save("hearts.xml", gameAndStateToXML(game, state))

	def saveString(game: GameInterface, state: State): Unit = {
	import java.io._
	val pw = new PrintWriter(new File("hearts.xml"))
	val prettyPrinter = new PrettyPrinter(120, 4)
	val xml = prettyPrinter.format(gameAndStateToXML(game, state))
	pw.write(xml)
	pw.close
	}
