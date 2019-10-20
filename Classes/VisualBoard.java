
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.Color;


public class VisualBoard extends JPanel {
	
	protected static final long serialVersionUID = 0L;
	protected static final int MARGIN_SIZE = 5;
	protected int scale;
	protected int scaleH;
	protected int scaleW;
	public static Color[][] colors;
	private double displayScale;
	private static int squareSize = 15;  // pixel size of each square
	private JFrame frame;
	public static Color boardColor = Color.LIGHT_GRAY;  
	
	protected VisualBoard() {
	}
	
	public VisualBoard(int scaleIn, double displayScaleIn) {
		scale = scaleIn;
		scaleH = scaleIn;
		scaleW = scaleIn;
		
		displayScale = displayScaleIn;
		squareSize*=displayScale;
		
		int overallSizeH = scaleH + 2 * MARGIN_SIZE;
		int overallSizeW = scaleW + 2 * MARGIN_SIZE;
		
		//makes all squares gray, change to checkered
		colors = new Color[overallSizeH][overallSizeW];
		for (int i = 0; i < overallSizeH; i++) {
			for (int j = 0; j < overallSizeW; j++) {

				colors[i][j] = boardColor;
			}
		}
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
				createAndShowFrame();
			}
		});	
		

	}
	public int getScale(){
		return scale;
	}
	public int getHt() {
		return scaleH;
	}
	public int getWd(){
		return scaleW;
	}
	
	private void createAndShowFrame() {
		frame = new JFrame("Drawing Grid");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize((scaleW + 2 * MARGIN_SIZE) * squareSize, 
				((scaleH) + 2 * MARGIN_SIZE) * squareSize);
		frame.setContentPane(this);
		frame.pack();
		frame.setVisible(true);
		frame.toFront();
		frame.setAlwaysOnTop(true);
	}
	
	public void setColor(int row, int col, Color color) {
		colors[row + MARGIN_SIZE][col + MARGIN_SIZE] = color;
		this.repaint();
	}
	
	public void kill() {
		if (frame != null)
			frame.dispose();
	}
	
	public Dimension getPreferredSize() {
		return new Dimension((scaleW + 2 * MARGIN_SIZE) * squareSize + 1, 
				((scaleH) + 2 * MARGIN_SIZE) * squareSize + 1);
	}
	
	//edit this for picture files i guess... 
	public void paint(Graphics g) {
		super.paint(g);
	
		int offset = MARGIN_SIZE * squareSize;
		
		for (int i = 0; i < scaleH + 2 * MARGIN_SIZE; i ++)
			for (int j = 0; j < scaleW + 2 * MARGIN_SIZE; j++) {
				g.setColor(colors[i][j]);
				g.fillRect(j * squareSize + 1, i * squareSize + 1, 
						squareSize, squareSize);
			}
		g.setColor(Color.BLACK);
		for (int i = 0; i < scaleW + 1; i++) 
			g.drawLine(offset + i * squareSize, offset, 
					offset + i * squareSize, offset + scaleH * squareSize);
		for (int j = 0; j < scaleH + 1; j++) 
			g.drawLine(offset, offset + j * squareSize, 
					offset + scaleW * squareSize, offset + j * squareSize);
	}
	

}
