import java.awt.Color;

import javax.swing.JOptionPane;

public class NamedPlace extends Plats {

	
	public NamedPlace(String name, Position position, String valdkategori, MapProgram mp) {
		super(name, position, valdkategori, mp);
		
	}
public String getTitel() {
	return "Named"; 
}
	public void showInfo() {
		String info = "" + getName() + " {" + getPosition() + "} ";
		JOptionPane.showMessageDialog(null, info, "PlatsInfo:", JOptionPane.QUESTION_MESSAGE);
}
	public String toString() {
	    return  getTitel() + " "  +  super.getType() + " " + super.getPosition() + " " +  super.getName();


	}
}
