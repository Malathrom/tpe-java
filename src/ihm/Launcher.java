package ihm;

import java.awt.EventQueue;

import io.GestionData;
import io.LectureModules;

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
			new LectureModules();
			break;

		case 3:
			//TODO TEST DES NOTES DE L'ETUDIANT
			//TODO Tester avec le fichier 4 etudiant
			//File file = new File("/Users/lucasnoga/Desktop/UTT/TX/pdf jury/PV ISI 2.txt");
			new GestionData().lireFichier();
			break;


		case 4:
			break;
		}
	}
}
