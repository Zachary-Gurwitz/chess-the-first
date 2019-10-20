import java.util.HashMap; 
import java.util.Map;

public class Board {
    static int SIZE = 8;
	static Square squares[][];
	public static HashMap<String, Square> squaresMap;
	public static Piece lastMoved = null;
	public static Piece pieceList[];
	static int numOfPieces; 
	
	VisualBoard vBoard;
	
	public Board(VisualBoard vBoard, int size) {
		SIZE = size;
		this.vBoard = vBoard;
		
		
	}
	
	//make Square objects in 2d array
	public void generateSquares() {
		squares = new Square[SIZE][SIZE];
		for(int file = 1; file <= SIZE; file++) {
			for(int rank = 1; rank <= SIZE; rank++) {
				squares[file-1][rank-1] = new Square(file, rank);
			}
		}
		
		
	}
	
	//setup the hashmap from coordinates to squares
	public void setupSquaresHashmap() {
		squaresMap = new HashMap<>(); 
		for(int file = 1; file <= SIZE; file++) {
			for(int rank = 1; rank <= SIZE; rank++) {
				squaresMap.put(squares[file-1][rank-1].coordinates, squares[file-1][rank-1]);
			}
		}
	}
	
	
	
	public void showBlankBoard() {
		for(int file = 0; file < SIZE; file++) {
			for(int rank = 0; rank < SIZE; rank++) {
				System.out.print(squares[file][rank].coordinates);
			}
		}
	}
	
	public void setUpNormalGame() {
		if(SIZE == 8) {
		numOfPieces = 32;
		//0->(numOfPieces-1) is for actual pieces,
		//numOfPieces->(2*numOfPieces-1) is for promotion,
		//rest is for castling
		pieceList = new Piece[numOfPieces*3];
		
		//pawns
		for(int i = 0; i < 8; i ++) {
			pieceList[i] = new Pawn(vBoard,i+1,2,"white");
		}
		
		
		
		for(int i = 0; i < 8; i ++) {
			pieceList[i+8] = new Pawn(vBoard,i+1,7,"black");
		}
		
		
		//rooks
		pieceList[16] = new Rook(vBoard,1,1,"white");
		pieceList[17] = new Rook(vBoard,8,1,"white");
		pieceList[18] = new Rook(vBoard,1,8,"black");
		pieceList[19] = new Rook(vBoard,8,8,"black");
		
		//knights
		pieceList[20] = new Knight(vBoard,2,1,"white");
		pieceList[21] = new Knight(vBoard,7,1,"white");
		pieceList[22] = new Knight(vBoard,2,8,"black");
		pieceList[23] = new Knight(vBoard,7,8,"black");
		
		//bishops
		pieceList[24] = new Bishop(vBoard,3,1,"white");
		pieceList[25] = new Bishop(vBoard,6,1,"white");
		pieceList[26] = new Bishop(vBoard,3,8,"black");
		pieceList[27] = new Bishop(vBoard,6,8,"black");
		
		//queens
		pieceList[28] = new Queen(vBoard,4,1,"white");
		pieceList[29] = new Queen(vBoard,4,8,"black");
		
		//kings
		pieceList[30] = new King(vBoard,5,1,"white");
		pieceList[31] = new King(vBoard,5,8,"black");
		
		
		} else {
			System.out.println("INVALID SETUP: BOARD ISN'T 8x8");
		}
		
	}
	
	public void setupCustomGame() {
		pieceList = new Piece[6];
		pieceList[0]= new Rook(vBoard,2,7,"WHITE");
		pieceList[1]= new Rook(vBoard,5,8,"WHITE");
		pieceList[2]= new Rook(vBoard,4,5,"WHITE");
		pieceList[3]= new King(vBoard,1,8,"WHITE");
		pieceList[4]= new King(vBoard,8,6,"BLACK");
		pieceList[5]= new Bishop(vBoard,3,6,"BLACK" );
	}
	
	
	public void drawAsciiBoard() {
		
	}
	
	//if i want a rectangle at some point
	public Board(int length, int width) {
		
	}
}
