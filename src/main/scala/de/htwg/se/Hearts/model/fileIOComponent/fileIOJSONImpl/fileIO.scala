package de.htwg.se.Hearts.model.fileIOComponent.fileIOJSONImpl

import de.htwg.se.Hearts.model.fileIOComponent.FileIOInterface
import de.htwg.se.Hearts.model.gameComponent.GameInterface
import de.htwg.se.Hearts.controller.controllerComponent.controllerBase._
import play.api.libs.json._
import scala.io.Source
import java.io._

class FileIO extends FileIOInterface:
	def load(controller: Controller): GameInterface =
		val source = Source.fromFile("hearts.json").getLines().mkString
		val json: JsValue = Json.parse(source)
		val state = (json \ "state").get
		controller.state.stateFromString(state.as[String], controller)
		val gameJson = (json \ "game").get
		gameJson.validate[GameInterface] match
			case JsSuccess(game, _) => game
			case e: JsError => throw new RuntimeException("Failed to parse hearts.json: " + JsError.toJson(e).toString())
	def save(game: GameInterface, state: State): Unit =
		val pw = new PrintWriter(new File("hearts.json"))
		pw.write(Json.prettyPrint(gameAndStateToJSON(game, state)))
		pw.close

	def saveExists: Boolean =
		val file = new File("hearts.json")
		file.exists() && file.length() > 0



	def gameAndStateToJSON(game: GameInterface, state: State) = {
		Json.obj(
			"state" -> state.getStateString,
			"game"  -> Json.toJson(game)
		)
	}
