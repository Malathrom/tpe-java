package ihm;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import io.Filtrage;

import javax.swing.JRadioButton;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.ButtonGroup;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.awt.Font;

/**
 * GestionStagesJuryIsi crée une fenétre graphique pour sélectionner le pv de jury ISI au format PDF
 * puis permet de construire le fichier TXT contenant le texte du PDF
 * puis permet d'avoir le résultat de l'étude faite par la Classe Filtrage dans un fichier CSV 
 * @author nigro
 * @version 1.0
 */
public class GestionStagesJuryIsi extends JFrame{
	/*TODO Ameliorer la recherche de fichier*/
	/*TODO pour le choix des fichier faire en sorte de memoriser le workspace*/
	/*TODO faire la gestion d'exception pour le click des boutons notament*/
	/*TODO faire la javadoc pour les getter et setter*/
	/*TODO faire des fonctions pour les evenements*/
	/*TODO faire des sysout sur les actions de la methode mouseListener pour comprendre*/
	/*TODO lire en detail la classe filtrage*/
	/*TODO regler l'encodage*/

	private static final long serialVersionUID = 1L;

	private JTextField sourceTXT;
	private JTextField cibleCSV;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JTextField sourcePDF;
	private JRadioButton isi1, isi2, isi3;
	private JButton exit, findPDF, conversionTxt_Csv, conversionPdf_Txt;
	
	/**sauvegarder le repertoire lors du choix du fichier*/
	/*TODO A voir si il veut que on la sauvegarde dans un fichier dans le cas ou l'appli est quitter*/
	String path = "";

	/**
	 * Creation de l'application.
	 */
	public GestionStagesJuryIsi() {
		new JFrame();
		initialize();
		addListener();
	}

	/**
	 * conversion d'un PDF en une chaine de caract�res
	 * @param pdfFile pointeur vers le fichier PDF 
	 * @return Une chaine de caract�res avec le texte du fichier PDF
	 * @throws IOException erreur d'ouverture du PDF
	 */
	static String getText(File pdfFile) throws IOException {
		PDDocument doc = PDDocument.load(pdfFile);
		return new PDFTextStripper().getText(doc);
	}

	/**
	 * conversion d'un fichier PDF en un fichier TXT
	 * @param sourcePDF nom du fichier PDF
	 * @param destinationTXT nom du fichier r�sultat TXT
	 * @throws IOException erreur d'ouverture ou d'�criture
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

		isi1 = new JRadioButton("Isi 1");
		isi1.setSelected(true);
		buttonGroup.add(isi1);
		isi1.setBounds(107, 149, 63, 23);
		this.getContentPane().add(isi1);

		isi2 = new JRadioButton("Isi 2");
		buttonGroup.add(isi2);
		isi2.setBounds(188, 149, 57, 23);
		this.getContentPane().add(isi2);

		isi3 = new JRadioButton("Isi 3");
		buttonGroup.add(isi3);
		isi3.setBounds(276, 149, 109, 23);
		this.getContentPane().add(isi3);

		// Bouton lançant la conversion TXT --> "Filtrage" --> CSV 
		conversionTxt_Csv = new JButton("Conversion  TXT -> CSV");
		conversionTxt_Csv.setBounds(296, 79, 176, 23);
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

		// Bouton de convertion PDF --> TXT 
		conversionPdf_Txt = new JButton("Conversion  PDF >- TXT");
		conversionPdf_Txt.setBounds(296, 28, 176, 23);
		this.getContentPane().add(conversionPdf_Txt);
	}

	/** Methode qui ajoute les listeners aux boutons*/
	private void addListener(){
		conversionTxt_Csv.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println(isi1.isSelected());/*TODO a enlever*/
				if (isi1.isSelected()) 
				{new Filtrage(sourceTXT.getText(),cibleCSV.getText(), 1);}
				if (isi2.isSelected()) 
				{new Filtrage(sourceTXT.getText(),cibleCSV.getText(), 2);}
				if (isi3.isSelected()) 
				{new Filtrage(sourceTXT.getText(),cibleCSV.getText(), 3);}
				System.out.println("Conversion TXT --> CSV termin�e");
			}
		});

		conversionPdf_Txt.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!(sourcePDF.getText()=="") && !(sourceTXT.getText()==""))
					try {
						maConversionPDFtoText(sourcePDF.getText(), sourceTXT.getText());
						System.out.println("Conversion PDF --> TXT terminée");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			}
		});

		findPDF.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String nomFichierPDF;
				String nomFichierTXT;
				String nomFichierCSV;
				JFileChooser chooser;
				if(path.equals("")){
					chooser = new JFileChooser();
				}else{
					chooser = new JFileChooser(new File(path));
				}
				
				FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF","pdf");
				chooser.setFileFilter(filter);
				chooser.setMultiSelectionEnabled(false);
				int returnVal = chooser.showOpenDialog(null);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					
					System.out.println(chooser.getSelectedFile().getAbsolutePath());
					path = chooser.getSelectedFile().getAbsolutePath();
					nomFichierPDF=chooser.getSelectedFile().getAbsolutePath();
					nomFichierTXT=nomFichierPDF.substring(0, nomFichierPDF.length()-4)+".txt";
					nomFichierCSV=nomFichierPDF.substring(0, nomFichierPDF.length()-4)+".csv";
					sourcePDF.setText(nomFichierPDF);
					sourceTXT.setText(nomFichierTXT);
					cibleCSV.setText(nomFichierCSV);
					System.out.println(nomFichierTXT+"\n"+nomFichierTXT+"\n"+nomFichierCSV);
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


	public JRadioButton getIsi1() {
		return isi1;
	}


	public void setIsi1(JRadioButton isi1) {
		this.isi1 = isi1;
	}


	public JRadioButton getIsi2() {
		return isi2;
	}


	public void setIsi2(JRadioButton isi2) {
		this.isi2 = isi2;
	}


	public JRadioButton getIsi3() {
		return isi3;
	}


	public void setIsi3(JRadioButton isi3) {
		this.isi3 = isi3;
	}


	public ButtonGroup getButtonGroup() {
		return buttonGroup;
	}
}
