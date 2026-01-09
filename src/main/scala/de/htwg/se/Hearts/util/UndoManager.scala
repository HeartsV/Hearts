package de.htwg.se.Hearts.util

import scala.collection.mutable.ListBuffer
import scala.collection.mutable.Stack

class CommandHistory:
    val undoStack: Stack[Command]= Stack()
    val redoStack: Stack[Command]= Stack()
    def push(c:Command): Unit = undoStack.push(c)
    def pop: Option[Command] =
        if undoStack.nonEmpty then Some(undoStack.pop)
        else None

    def redoPush(c:Command): Unit = redoStack.push(c)
    def redoPop:Option[Command] =
        if redoStack.nonEmpty then Some(redoStack.pop)
        else None
