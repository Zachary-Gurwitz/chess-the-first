import java.awt.Color;



public class VisualBoardMaker {

	
	public static void moveOnBoard(VisualBoard board, int file1, int rank1, int file2, int rank2, Color pieceColor) {
		board.setColor(Board.SIZE-(rank2), file2-1, pieceColor);
		board.setColor(Board.SIZE-(rank1), file1-1, VisualBoard.boardColor);
	} 
	public static void initializePiecePosition(VisualBoard board, int file, int rank, Color pieceColor) {
		board.setColor(Board.SIZE-(rank), file-1, pieceColor);
	}
	
	public static void erasePiece(VisualBoard board, int file, int rank) {
		board.setColor(Board.SIZE-(rank), file-1, VisualBoard.boardColor);
	}
}
