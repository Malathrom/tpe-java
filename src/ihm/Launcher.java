package ihm;

import java.awt.EventQueue;

import modules.LectureModules;
import operation.CalculNote;
import operation.Note;
import operation.data.Module;

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
			new CalculNote().recuperationNotes();
			Module if26 = new Module("IF26", "TM", "ISI", 2, Note.A, 6);
			break;
		default:
			break;
		}
		
	}

}
