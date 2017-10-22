package main;

import java.awt.EventQueue;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import ihm.GestionStagesJuryIsi;
import io.LectureModules;
import operation.GestionData;
import operation.GestionNote;
import operation.data.Etudiant;

public class Launcher {

	/**
	 * Le programme principal
	 * @param args Paramètre non utilisé
	 */
	public static void main(String[] args) {

		switch (2) {
		case 1:
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						new GestionStagesJuryIsi();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			break;

		case 2:
			System.out.println(LectureModules.lireModules());//Test sur les modules
			break;

		case 3:
			//File file = new File("src/test/etudiant_test.txt");//Fichier de test//TODO a enlever
			//File file = new File("src/test/4etudiants.txt");//Fichier de test 2//TODO a enlever
			File file = new File("src/test/PV ISI 2.txt");//Fichier de test 3//TODO a enlever		
			List<Etudiant> etudiants = GestionData.listeEtudiant(file);//Test sur les fichiers etudiants
			Iterator<Etudiant> it = etudiants.iterator();
			while (it.hasNext()) {
				Etudiant etudiant = it.next();
				System.out.println(etudiant);
			}
			break;

		case 4:
			boolean b = Pattern.matches("A|B|C", "A");//si c'est un nouvelle etudiant
			System.out.println(b);
			break;

		case 5:
			String contenu = "A15";
			String regex = "(A|P)[0-9]{2}";
			String regex2 = "[0-9]{2}/[0-9]{2}";//Total
			if (Pattern.matches(regex, contenu))
				System.out.println("yo");
			if (Pattern.matches(regex2, contenu))
				System.out.println("y2");
				break;
		case 6:
			new GestionNote();

			break;
		}
	}
}