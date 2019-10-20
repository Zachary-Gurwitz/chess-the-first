
public enum InvalidMoveError {
	WRONG_TURN, INVALID_DIRECTION, SAME_COLOR_CAPTURE, BLOCKED, NOW_IN_CHECK, ILLEGAL, PAWN_CANNOT_CAPTURE, 
	PAWN_HAS_ALREADY_MOVED, CAPTURED, INVALID_STARTING_POSITION, CANNOT_CASTLE, NONE;
}
/*
not your turn
invalid direction
same color capture
blocked
your king is now in check!
illegal move
pawn cannot capture
THIS PAWN HAS ARLEADY MOVED SO IT CANNOT MOVE 2 SPACES
this piece has been captured
NULL
*/