package de.htwg.se.Hearts.util
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import de.htwg.se.Hearts.controller.controllerComponent.controllerBase.PlayCardCommand
import scala.collection.mutable.Stack

class CommandHistorySpec extends AnyWordSpec with Matchers  {
    "A History" should {
        val hs = CommandHistory()
        val command = PlayCardCommand(index = Some(1))
        "be able to queue und dequeue commands" in {
            hs.pop should be (None)
            hs.push(command)
            hs.undoStack should be (Stack(command))
            hs.pop should be (Some(command))
        }
        "be able to queue and dequeue redo commands" in {
            hs.redoPop should be (None)
            hs.redoPush(command)
            hs.redoStack should be (Stack(command))
            hs.redoPop should be (Some(command))
        }
        "be able to clear redo Stack" in {
        hs.redoPush(command)
        hs.clearRedoStack
        hs.redoStack should be (Stack())
        }

    }
}
