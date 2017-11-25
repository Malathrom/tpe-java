package ihm;

import javax.swing.JFrame;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import io.SauvegardeRepertoire;
import operation.DecisionJury;

import javax.swing.JButton;
import javax.swing.JFileChooser;
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

	//TODO faire la gestion d'exception pour le click des boutons notament
	//TODO regarder mes anciennes interfaces pour placer, redimesionner la fenetres
	//TODO voir si c’est possible de faire un aperçu d’un pdf dans un Panel en java

	private static final long serialVersionUID = 1L;

	private JTextField sourceTXT;
	private JTextField cibleCSV;
	private JTextField sourcePDF;
	private File fileTXT, filePDF, fileDecisionJury, fileStats;
	private File dirAvisJury, dirStats, dirDatasTxt;;
	private JButton exit, findPDF, conversionTxt_Csv, conversionPdf_Txt, avisJury, statistique;

	/**
	 * Creation de l'application.
	 */
	public IHMAvisJury() {
		new JFrame();
		initialize();
		addListener();
	}

	/**
	 * conversion d'un PDF en une chaine de caract�res
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
		this.setBounds(100, 100, 799, 241);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(null);

		JLabel lblTxt = new JLabel("Source TXT");
		lblTxt.setBounds(10, 65, 88, 14);
		this.getContentPane().add(lblTxt);

		JLabel lblCsv = new JLabel("Cible CSV");
		lblCsv.setBounds(10, 115, 88, 14);
		this.getContentPane().add(lblCsv);

		sourceTXT = new JTextField();
		sourceTXT.setBounds(108, 59, 627, 20);
		sourceTXT.setColumns(10);
		this.getContentPane().add(sourceTXT);

		cibleCSV = new JTextField();
		cibleCSV.setBounds(108, 112, 627, 20);
		cibleCSV.setColumns(10);
		this.getContentPane().add(cibleCSV);

		JLabel isiLevel = new JLabel("Niveau ISI");
		isiLevel.setBounds(10, 153, 88, 14);
		this.getContentPane().add(isiLevel);

		// Bouton lançant la conversion TXT --> "Filtrage" --> CSV 
		conversionTxt_Csv = new JButton("Conversion  TXT -> CSV");
		conversionTxt_Csv.setBounds(296, 83, 176, 23);
		//TODO bouton supprimé this.getContentPane().add(conversionTxt_Csv);

		// Bouton pour quitter l'application
		exit = new JButton("Quitter");
		exit.setBounds(664, 169, 109, 23);
		this.getContentPane().add(exit);

		// Bouton pour le chargement du fichier PDF 
		findPDF = new JButton("...");
		findPDF.setFont(new Font("Tahoma", Font.BOLD, 11));
		findPDF.setBounds(740, 7, 33, 23);
		this.getContentPane().add(findPDF);

		JLabel lblNewLabel_3 = new JLabel("Source PDF");
		lblNewLabel_3.setBounds(10, 11, 77, 14);
		this.getContentPane().add(lblNewLabel_3);

		sourcePDF = new JTextField();
		sourcePDF.setBounds(108, 8, 627, 20);
		sourcePDF.setColumns(10);
		this.getContentPane().add(sourcePDF);

		// Bouton de conversion PDF --> TXT 
		conversionPdf_Txt = new JButton("Conversion  PDF >- TXT");
		conversionPdf_Txt.setBounds(296, 32, 176, 23);
		this.getContentPane().add(conversionPdf_Txt);

		// Bouton pour la decisionJury 
		avisJury = new JButton("Avis Jury");
		avisJury.setBounds(296, 140, 176, 23);
		this.getContentPane().add(avisJury);

		// Bouton pour les statistiques
		statistique = new JButton("Generer statistiques");
		statistique.setBounds(296, 170, 176, 23);
		this.getContentPane().add(statistique);
		
		lockButton();
	}

	/** Methode qui ajoute les listeners aux boutons*/
	private void addListener(){
		
		
		
		findPDF.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				choixRepertoire();
			}
		});

		conversionPdf_Txt.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!(sourcePDF.getText()=="") && !(sourceTXT.getText()=="")){
					try {
						if(fileTXT.exists()){
							//on demande si on veut l'ecraser
							int option = JOptionPane.showConfirmDialog(null, "Voulez-vous ecraser le fichier " + fileTXT.getName() +" ?", "Confirmation de la suppression", 
									JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
							requestFocus();
							if(option == JOptionPane.OK_OPTION){
								maConversionPDFtoText(sourcePDF.getText(), sourceTXT.getText());
								JOptionPane.showMessageDialog(null, "le fichier " + filePDF.getName() +"\na été converti en "+ fileTXT.getName(), "Info",  
										JOptionPane.INFORMATION_MESSAGE);
							}
						}
						else{
							maConversionPDFtoText(sourcePDF.getText(), sourceTXT.getText());
						}
					} catch (FileNotFoundException e2) {
						JOptionPane.showMessageDialog(null, "le fichier " + filePDF.getName() +"\n'existe pas", "Erreur",  JOptionPane.ERROR_MESSAGE);
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(null, "le fichier " + filePDF.getName() +"\nn'a pas été converti en "+ fileTXT.getName(), "Erreur",  
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		avisJury.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				creationDecisionJury();
			}
		});

		//TODO a ppelrler les methodes pour les statistiques
		statistique.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("test");
				//TODO on teste si la decision de jury a ete faite donc on teste su le fichier decision existe sinon on recreer la liste des etudiants du PDF
				//if(fileCSV.exists()){
			}
		});

		exit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				System.exit(0);
			}
		});
	}

	//TODO a commenter
	private void creationDecisionJury() {		
		//TODO test si les ficheir existe ou pas si le fichier text existe on recupere les donnees
		DecisionJury.ecritureDecisionJury(sourceTXT.getText(), cibleCSV.getText());
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
		}
		unlockButton();
	}

	//TODO a commenter
	private void gestionFichier(JFileChooser chooser) {
		//Recuperation du PDF choisi dans le JFileChooser
		filePDF = new File(chooser.getSelectedFile().getAbsolutePath());
		sourcePDF.setText(filePDF.getAbsolutePath());

		//Creation du repertoire de decision jury
		dirDatasTxt = new File(filePDF.getParent()+"/DatasTxt");//creation du repertoire d'avis de jury
		if(!dirDatasTxt.exists())
			dirDatasTxt.mkdir();

		//Creation du repertoire de decision jury
		dirAvisJury = new File(filePDF.getParent()+"/AvisJury");//creation du repertoire d'avis de jury
		if(!dirAvisJury.exists())
			dirAvisJury.mkdir();

		//Creation du repertoire de decision jury
		dirStats = new File(filePDF.getParent()+"/Stats");//creation du repertoire d'avis de jury
		if(!dirStats.exists())
			dirStats.mkdir();

		//creation du fichier txt pour les donnees des etudiants
		String nomFichierTXT = filePDF.getName().replace(".pdf", ".txt");
		fileTXT = new File(dirDatasTxt.getAbsolutePath()+"/"+nomFichierTXT);
		sourceTXT.setText(fileTXT.getAbsolutePath()); 	

		//creation du fichier de decision
		String nomDecisionJury = filePDF.getName().replace(".pdf", ".csv");
		fileDecisionJury = new File(dirAvisJury.getAbsolutePath()+"/"+nomDecisionJury);
		cibleCSV.setText(fileDecisionJury.getAbsolutePath()); 

		//creation du fichier pour les stats
		String nomStats = filePDF.getName().replace(".pdf", ".csv");//TODO a voir dans quel format sera le fichier de stat
		fileStats = new File(dirStats.getAbsolutePath()+"/"+nomStats);
		//TODO faire un JTextfield pour les stats cibleCSV.setText(fileStats.getAbsolutePath()); 
	}
	
	/** Bloquer bouton empeche l'actiavtion des boutons tant que le chemin vers le pdf n'est pas la*/
	private void lockButton(){
		conversionPdf_Txt.setEnabled(false);
		avisJury.setEnabled(false);
		statistique.setEnabled(false);
	}

	/** Debloquer bouton empeche l'actiavtion des boutons tant que le chemin vers le pdf n'est pas la*/
	private void unlockButton() {
		conversionPdf_Txt.setEnabled(true);
		avisJury.setEnabled(true);
		statistique.setEnabled(true);	
	}
}
