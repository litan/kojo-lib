package othello

import org.junit.runner.RunWith
import org.scalacheck.Prop.forAll
import org.scalacheck.Prop.propBoolean
import org.scalatest.FunSuite
import org.scalatest.Matchers
import org.scalatestplus.scalacheck.Checkers
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
@annotation.nowarn
class BoardTestSuite extends FunSuite with Matchers {

  test("newBoard") {
    for (
      (n, expected) <- List(
        (4, Vector(0, 0, 0, 0, 0, 1, 2, 0, 0, 2, 1, 0, 0, 0, 0, 0)),
        (5, Vector(0, 0, 1, 0, 0, 0, 1, 2, 2, 0, 2, 1, 0, 1, 2, 0, 2, 2, 1, 0, 0, 0, 1, 0, 0)),
        (6, Vector(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 0, 0, 0, 0, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)),
        (7, Vector(0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 0, 1, 2, 0, 0, 2, 1, 0, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 0, 1, 2, 0, 0, 2, 1, 0, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0))
      )
    ) {
      val b = if (n < 7) Board.newBoard(n) else Board.newBoard(n, 1)
      b.board shouldBe expected
    }
  }

  import scala.collection.mutable.ArrayBuffer
  test("rest todo") {
    val size = 6
    var b = new Board(size, Vector.fill(size * size)(0))
    assert(b.board == Vector(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0), "1")
    val foo = b.place(White, Room(1, 1))
    assert(b.board == Vector(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0), "2")
    b = b.place(White, Seq(Room(2, 2), Room(3, 3)))
    b = b.place(Black, Seq(Room(2, 3), Room(3, 2)))
    val newBoard = b
    // newBoard.print("t1")
    assert(newBoard.board == Vector(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 0, 0, 0, 0, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0), "3")
    var turn: Stone = Black
    for (
        (i, moves, finalBoard) <- List(
            (0, List(Room(2, 1), Room(3, 1), Room(4, 1)), Vector(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 0, 0, 0, 2, 2, 1, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)),
            (1, List(Room(1, 2), Room(1, 3), Room(1, 4)), Vector(0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 0, 0, 0, 2, 2, 0, 0, 0, 0, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)),
            (2, List(Room(4, 3), Room(4, 2), Room(4, 1)), Vector(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 0, 0, 0, 0, 2, 2, 0, 0, 0, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0)),
            (3, List(Room(3, 4), Room(2, 4), Room(1, 4)), Vector(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 1, 2, 2, 0, 0, 0, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
        )
    ) {
        val payoffs = ArrayBuffer.empty[Int]
        for (move <- moves) {
            payoffs += b.movePayoff(move, turn)
            b = b.move(turn, move)
            turn = if (turn == Black) White else Black
        }
        //println(payoffs)
        assert(payoffs == ArrayBuffer(1, 1, 2), "payoffs $i")
        //println(b.board)
        //b.print()
        assert(b.board == finalBoard, s"board $i")
        b = newBoard
        turn = Black
    }
    // println("Board is functional.")
    true shouldBe true
  }
}
