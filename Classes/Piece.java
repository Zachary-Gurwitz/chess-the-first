import java.awt.Color;
import java.util.*; 
public class Piece {
	
	public int file;
	public int rank;
	public String team;
	public String coordinates = "";
	public boolean alive = true;
	public boolean hasMoved = false;
	public InvalidMoveError error = InvalidMoveError.NONE;
	public Color color;
	//next are only needed for Pawn
	public PieceType TYPE;
	public boolean justMovedTwice = false;
	boolean enPassanting = false;
	//used to find the piece being captured in EP 
	String enPassantSquare;
	//name of the visualBoard
	public VisualBoard vBoard;
	
	public Piece(VisualBoard vBoard, int file, int rank, String team) {
		this.file = file;
		this.rank = rank;
		
		
		
		this.vBoard = vBoard;
		setValidTeam(team);
		setCoordinates();
		setSquare(coordinates);
		if(this.team.equals("WHITE")){
			color = Color.WHITE;
		} else if (this.team.equals("BLACK")) {
			color = Color.BLACK;
		}
		VisualBoardMaker.initializePiecePosition(vBoard, file, rank, color);
		//System.out.println(Board.squaresMap.get(coordinates).occupied + "-");
		
	}
	
	
	//make sure the team is WHITE or BLACK or whatever listed in boolean validTeam
	private void setValidTeam(String team) {
		if (validTeam(team)) {
			this.team = team.toUpperCase();
		} else {
			System.out.println("INVALID TEAM! YOU WILL HAVE ERRORS WITH THIS PIECE!");
		}
	}
	
	//list of valid teams
	private boolean validTeam(String team) {
		if(team.toUpperCase().equals("WHITE") || team.toUpperCase().equals("BLACK")) {
			return true;
		}
		return false;
	}
	
	//make the coordinate string of the piece from the rank and file
	private void setCoordinates() {
		coordinates = ((char) (96 + file)) + Integer.toString(rank); 
		//System.out.println(coordinates);
	}
	
	//main move method
	public void move(String start, String finish, boolean wantToPrint) {
		boolean alreadyPrintedError = false;
		error = InvalidMoveError.NONE;
		if(correctTurn()) {
			
			if(Game.castling) {
				castle(start, finish, team);
				
			}
			else if(checkIfValidMove(start, finish)) {
				//potentially problematic
				if((this.TYPE.equals(PieceType.KING) && !ownKingAttackedMovingKing(team, start, finish)) || !ownKingAttackedMovingNonKing(team,start,finish)) {
				
					if(capturing(start, finish)) {
						
						capture(finish);
								
					}	
					drawPiece(start, finish);
					Game.wasJustValidMove = true;	
					Board.squaresMap.get(start).setUnoccupied();
					Board.squaresMap.get(finish).setOccupied(this);
					coordinates = Board.squaresMap.get(finish).coordinates;
					
					
					Board.lastMoved = this;
							
					resetEnPassant();
							
					hasMoved = true;
						
					Game.turnIncrement();
						
					if(wantToPrint) {
						errorPrintout(error);
						alreadyPrintedError = true;
					}
					if(attackingKing(team)) {
						if(Game.checkMate(team)) {
							System.out.println("CHECKMATE!");
							Game.checkmate = true;
						} else {
							System.out.println("CHECK!");
						}
					} else if(Game.checkMate(team)) {
						Game.draw = true;
					}
				} else {
					error = InvalidMoveError.NOW_IN_CHECK;
					
					Game.wasJustValidMove = false;
				}
						
		} else {
					
					//error = InvalidMoveError.ILLEGAL;
					
			Game.wasJustValidMove = false;
		}
			
	} else {
			error = InvalidMoveError.WRONG_TURN;
			Game.wasJustValidMove = false;
				
		} 
	if(wantToPrint && !alreadyPrintedError) {
		errorPrintout(error);
	}
}
		
	//do this after Board.generateSquares();
	public void setSquare(String location) {
		Board.squaresMap.get(location).setOccupied(this);
		//System.out.println(Board.squaresMap.get(location).occupied + "-");
	}
	

	
	//to be overridden by subclasses
	public boolean checkIfValidMove(String start, String finish) {
		if(alive) {
			if(Board.squaresMap.containsKey(start) && Board.squaresMap.containsKey(finish)) {
				error = InvalidMoveError.NONE;
				return true;	
			}
		}
		error = InvalidMoveError.CAPTURED;
		return false;
	}
	
	
	public boolean sameRankMove(String start, String finish) {
		return Board.squaresMap.get(start).rank == Board.squaresMap.get(finish).rank;
	}
	
	public boolean sameFileMove(String start, String finish) {
		return Board.squaresMap.get(start).file == Board.squaresMap.get(finish).file;
	}
	
	//check if moving from bottom left to top right or vice versa
	public boolean samePositiveDiagonalMove(String start, String finish) {
		if(Board.squaresMap.get(start).file - Board.squaresMap.get(start).rank == Board.squaresMap.get(finish).file - Board.squaresMap.get(finish).rank) {
			return true;
		}
		return false;
	}
	
	//check if moving from top left to bottom right or vice-versa
	public boolean sameNegativeDiagonalMove(String start, String finish) {
		if(Board.squaresMap.containsKey(start) && Board.squaresMap.containsKey(finish)){
			
			if(Board.squaresMap.get(start).file + Board.squaresMap.get(start).rank == Board.squaresMap.get(finish).file + Board.squaresMap.get(finish).rank) {
				return true;
			}
			
		}
		return false;
	}
	
	//checks if moving along a diagonal
	public boolean sameDiagonal(String start, String finish) {
		if(samePositiveDiagonalMove(start, finish) || sameNegativeDiagonalMove(start, finish)) {
			return true;
		}
		return false;
	}
	
	//check if a moving piece is blocked
	public boolean blocked(String start, String finish) {
		if(sameRankMove(start, finish)) {
			for(int i = 1; i < Math.abs(Board.squaresMap.get(start).file - Board.squaresMap.get(finish).file); i++) {								
				
				if(Board.squaresMap.get(start).file - Board.squaresMap.get(finish).file < 0) {
					//System.out.println((char) (Board.squaresMap.get(start).file + 96 + i) + Integer.toString(Board.squaresMap.get(start).rank));
					
					if(Board.squaresMap.get((char) (Board.squaresMap.get(start).file + 96 + i) + Integer.toString(Board.squaresMap.get(start).rank)).occupied) {	 
						if(Board.squaresMap.get((char) (Board.squaresMap.get(start).file + 96 + i) + Integer.toString(Board.squaresMap.get(start).rank)).occupiedBy.alive) {
							return true;
						}
					}
				} else {
					//System.out.println((char) (Board.squaresMap.get(start).file + 96 - i) + Integer.toString(Board.squaresMap.get(start).rank));
	
					if(Board.squaresMap.get((char) (Board.squaresMap.get(start).file + 96 - i) + Integer.toString(Board.squaresMap.get(start).rank)).occupied) {	 
						if(Board.squaresMap.get((char) (Board.squaresMap.get(start).file + 96 - i) + Integer.toString(Board.squaresMap.get(start).rank)).occupiedBy.alive) {
							return true;
						}
					}
				}
			}
			//if attempting to capture same color piece
			if(sameColorCapture(start, finish)) {
				
				return true;
				
			}
		}
		if(sameFileMove(start, finish)) {
			for(int i = 1; i < Math.abs(Board.squaresMap.get(start).rank - Board.squaresMap.get(finish).rank); i++) {
				
				if(Board.squaresMap.get(start).rank - Board.squaresMap.get(finish).rank < 0) {
					//System.out.println((char) (Board.squaresMap.get(start).file + 96) + Integer.toString(Board.squaresMap.get(start).rank + i));
					
					if(Board.squaresMap.get((char) (Board.squaresMap.get(start).file + 96) + Integer.toString(Board.squaresMap.get(start).rank+i)).occupied) {	 
						if(Board.squaresMap.get((char) (Board.squaresMap.get(start).file + 96) + Integer.toString(Board.squaresMap.get(start).rank+i)).occupiedBy.alive) {
							return true;
					
						}
					}
				} else {
					//System.out.println((char) (Board.squaresMap.get(start).file + 96) + Integer.toString(Board.squaresMap.get(start).rank - i));
	
					if(Board.squaresMap.get((char) (Board.squaresMap.get(start).file + 96) + Integer.toString(Board.squaresMap.get(start).rank - i)).occupied) {	 
						if(Board.squaresMap.get((char) (Board.squaresMap.get(start).file + 96) + Integer.toString(Board.squaresMap.get(start).rank - i)).occupied) {	 
								
							return true;
						}
					}
				}
			}
			//if attempting to capture same color piece
			if(sameColorCapture(start, finish)) {
					return true;
				}
			
		}
		if(samePositiveDiagonalMove(start, finish)) {
			for(int i = 1; i < Math.abs(Board.squaresMap.get(start).rank - Board.squaresMap.get(finish).rank); i++) {
				if(Board.squaresMap.get(start).rank - Board.squaresMap.get(finish).rank < 0) {
					//System.out.println((char) (Board.squaresMap.get(start).file + 96 + i) + Integer.toString(Board.squaresMap.get(start).rank + i));
					
					if(Board.squaresMap.get((char) (Board.squaresMap.get(start).file + 96 + i) + Integer.toString(Board.squaresMap.get(start).rank+i)).occupied) {	 
						if(Board.squaresMap.get((char) (Board.squaresMap.get(start).file + 96 + i) + Integer.toString(Board.squaresMap.get(start).rank+i)).occupied) {
							return true;
						}
					}
				} else {
					//System.out.println((char) (Board.squaresMap.get(start).file + 96 - i) + Integer.toString(Board.squaresMap.get(start).rank - i));
	
					if(Board.squaresMap.get((char) (Board.squaresMap.get(start).file + 96 -i) + Integer.toString(Board.squaresMap.get(start).rank - i)).occupied) {	 
						if(Board.squaresMap.get((char) (Board.squaresMap.get(start).file + 96 -i) + Integer.toString(Board.squaresMap.get(start).rank - i)).occupied) {	 
							
							return true;
						}
					}
				}
			}
			if(sameColorCapture(start, finish)) {
				return true;	
			}
		}
		if(sameNegativeDiagonalMove(start, finish)) {
			for(int i = 1; i < Math.abs(Board.squaresMap.get(start).rank - Board.squaresMap.get(finish).rank); i++) {
				if(Board.squaresMap.get(start).rank - Board.squaresMap.get(finish).rank < 0) {
					//System.out.println((char) (Board.squaresMap.get(start).file + 96 - i) + Integer.toString(Board.squaresMap.get(start).rank + i));
					
					if(Board.squaresMap.get((char) (Board.squaresMap.get(start).file + 96 - i) + Integer.toString(Board.squaresMap.get(start).rank+i)).occupied) {	 
						if(Board.squaresMap.get((char) (Board.squaresMap.get(start).file + 96 - i) + Integer.toString(Board.squaresMap.get(start).rank+i)).occupied) {	 
							
							return true;
						}
					}
				} else {
					//System.out.println((char) (Board.squaresMap.get(start).file + 96 + i) + Integer.toString(Board.squaresMap.get(start).rank - i));
	
					if(Board.squaresMap.get((char) (Board.squaresMap.get(start).file + 96 + i) + Integer.toString(Board.squaresMap.get(start).rank - i)).occupied) {	 
						if(Board.squaresMap.get((char) (Board.squaresMap.get(start).file + 96 + i) + Integer.toString(Board.squaresMap.get(start).rank - i)).occupied) {	 
							return true;
						}
					}
				}
			}
			if(sameColorCapture(start, finish)) {
				return true;	
			}
		}
		
			return false;
	}
	
	//check if attempting to capture same team
	public boolean sameColorCapture(String start, String finish) {
		if(Board.squaresMap.get(finish).occupiedBy != null) {
			if(Board.squaresMap.get(finish).occupiedBy.alive)
				if(Board.squaresMap.get(finish).occupiedBy.team.equals(this.team)) {
					error = InvalidMoveError.SAME_COLOR_CAPTURE;
					return true;
				}
		}
		return false;
	}
	
	//check if capturing a piece
	public boolean capturing(String start, String finish) {
		if(this.TYPE.equals(PieceType.PAWN)) {
			if(enPassanting) {
						return true;
			}
		}
		if(Board.squaresMap.get(finish).occupiedBy != null) {
			if(!Board.squaresMap.get(finish).occupiedBy.team.equals(this.team)) {
				return true;
			}
		}
		return false;
	}
	


	public void resetEnPassant() {
		for(String key : Board.squaresMap.keySet()) {
		
			if(Board.squaresMap.get(key).occupiedBy != null) {
				
				if(Board.squaresMap.get(key).occupiedBy.TYPE.equals(PieceType.PAWN)) {			
					
					if(Board.squaresMap.get(key).occupiedBy.hasMoved) {
						
						if(Board.lastMoved != Board.squaresMap.get(key).occupiedBy) {
		
							Board.squaresMap.get(key).occupiedBy.justMovedTwice = false;
							
						}
					}
				}
			}
		}
		
	}
	
	public void capture(String finish) {
		if(enPassanting) {
			Board.squaresMap.get(enPassantSquare).occupiedBy.alive = false;
			Board.squaresMap.get(enPassantSquare).occupiedBy.erasePiece(enPassantSquare.charAt(0) - 96, (int) (enPassantSquare.charAt(1)) - (int)('0'));
			Board.squaresMap.get(enPassantSquare).setUnoccupied();
			
		} else {
			Board.squaresMap.get(finish).occupiedBy.alive = false;
			Board.squaresMap.get(finish).occupiedBy.erasePiece(Board.squaresMap.get(finish).occupiedBy.file,Board.squaresMap.get(finish).occupiedBy.rank);
			Board.squaresMap.get(finish).setUnoccupied();
		}
	}
	
	public boolean correctTurn() {
		if(team.equals("WHITE") && Game.teamTurn == 0) {
			return true;
		}
		if(team.equals("BLACK") && Game.teamTurn == 1) {
			return true;
		}
		
		return false;
	}
	
	public boolean promoting(String finish) {
		if(TYPE.equals(PieceType.PAWN)) {
			//System.out.println('@');
			if(team.equals("WHITE")) {
				//System.out.println(Board.squaresMap.get(finish).occupiedBy.rank);
				if(Board.squaresMap.get(finish).rank == Board.SIZE) {
					//System.out.println('@');
					return true;
				}
			} else if(team.equals("BLACK")) {
				if(Board.squaresMap.get(finish).rank == 1) {
					return true;
				}
			}
		}
		return false;
	}
	
	//check for check
	boolean attackingKing(String team) {
		
		
		for(String key : Board.squaresMap.keySet()) {
			if(Board.squaresMap.get(key).occupied) {
		
				if(Board.squaresMap.get(key).occupiedBy.checkIfValidMove(Board.squaresMap.get(key).occupiedBy.coordinates, enemyKingCoordinates(team) )) {
					return true;
				}
			}
		}
		return false;
	}
	
	//find the coordinates of the enemy king for check checker
	public String enemyKingCoordinates(String team) {
		for(String key : Board.squaresMap.keySet()) {
			if(Board.squaresMap.get(key).occupied) {
				if(Board.squaresMap.get(key).occupiedBy.TYPE.equals(PieceType.KING) && !Board.squaresMap.get(key).occupiedBy.team.equals(team)){
					return Board.squaresMap.get(key).occupiedBy.coordinates;
				}
			}
		}
		return "-1";
	}
	
	//if not potentially moving king
	public static String friendlyKingCoordinates(String team) {
		for(String key : Board.squaresMap.keySet()) {
			if(Board.squaresMap.get(key).occupied) {
				if(Board.squaresMap.get(key).occupiedBy.TYPE.equals(PieceType.KING) && Board.squaresMap.get(key).occupiedBy.team.equals(team)){
					return Board.squaresMap.get(key).occupiedBy.coordinates;
				}
			}
		}
		return "-1";
	}
	

	
	public boolean ownKingAttackedMovingKing(String team, String start, String finish) {
		
		//dont let the ghost of the king block the check
		Board.squaresMap.get(start).occupiedBy.alive = false;
		
		//if capturing a piece to get out of check, temporarily kill it
		if(Board.squaresMap.containsKey(finish)){	
			
				if(Board.squaresMap.get(finish).occupied) {
					Board.squaresMap.get(finish).occupiedBy.alive = false;
				}
		}
		
		
		
		//still in check?
		for(String key : Board.squaresMap.keySet()) {
			if(Board.squaresMap.get(key).occupied) {
				if(!Board.squaresMap.get(key).occupiedBy.team.equals(team)) {
					if(Board.squaresMap.get(key).occupiedBy.checkIfValidMove(Board.squaresMap.get(key).occupiedBy.coordinates, finish )) {
						
						if(Board.squaresMap.get(finish).occupied) {
							Board.squaresMap.get(finish).occupiedBy.alive = true;
						}
						Board.squaresMap.get(start).occupiedBy.alive = true;
						return true;
					}
				}
			}
		}
		
		//revive previously killed pieces
		if(Board.squaresMap.containsKey(finish)){
			if(Board.squaresMap.get(finish).occupied) {
				Board.squaresMap.get(finish).occupiedBy.alive = true;
			}
		}
		Board.squaresMap.get(start).occupiedBy.alive = true;
		return false;
	}
	
	public boolean ownKingAttackedMovingNonKing(String team, String start, String finish) {
		//if moving a non-king:
		if(Board.squaresMap.get(finish).occupied) {
			Board.squaresMap.get(finish).occupiedBy.alive = false;
		}
		//still in check?
		for(String key : Board.squaresMap.keySet()) {
			if(Board.squaresMap.get(key).occupied) {
				if(!Board.squaresMap.get(key).occupiedBy.team.equals(team)) {
					if(Board.squaresMap.get(key).occupiedBy.checkIfValidMove(Board.squaresMap.get(key).occupiedBy.coordinates, friendlyKingCoordinates(team) )) {
						
						if(Board.squaresMap.get(finish).occupied) {
							Board.squaresMap.get(finish).occupiedBy.alive = true;
						}
						return true;
					}
				}
			}
		}
		
		if(Board.squaresMap.get(finish).occupied) {
			Board.squaresMap.get(finish).occupiedBy.alive = true;
		}
		return false;
	}
	
	public boolean ownKingAttackedNotMoving(String team) {
		
		
		//in check?
		for(String key : Board.squaresMap.keySet()) {
			if(Board.squaresMap.get(key).occupied) {
				if(!Board.squaresMap.get(key).occupiedBy.team.equals(team)) {
					if(Board.squaresMap.get(key).occupiedBy.checkIfValidMove(Board.squaresMap.get(key).occupiedBy.coordinates, friendlyKingCoordinates(team) )) {
						
		
						return true;
					}
				}
			}
		}
		

		return false;
	}
	
	
	public boolean squareAttackedByEnemy(int file, int rank, String team) {
		String square = "";
		square += (char) (file + 96);
		square += Integer.toString(rank); 
		
		
		
		for(String key : Board.squaresMap.keySet()) {
			if(Board.squaresMap.get(key).occupied) {
				if(!Board.squaresMap.get(key).occupiedBy.team.equals(team)) {
					if(Board.squaresMap.get(key).occupiedBy.checkIfValidMove(Board.squaresMap.get(key).occupiedBy.coordinates, square )) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public boolean squareAttackedByEnemy(String coordinates, String team) {
		String square = coordinates;		
		for(String key : Board.squaresMap.keySet()) {
			if(Board.squaresMap.get(key).occupied) {
				if(!Board.squaresMap.get(key).occupiedBy.team.equals(team)) {
					if(Board.squaresMap.get(key).occupiedBy.checkIfValidMove(Board.squaresMap.get(key).occupiedBy.coordinates, square )) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public boolean legalCastle(String start, String finish, String team) {
		Piece friendlyKing = Board.squaresMap.get(friendlyKingCoordinates(team)).occupiedBy;
		String rookPosition = "";
		if(friendlyKing.hasMoved) {
			return false;
		}
		
		if(team.equals("WHITE")) {
			if (start.toUpperCase().equals("O-O-O")){
				if(Board.squaresMap.get("a1").occupiedBy.hasMoved) {
					return false;
				}
				if(Board.squaresMap.get("b1").occupied || Board.squaresMap.get("c1").occupied || Board.squaresMap.get("d1").occupied) {
					return false;
				}
			}
			else if (start.toUpperCase().equals("O-O")){
				rookPosition += (char)(Board.SIZE + 96);
				rookPosition += 1;
				if(Board.squaresMap.get(rookPosition).occupiedBy.hasMoved) {
					return false;
				}
				if(Board.squaresMap.get("f1").occupied || Board.squaresMap.get("g1").occupied) {
					return false;
				}
			}
		} 
		else if(team.equals("BLACK")) {

			if (start.toUpperCase().equals("O-O-O")){
				rookPosition += 'a';
				rookPosition += Board.SIZE;
				if(Board.squaresMap.get(rookPosition).occupiedBy.hasMoved) {
					return false;
				}
				if(Board.squaresMap.get("b8").occupied || Board.squaresMap.get("c8").occupied || Board.squaresMap.get("d8").occupied) {
					return false;
				}
			}
			else if (start.toUpperCase().equals("O-O")){
				rookPosition += (char)(Board.SIZE + 96);
				rookPosition += Board.SIZE;
				if(Board.squaresMap.get(rookPosition).occupiedBy.hasMoved) {
					return false;
				}
				if(Board.squaresMap.get("f8").occupied || Board.squaresMap.get("g8").occupied ) {
					return false;
				}
			}
		}
		
		if(ownKingAttackedNotMoving(team)) {
			return false;
		}
		if(start.toUpperCase().equals("O-O")) {
			for(int i = 0; i < Game.NUM_OF_SQUARES_CASTLING; i++) {
				if(squareAttackedByEnemy(file + i, rank, team)) {
					return false;
				}
			}
		} 
		else if (start.toUpperCase().equals("O-O-O")) {
			for(int i = 0; i < Game.NUM_OF_SQUARES_CASTLING; i++) {
				if(squareAttackedByEnemy( file - i, rank, team)) {
					return false;
				}
			}
		}
		
		return true;
	}
	

	//Board.pieceList[30/31] should be the white/black kings
	public void castle(String start, String finish, String team) {
		String squareToDie = "";
		if(legalCastle(start, finish, team)) {
			if(team.equals("WHITE")) {	
				if(start.toUpperCase().equals("O-O")) {
					squareToDie += (char)(Board.SIZE + 96);
					squareToDie += 1;
					capture(squareToDie);
					squareToDie = friendlyKingCoordinates(team);
					capture(squareToDie);
					Board.pieceList[Board.numOfPieces*2 + Game.numOfCastles] = new Rook(vBoard,Board.pieceList[30].file + Game.NUM_OF_SQUARES_CASTLING-1,1,"WHITE");	
					Board.pieceList[Board.numOfPieces*2 + Game.numOfCastles + 1] = new King(vBoard,Board.pieceList[30].file + Game.NUM_OF_SQUARES_CASTLING,1,"WHITE");
					Game.numOfCastles += 2;
				}
				else if(start.toUpperCase().equals("O-O-O")) {
					squareToDie += 'a';
					squareToDie += 1;
					capture(squareToDie);
					squareToDie = friendlyKingCoordinates(team);
					capture(squareToDie);
					Board.pieceList[Board.numOfPieces*2 + Game.numOfCastles] = new Rook(vBoard,Board.pieceList[30].file - Game.NUM_OF_SQUARES_CASTLING, 1,"WHITE");	
					Board.pieceList[Board.numOfPieces*2 + Game.numOfCastles + 1] = new King(vBoard,Board.pieceList[30].file - Game.NUM_OF_SQUARES_CASTLING+1, 1,"WHITE");
					Game.numOfCastles += 2;
				}
			}
			else if(team.equals("BLACK")) {
				if(start.toUpperCase().equals("O-O")) {
					squareToDie += (char)(Board.SIZE + 96);
					squareToDie += Board.SIZE;
					capture(squareToDie);
					squareToDie = friendlyKingCoordinates(team);
					capture(squareToDie);
					Board.pieceList[Board.numOfPieces*2 + Game.numOfCastles] = new Rook(vBoard,Board.pieceList[31].file + Game.NUM_OF_SQUARES_CASTLING-1,Board.SIZE,"BLACK");	
					Board.pieceList[Board.numOfPieces*2 + Game.numOfCastles + 1] = new King(vBoard,Board.pieceList[31].file + Game.NUM_OF_SQUARES_CASTLING,Board.SIZE,"BLACK");
					Game.numOfCastles += 2;
				}
				else if(start.toUpperCase().equals("O-O-O")) {
					squareToDie += 'a';
					squareToDie += Board.SIZE;
					capture(squareToDie);
					squareToDie = friendlyKingCoordinates(team);
					capture(squareToDie);
					Board.pieceList[Board.numOfPieces*2 + Game.numOfCastles] = new Rook(vBoard,Board.pieceList[31].file - Game.NUM_OF_SQUARES_CASTLING, Board.SIZE,"BLACK");	
					Board.pieceList[Board.numOfPieces*2 + Game.numOfCastles + 1] = new King(vBoard,Board.pieceList[31].file - Game.NUM_OF_SQUARES_CASTLING+1, Board.SIZE,"BLACK");
					Board.squaresMap.get(friendlyKingCoordinates(team)).occupiedBy.hasMoved = true;
					Game.numOfCastles += 2;
				}
			}
			//makes it that the king has already moved
			Board.squaresMap.get(friendlyKingCoordinates(team)).occupiedBy.hasMoved = true;
			Game.turnIncrement();
		} else {
			error = InvalidMoveError.CANNOT_CASTLE;
		}
		Game.castling = false;
	}
	public void errorPrintout(InvalidMoveError error) {
		switch(error){
		case BLOCKED:
			System.out.println("BLOCKED!");
			break;
		case CAPTURED:
			System.out.println("THIS PIECE HAS BEEN CAPTURED!");
			break;
		case ILLEGAL:
			System.out.println("THIS MOVE IS ILLEGAL!");
			break;
		case INVALID_DIRECTION:
			System.out.println("INVALID DIRECTION!");
			break;
		case INVALID_STARTING_POSITION:
			System.out.println("INVALID STARTING POSITION");
			break;
		case NONE:
			//intentionally left blank
			break;
		case NOW_IN_CHECK:
			System.out.println("YOU NOW/STILL ARE IN CHECK!");
			break;
		case PAWN_CANNOT_CAPTURE:
			System.out.println("THAT PAWN CANNOT CAPTURE IN THAT DIRECTION!");
			break;
		case PAWN_HAS_ALREADY_MOVED:
			System.out.println("THAT PAWN CAN NO LONGER MOVE TWO SPACES!");
			break;
		case SAME_COLOR_CAPTURE:
			System.out.println("YOU CANNOT CAPTURE YOUR OWN PIECE!");
			break;
		case WRONG_TURN:
			System.out.println("IT IS NOT YOUR TURN!");
			break;
		case CANNOT_CASTLE:
			System.out.println("YOU CANNOT CASTLE THAT WAY!");
			break;
		default:
			break;
		
			
		}
	}
	

	
	public void drawPiece(String start, String finish) {
		int f1 = start.charAt(0)-96;
		int r1 = Character.getNumericValue(start.charAt(1));
		int f2 = finish.charAt(0)-96;
		int r2 = Character.getNumericValue(finish.charAt(1));
		VisualBoardMaker.moveOnBoard(vBoard, f1, r1, f2, r2, color);
	}
	
	public void erasePiece(int file, int rank) {
		VisualBoardMaker.erasePiece(vBoard, file, rank);
	}
	
	
	
	
}
