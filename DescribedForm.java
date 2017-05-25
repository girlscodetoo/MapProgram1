

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DescribedForm extends JPanel {
	
	private JTextField namnfält = new JTextField(10); 
	private JTextField descriptions = new JTextField(19); 

public DescribedForm() {
	
	
	setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	JPanel namnfältet = new JPanel(); 
	add(namnfältet);
	namnfältet.add(new JLabel("Name:"));
	namnfältet.add(namnfält);
	
	JPanel describefält = new JPanel(); 
	add(describefält); 
	describefält.add(new JLabel ("Descripition")); 
	describefält.add(descriptions); 
	
}
public	String getNamn(){
	return namnfält.getText(); 
}
public	String getDescription(){
	return descriptions.getText(); 
}
public	void setNamn(String b){
	namnfält.setText(b); 
}
public	void setDescription(String d){
	descriptions.setText(d); 

}

}