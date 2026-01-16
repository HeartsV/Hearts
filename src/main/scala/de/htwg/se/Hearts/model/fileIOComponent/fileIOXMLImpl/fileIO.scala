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
import de.htwg.se.Hearts.model.gameComponent.gameBase.Game

class fileIO extends fileIOInterface:
	def load(controller: Controller): GameInterface =
		val xml = scala.xml.XML.loadFile("hearts.xml")
		controller.state.stateFromXml((xml \\ "state").head, controller)
		controller.game.gameFromXML((xml \\ "game").head)

	def save(game: GameInterface, state: State): Unit = saveString(game, state)

	def gameAndStateToXML(game: GameInterface, state: State): Elem =
		<gameSave>
			<state>{state.getStateString}</state>
			<game>
				<playNumber>{game.getPlayerNumber.getOrElse("") }</playNumber>
				<startWithHearts>{ game.getStartWithHearts}</startWithHearts>
				<keepProcessRunning>{game.getKeepProcessRunning}</keepProcessRunning>
				<firstCard>{game.getFirstCard}</firstCard>
				<players>{game.getPlayers.map(p => p.playerToXML)}</players>
				<maxScore>{game.getMaxScore.getOrElse("")}</maxScore>
				<currentPlayerIndex>{game.getCurrentPlayerIndex.getOrElse("")}</currentPlayerIndex>
				<trickCards>{game.getTrickCards.map(tc => tc.cardToXML)}</trickCards>
				<highestCard>{game.getHighestCard}</highestCard>
				<currentWinnerIndex>{game.getCurrentWinnerIndex.getOrElse("")}</currentWinnerIndex>
				<lastCardPlayed>{game.getLastCardPlayed}</lastCardPlayed>
			</game>
		</gameSave>

	//def saveXML(game: GameInterface, state: State): Unit = scala.xml.XML.save("hearts.xml", gameAndStateToXML(game, state))

	def saveString(game: GameInterface, state: State): Unit =
		import java.io._
		val pw = new PrintWriter(new File("hearts.xml"))
		val prettyPrinter = new PrettyPrinter(120, 4)
		val xml = prettyPrinter.format(gameAndStateToXML(game, state))
		pw.write(xml)
		pw.close

