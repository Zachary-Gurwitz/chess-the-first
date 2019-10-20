import java.awt.Color;

public class Bishop extends Piece {
	public Bishop(VisualBoard vBoard, int file, int rank, String team) {
		super(vBoard, file, rank, team);
		TYPE = PieceType.BISHOP;
		if(this.team.equals("WHITE")){
			color = new Color(52, 178, 51);
		} else if (this.team.equals("BLACK")) {
			color = new Color(0,127,0);
		}
		VisualBoardMaker.initializePiecePosition(vBoard, file, rank, color);
	}
	
	public boolean checkIfValidMove(String start, String finish) {
		if(alive) {
			if(start.equals(coordinates)) {
				if(validDirection(start, finish)) {
					if(!blocked(start, finish)) {
						Board.squaresMap.get(start).occupiedBy.error = InvalidMoveError.NONE;
						return true;
					} else {
						Board.squaresMap.get(start).occupiedBy.error = InvalidMoveError.BLOCKED;
					}
				} else {
					Board.squaresMap.get(start).occupiedBy.error = InvalidMoveError.INVALID_DIRECTION;
				}
			} else {
				Board.squaresMap.get(start).occupiedBy.error = InvalidMoveError.INVALID_STARTING_POSITION;
			}
		} else {
			Board.squaresMap.get(start).occupiedBy.error = InvalidMoveError.CAPTURED;
		}
		return false;
		
	}
	public boolean validDirection(String start, String finish) {
		
		return sameDiagonal(start, finish);
	}

	public boolean blocked() {
		return false;
	}
}
