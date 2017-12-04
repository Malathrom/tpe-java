package main;

import java.awt.EventQueue;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import data.Etudiant;
import ihm.IHMAvisJury;
import io.LectureModules;
import operation.GestionData;

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
						new IHMAvisJury();
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
			//File file = new File("src/test/4etudiants.txt");//Fichier de test//TODO a enlever
			File file = new File("src/test/testPV4.txt");//Fichier de test//TODO a enlever
			//File file = new File("src/test/test1ETU.txt");//Fichier de test//TODO a enlever
			//File file = new File("src/test/PV ISI 2.txt");//Fichier de test 3//TODO a enlever	
			//File file = new File("src/test/PV ISI 4.txt");
			//File file = new File("src/test/PV ISI 6.txt");
			List<Etudiant> etudiants = GestionData.listeEtudiant(file);//Test sur les fichiers etudiants
			Iterator<Etudiant> it = etudiants.iterator();
			while (it.hasNext()) {
				Etudiant etudiant = it.next();
				System.out.println(etudiant);
			}
			break;

		case 4:
			Etudiant e1 =  new Etudiant("noga", "Lucas", null, 0, 0);
			Etudiant e2 =  new Etudiant("noga", "Lucas", null, 0, 0);
			if(e1.equals(e2))
				System.out.println("salut");
			if(e1==e2)
				System.out.println("salut");
			if(e1!=e2)
				System.out.println("salut2");
			break;

		case 5:
			List<Etudiant> etus = new ArrayList<Etudiant>();
			List<Etudiant> listEtu = new ArrayList<Etudiant>();
			etus.add(new Etudiant("noga", "Lucas", null, 56, 4));
			etus.add(new Etudiant("nogsss", "Lugrescas", null, 0, 0));
			etus.add(new Etudiant("noga", "Lucas", null, 56, 0));
			etus.add(new Etudiant("nogssds", "Lugrescas", null, 0, 0));
			int i = 0;
			for (Etudiant etudiant : etus) {
				i++;
				System.out.println(etudiant.getNom());
				System.out.println(i);
				if (!listEtu.contains(etudiant)){
					listEtu.add(etudiant);
				}
			}

			System.out.println(listEtu);
			break;
		case 6:
			String[] array = new String[] { "a", "b", "c" };
			String joined2 = String.join(" ", array);
			System.out.println(joined2);
			break;
		}
	}
}