package de.htwg.se.Hearts.aview

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import scalafx.application.Platform
import scalafx.scene.image.ImageView
import scalafx.scene.layout.HBox
import de.htwg.se.Hearts.controller.controllerComponent.ControllerInterface
import de.htwg.se.Hearts.controller.controllerComponent.controllerBase.Controller
import de.htwg.se.Hearts.model.gameComponent.gameBase.Game

class GuiRenderSpec extends AnyWordSpec with Matchers {

  val dummyController: ControllerInterface = Controller(Game())
  Platform.startup(() => ())

  "Gui.renderTrick" should {
    "create an HBox with one ImageView per url" in {
      val gui = new Gui(dummyController)

      val urls = List("file:a.png", "file:b.png", "file:c.png")
      val box: HBox = gui.renderTrick(urls)

      box.children.size shouldBe 3
      all (box.children) shouldBe a [javafx.scene.image.ImageView]
    }
  }

  "Gui.renderHand" should {
    "create an HBox with one ImageView per url" in {
      val gui = new Gui(dummyController)

      val urls = List("file:a.png", "file:b.png")
      val box: HBox = gui.renderHand(urls)

      box.children.size shouldBe 2
      all (box.children) shouldBe a [javafx.scene.image.ImageView]
    }
  }
}

