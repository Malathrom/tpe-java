package ihm;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import traitement.Filtrage;

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
 * GestionStagesJuryIsi cr�e une fen�tre graphique pour s�lectionner le pv de jury ISI au format PDF
 * puis permet de construire le fichier TXT contenant le texte du PDF
 * puis permet d'avoir le r�sultat de l'�tude faite par la Classe Filtrage dans un fichier CSV 
 * @author nigro
 * @version 1.0
 */
public class GestionStagesJuryIsi {

	private JFrame frmGestionStagesJury;
	private JTextField sourceTXT;
	private JTextField cibleCSV;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JTextField sourcePDF;
	private JRadioButton isi1, isi2, isi3;

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
	
	/*TODO pour le choix des fichier faire en sorte de memoriser le workspace*/
	/*TODO faire la gestion d'exception pour le click des boutons notament*/

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
	 * Creation de l'application.
	 */
	public GestionStagesJuryIsi() {
		initialize();
	}

	/**
	 * Initialisation du contenu de la fen�tre.
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
		
		isi1 = new JRadioButton("Isi 1");
		isi1.setSelected(true);
		buttonGroup.add(isi1);
		isi1.setBounds(107, 149, 63, 23);
		frmGestionStagesJury.getContentPane().add(isi1);
		
		isi2 = new JRadioButton("Isi 2");
		buttonGroup.add(isi2);
		isi2.setBounds(188, 149, 57, 23);
		frmGestionStagesJury.getContentPane().add(isi2);
		
		isi3 = new JRadioButton("Isi 3");
		buttonGroup.add(isi3);
		isi3.setBounds(276, 149, 109, 23);
		frmGestionStagesJury.getContentPane().add(isi3);
		
		// Bouton lan�ant la conversion TXT --> "Filtrage" --> CSV 
		JButton ConversionBouton = new JButton("Conversion  TXT -> CSV");
		ConversionBouton.addMouseListener(new MouseAdapter() {
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
						System.out.println("Conversion PDF --> TXT termin�e");
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


	public JFrame getFrmGestionStagesJury() {
		return frmGestionStagesJury;
	}


	public void setFrmGestionStagesJury(JFrame frmGestionStagesJury) {
		this.frmGestionStagesJury = frmGestionStagesJury;
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
