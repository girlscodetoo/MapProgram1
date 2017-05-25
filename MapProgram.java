
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*; //Borderklasserna 
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.filechooser.*;
import java.util.List;
import java.util.Locale.Category;

import javax.swing.filechooser.FileNameExtensionFilter;

public class MapProgram extends JFrame {

	// Structures
	Map<Position, Plats> places = new HashMap<>();
	Map<String, List<Plats>> placespername = new HashMap<>();
	HashSet<Plats> busCategory = new HashSet<Plats>();
	HashSet<Plats> undergroundCategory = new HashSet<Plats>();
	HashSet<Plats> trainCategory = new HashSet<Plats>();
	HashSet<Plats> markedPlaces = new HashSet<Plats>();

	// CategoryList
	String[] categories = { "Bus", "Underground", "Train" };
	JList<String> jlistCateg = new JList<>(categories);
	FileFilter textfileFilter = new FileNameExtensionFilter("textfile", "txt", "csv");
	JTextField searchfield;
	boolean checkedchanged = false;

	// JButtons
	JButton newB;
	JRadioButton namedR;
	JRadioButton describedB;

	// Menue
	JMenu archive;
	ButtonGroup bg;
	JOptionPane inputCoor;

	JFileChooser jfc = new JFileChooser(".");
	JScrollPane scroll = null;
	BildPanel bp = null;
	NamedPlace np;
	DescribedPlace dp;
	FileFilter bildFilter;

	public MapProgram() {
		super("Inlämning");

		bildFilter = new FileNameExtensionFilter("bild", "jpg", "gif", "png");
		jfc.setFileFilter(bildFilter);

		// Min Meny
		JMenuBar archivemeny = new JMenuBar();
		JMenuItem New = new JMenuItem("New Map");
		New.addActionListener(new NewLyssArchive());

		JMenuItem Load = new JMenuItem("Load Places");
		Load.addActionListener(new loadLyssMenue());
		JMenuItem Save = new JMenuItem("Save");
		Save.addActionListener(new saveLyssMenue());
		JMenuItem Exit = new JMenuItem("Exit");
		Exit.addActionListener(new ExitLyssArchive());

		archive = new JMenu("Archive");
		archive.add(New);
		archive.add(Load);
		archive.add(Save);
		archive.add(Exit);
		archivemeny.add(archive);
		setJMenuBar(archivemeny);

		JPanel ontop = new JPanel();
		ontop.getLayout();

		JPanel subPanel = new JPanel();
		subPanel.setLayout(new BoxLayout(subPanel, BoxLayout.Y_AXIS));

		ontop.add(subPanel, BorderLayout.WEST);

		newB = new JButton("New");
		newB.addActionListener(new NyLyssArchive());
		ontop.add(newB);

		namedR = new JRadioButton("Named");
		subPanel.add(namedR);

		describedB = new JRadioButton("Described");
		subPanel.add(describedB);

		// Mitt SearchFält
		searchfield = new JTextField("Search", 10);
		ontop.add(searchfield);

		JButton hidebutton = new JButton("Hide");
		hidebutton.addActionListener(new HideButtonLyss());
		ontop.add(hidebutton);

		JButton Searchb = new JButton("Search");
		Searchb.addActionListener(new SearchbLyssArchive());
		ontop.add(Searchb);

		JButton Removeb = new JButton("Remove");
		Removeb.addActionListener(new RemovebLyss());
		ontop.add(Removeb);

		JButton Coordinatesb = new JButton("Coordinates");
		Coordinatesb.addActionListener(new CoordinatesbLyssArchive());
		ontop.add(Coordinatesb);

		// Man ska inte kunna välja alla samtidigt
		bg = new ButtonGroup();
		bg.add(namedR);
		bg.add(describedB);
		add(ontop, BorderLayout.NORTH);

		JPanel side = new JPanel();
		JPanel subSide = new JPanel();

		JLabel Categories = new JLabel("Categorier");
		subSide.add(Categories);
		jlistCateg.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jlistCateg.addListSelectionListener(new ListLyss());

		JScrollPane scroller = new JScrollPane(jlistCateg);

		subSide.add(scroller);
		JButton HideCategory = new JButton("Hide Category");
		HideCategory.addActionListener(new HidebLyssArchive());
		subSide.add(HideCategory);
		subSide.setLayout(new BoxLayout(subSide, BoxLayout.Y_AXIS));

		side.add(subSide);
		add(BorderLayout.EAST, side);

		// Visa Frame
		setSize(1000, 300);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	class NyLyssArchive implements ActionListener {
		public void actionPerformed(ActionEvent ave) {
			if (bp == null) {
				return;
			}
			bp.addNewPlaceListener();
			checkedchanged = true;
		}
	}

	class SearchbLyssArchive implements ActionListener {
		public void actionPerformed(ActionEvent ave) {

			try {
				deselectMarked();
				String usersearch = searchfield.getText();
				if (usersearch.equals("")) {
					return;
				}

				List<Plats> samma = placespername.get(usersearch);

				if (samma == null) {
					JOptionPane.showMessageDialog(MapProgram.this, " Finns ingen med detta namn!");
					return;
				}

				for (Plats p : samma) {
					p.setVisible(true);
					p.setMarked();
					searchfield.setText("");
					checkedchanged = true;
				}
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(MapProgram.this, "FEL inmatning");

			}
		}
	}

	class HideButtonLyss implements ActionListener {
		public void actionPerformed(ActionEvent ave) {
			for (Plats p : markedPlaces) {
				p.setVisible(false);
				checkedchanged = true;

			}
		}
	}

	public void checkedchanged() {
		if (checkedchanged == true) {
			JOptionPane.showMessageDialog(MapProgram.this,
					"Du har data som inte är sparat, säker att du vill fortsätta?");
		} else if (checkedchanged == false)
			return;
	}

	public void deselectMarked() {
		Set<Plats> marked = new HashSet<>();
		for (Plats p : markedPlaces)
			if (p.getMarked()) {
				marked.add(p);
			}

		for (Plats pl : marked)
			pl.setMarked();
		checkedchanged = true;
	}

	public void addplacetolist(Plats p) {

		Position position = p.getPosition();
		places.put(position, p);

		List<Plats> namneti = placespername.get(p.getName());
		if (namneti == null) {
			namneti = new ArrayList<Plats>();
			placespername.put(p.getName(), namneti);
		}
		namneti.add(p);

		if (p.getType() != null) {

			if (p.getType().equals("Bus")) {
				busCategory.add(p);
			}
			if (p.getType().equals("Train")) {
				trainCategory.add(p);

			} else if (p.getType().equals("Underground")) {
				undergroundCategory.add(p);
			}
		}
		checkedchanged = true;
	}

	public void removemarkedplace(Plats p) {
		markedPlaces.remove(p);
	}

	public void addmarkedplace(Plats p) {
		markedPlaces.add(p);
		checkedchanged = true;
	}

	class ListLyss implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent lev) {
			String category = jlistCateg.getSelectedValue();

			try {
				if (category.equals("Bus")) {
					for (Plats p : busCategory)
						p.setVisible(true);
				}

				if (category.equals("Underground")) {
					for (Plats p : undergroundCategory)
						p.setVisible(true);
				}

				else if (category.equals("Train")) {
					for (Plats p : trainCategory)
						p.setVisible(true);
				}
				checkedchanged = true;
			} catch (NullPointerException Io) {

			}
		}
	}

	class CoordinatesbLyssArchive implements ActionListener {
		public void actionPerformed(ActionEvent ave) {

			try {
				inputCoor = new JOptionPane();
				CoordinatesForm c = new CoordinatesForm();
				int svar = JOptionPane.showConfirmDialog(MapProgram.this, c, "Input Coordinates",
						JOptionPane.OK_CANCEL_OPTION);

				if (svar != JOptionPane.OK_OPTION)
					return;

				int svarx = c.getx();
				int svary = c.gety();
				Position a = new Position(svary, svarx);
				System.out.print(a);
				Plats p = null;

				if (places.containsKey(a)) {
					p = places.get(a);

					deselectMarked();
					p.setMarked();
					p.setVisible(true);
					bp.add(p);
					bp.revalidate();
					bp.repaint();
					checkedchanged = true;
				}
				if (p == null) {
					JOptionPane.showMessageDialog(null, "Det finns ingen kordinat på den här platsen");
				}
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "Fel inmatning!");
			}
		}
	}

	class loadLyssMenue implements ActionListener {
		public void actionPerformed(ActionEvent ave) {
			jfc.setFileFilter(textfileFilter);
			checkedchanged();
			try {
				int svar = jfc.showOpenDialog(null);
				if (svar != JFileChooser.APPROVE_OPTION)
					return;

				File fil = jfc.getSelectedFile();

				FileReader infil = new FileReader(fil);
				BufferedReader in = new BufferedReader(infil);
				String line;
				while ((line = in.readLine()) != null) {
					String[] tokens = line.split(",");

					String titel = tokens[0];
					String TypeKategori = tokens[1];
					int x = Integer.parseInt(tokens[2]);
					int y = Integer.parseInt(tokens[3]);
					String namne = tokens[4];

					Position da = new Position(x, y);

					if (titel.equals("Named")) {
						NamedPlace np = new NamedPlace(namne, da, TypeKategori, MapProgram.this);
						addplacetolist(np);
						np.setBounds(x - 25, y - 50, 50, 50);
						bp.add(np);
						bp.revalidate();
						bp.repaint();

					} else {
						String desc = tokens[5];
						DescribedPlace db = new DescribedPlace(namne, da, TypeKategori, desc, MapProgram.this);
						addplacetolist(db);
						db.setBounds(x - 25, y - 50, 50, 50);
						bp.add(db);
						bp.revalidate();
						bp.repaint();

					}
					checkedchanged = true;

				}
				in.close();
				infil.close();

			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(MapProgram.this, "Kan inte öppna filen!");
			} catch (IOException e) {
				JOptionPane.showMessageDialog(MapProgram.this, "Fel: " + e.getMessage());
			}
		}
	}

	class saveLyssMenue implements ActionListener {
		public void actionPerformed(ActionEvent ave) {
			jfc.setFileFilter(textfileFilter);

			try {
				int svar = jfc.showSaveDialog(null);
				if (svar != JFileChooser.APPROVE_OPTION) {
					return;
				}

				File fil = jfc.getSelectedFile();
				String filnamn = fil.getName();

				FileWriter utfil = new FileWriter(filnamn, true);
				PrintWriter out = new PrintWriter(utfil);

				for (Plats p : places.values()) {

					if (p instanceof NamedPlace)
						out.println(
								((NamedPlace) p).getTitel() + "," + p.getType() + "," + p.getPosition().getPositionX()
										+ "," + p.getPosition().getPositionY() + "," + p.getName());

					else if (p instanceof DescribedPlace)
						out.println(((DescribedPlace) p).getTitel() + "," + p.getType() + ","
								+ p.getPosition().getPositionX() + "," + p.getPosition().getPositionY() + ","
								+ p.getName() + "," + ((DescribedPlace) p).getPlaceDescription());
				}
				out.close();
				utfil.close();

			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(MapProgram.this, "Filen kan inte nås");
			} catch (IOException e) {
				JOptionPane.showMessageDialog(MapProgram.this, "Fel :" + e.getMessage());
			}
		}
	}

	class HidebLyssArchive implements ActionListener {
		public void actionPerformed(ActionEvent ave) {
			String category = jlistCateg.getSelectedValue();

			if (category.equals("Bus")) {
				Iterator<Plats> iter = busCategory.iterator();
				while (iter.hasNext()) {
					Plats bus = iter.next();
					bus.setVisible(false);

				}
			}

			else if (category.equals("Train")) {
				Iterator<Plats> itertwo = trainCategory.iterator();
				while (itertwo.hasNext()) {
					Plats train = itertwo.next();
					train.setVisible(false);

				}
			} else if (category.equals("Underground")) {

				Iterator<Plats> iterthree = undergroundCategory.iterator();
				while (iterthree.hasNext()) {
					Plats underground = iterthree.next();
					underground.setVisible(false);

				}
			}
			checkedchanged = true;
			jlistCateg.clearSelection();
		}
	}

	class NewLyssArchive implements ActionListener {
		public void actionPerformed(ActionEvent ave) {
			checkedchanged();

			jfc.setFileFilter(bildFilter);

			int svar = jfc.showOpenDialog(null);

			if (svar != JFileChooser.APPROVE_OPTION)
				return;

			File fil = jfc.getSelectedFile();
			String filnamn = fil.getAbsolutePath();
			bp = new BildPanel(filnamn);

			if (scroll != null)
				remove(scroll);
			scroll = new JScrollPane(bp);
			add(scroll);
			validate();
			repaint();
			checkedchanged = true;
		}
	}

	class RemovebLyss implements ActionListener {
		public void actionPerformed(ActionEvent ave) {

			try {
				Iterator<Plats> iter = markedPlaces.iterator();
				while (iter.hasNext()) {
					Plats p = iter.next();
					iter.remove();
					removeaPlaceFromAll(p);
				}
				checkedchanged = true;

			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(MapProgram.this, "Fel inmatning");
			}
		}
	}

	public void removeaPlaceFromAll(Plats p) {
		bp.remove(p);

		String pName = p.getName();

		Collection<Plats> namnen = places.values();
		Iterator<Plats> it = namnen.iterator();
		while (it.hasNext()) {
			Plats h = it.next();
			if (h != null)
				it.remove();
		}

		for (Map.Entry<String, List<Plats>> entry : placespername.entrySet()) {
			String name = entry.getKey();
			List<Plats> platser = entry.getValue();
			if (name.equals((pName))) {
				Iterator<Plats> platsItt = platser.iterator();
				while (platsItt.hasNext()) {
					Plats listPlats = platsItt.next();
					if (listPlats.equals(p)) {
						platsItt.remove();
					}

				}
			}
			if (platser.isEmpty()) {
				placespername.remove(name);
			}
		}

		busCategory.remove(p);
		undergroundCategory.remove(p);
		trainCategory.remove(p);
		bp.repaint();
		bp.revalidate();

	}

	class ExitLyssArchive implements ActionListener {
		public void actionPerformed(ActionEvent ave) {
			checkedchanged();

			busCategory.clear();
			undergroundCategory.clear();
			markedPlaces.clear();
			trainCategory.clear();
			placespername.clear();
			places.clear();
			System.exit(0);
		}
	}

	public static void main(String[] args) {
		new MapProgram();

	}

	class BildPanel extends JPanel {

		private ImageIcon bild;
		PlaceLyss placeLyss = new PlaceLyss();
		Position p;

		public BildPanel(String filnamn) {
			bild = new ImageIcon(filnamn);
			int w = bild.getIconWidth();
			int h = bild.getIconHeight();

			setPreferredSize(new Dimension(w, h));
			setLayout(null);
		}

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(bild.getImage(), 0, 0, this);
		}

		public void addNewPlaceListener() {
			this.addMouseListener(new PlaceLyss());
			this.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			newB.setEnabled(false);
		}

		class PlaceLyss extends MouseAdapter {
			int x;
			int y;

			@Override
			public void mouseClicked(MouseEvent mev) {
				try {

					x = mev.getX();
					y = mev.getY();
					p = new Position(x, y);

					if (places.containsKey(p)) {
						JOptionPane.showMessageDialog(null, "Det finns redan en plats här! TEST " + places);
						return;
					}

					String choosencategory = jlistCateg.getSelectedValue();
					String answer = "";

					if (namedR.isSelected()) {
						answer = JOptionPane.showInputDialog(null, "platsinfo" + "Name");
						if (answer.equals("")) {
							JOptionPane.showMessageDialog(MapProgram.this, "Du måste fylla i ett namn!");
							return;
						}

						np = new NamedPlace(answer, p, choosencategory, MapProgram.this);
						np.setBounds(this.x - 25, this.y - 50, 50, 50);
						add(np);
						addplacetolist(np);

					}

					if (describedB.isSelected()) {
						DescribedForm f = new DescribedForm();
						JOptionPane.showMessageDialog(MapProgram.this, f, "PlatsInfo:", JOptionPane.QUESTION_MESSAGE);

						String name = f.getNamn();
						if (name.equals("")) {
							JOptionPane.showMessageDialog(MapProgram.this, "Du måste fylla i ett namn!");
							return;
						}

						String description = f.getDescription();
						if (description.equals("")) {
							JOptionPane.showMessageDialog(MapProgram.this, "Du måste fylla i ett beskrivning!");
							return;
						}

						dp = new DescribedPlace(name, p, choosencategory, description, MapProgram.this);
						dp.setBounds(this.x - 25, this.y - 50, 50, 50);
						add(dp);
						addplacetolist(dp);

					}

					repaint();
					bp.removeMouseListener(this);
					BildPanel.this.setCursor(Cursor.getDefaultCursor());
					checkedchanged = true;

					jlistCateg.clearSelection();
					bg.clearSelection();

					newB.setEnabled(true);
				} catch (NumberFormatException e) {
				}
			}
		}
	}
}
