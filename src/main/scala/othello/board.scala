package othello

// this board is used only in alpha-beta search
object Board {
  def newBoard(size: Int, variant: Int = 0): Board = {
    var b = new Board(size, Vector.fill(size * size)(0))
    CoreBoard.newBoard(b, variant)
    b
  }
}

class Board(val size: Int, var board: Vector[Int]) extends CoreBoard {
    def s2n(s: Stone) = s match {
        case Black => 2
        case White => 1
        case _     => 0
    }
    def n2s(n: Int) = n match {
        case 2 => Black
        case 1 => White
        case _ => Empty
    }
    def stone(r: Room) = n2s(board(r.y * size + r.x))
    def move(turn: Stone, room: Room): Board = place(turn, moveCore(turn, room))
    def place(s: Stone, r: Room) = new Board(
        size, board.updated(r.y * size + r.x, s2n(s)))
    def place(s: Stone, rooms: Seq[Room]) = {
        var newBoard = board
        for (r <- rooms) newBoard = newBoard.updated(r.y * size + r.x, s2n(s))
        new Board(size, newBoard)
    }
    def print(msg: String = "", lineHeader: String = "") = {
        for (y <- range.reverse) {
            val row = for (x <- range) yield stone(Room(y, x))
            println(row.mkString(lineHeader, " ", ""))
        }
        if (msg.size > 0) println(lineHeader + msg)
        for (p <- List(White, Black))
            println(s"$lineHeader ${p.name.capitalize}: ${count(p)}")
    }

    def placeSeq(rooms: Seq[(Int, Int)])(stone: Stone): Unit = {
        val newBoard = place(stone, rooms.map(p => Room(p._1, p._2)))
        board = newBoard.board
    }

}


