
package modules;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import operation.Note;
import operation.data.Module;


/**TODO a commenter*/
public class LectureModules {

	/**TODO a commenter*/
	private static String file = "src/files/modules.txt";

	/**TODO a commenter*/
	public LectureModules(){}

	/**TODO a commenter*/
	public static List<Module> lireModules(){

		List<Module> modules = new ArrayList<Module>();
		BufferedReader lecteurAvecBuffer = null;
		String ligne;

		//Represente les mots d'une ligne 
		String listeMots[];

		String nomModule, parcoursModule, categorieModule;
		int creditModule;

		// Parcours toutes les lignes du fichier texte
		try {
			lecteurAvecBuffer = new BufferedReader(new FileReader(new File(file)));
			while ((ligne = lecteurAvecBuffer.readLine()) != null){
				listeMots=ligne.split(" ");
				nomModule = listeMots[0];
				creditModule = Integer.valueOf(listeMots[1]);
				nomModule = listeMots[0];
				categorieModule = listeMots[2];
				parcoursModule = listeMots[3];
				modules.add(new Module(nomModule, categorieModule, parcoursModule, creditModule, 0, Note.NULL));
			}
			lecteurAvecBuffer.close();
		} catch (IOException e) {
			System.out.println("Erreur de fichier");
			e.printStackTrace();
		}	
		return modules;
	}
	
	/*try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File("tonfichier")));
			// normalement si le fichier n'existe pas, il est crée à la racine du projet
			writer.write("....");
			writer.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}*/
}


