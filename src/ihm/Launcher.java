package ihm;

import java.awt.EventQueue;
import java.io.File;

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
			File file = new File("/Users/lucasnoga/Desktop/UTT/TX/pdf jury/PV ISI 2.txt");
			break;
		
			
		default:
			break;
		}
		
	}

}
