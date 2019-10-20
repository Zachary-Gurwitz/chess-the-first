import java.util.Scanner;


public class Game {
	final static int TEAMS = 2;
	final static int NUM_OF_SQUARES_CASTLING = 2;
	public static int numOfCastles = 0;
	static int teamTurn = 0;
	static int moveNum = 1;
	private static float turnNum = 1;
	static boolean wasJustValidMove = false;
	public static int numOfPromotions = 0;
	public static boolean castling = false;
	public static int drawOffer = 0;
	final static int SIZE = 8;
	
	public static boolean checkmate = false;
	public static boolean draw = false;
	public static boolean abort = false;
	
	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		
		
		VisualBoard vb = new VisualBoard(SIZE,1);
		
		Board b = new Board(vb, SIZE);
		
		
		b.generateSquares();
		b.setupSquaresHashmap();
		
		b.setUpNormalGame();
		
		
		//b.setupCustomGame();
	
		
		while(!checkmate && !draw && !abort && !draw) {

			askMove(vb, sc);
		
		}
		if(abort) {
			System.out.println("GAME ABORTED.");
		}
		if(draw) {
			System.out.println("IT'S A DRAW!");
		}
		if(checkmate) {
			System.out.println("GOOD GAME!");
		}
		
		sc.close();
	}
	
	//figures out whose turn it is
	public static void turnIncrement() {
		teamTurn++;
		teamTurn = teamTurn % TEAMS;
		turnNum += .5;
		moveNum = (int)turnNum;
	}
	
	public static void askMove(VisualBoard vBoard, Scanner sc) {
		
		String move;
		String start = "";
		String finish = "";
		boolean validMoveSyntax = true;
		
		
		
		do{
			printTurn();
			move = sc.next();
			if(!move.toUpperCase().equals("DRAW") && drawOffer !=0 && drawOffer < TEAMS ) {
				drawOffer = 0;
				System.out.println("DRAW DECLINED.");
				turnIncrement();
				
				
			}
			
			if(move.toUpperCase().equals("DRAW")) {
				validMoveSyntax = true;
				drawOffer += 1;
				turnIncrement();
			}
			else if(move.toUpperCase().equals("O-O") || move.toUpperCase().equals("O-O-O")) {
				validMoveSyntax = true;
				start = move;
				finish = move;
				castling = true;
			}
			else if(move.toLowerCase().equals("abort")){
				abort = true;
				validMoveSyntax = true;
			}

			else if(move.length() != 5 ||(move.length() >= 3 && move.charAt(2) != '_')) {
				System.out.println("INVALID MOVE SYNTAX");
				validMoveSyntax = false;
			} 
			else if(move.length() == 5) {
				start = move.substring(0, 2);
				finish = move.substring(3, 5);
				validMoveSyntax = true;
			}

			

		} while (validMoveSyntax == false);
		

		if(drawOffer == TEAMS) {
			draw = true;
		}
		
		if(castling || (Board.squaresMap.containsKey(start) && Board.squaresMap.containsKey(finish)) ) {
			if(!start.toUpperCase().equals("O-O-O") && !start.toUpperCase().equals("O-O") ) {
				if(Board.squaresMap.get(start).occupied) {
					//move function
					Board.squaresMap.get(start).occupiedBy.move(start,finish,true);
					
					if(wasJustValidMove) {
						promotion(vBoard, finish, sc);
						if(Board.squaresMap.get(finish).occupiedBy.attackingKing(Board.squaresMap.get(finish).occupiedBy.team)) {
							if(Game.checkMate(Board.squaresMap.get(finish).occupiedBy.team)) {
								System.out.println("CHECKMATE!");
								Game.checkmate = true;
							} else {
								System.out.println("CHECK!");
							}
						} else if(Game.checkMate(Board.squaresMap.get(finish).occupiedBy.team)) {
							Game.draw = true;
						}
					}
	
				} else {
					System.out.println("NO PIECE AT THAT STARTING LOCATION");
					askMove(vBoard, sc);
				}
			} else {
				if(teamTurn == 0) {
					Board.squaresMap.get(Piece.friendlyKingCoordinates("WHITE")).occupiedBy.move(start,finish,true);
				} else if (teamTurn == 1) {
					Board.squaresMap.get(Piece.friendlyKingCoordinates("BLACK")).occupiedBy.move(start,finish,true);
				}
			}	
		} else if(!move.toUpperCase().equals("abort") && !move.toUpperCase().equals("DRAW")){
			System.out.println("NON-VALID START OR FINISH LOCATION");
			askMove(vBoard, sc);
		}
		
	}
	public static void promotion(VisualBoard vBoard, String finish, Scanner sc) {
		if(Board.squaresMap.get(finish).occupiedBy.promoting(finish)) {
			System.out.println("WHICH PIECE DO YOU WANT TO PROMOTE TO?");
			System.out.println("ENTER \"q\", \"r\", \"b\", or \"n\":");
			String pieceCode;
			PieceType promotionPiece; 
			do {
				pieceCode = sc.next();
			} while (!pieceCode.equals("q") && !pieceCode.equals("r") && !pieceCode.equals("b") && !pieceCode.equals("n"));
			if(pieceCode.equals("q")) {
				promotionPiece = PieceType.QUEEN;
			}
			else if(pieceCode.equals("r")) {
				promotionPiece = PieceType.ROOK;
			}
			else if(pieceCode.equals("b")) {
				promotionPiece = PieceType.BISHOP;
			}
			else if(pieceCode.equals("n")) {
				promotionPiece = PieceType.KNIGHT;
			}
			else {
				promotionPiece = PieceType.EMPTY;
			}
			actuallyPromote(vBoard, promotionPiece, finish);
			
		}
	}
	
	public static void actuallyPromote(VisualBoard vBoard, PieceType promotionPiece, String finish) {
		
		int newFile = Board.squaresMap.get(finish).file;
		int newRank = Board.squaresMap.get(finish).rank;
		
		String newTeam = Board.squaresMap.get(finish).occupiedBy.team;
		Board.squaresMap.get(finish).occupiedBy.alive = false;
		Board.squaresMap.get(finish).setUnoccupied();
		
		switch(promotionPiece) {

			case ROOK:
				Board.pieceList[numOfPromotions+Board.numOfPieces] = new Rook(vBoard,newFile, newRank, newTeam);
				break;
				
			case QUEEN:
				Board.pieceList[numOfPromotions+Board.numOfPieces] = new Queen(vBoard,newFile, newRank, newTeam);
				break;
				
			case BISHOP:
				Board.pieceList[numOfPromotions+Board.numOfPieces] = new Bishop(vBoard,newFile, newRank, newTeam);
				break;
			
			case KNIGHT:
				Board.pieceList[numOfPromotions+Board.numOfPieces] = new Knight(vBoard,newFile, newRank, newTeam);
				break;
				
			default:
				System.out.println("ERROR, TRYING TO PROMOTE TO INVALID PIECE");
		}
		numOfPromotions++;
	}
	
	//only call if putting in check!
	public static boolean checkMate(String team) {
		String otherTeam;
		if(team.equals("WHITE")) {
			otherTeam = "BLACK";
		} else {
			otherTeam = "WHITE";
		}
		for(String checkStart : Board.squaresMap.keySet()) {
			for(String checkFinish : Board.squaresMap.keySet()) {
				if(Board.squaresMap.get(checkStart).occupied) {
					if(Board.squaresMap.get(checkStart).occupiedBy.team.equals(otherTeam)) {
						if(Board.squaresMap.get(checkStart).occupiedBy.checkIfValidMove(checkStart, checkFinish)) {
							if(!Board.squaresMap.get(checkStart).occupiedBy.TYPE.equals(PieceType.KING)) {
								if(!Board.squaresMap.get(checkStart).occupiedBy.ownKingAttackedMovingNonKing(otherTeam, checkStart, checkFinish)) {
									return false;
								}
							} else {
								if(!Board.squaresMap.get(checkStart).occupiedBy.ownKingAttackedMovingKing(otherTeam, checkStart, checkFinish)) {
									return false;
								}
							}
						}
					}
				}
			}
		}
		
		return true;
	}
	


	public static void printTurn() {
		System.out.print(moveNum + " " + teamTurn + " ");
	}
}
