import java.awt.Color;

public class Pawn extends Piece{
	
	
	
	
	public Pawn(VisualBoard vBoard, int file, int rank, String team) {
		super( vBoard,  file,  rank,  team);
		TYPE = PieceType.PAWN;
		justMovedTwice = false;
		if(this.team.equals("WHITE")){
			color = new Color(225,225,225);
		} else if (this.team.equals("BLACK")) {
			color = new Color(50,50,50);
		}
		VisualBoardMaker.initializePiecePosition(vBoard, file, rank, color);
	}


	public boolean checkIfValidMove(String start, String finish) {
		enPassanting = false;
		if(Board.squaresMap.containsKey(start) && Board.squaresMap.containsKey(finish)) {		
			if(start.equals(coordinates)) {
				if(alive) {
					if(team.equals("WHITE")) {
						
						//capture normal
						if(Math.abs(Board.squaresMap.get(start).file - Board.squaresMap.get(finish).file) == 1) {
							if(Board.squaresMap.get(finish).rank - Board.squaresMap.get(start).rank == 1) {
								//avoids nullpointer with short circuiting
								if(Board.squaresMap.get(finish).occupied && !Board.squaresMap.get(finish).occupiedBy.team.equals(this.team) ) {
									Board.squaresMap.get(start).occupiedBy.error = InvalidMoveError.NONE;
									return true;
								} else {
									Board.squaresMap.get(start).occupiedBy.error = InvalidMoveError.PAWN_CANNOT_CAPTURE;
								}
							}
						}
						
						//capture en passant 
						if(Math.abs(Board.squaresMap.get(start).file - Board.squaresMap.get(finish).file) == 1) {
							if(Board.squaresMap.get(finish).rank - Board.squaresMap.get(start).rank == 1) {
								enPassantSquare = "";
								enPassantSquare += finish.charAt(0);
								enPassantSquare += (char)(finish.charAt(1) - 1);
								if(Board.squaresMap.get(enPassantSquare).occupied && !Board.squaresMap.get(enPassantSquare).occupiedBy.team.equals(this.team) 
										&& Board.squaresMap.get(enPassantSquare).occupiedBy.justMovedTwice ) {
									
									enPassanting = true;
									Board.squaresMap.get(start).occupiedBy.error = InvalidMoveError.NONE;
									return true;
								} 
								
								
							}
						}
						
						//forward
						if(Board.squaresMap.get(start).file == Board.squaresMap.get(finish).file) {
							if(Board.squaresMap.get(finish).rank - Board.squaresMap.get(start).rank == 1) {
								if(!Board.squaresMap.get(finish).occupied) {
									Board.squaresMap.get(start).occupiedBy.error = InvalidMoveError.NONE;
									return true;
								} else {
									Board.squaresMap.get(start).occupiedBy.error = InvalidMoveError.BLOCKED;
								}
							} else if(Board.squaresMap.get(finish).rank - Board.squaresMap.get(start).rank != 2){
								Board.squaresMap.get(start).occupiedBy.error = InvalidMoveError.INVALID_DIRECTION;
							}
						} else {
							Board.squaresMap.get(start).occupiedBy.error = InvalidMoveError.INVALID_DIRECTION;
						}
						
						//forward 2 squares
						if(Board.squaresMap.get(start).file == Board.squaresMap.get(finish).file) {
							if(Board.squaresMap.get(finish).rank - Board.squaresMap.get(start).rank == 2) {
								if(!hasMoved) {	
									if(!(Board.squaresMap.get(finish).occupied || Board.squaresMap.get(String.valueOf(start.charAt(0)) + String.valueOf((char)(start.charAt(1) + 1))).occupied )) {
										justMovedTwice = true;
										Board.squaresMap.get(start).occupiedBy.error = InvalidMoveError.NONE;
										return true;
									} else {
										Board.squaresMap.get(start).occupiedBy.error = InvalidMoveError.BLOCKED;
									}
								} else {
									Board.squaresMap.get(start).occupiedBy.error = InvalidMoveError.PAWN_HAS_ALREADY_MOVED;
								} 
							} else {
								Board.squaresMap.get(start).occupiedBy.error = InvalidMoveError.INVALID_DIRECTION;
							}
						} else {
							Board.squaresMap.get(start).occupiedBy.error = InvalidMoveError.INVALID_DIRECTION;
						}

		
					}
					
					if(team.equals("BLACK")) {
						//capture normal
						if(Math.abs(Board.squaresMap.get(start).file - Board.squaresMap.get(finish).file) == 1) {
							if(Board.squaresMap.get(finish).rank - Board.squaresMap.get(start).rank == -1) {
								if(Board.squaresMap.get(finish).occupied && !Board.squaresMap.get(finish).occupiedBy.team.equals(this.team) ) {
									Board.squaresMap.get(start).occupiedBy.error = InvalidMoveError.NONE;
									return true;
								} else {
									Board.squaresMap.get(start).occupiedBy.error = InvalidMoveError.PAWN_CANNOT_CAPTURE;
								}
							}
						}
						//capture en passant
						if(Math.abs(Board.squaresMap.get(start).file - Board.squaresMap.get(finish).file) == 1) {
							if(Board.squaresMap.get(finish).rank - Board.squaresMap.get(start).rank == -1) {
								enPassantSquare = "";
								enPassantSquare += finish.charAt(0);
								enPassantSquare += (char)(finish.charAt(1) + 1);
								if(Board.squaresMap.get(enPassantSquare).occupied && !Board.squaresMap.get(enPassantSquare).occupiedBy.team.equals(this.team)
										&& Board.squaresMap.get(enPassantSquare).occupiedBy.justMovedTwice ) {
									
									enPassanting = true;
									Board.squaresMap.get(start).occupiedBy.error = InvalidMoveError.NONE;
									return true;
								} 
								
								
							}
						}
						//forward
						if(Board.squaresMap.get(start).file == Board.squaresMap.get(finish).file) {
							if(Board.squaresMap.get(finish).rank - Board.squaresMap.get(start).rank == -1) {
								if(!Board.squaresMap.get(finish).occupied) {
									Board.squaresMap.get(start).occupiedBy.error = InvalidMoveError.NONE;
									return true;
								} else {
									Board.squaresMap.get(start).occupiedBy.error = InvalidMoveError.BLOCKED;
								}
							} else if(Board.squaresMap.get(finish).rank - Board.squaresMap.get(start).rank != -2){
								Board.squaresMap.get(start).occupiedBy.error = InvalidMoveError.INVALID_DIRECTION;
							}
						} else {
							Board.squaresMap.get(start).occupiedBy.error = InvalidMoveError.INVALID_DIRECTION;
						}
						
						//forward 2 squares
						if(Board.squaresMap.get(start).file == Board.squaresMap.get(finish).file) {
							if(Board.squaresMap.get(finish).rank - Board.squaresMap.get(start).rank == -2) {
								if(!hasMoved) {	
									if(!(Board.squaresMap.get(finish).occupied || Board.squaresMap.get(String.valueOf(start.charAt(0)) + String.valueOf((char)(start.charAt(1) - 1))).occupied )) {
										justMovedTwice = true;
										Board.squaresMap.get(start).occupiedBy.error = InvalidMoveError.NONE;
										return true;
									} else {
										Board.squaresMap.get(start).occupiedBy.error = InvalidMoveError.BLOCKED;
									}
								} else {
									Board.squaresMap.get(start).occupiedBy.error = InvalidMoveError.PAWN_HAS_ALREADY_MOVED;
								} 
							} else {
								Board.squaresMap.get(start).occupiedBy.error = InvalidMoveError.INVALID_DIRECTION;
							}
						} else {
							Board.squaresMap.get(start).occupiedBy.error = InvalidMoveError.INVALID_DIRECTION;
						}


					}
				} else {
					Board.squaresMap.get(start).occupiedBy.error = InvalidMoveError.CAPTURED;
				}
			} else {
				Board.squaresMap.get(start).occupiedBy.error = InvalidMoveError.INVALID_STARTING_POSITION;
			} 
		}
		return false;
	}
	
	public boolean capturingEnPassant(String start, String finish) {
		if(enPassanting) {
			if(Board.squaresMap.get(finish).occupiedBy != null) {
				if(!Board.squaresMap.get(finish).occupiedBy.team.equals(this.team)) {
					return true;
				}
			}
		}
		return false;
	}
	
}
