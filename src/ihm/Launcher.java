package ihm;

import java.awt.EventQueue;

import modules.LectureModules;
import operation.CalculNote;

public class Launcher {
	
	/**
	 * Le programme principal
	 * @param args Param�tre non utilis�
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
			new CalculNote().recuperationNotes();
			break;
		default:
			break;
		}
		
	}

}
