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

		switch (2) {
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
			new GestionData().lireFichier();//Test sur les fichiers etudiants
			break;


		case 4:
			break;
		}
	}
}
