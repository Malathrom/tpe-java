package main;

import java.awt.EventQueue;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ihm.GestionStagesJuryIsi;
import io.LectureModules;
import operation.GestionData;
import operation.GestionNote;
import operation.data.*;

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
			File file = new File("src/test/4etudiants.txt");//Fichier de test 2//TODO a enlever
			//File file = new File("src/test/PV ISI 2.txt");//Fichier de test 3//TODO a enlever
			GestionData g = new GestionData(file);
			g.lireFichier();//Test sur les fichiers etudiants
			//TODO on recupera ici les etudiants et les modules

			break;

		case 4:
			boolean b = Pattern.matches("[0-9]{2}/[0-9]{2}/20[0-9]{2}", "03/10/2017");//si c'est un nouvelle etudiant
			System.out.println(b);
			break;

		case 5:
			String sepSemestre = "(A|P)[0-9]{2}";
			String data = "A15";

			if (Pattern.matches(sepSemestre, data))
				System.out.println(sepSemestre.matches(data) + " " + data);
			break;

		case 6:
			new GestionNote();
			
			break;
		}
	}
}