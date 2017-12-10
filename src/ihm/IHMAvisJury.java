package ihm;

import javax.swing.JFrame;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import io.SauvegardeRepertoire;
import operation.DecisionJury;
import operation.Statistiques;

import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.awt.Font;

/**
 * GestionStagesJuryIsi crée une fenétre graphique pour sélectionner le pv de jury ISI au format PDF
 * puis permet de construire le fichier TXT contenant le texte du PDF
 * puis permet d'avoir le résultat de l'étude faite par la Classe Filtrage dans un fichier CSV 
 * @author nigro
 * @version 1.0
 */
public class IHMAvisJury extends JFrame{

	//TODO regarder mes anciennes interfaces pour placer, redimesionner la fenetres
	//TODO voir si c’est possible de faire un aperçu d’un pdf dans un Panel en java

	private static final long serialVersionUID = 1L;

	private JTextField sourceTXT, cibleCSV, sourcePDF, cibleStat;
	private JLabel message;
	private File fileTXT, fileSourcePDF, fileDestPDF, fileDecisionJury, fileStats;
	private File dirAvisJury, dirStats, dirDatasTxt, dirAvisJuryCSV, dirAvisJuryPDF;
	private JButton exit, findPDF, conversionPdf_Txt, avisJury, statistique;

	/**
	 * Creation de l'application.
	 */
	public IHMAvisJury() {
		new JFrame();
		initialize();
		addListener();
	}

	/**
	 * conversion d'un PDF en une chaine de caractères
	 * @param pdfFile pointeur vers le fichier PDF 
	 * @return Une chaine de caractères avec le texte du fichier PDF
	 * @throws IOException erreur d'ouverture du PDF
	 */
	static String getText(File pdfFile) throws IOException {
		PDDocument doc = PDDocument.load(pdfFile);
		return new PDFTextStripper().getText(doc);
	}

	/**
	 * conversion d'un fichier PDF en un fichier TXT
	 * @param sourcePDF nom du fichier PDF
	 * @param destinationTXT nom du fichier résultat TXT
	 * @throws IOException erreur d'ouverture ou d'écriture
	 */
	static void maConversionPDFtoText (String sourcePDF, String destinationTXT) throws IOException{
		BufferedWriter ecritureAvecBuffer= null;
		try {
			String text = getText(new File(sourcePDF));
			ecritureAvecBuffer = new BufferedWriter(new FileWriter(destinationTXT));
			ecritureAvecBuffer.write(text);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			ecritureAvecBuffer.close();
		}
	}

	/**
	 * Initialisation du contenu de la fenêtre.
	 */
	private void initialize() {
		this.setVisible(true);
		this.setTitle("Gestion Stages Jury ISI");
		this.setBounds(100, 100, 800, 250);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(null);

		JLabel lblPDF = new JLabel("Source PDF");
		lblPDF.setBounds(10, 10, 77, 15);
		this.getContentPane().add(lblPDF);

		JLabel lblTxt = new JLabel("Cible TXT");
		lblTxt.setBounds(10, 65, 88, 15);
		this.getContentPane().add(lblTxt);

		JLabel lblCsv = new JLabel("Cible CSV");
		lblCsv.setBounds(10, 120, 88, 15);
		this.getContentPane().add(lblCsv);

		JLabel lblStat = new JLabel("Cible Stats");
		lblStat.setBounds(10, 175, 88, 15);
		this.getContentPane().add(lblStat);

		sourcePDF = new JTextField();
		sourcePDF.setBounds(108, 8, 627, 20);
		this.getContentPane().add(sourcePDF);

		sourceTXT = new JTextField();
		sourceTXT.setBounds(108, 59, 627, 20);
		this.getContentPane().add(sourceTXT);

		cibleCSV = new JTextField();
		cibleCSV.setBounds(108, 112, 627, 20);
		this.getContentPane().add(cibleCSV);

		cibleStat = new JTextField();
		cibleStat.setBounds(108, 170, 627, 20);
		this.getContentPane().add(cibleStat);

		// Bouton pour le chargement du fichier PDF 
		findPDF = new JButton("...");
		findPDF.setFont(new Font("Tahoma", Font.BOLD, 11));
		findPDF.setBounds(740, 10, 33, 20);
		this.getContentPane().add(findPDF);

		// Bouton de conversion PDF --> TXT 
		conversionPdf_Txt = new JButton("Conversion  PDF >- TXT");
		conversionPdf_Txt.setBounds(296, 32, 176, 20);
		this.getContentPane().add(conversionPdf_Txt);

		// Bouton pour la decisionJury 
		avisJury = new JButton("Generer Avis Jury");
		avisJury.setBounds(296, 85, 176, 20);
		this.getContentPane().add(avisJury);

		// Bouton pour les statistiques
		statistique = new JButton("Generer statistiques");
		statistique.setBounds(300, 140, 176, 20);
		this.getContentPane().add(statistique);

		message = new JLabel("");
		message.setBounds(10, 200, 1000, 20);
		this.getContentPane().add(message);

		// Bouton pour quitter l'application
		exit = new JButton("Quitter");
		exit.setBounds(670, 200, 110, 20);
		this.getContentPane().add(exit);

		lockButton();
	}

	/** Methode qui ajoute les listeners aux boutons*/
	private void addListener(){
		this.setFocusable(true);
		this.addMouseListener(new MouseAdapter() {//on donne le focus a la fenetre
			@Override
			public void mouseClicked(MouseEvent me) {
				requestFocusInWindow();
			}
		});

		//Ajout d'un listener de touche de clavier
		this.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_R:
					if(findPDF.isEnabled())
						choixRepertoire();
					break;
				case KeyEvent.VK_C:
					if(conversionPdf_Txt.isEnabled())
						convertirPDF();
					break;
				case KeyEvent.VK_A:
					if(avisJury.isEnabled())
						creationDecisionJury();
					break;
				case KeyEvent.VK_S:
					if(statistique.isEnabled())
						genererStatistique();
					break;
				case KeyEvent.VK_Q:
					System.exit(0); 
					break;
				default:
					break;
				}
			}
			@Override
			public void keyTyped(KeyEvent e) {}

			@Override
			public void keyReleased(KeyEvent e) {}
		});

		// Listener qui detecte si le textfield pdf est non vide
		sourcePDF.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				detectionPDF();
			}
		});

		findPDF.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(findPDF.isEnabled())
					choixRepertoire();
			}
		});

		conversionPdf_Txt.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(conversionPdf_Txt.isEnabled())
					convertirPDF();
			}
		});

		avisJury.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(avisJury.isEnabled())
					creationDecisionJury();
			}
		});

		statistique.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(statistique.isEnabled())
					genererStatistique();
			}
		});

		exit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				System.exit(0);
			}
		});
	}

	/**
	 * Methode declenché lors de la decision de jury
	 */
	private void creationDecisionJury() {		
		if(fileDecisionJury.exists()){
			int option = dialogEcrasmentFichier(fileDecisionJury); //on demande si on veut l'ecraser
			requestFocus();
			if(option == JOptionPane.OK_OPTION){
				DecisionJury.ecritureDecisionJury(fileSourcePDF.getAbsolutePath(), fileDestPDF.getAbsolutePath(), sourceTXT.getText(), cibleCSV.getText());
				dialogDecisionJury(fileDecisionJury);
			}
		}
		else
			DecisionJury.ecritureDecisionJury(fileSourcePDF.getAbsolutePath(), fileDestPDF.getAbsolutePath(), sourceTXT.getText(), cibleCSV.getText());
	}

	/**
	 * Methode qui detecte si le PDF est present est valable
	 */
	private void detectionPDF() {
		//FAIRE LE FILEPDF.exist
		if (sourcePDF.getText().equals("")){
			lockButton();
			message.setText("<html><span color='red'>Pas de PDF</span></html>");
		}
		else{
			if(new File(sourcePDF.getText()).exists()){
				message.setText("");
				unlockButton();
			}
			else
				message.setText("<html><span color='red'>le fichier "+ sourcePDF.getText() +" n'existe pas</span></html>");
		}
	}

	/**
	 * Methode declenché lors de la generation des statistiques
	 */
	private void genererStatistique() {
		//TODO on teste si la decision de jury a ete faite donc on teste su le fichier decision existe sinon on recreer la liste des etudiants du PDF
		if(fileStats.exists()){
			int option = dialogEcrasmentFichier(fileStats); //on demande si on veut l'ecraser
			requestFocus();
			if(option == JOptionPane.OK_OPTION){
				Statistiques.ecritureStatistiques(sourceTXT.getText(), fileStats.getAbsolutePath());
				dialogDecisionJury(fileStats);
			}
		}
		else
			Statistiques.ecritureStatistiques(sourceTXT.getText(), fileStats.getAbsolutePath());
	}

	/**
	 * Méthode declenché lors de la conversion du PDF en TXT
	 */
	private void convertirPDF() {
		if (!(sourcePDF.getText()=="") && !(sourceTXT.getText()=="")){
			try {
				if(fileTXT.exists()){
					//on demande si on veut l'ecraser
					int option = dialogEcrasmentFichier(fileTXT);
					requestFocus();
					if(option == JOptionPane.OK_OPTION){
						maConversionPDFtoText(sourcePDF.getText(), sourceTXT.getText());
						dialogSuccesConversionFichier(fileSourcePDF, fileTXT);
					}
				}
				else
					maConversionPDFtoText(sourcePDF.getText(), sourceTXT.getText());
			} catch (FileNotFoundException e2) {
				dialogFichierInexistant(fileSourcePDF);
			} catch (IOException e1) {
				dialogEchecConversionFichier(fileSourcePDF, fileTXT);
			}
		}
	}

	/**
	 * ChoixRepertoire permet de definir le repertoire le plus adequat pour le JFileChooser
	 */
	private void choixRepertoire(){
		String path = ""; //Chemin a parcourir
		JFileChooser chooser = null;
		if(SauvegardeRepertoire.getPaths().isEmpty()){//Si la liste des repertoires est vide
			chooser = new JFileChooser();
		}
		else{//si elle n'est pas vide
			Iterator<String> it = SauvegardeRepertoire.getPaths().iterator();
			while (it.hasNext() && path.equals("")) {
				String str = (String) it.next();
				File file = new File(str);

				if (file.exists()) //on recupere le premier repertoire possible	
					path = file.getAbsolutePath();
			}

			if(path.equals(""))//si on a pas trouvé de chemin coherent
				chooser = new JFileChooser();
			else
				chooser = new JFileChooser(path);
		}

		FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF","pdf");
		chooser.setFileFilter(filter);
		chooser.setMultiSelectionEnabled(false);
		int returnVal = chooser.showOpenDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			SauvegardeRepertoire.ajoutPath(chooser);//permet de sauvegarder les repertoires
			gestionFichier(chooser);
			unlockButton();
		}
	}

	/**
	 * Méthode qui gere les fichiers et les dossier en entrée/sortie
	 * @param chooser le choix du fichier PDF
	 */
	private void gestionFichier(JFileChooser chooser) {
		//Recuperation du PDF choisi dans le JFileChooser
		fileSourcePDF = new File(chooser.getSelectedFile().getAbsolutePath());
		sourcePDF.setText(fileSourcePDF.getAbsolutePath());

		//Creation du repertoire de decision jury
		dirDatasTxt = new File(fileSourcePDF.getParent()+"/DatasTxt");//creation du repertoire d'avis de jury
		if(!dirDatasTxt.exists())
			dirDatasTxt.mkdir();

		//Creation du repertoire de decision jury
		dirAvisJury = new File(fileSourcePDF.getParent()+"/AvisJury");//creation du repertoire d'avis de jury
		if(!dirAvisJury.exists())
			dirAvisJury.mkdir();

		//Creation du repertoire de decision jury CSV
		dirAvisJuryCSV = new File(dirAvisJury.getAbsolutePath()+"/csv");//creation du repertoire pour les CSV
		if(!dirAvisJuryCSV.exists())
			dirAvisJuryCSV.mkdir();

		//Creation du repertoire de decision jury PDF
		dirAvisJuryPDF = new File(dirAvisJury.getAbsolutePath()+"/pdf");//creation du repertoire pour les PDF
		if(!dirAvisJuryPDF.exists())
			dirAvisJuryPDF.mkdir();

		//Creation du repertoire de stat jury
		dirStats = new File(fileSourcePDF.getParent()+"/Stats");//creation du repertoire d'avis de jury
		if(!dirStats.exists())
			dirStats.mkdir();

		//creation du fichier txt pour les donnees des etudiants
		String nomFichierTXT = fileSourcePDF.getName().replace(".pdf", ".txt");
		fileTXT = new File(dirDatasTxt.getAbsolutePath()+"/"+nomFichierTXT);
		sourceTXT.setText(fileTXT.getAbsolutePath()); 	

		//creation du fichier de decision csv
		String nomDecisionJury = fileSourcePDF.getName().replace(".pdf", ".csv");
		fileDecisionJury = new File(dirAvisJuryCSV.getAbsolutePath()+"/"+nomDecisionJury);
		cibleCSV.setText(fileDecisionJury.getAbsolutePath()); 

		//creation du fichier de decision pdf
		String nomPDFDecisionJury = fileSourcePDF.getName();
		fileDestPDF = new File(dirAvisJuryPDF.getAbsolutePath()+"/"+nomPDFDecisionJury);

		//creation du fichier pour les stats
		String nomStats = fileSourcePDF.getName().replace(".pdf", ".csv");//TODO a voir dans quel format sera le fichier de stat
		fileStats = new File(dirStats.getAbsolutePath()+"/"+nomStats);
		cibleStat.setText(dirStats.getAbsolutePath()+"/"+nomStats);
	}

	/**
	 * Bloquer bouton empeche l'actiavtion des boutons tant que le chemin vers le pdf n'est pas la
	 */
	private void lockButton(){
		conversionPdf_Txt.setEnabled(false);
		avisJury.setEnabled(false);
		statistique.setEnabled(false);
	}

	/**
	 * Débloquer bouton empeche l'actiavtion des boutons tant que le chemin vers le pdf n'est pas la
	 */
	private void unlockButton() {
		conversionPdf_Txt.setEnabled(true);
		avisJury.setEnabled(true);
		statistique.setEnabled(true);	
	}

	/**
	 * Méthode affichant la boite de dialog pour demander l'ecrasement d'un fichier
	 * @param file le fichier qui doit etre ecraser
	 * @return ok ou non
	 */
	private int dialogEcrasmentFichier(File file) {
		String message = "Voulez-vous ecraser le fichier " + file.getName() +" ?";
		String message2 = "Confirmation de la suppression";
		int option = JOptionPane.showConfirmDialog(null, message, message2, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		return option;
	}

	/**
	 * Méthode affichant la boite de dialog pour afficher le succes de la conversion du fichier
	 * @param file le fichier source de la conversion
	 * @param file2 le fichier converti
	 */
	private void dialogSuccesConversionFichier(File file, File file2){
		String message = "le fichier " + file.getName() +"\na été converti en "+ file2.getName();
		JOptionPane.showMessageDialog(null, message, "Info", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Méthode affichant la boite de dialog pour afficher un fichier inexistant
	 * @param file le fichier inexistant
	 */
	private void dialogFichierInexistant(File file){
		JOptionPane.showMessageDialog(null, "le fichier " + file.getName() +"\n'existe pas", "Erreur", JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Méthode affichant la boite de dialog pour afficher l'echec de la conversion du fichier
	 * @param file le fichier source de la conversion
	 * @param file2 le fichier non converti
	 */
	private void dialogEchecConversionFichier(File file, File file2){
		JOptionPane.showMessageDialog(null, "le fichier " + file.getName() +"\nn'a pas été converti en "+ file2.getName(), "Erreur", JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Méthode affichant la boite de dialog pour demander l'ecrasement d'un fichier
	 * @param file le fichier qui doit etre ecraser
	 */
	private void dialogDecisionJury(File file) {
		JOptionPane.showMessageDialog(null, "le fichier de decisionJury " + file.getName() +" a été écrit", "Info", JOptionPane.INFORMATION_MESSAGE);

	}

}
