import java.awt.Color;

public class Square {
	int file;
	int rank;
	String coordinates = "";
	boolean occupied = false;
	Piece occupiedBy = null;
	Color color;
	
	public Square(int file, int rank) {
		this.file = file;
		this.rank = rank;
		//define coordinates string
		coordinates += ((char) (96 + file)) + Integer.toString(rank);

	}
	
	//make the square occupied by a moving piece
	public void setOccupied(Piece piece) {
		occupied = true;
		occupiedBy = piece;
	
	}
	
	//make the square unoccupied after a moving piece
	public void setUnoccupied() {
		occupied = false;
		occupiedBy = null;
	}
}
