import java.awt.Color;

import javax.swing.JOptionPane;

public class DescribedPlace extends Plats {

	private String description; 

	
public DescribedPlace(String name, Position position, String valdkategori, String description, MapProgram mp) {
		super(name, position, valdkategori, mp);
		this.description = description; 

		
	}
	public String getPlaceDescription(){
		return description; 
	
	}
	public void showInfo() {
	DescribedForm t = new DescribedForm();
		String description = getPlaceDescription(); 
		String position =  "  {" +  getPosition() + "}";
		t.setNamn(getName()); 
		t.setDescription(description + position);
		JOptionPane.showMessageDialog(null, t,  "PlatsInfo:", JOptionPane.QUESTION_MESSAGE);

}
	public String getTitel() {
		return "Described"; 
	}	
	public String toString() {
	    return  getTitel() + ""  + super.getType()+  " " + super.getPosition() +  " " +  super.getName() +  " " + getPlaceDescription() + " " ;//METOD TO STRIN GÖF R ATT KALLA! 


	}
}
