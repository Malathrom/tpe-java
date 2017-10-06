package operation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**Classe permettant de faire les calculs sur les notes des etudiants*/
public class CalculNote{


	/**TODO a commenter*/
	private static String file = "src/texte/etudiant_test.txt";

	/*FAir une classe Etudiant avec un nom, un prenom , uneArrayList UV
	Faire Une Classe UV avec un semestre et un Note
	faire une LISTE D'ETUDIANT*/
	

	public CalculNote(){}


	public void recuperationNotes(){
		BufferedReader lecteurAvecBuffer = null;
		String ligne;
		int i = 0;

		//Represente les mots d'une ligne 
		String listeMots[];

		// Parcours toutes les lignes du fichier texte
		try {
			lecteurAvecBuffer = new BufferedReader(new FileReader(new File(file)));
			while ((ligne = lecteurAvecBuffer.readLine()) != null){
				listeMots=ligne.split(" ");
				for (String str : listeMots) {
					System.out.println(str);
				}
			}
			lecteurAvecBuffer.close();
		} catch (IOException e) {
			System.out.println("Erreur de fichier");
			e.getMessage();
		}
	}

	/** retourne un tableau associatif avec pour cle la note et comme valeur le nombre de fois la note obtenu dans le semestre
	 * @return */
	public static HashMap<Note, Integer> calculsemestre(String txt){
		return null;	
	}

	/**Permet de calculer les notes sur l'ensemble du semestre*/
	/** retourne un tableau associatif avec pour cle la note et comme valeur le nombre de fois la note obtenu dans le cursus*/
	public static HashMap<Note, Integer> calculCursus(String txt){
		return null;
	}

}
