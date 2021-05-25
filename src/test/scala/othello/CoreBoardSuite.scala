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
class CoreBoardTestSuite extends FunSuite with Matchers {
  val foo1 = new FooBoard()
  test("inital cell counts") {
    foo1.count(White) shouldBe 0
    foo1.count(Black) shouldBe 0
    foo1.count(Empty) shouldBe 4
  }
  test("we humans count from 1 and we first state the column number") {
      Room(0, 1).toString shouldBe "2x1"
  }
  test("rest todo") {
    for (s <- List(White, Black)) assert(foo1.stones(s) == Vector(), s"get $s stones")
    assert(foo1.stones(Empty) == Vector(Room(0, 0), Room(1, 0), Room(0, 1), Room(1, 1)), "get empty roomw")
    assert(foo1.findTheNeighbors(Room(0, 0)) == List(
      Neighbor(E, Room(0, 1)), Neighbor(N, Room(1, 0)), Neighbor(NE, Room(1, 1))
    ), "what's next to 1x1?")
    // here is the list in order: List(Neighbor(W,1x2), Neighbor(S,2x1), Neighbor(SW,1x1))
    assert(foo1.findTheNeighbors(Room(1, 1)).toSet == Set(
      Neighbor(S, Room(0, 1)), Neighbor(W, Room(1, 0)), Neighbor(SW, Room(0, 0))
    ), "what's next to 2x2?")
    // here is the list in order: List(Neighbor(W,1x1), Neighbor(N,2x2), Neighbor(NW,1x2))
    assert(foo1.findTheNeighbors(Room(0, 1)).toSet == Set(
      Neighbor(W, Room(0, 0)), Neighbor(N, Room(1, 1)), Neighbor(NW, Room(1, 0))
    ), "what's next to 2x1?")
    // List(Neighbor(E,2x2), Neighbor(S,1x1), Neighbor(SE,2x1))
    assert(foo1.findTheNeighbors(Room(1, 0)).toSet == Set(
      Neighbor(E, Room(1, 1)), Neighbor(S, Room(0, 0)), Neighbor(SE, Room(0, 1))
    ), "what's next to 1x2?")
    true shouldBe true
  }

}
