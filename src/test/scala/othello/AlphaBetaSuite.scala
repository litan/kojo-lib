package game.othello

import org.junit.runner.RunWith
import org.scalacheck.Prop.forAll
import org.scalacheck.Prop.propBoolean
import org.scalatest.FunSuite
import org.scalatest.Matchers
import org.scalatestplus.scalacheck.Checkers
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
@annotation.nowarn
class OthelloAlphaBetaTestSuite extends FunSuite with Matchers {

  class Game(size: Int) {
    val b = Board.newBoard(size)
    val state = new State(b, Black)
    def play: State = loop(state)
    var i = 0
    import scala.annotation.tailrec
    @tailrec
    private def loop(state: State): State = {
      i += 1
      state.board.print(i.toString)
      if (state.isGameOver) state
      else {
        if (i > size * size) {
          println("Not halting, is it?")
          return state
        }
        ABS.move(state) match {
          case Some(room) =>
            val newState = state.move(room)
            println(s"Move $i by ${state.turn}: $room")
            loop(newState)
          case _ =>
            val state2 = new State(state.board, state.opponent)
            ABS.move(state2) match {
              case Some(room) =>
                val newState = state2.move(room)
                println(s"Move $i by ${state2.turn} again: $room")
                loop(newState)
              case _ => throw new Exception("Not here!")
            }
        }
      }
    }
  }

  test("alpha beta on a 4x4 board") {
    val g = new Game(4)
    ABS.maxDepth = 12
    val t0 = Util.epochTime
    g.play
    println(f"Alpha-beta game with depth=${ABS.maxDepth} took ${Util.epochTime - t0}%.3f seconds")
    true shouldBe true
  }
}
