import java.awt.Color;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.Border;
import java.awt.Graphics;

abstract class Plats extends JComponent { 

	private String name;
	private Position position;
	private Color color; 
	private boolean marked = false;
	private String Type;
	MapProgram mp;

	
	private int[] xet = { 0, 25, 50 }; 
	private int[] yet = { 0, 50, 0 };

	public Plats(String name, Position position, String valdkategori, MapProgram mp) {
		this.name = name;
		this.position = position;
		this.mp = mp;
		addMouseListener(new MyPlaceLyss());

	
		if (valdkategori == null) {
			this.color = Color.BLACK;
			this.Type = "None";
		}

		else if (valdkategori.equals("Bus")) { 
			this.color = Color.RED;
			this.Type = "Bus";
		}

		else if (valdkategori.equals("Underground")) {
			this.color = Color.BLUE;
			this.Type = "Underground";
		}

		else if (valdkategori.equals("Train")) {
			this.color = Color.GREEN;
			this.Type = "Train";
		}
	}

	public class MyPlaceLyss extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent mev) {
			int klicked = mev.getButton();

			if (klicked == 1) {
				setMarked();
				repaint();

			}
			if (klicked == 3) {
				showInfo();


			}

			repaint();
		}
	}

	public Position getPosition() {
		return position;

	}

	public String getType() {
		return Type;
	}

	abstract public void showInfo();

	public String getName() {
		return this.name;
	}

	public void setMarked() { // switchmark!
		marked = !marked; 
		if (marked) {
			mp.addmarkedplace(this);
		} else {
			mp.removemarkedplace(this);
		}
	}

	public boolean getMarked() {
		return marked;
	}


	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(color);
		g.fillPolygon(xet, yet, 3);
		
		if (marked) {
			
			g.setColor(color.ORANGE);
			g.fillOval(this.getWidth() - 35, this.getHeight() - 40, 20, 20);
		}

		else if (marked == false) {
		
			g.setColor(color);
			g.fillPolygon(xet, yet, 3);
		}
	}
}
