package ihm;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFileChooser;

import io.Filtrage;
import io.LectureModules;
import io.SauvegardeRepertoire;
import operation.CalculNote;
import operation.Note;
import operation.data.Module;

public class Launcher {

	/**
	 * Le programme principal
	 * @param args Paramètre non utilisé
	 */
	public static void main(String[] args) {

		switch (1) {
		case 1:
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						GestionStagesJuryIsi window = new GestionStagesJuryIsi();
						window.setVisible(true);
						/*TODO regarder mes anciennes interfaces pour placer, redimesionner la fenetres*/
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			break;

		case 2:
			new LectureModules();
			break;

		case 3:
			new CalculNote().recuperationNotes();
			Module if26 = new Module("IF26", "TM", "ISI", 6, 2, Note.A);
			System.out.println(if26);
			break;

		case 4:
			//TODO tester les notes d'un etudiant
			File file = new File("/Users/lucasnoga/Desktop/UTT/TX/pdf jury/PV ISI 2.txt");
			//File file = new File("src/test/etudiant_test.txt");
			File sorti = new File("src/test/sortie.txt");
			Filtrage filtre = new Filtrage();

			BufferedReader lecteurAvecBuffer = null;
			BufferedWriter sortie = null;
			String ligne;
			String listeMots[];
			ArrayList<String> maliste = new ArrayList<String>();
			
			try {
				lecteurAvecBuffer = new BufferedReader(new FileReader(file));
				sortie = new BufferedWriter(new FileWriter(sorti));
				filtre.initialiseRecherche();// Initialisation

				// Parcours toutes les lignes du fichier texte
				while ((ligne = lecteurAvecBuffer.readLine()) != null){
					listeMots=ligne.split(" ");
					for (String string : listeMots) {
						maliste.add(string);
					}
					maliste.add("NULL");
				}
				Iterator<String> it = maliste.iterator();
				while(it.hasNext()){
					String str = it.next();
					if (str.equals("NULL")) {
						System.out.print("\n");
						sortie.write("\n");
					}else{
						System.out.print(str+";");
						sortie.write(str+";");
					}
				}
			}
			catch(FileNotFoundException exc) {
				System.out.println("Erreur d'ouverture");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		
		case 5:
			break;
		}
	}
}
