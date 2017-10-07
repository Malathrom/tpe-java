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

	
	/**TODO Faire un fichier repertoire courant qui stocke l'ensmeble des repertoires cournst utilisables puis dans la claase 
	 * filtrage associer a la variable path le premier repertoire courant
	 * et a chaque fois qu'on change on ecrit a la premier ligne donc on recuperer dans une chaine tous le fichier on ecrit le 
	 * nouveau repertoire puis la chaine qui contenait tout ce qui avait avant
	 * A METTRE DANS LES TACHES
	 * 
	 * TODO faire une LISTE D'ETUDIANT*/
	

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
				System.out.print("ligne:"+ ++i + "   ");////////////////////////////////////////////////////////////////////////////////////////////////////////
				for (String mot : listeMots) {
					System.out.print(mot + ";");////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				}
				System.out.println();//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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
