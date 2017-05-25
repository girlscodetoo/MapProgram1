import javax.swing.*;
import java.awt.*; //Borderklasserna 
import java.util.HashMap;
import java.util.Map;
import java.awt.event.*;
import java.io.File;
import javax.swing.filechooser.*;
import java.util.List;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;

public class FileRead extends JFrame {
	private static final String FILENAME = "D:javafaltet.places 2.txt";

	public static void main(String[] args) {
		
		new FileRead(); 

		try {

FileReader infil = new FileReader(FILENAME); 

			BufferedReader ine = new BufferedReader(infil);
			
	FileWriter utfil = new FileWriter(FILENAME); 

			String line;
			while ((line = ine.readLine()) != null) {
				System.out.println(line);

				
			}
			infil.close();
			ine.close();
		} catch (FileNotFoundException e) {
			System.err.println("kan inte öppna" + FILENAME);
		} catch (IOException e) {
			System.err.println("fel" + e.getMessage());
		}
	}
}
