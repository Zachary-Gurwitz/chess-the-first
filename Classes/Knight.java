import java.awt.Color;

public class Knight extends Piece{
	public Knight(VisualBoard vBoard, int file, int rank, String team) {
		super(vBoard, file, rank, team);
		TYPE = PieceType.KNIGHT;
		if(this.team.equals("WHITE")){
			color = new Color(165,42,42);
		} else if (this.team.equals("BLACK")) {
			color = new Color(83,21,21);
		}
		VisualBoardMaker.initializePiecePosition(vBoard, file, rank, color);
	}
	
	public boolean checkIfValidMove(String start, String finish) {
		if(alive){
			if(start.equals(coordinates)) {
				if(validDirection(start, finish)) {
					if(!sameColorCapture(start, finish)) {
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
		if(Board.squaresMap.containsKey(start) && Board.squaresMap.containsKey(finish)) {
			if(Math.abs(Board.squaresMap.get(start).rank - Board.squaresMap.get(finish).rank) == 2 && 
			Math.abs(Board.squaresMap.get(start).file - Board.squaresMap.get(finish).file) == 1) {
				return true;
			} 
			if(Math.abs(Board.squaresMap.get(start).rank - Board.squaresMap.get(finish).rank) == 1 && 
					Math.abs(Board.squaresMap.get(start).file - Board.squaresMap.get(finish).file) == 2) {
				return true;
			}
		}
		return false;
	}
}
