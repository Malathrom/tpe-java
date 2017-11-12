package ihm;

import javax.swing.JFrame;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import io.Conversion;
import io.SauvegardeRepertoire;

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
	private File fileTXT, fileCSV, filePDF;
	private JButton exit, findPDF, conversionTxt_Csv, conversionPdf_Txt, avisJury;

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
		this.getContentPane().add(conversionTxt_Csv);

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

		// Bouton de conversion PDF --> TXT 
		avisJury = new JButton("Avis Jury");
		avisJury.setBounds(296, 140, 176, 23);
		this.getContentPane().add(avisJury);


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
				if(fileCSV.exists()){
					//on demande si on veut l'ecraser
					int option = JOptionPane.showConfirmDialog(null, "Voulez-vous ecraser le fichier " + fileCSV.getName() +" ?", "Confirmation de la suppression", 
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					requestFocus();
					if(option == JOptionPane.OK_OPTION){
						conversion_TXT_CSV();
						JOptionPane.showMessageDialog(null, "le fichier " + fileTXT.getName() +"\na été converti en "+ fileCSV.getName(), "Info",  
								JOptionPane.INFORMATION_MESSAGE);
					}else{
						JOptionPane.showMessageDialog(null, "le fichier " + fileTXT.getName() +"\nn'a pas été converti en "+ fileCSV.getName(), "Erreur",  
								JOptionPane.ERROR_MESSAGE);
					} 
				}
				else{
					conversion_TXT_CSV();
				}
			}
		});

		exit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				System.exit(0);
			}
		});
	}

	/**Methode qui convertit un fichier txt en format csv*/
	private void conversion_TXT_CSV() {
		new Conversion(sourceTXT.getText(), cibleCSV.getText());
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
			affichageFichier(chooser);
		}
	}

	/**
	 * affichageFichier affiche sur l'ihm les chemins des fichiers pdf, csv, txt
	 * @param chooser le JFileChooser lors du choix des fichiers
	 */
	private void affichageFichier(JFileChooser chooser) {
		String nomFichierPDF = chooser.getSelectedFile().getAbsolutePath();
		sourcePDF.setText(nomFichierPDF);
		String nomFichierTXT = nomFichierPDF.replace(".pdf", ".txt");
		sourceTXT.setText(nomFichierTXT);
		String nomFichierCSV = nomFichierPDF.replace(".pdf", ".csv");
		cibleCSV.setText(nomFichierCSV);
		filePDF = new File(nomFichierPDF);
		try {
			System.out.println(filePDF.getCanonicalPath());
			System.out.println(filePDF.getName());
			System.out.println(filePDF.getPath());
		} catch (IOException e) { e.printStackTrace(); }
		fileTXT = new File(nomFichierTXT);
		fileCSV = new File(nomFichierCSV);
	}

	/*TODO faire la javadoc pour les getter et setter*/
	public JTextField getSourceTXT() {
		return sourceTXT;
	}

	public void setSourceTXT(JTextField sourceTXT) {
		this.sourceTXT = sourceTXT;
	}

	public JTextField getCibleCSV() {
		return cibleCSV;
	}

	public void setCibleCSV(JTextField cibleCSV) {
		this.cibleCSV = cibleCSV;
	}

	public JTextField getSourcePDF() {
		return sourcePDF;
	}

	public void setSourcePDF(JTextField sourcePDF) {
		this.sourcePDF = sourcePDF;
	}
}
