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
class EBoardTestSuite extends FunSuite with Matchers {
  val board = new EBoard(8, Black, 0)
  test("8x8 e-board") {
    true shouldBe true
  }
  // note: Room(y, x) is printed: (x+1)x(y+1)
  test("black's starting moves") {
    board.moves(Black).toString shouldBe "Vector(4x3, 3x4, 6x5, 5x6)"
  }

  test("white's starting moves") {
    board.moves(White).toString shouldBe "Vector(5x3, 6x4, 3x5, 4x6)"
  }
  test("neighbors") {
    board.neighborsToFlip(Room(3, 2), Black) shouldBe List(Neighbor(E, Room(3, 3)))
  }
  test("stones to flip") {
    board.moveCore(Black, Room(3, 2)) shouldBe List(Room(3, 2), Room(3, 3))
  }
}
