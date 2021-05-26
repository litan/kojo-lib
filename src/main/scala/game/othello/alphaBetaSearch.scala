package game.othello
//uses only types and board

class State(val board: Board, val turn: Stone) {
    val opponent: Stone = if (turn == White) Black else White
    def score: Int = board.score(turn) - board.score(opponent)
    def isGameOver: Boolean = {
        if (moves.size > 0) false else {
            val newState = new State(board, opponent)
            newState.moves.size == 0
        }
    }
    def moves: Seq[Room] = board.moves(turn)
    def move(room: Room): State = {
        val newBoard = board.move(turn, room)
        new State(newBoard, opponent)
    }
}

object ABS {
    var maxDepth = 3
    def move(state: State): Option[Room] = {
        var out: Option[Room] = None
        val t0 = Util.epochTime
        out = move_(state)
        val delta = Util.epochTime - t0
        println(f"Alpha-beta search took $delta%.3f seconds")
        out
    }
    def move_(state: State): Option[Room] =
        if (state.moves.isEmpty) None
        else Some((for (move <- state.moves) yield move ->
            abMove(state.move(move), maxDepth)).minBy(_._2)._1)
    // todo: maxDepth must be adaptive, or better just find out how many branches we can process...
    def abMove(state: State, depth: Int): Int =
        if (state.isGameOver || depth == 0 || state.moves.isEmpty) state.score
        else minimize(state, depth, Int.MinValue, Int.MaxValue)

    def minimize(state: State, depth: Int, alpha: Int, beta: Int): Int =
        if (state.isGameOver || depth == 0 || state.moves.isEmpty) state.score
        else {
            var newBeta = beta
            state.moves.foreach { move =>
                val newState = state.move(move)
                newBeta = math.min(newBeta, maximize(newState, depth - 1, alpha, newBeta))
                if (alpha >= newBeta) return alpha
            }
            newBeta
        }
    def maximize(state: State, depth: Int, alpha: Int, beta: Int): Int =
        if (state.isGameOver || depth == 0 || state.moves.isEmpty) state.score
        else {
            var newAlpha = alpha
            state.moves.foreach { move =>
                val newState = state.move(move)
                newAlpha = math.max(newAlpha, minimize(newState, depth - 1, newAlpha, beta))
                if (newAlpha >= beta) return beta
            }
            newAlpha
        }
}
