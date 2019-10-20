import java.awt.Color;

public class Rook extends Piece{
	
	
	
	public Rook(VisualBoard vBoard, int file, int rank, String team) {
		super(vBoard, file, rank, team);
		TYPE = PieceType.ROOK;
		if(this.team.equals("WHITE")){
			color = new Color(255,255,0);
		} else if (this.team.equals("BLACK")) {
			color = new Color(127,127,0);
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
		if(Board.squaresMap.containsKey(start) && Board.squaresMap.containsKey(finish)){	
				
				if((sameRankMove(start, finish) || sameFileMove(start, finish)) && !(sameRankMove(start, finish) && sameFileMove(start, finish))) {
					return true;
				}
			
		}
		return false;
	}
	
	
	
	
	

}