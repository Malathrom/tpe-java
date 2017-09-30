import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

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
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * GestionStagesJuryIsi crée une fenêtre graphique pour sélectionner le pv de jury ISI au format PDF
 * puis permet de construire le fichier TXT contenant le texte du PDF
 * puis permet d'avoir le résultat de l'étude faite par la Classe Filtrage dans un fichier CSV 
 * @author nigro
 * @version 1.0
 */
public class GestionStagesJuryIsi {

	private JFrame frmGestionStagesJury;
	private JTextField sourceTXT;
	private JTextField cibleCSV;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JTextField sourcePDF;


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
	 * Le programme principal
	 * @param args Paramètre non utilisé
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GestionStagesJuryIsi window = new GestionStagesJuryIsi();
					window.frmGestionStagesJury.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Creation de l'application.
	 */
	public GestionStagesJuryIsi() {
		initialize();
	}

	/**
	 * Initialisation du contenu de la fenêtre.
	 */
	private void initialize() {
		frmGestionStagesJury = new JFrame();
		frmGestionStagesJury.setTitle("Gestion Stages Jury ISI");
		frmGestionStagesJury.setBounds(100, 100, 799, 241);
		frmGestionStagesJury.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmGestionStagesJury.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Source TXT");
		lblNewLabel.setBounds(10, 65, 88, 14);
		frmGestionStagesJury.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Cible CSV");
		lblNewLabel_1.setBounds(10, 115, 88, 14);
		frmGestionStagesJury.getContentPane().add(lblNewLabel_1);
		
		sourceTXT = new JTextField();
		sourceTXT.setBounds(108, 59, 627, 20);
		frmGestionStagesJury.getContentPane().add(sourceTXT);
		sourceTXT.setColumns(10);
		
		cibleCSV = new JTextField();
		cibleCSV.setBounds(108, 112, 627, 20);
		frmGestionStagesJury.getContentPane().add(cibleCSV);
		cibleCSV.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("Niveau ISI");
		lblNewLabel_2.setBounds(10, 153, 88, 14);
		frmGestionStagesJury.getContentPane().add(lblNewLabel_2);
		
		JRadioButton isi1 = new JRadioButton("Isi 1");
		isi1.setSelected(true);
		buttonGroup.add(isi1);
		isi1.setBounds(107, 149, 63, 23);
		frmGestionStagesJury.getContentPane().add(isi1);
		
		JRadioButton isi2 = new JRadioButton("Isi 2");
		buttonGroup.add(isi2);
		isi2.setBounds(188, 149, 57, 23);
		frmGestionStagesJury.getContentPane().add(isi2);
		
		JRadioButton isi3 = new JRadioButton("Isi 3");
		buttonGroup.add(isi3);
		isi3.setBounds(276, 149, 109, 23);
		frmGestionStagesJury.getContentPane().add(isi3);
		
		// Bouton lançant la conversion TXT --> "Filtrage" --> CSV 
		JButton ConversionBouton = new JButton("Conversion  TXT -> CSV");
		ConversionBouton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (isi1.isSelected()) 
				   {new Filtrage(sourceTXT.getText(),cibleCSV.getText(), 1);}
				if (isi2.isSelected()) 
				   {new Filtrage(sourceTXT.getText(),cibleCSV.getText(), 2);}
				if (isi3.isSelected()) 
				   {new Filtrage(sourceTXT.getText(),cibleCSV.getText(), 3);}
				System.out.println("Conversion TXT --> CSV terminée");
			}
		});
		ConversionBouton.setBounds(296, 79, 176, 23);
		frmGestionStagesJury.getContentPane().add(ConversionBouton);
		
		// Bouton pour quitter l'application
		JButton btnNewButton_1 = new JButton("Quitter");
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				System.exit(0);
			}
		});
		btnNewButton_1.setBounds(664, 169, 109, 23);
		frmGestionStagesJury.getContentPane().add(btnNewButton_1);
		
		// Bouton pour le chargement du fichier PDF 
		JButton findPDF = new JButton("...");
		findPDF.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				  String nomFichierPDF;
				  String nomFichierTXT;
				  String nomFichierCSV;
				  JFileChooser chooser = new JFileChooser();
			      FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF","pdf");
			      chooser.setFileFilter(filter);
			      chooser.setMultiSelectionEnabled(false);
			      int returnVal = chooser.showOpenDialog(null);
			      if(returnVal == JFileChooser.APPROVE_OPTION) {
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
		findPDF.setFont(new Font("Tahoma", Font.BOLD, 11));
		findPDF.setBounds(740, 7, 33, 23);
		frmGestionStagesJury.getContentPane().add(findPDF);
		
		JLabel lblNewLabel_3 = new JLabel("Source PDF");
		lblNewLabel_3.setBounds(10, 11, 77, 14);
		frmGestionStagesJury.getContentPane().add(lblNewLabel_3);
		
		sourcePDF = new JTextField();
		sourcePDF.setBounds(108, 8, 627, 20);
		frmGestionStagesJury.getContentPane().add(sourcePDF);
		sourcePDF.setColumns(10);
		
		// Bouton de convertion PDF --> TXT 
		JButton ConvertirEnTXT = new JButton("Conversion  PDF >- TXT");
		ConvertirEnTXT.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!(sourcePDF.getText()=="") && !(sourceTXT.getText()==""))
					try {
						maConversionPDFtoText (sourcePDF.getText(), sourceTXT.getText());
						System.out.println("Conversion PDF --> TXT terminée");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			}
		});
		ConvertirEnTXT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		ConvertirEnTXT.setBounds(296, 28, 176, 23);
		frmGestionStagesJury.getContentPane().add(ConvertirEnTXT);
	}
}
