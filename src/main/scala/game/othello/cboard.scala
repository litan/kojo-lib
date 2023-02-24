package game.othello
// depends only on types

import scala.collection.mutable.ArrayBuffer

// this is just to clarify (and test) what needs to be implemented
// at a minimum for a functional board:
class FooBoard(val size: Int = 2) extends CoreBoard {
  def stone(r: Room): Stone = Empty
  def placeSeq(rs: Seq[(Int, Int)])(s: Stone): Unit = println("eternally empty board")
  CoreBoard.newBoard(this, 2)
}

trait CoreBoard {
  def size: Int
  val lastRoom = size - 1
  val last = lastRoom
  val end = last
  val range = 0 to last

  def stone(r: Room): Stone
  def stones(s: Stone): Seq[Room] = for {
    x <- range
    y <- range if stone(Room(y, x)) == s
  } yield Room(y, x)

  def score(turn: Stone) = {
    count(turn) // + 4 * countIf(turn)(isCorner) + 2 * countIf(turn)(isTwoAwayFromCorner)
    // + countIf(turn)(isInnerCorner) - 2 * countIf(turn)(isTrapCorner)
    // - countIf(turn)(isTrapSide)
  }
  def count(turn: Stone) = countIf(turn) { room => true }
  def countIf(turn: Stone)(f: Room => Boolean) =
    (for {
      x <- range
      y <- range
      if stone(Room(y, x)) == turn && f(Room(y, x))
    } yield 1).size

  def isGameOver = moves(White).isEmpty && moves(Black).isEmpty

  def moves(turn: Stone) =
    (for {
      y <- range
      x <- range if stone(Room(y, x)) == Empty
    } yield Room(y, x)).filter {
      neighborsToFlip(_, turn).size > 0
    }

  def opponent(turn: Stone) = if (turn == White) Black else White

  def movePayoff(room: Room, turn: Stone): Int =
    findTheNeighbors(room).map { n =>
      val line = isTheEndOfLineLegal(n, turn)
      if (stone(n.room) == opponent(turn) && line._1) line._2 else 0
    }.sum

  def moveCore(turn: Stone, room: Room): Seq[Room] =
    if (stone(room) != Empty) Seq()
    else {
      val rooms = ArrayBuffer(room)
      neighborsToFlip(room, turn).foreach { n =>
        rooms += n.room
        theRestOfTheLine(n).takeWhile(stone(_) == opponent(turn)).foreach { r =>
          rooms += r
        }
      }
      rooms.toSeq
    }

  def neighborsToFlip(r: Room, turn: Stone): Seq[Neighbor] =
    findTheNeighbors(r).filter { n =>
      stone(n.room) == opponent(turn) && isTheEndOfLineLegal(n, turn)._1
    }
  def isTheEndOfLineLegal(n: Neighbor, turn: Stone): (Boolean, Int) = {
    val theRest = theRestOfTheLine(n)
    val line = theRest.dropWhile { r => stone(r) != turn && stone(r) != Empty }
    if (line.isEmpty) (false, 0)
    else (stone(line.head) == turn, 1 + theRest.size - line.size)
  }

  def findTheNeighbors(r: Room): Seq[Neighbor] = Seq(
    Neighbor(E, Room(r.y, r.x + 1)),
    Neighbor(W, Room(r.y, r.x - 1)),
    Neighbor(N, Room(r.y + 1, r.x)),
    Neighbor(S, Room(r.y - 1, r.x)),
    Neighbor(NE, Room(r.y + 1, r.x + 1)),
    Neighbor(NW, Room(r.y + 1, r.x - 1)),
    Neighbor(SE, Room(r.y - 1, r.x + 1)),
    Neighbor(SW, Room(r.y - 1, r.x - 1))
  ).filter { n => isValid(n.room) }
  def isValid: Room => Boolean = {
    case Room(y, x) => 0 <= y && y < size && 0 <= x && x < size
  }
  def isInnerCorner: Room => Boolean = {
    case Room(y, x) =>
      (x == 2 && (y == 2 || y == end - 2)) ||
      (x == end - 2 && (y == 2 || y == end - 2))
  }
  def theRestOfTheLine(n: Neighbor): Seq[Room] = {
    val line = ArrayBuffer.empty[Room]
    val (x, y) = (n.room.x, n.room.y)
    n.dir match {
      case E => for (i <- x + 1 to last) /*   */ line += Room(y, i)
      case W => for (i <- x - 1 to 0 by -1) /**/ line += Room(y, i)
      case N => for (i <- y + 1 to last) /*   */ line += Room(i, x)
      case S => for (i <- y - 1 to 0 by -1) /**/ line += Room(i, x)
      case NE => // both x and y increase
        if (x >= y) for (i <- x + 1 to last) /*       */ line += Room(y + i - x, i)
        else for (i <- y + 1 to last) /*              */ line += Room(i, x + i - y)
      case SW => // both x and y decrease
        if (x >= y) for (i <- y - 1 to 0 by -1) /*    */ line += Room(i, x - y + i)
        else for (i <- x - 1 to 0 by -1) /*           */ line += Room(y - x + i, i)
      case NW => // x decreases as y increases (and vice versa)
        if (x + y >= last) for (i <- y + 1 to last) /**/ line += Room(i, x + y - i)
        else for (i <- x - 1 to 0 by -1) /*           */ line += Room(y + x - i, i)
      case SE => // x increases as y decreases
        if (x + y >= last) for (i <- x + 1 to last) /**/ line += Room(y + x - i, i)
        else for (i <- y - 1 to 0 by -1) /*           */ line += Room(i, x + y - i)
    }
    line.toSeq
  }

  def placeSeq(rooms: Seq[(Int, Int)])(stone: Stone): Unit
  def place4: Room => Unit = {
    case Room(y, x) =>
      placeSeq(Seq((y, x), (y + 1, x + 1)))(White)
      placeSeq(Seq((y + 1, x), (y, x + 1)))(Black)
  }

}

object CoreBoard {
  def newBoard(board: CoreBoard, variant: Int = 0): Unit = {
    for {
      x <- board.range
      y <- board.range
    }
      board.placeSeq(Seq(y -> x))(Empty)
    val size = board.size
    val mid: Int = size / 2
    val end = size - 1
    variant match {
      case 2 => // empty board. How to start the game?
      case 1 =>
        require(size > 6, "Board for this variant needs to be 7x7 or bigger")
        board.place4(Room(1, 1))
        board.place4(Room(end - 2, end - 2))
        board.place4(Room(1, end - 2))
        board.place4(Room(end - 2, 1))
      case 0 =>
        val even = size % 2 == 0
        if (even) board.place4(Room(mid - 1, mid - 1))
        else {
          val (a, b) = (mid - 1, mid + 1)
          val (ap, am, bp, bm) = (a + 1, a - 1, b + 1, b - 1)
          board.placeSeq(Seq(a -> a, b -> b))(White)
          board.placeSeq(Seq(a -> b, b -> a))(Black)
          board.placeSeq(Seq(ap -> a, ap -> b, bp -> bm, am -> bm))(White)
          board.placeSeq(Seq(a -> ap, b -> ap, ap -> am, ap -> bp))(Black)
        }
    }
  }
}
