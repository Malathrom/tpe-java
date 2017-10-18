package ihm;

import java.awt.EventQueue;
import java.io.File;

import io.LectureModules;
import operation.GestionData;

public class Launcher {

	/**
	 * Le programme principal
	 * @param args Paramètre non utilisé
	 */
	public static void main(String[] args) {

		switch (3) {
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
			System.out.println(LectureModules.lireModules());//Test sur les modules
			break;

		case 3:
			File file = new File("src/test/etudiant_test.txt");//Fichier de test//TODO a enlever
			//File file2 = new File("src/test/4etudiants.txt");//Fichier de test 2//TODO a enlever
			//File file = new File("src/test/PV ISI 2.txt");//Fichier de test 3//TODO a enlever
			new GestionData(file).lireFichier();//Test sur les fichiers etudiants
			//TODO on recupera ici les etudiants et les modules
			break;


		case 4:
			break;
		}
	}
}
