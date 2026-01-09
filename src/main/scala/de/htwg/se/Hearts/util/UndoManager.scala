package de.htwg.se.Hearts.util

import scala.collection.mutable.ListBuffer
import scala.collection.mutable.Stack

class CommandHistory {
    private val undoStack: Stack[Command]= Stack()
    private val redoStack: Stack[Command]= Stack()
    def push(c:Command): Unit = undoStack.push(c)
    def pop: Command = undoStack.pop

}
