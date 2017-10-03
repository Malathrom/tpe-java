package ihm;

import java.awt.EventQueue;

public class Launcher {
	
	/**
	 * Le programme principal
	 * @param args Param�tre non utilis�
	 */
	public static void main(String[] args) {
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
	}

}
