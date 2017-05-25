
import javax.swing.*;

public class CoordinatesForm extends JPanel {

	private JTextField xfält = new JTextField(10);
	private JTextField yfält = new JTextField(5);

	public CoordinatesForm() {

		JPanel firstline = new JPanel();
		add(firstline);
		firstline.add(new JLabel("X Kordinaten:"));
		firstline.add(xfält);

		JPanel secondline = new JPanel();
		add(secondline);
		secondline.add(new JLabel("Y Kordinaten;"));
		secondline.add(yfält);
	}

	public int gety() {
		return Integer.parseInt(xfält.getText());
	}

	public int getx() {
		return Integer.parseInt(yfält.getText()); 
	}
}
