package de.htwg.se.Hearts.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import play.api.libs.json._

import de.htwg.se.Hearts.model.gameComponent._
import de.htwg.se.Hearts.model.gameComponent.gameBase.{Card, Player, Game}
import de.htwg.se.Hearts.model.gameComponent.CardInterface.given
import de.htwg.se.Hearts.model.gameComponent.PlayerInterface.given
import de.htwg.se.Hearts.model.gameComponent.GameInterface.given
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.BeforeAndAfterEach
import java.io.{File, PrintWriter}
import scala.io.Source
import scala.xml.XML
import de.htwg.se.Hearts.controller.controllerComponent.controllerBase.{Controller, State}

class FileIOSpec extends AnyWordSpec with Matchers{

	"A CardInterface JSON format" should {

		"write CardInterface to JSON with rank/suit strings" in {
			val c: CardInterface = Card(Rank.Two, Suit.Clubs)

			val js = Json.toJson(c)

			(js \ "rank").as[String] shouldBe "Two"
			(js \ "suit").as[String] shouldBe "Clubs"
		}

		"read CardInterface from JSON into a Card" in {
			val js = Json.obj(
				"rank" -> "Ace",
				"suit" -> "Spades"
			)

			val parsed = js.validate[CardInterface]
			parsed.isSuccess shouldBe true

			parsed.get.asInstanceOf[Card] shouldBe Card(Rank.Ace, Suit.Spades)
		}
	}

	"A Either[String, CardInterface] JSON format" should {

		"write Left(msg) as { left: msg }" in {
			val e: Either[String, CardInterface] = Left("No game saved!")

			val js = Json.toJson(e)

			(js \ "left").as[String] shouldBe "No game saved!"
			(js \ "right").toOption shouldBe None
		}

		"write Right(card) as { right: {rank,suit} }" in {
			val e: Either[String, CardInterface] = Right(Card(Rank.Queen, Suit.Hearts))

			val js = Json.toJson(e)

			val right = (js \ "right").get
			(right \ "rank").as[String] shouldBe "Queen"
			(right \ "suit").as[String] shouldBe "Hearts"
		}

		"read { left: msg } back into Left(msg)" in {
			val js = Json.obj("left" -> "No Card")

			js.validate[Either[String, CardInterface]].get shouldBe Left("No Card")
		}

		"read { right: card } back into Right(card)" in {
			val js = Json.obj(
				"right" -> Json.obj(
				"rank" -> "Ten",
				"suit" -> "Diamonds"
				)
			)

			val parsed = js.validate[Either[String, CardInterface]].get
			parsed match {
				case Right(card: Card) => card shouldBe Card(Rank.Ten, Suit.Diamonds)
				case other => fail(s"Expected Right(Card), got: $other")
			}
		}

		/*"return Left(msg) when 'right' is missing but 'left' exists" in {
			val js = Json.obj("left" -> "No game saved!\n")

			js.validate[Either[String, CardInterface]].get shouldBe Left("No game saved!\n")
		}*/

		"return Left('No Card') when neither 'right' nor 'left' exists" in {
			val js = Json.obj()

			js.validate[Either[String, CardInterface]].get shouldBe Left("No Card")
		}
	}

	"A PlayerInterface JSON format" should {

		"roundtrip PlayerInterface (write then read)" in {
		val hand: List[CardInterface] = List(Card(Rank.Two, Suit.Clubs))
		val won: List[CardInterface]  = List(Card(Rank.Ace, Suit.Spades))

		val p: PlayerInterface = Player("Alice", hand, won, 7)

		val js = Json.toJson(p)
		val parsed = js.validate[PlayerInterface]

		parsed.isSuccess shouldBe true
		parsed.get.asInstanceOf[Player] shouldBe Player("Alice", hand, won, 7)
		}
	}

	"A GameInterface JSON format" should {

    "roundtrip GameInterface (write then read)" in {
		val p1: PlayerInterface = Player(
			name = "Alice",
			hand = List(Card(Rank.Two, Suit.Clubs)),
			wonCards = Nil,
			points = 0
		)

		val g: GameInterface = Game(
			playerNumber = Some(4),
			startWithHearts = false,
			keepProcessRunning = true,
			firstCard = true,
			players = Vector(p1),
			maxScore = Some(100),
			currentPlayerIndex = Some(0),
			trickCards = List(Card(Rank.Three, Suit.Clubs)),
			highestCard = Some(Card(Rank.Three, Suit.Clubs)),
			currentWinnerIndex = Some(0),
			errorOrlastCardPlayed = Left("No Card")
		)
		val js = Json.toJson(g)
		(js \ "errorOrlastCardPlayed").toOption.isDefined shouldBe true

		val parsed = js.validate[GameInterface]
		parsed.isSuccess shouldBe true
		parsed.get.asInstanceOf[Game] shouldBe g.asInstanceOf[Game]
		}
  }
}



class FileIOJsonSpec extends AnyWordSpec with Matchers with BeforeAndAfterEach {

	private val saveFile = new File("hearts.json")
	private var backup: Option[String] = None
	private  def sampleGame(last: Either[String, CardInterface] = Left("No Card")): GameInterface = {
		val p: PlayerInterface =
		Player("Alice", hand = List(Card(Rank.Two, Suit.Clubs)), wonCards = Nil, points = 0)

		Game(
		playerNumber = Some(4),
		startWithHearts = false,
		keepProcessRunning = true,
		firstCard = true,
		players = Vector(p),
		maxScore = Some(100),
		currentPlayerIndex = Some(0),
		trickCards = List(Card(Rank.Three, Suit.Clubs)),
		highestCard = Some(Card(Rank.Three, Suit.Clubs)),
		currentWinnerIndex = Some(0),
		errorOrlastCardPlayed = last
		)
	}
	override def beforeEach(): Unit = {
		if (saveFile.exists()) {
		backup = Some(Source.fromFile(saveFile).getLines().mkString("\n"))
		saveFile.delete()
		} else backup = None
	}

	override def afterEach(): Unit = {
		if (saveFile.exists()) saveFile.delete()
		backup.foreach { content =>
		val pw = new PrintWriter(saveFile)
		try pw.write(content) finally pw.close()
		}
	}

	private object DummyState extends State(controller = Controller(game = Game())) {
		override def getStateString: String = "GamePlayState"
	}

	"JSON FileIO" should {
		import fileIOComponent.fileIOJSONImpl._
		"saveExists be false when hearts.json does not exist" in {
		val io = new FileIO
		io.saveExists shouldBe false
		}

		"save creates hearts.json and saveExists becomes true" in {
		val io = new FileIO
		io.save(sampleGame(), DummyState)

		io.saveExists shouldBe true
		saveFile.exists() shouldBe true
		saveFile.length() should be > 0L
		}

		"save writes JSON with state + game" in {
		val io = new FileIO
		io.save(sampleGame(), DummyState)

		val raw = Source.fromFile(saveFile).getLines().mkString
		val js = Json.parse(raw)

		(js \ "state").as[String] shouldBe "GamePlayState"
		(js \ "game").toOption.isDefined shouldBe true
		}

		"load returns the saved game and sets controller state from JSON" in {
		val io = new FileIO
		val g = sampleGame(Left("No game saved!\n"))
		io.save(g, DummyState)

		val controller = new Controller(Game())
		controller.passStateString should not be "GamePlayState"

		val loaded = io.load(controller)
		loaded.asInstanceOf[Game] shouldBe g.asInstanceOf[Game]

		controller.passStateString shouldBe "GamePlayState"
		}

		"gameAndStateToJSON returns JsObject with state and game fields" in {
		val io = new FileIO
		val js = io.gameAndStateToJSON(sampleGame(), DummyState)

		(js \ "state").as[String] shouldBe "GamePlayState"
		(js \ "game").toOption.isDefined shouldBe true
		}
	}
}

class FileIOXmlSpec extends AnyWordSpec with Matchers with BeforeAndAfterEach {

	private val saveFile = new File("hearts.xml")
	private var backup: Option[String] = None

	override def beforeEach(): Unit = {
		// Backup existing hearts.xml (if present)
		if (saveFile.exists()) {
		backup = Some(Source.fromFile(saveFile).getLines().mkString("\n"))
		saveFile.delete()
		} else backup = None
	}

	override def afterEach(): Unit = {
		// Cleanup + restore previous file
		if (saveFile.exists()) saveFile.delete()
		backup.foreach { content =>
		val pw = new PrintWriter(saveFile)
		try pw.write(content) finally pw.close()
		}
	}

	private def sampleGame(last: Either[String, CardInterface] = Left("No Card")): GameInterface = {
		val p: PlayerInterface =
		Player("Alice", hand = List(Card(Rank.Two, Suit.Clubs)), wonCards = Nil, points = 0)

		Game(
		playerNumber = Some(4),
		startWithHearts = false,
		keepProcessRunning = true,
		firstCard = true,
		players = Vector(p),
		maxScore = Some(100),
		currentPlayerIndex = Some(0),
		trickCards = List(Card(Rank.Three, Suit.Clubs)),
		highestCard = Some(Card(Rank.Three, Suit.Clubs)),
		currentWinnerIndex = Some(0),
		errorOrlastCardPlayed = last
		)
	}

	// Dummy state: XML save uses only getStateString
	private object DummyState extends State(null) {
		override def getStateString: String = "GamePlayState"
	}

	"XML FileIO" should {
		import fileIOComponent.fileIOXMLImpl._
		"create hearts.xml on save and include state + game tags" in {
		val io = new FileIO
		io.save(sampleGame(), DummyState)

		saveFile.exists() shouldBe true
		saveFile.length() should be > 0L

		val xml = XML.loadFile(saveFile)
		(xml \\ "state").text.trim shouldBe "GamePlayState"
		(xml \\ "game").nonEmpty shouldBe true
		}

		"load the saved game back (roundtrip) and set controller state from XML" in {
		val io = new FileIO
		val g = sampleGame(Left("Game saved!\n"))
		io.save(g, DummyState)

		val controller = new Controller(Game())
		controller.passStateString should not be "GamePlayState"

		val loaded = io.load(controller)

		loaded.asInstanceOf[Game] shouldBe g.asInstanceOf[Game]

		controller.passStateString shouldBe "GamePlayState"
		}

		"gameAndStateToXML return an Elem with state + game" in {
		val io = new FileIO
		val xmlElem = io.gameAndStateToXML(sampleGame(), DummyState)

		(xmlElem \\ "state").text.trim shouldBe "GamePlayState"
		(xmlElem \\ "game").nonEmpty shouldBe true
		}
	}
}

	val saveFile1 = new File("hearts.xml")
	val saveFile2 = new File("hearts.json")

	def deleteFile(saveFile: File): Unit =
		if (saveFile.exists()) then
			saveFile.delete()

	val a = (deleteFile(saveFile1),deleteFile(saveFile2))


