package modules;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**TODO a commenter*/
public class LectureModules {

	/**TODO a commenter*/
	private static String file = "src/texte/modules.txt";

	/**TODO a commenter*/
	public LectureModules(){}

	/**TODO a commenter*/
	public static List<Module> lireModules(){

		List<Module> modules = new ArrayList<Module>();
		BufferedReader lecteurAvecBuffer = null;
		String ligne;

		//Represente les mots d'une ligne 
		String listeMots[];

		String nomModule;
		int creditModule;

		// Parcours toutes les lignes du fichier texte
		try {
			lecteurAvecBuffer = new BufferedReader(new FileReader(new File(file)));
			while ((ligne = lecteurAvecBuffer.readLine()) != null){
				listeMots=ligne.split(" ");
				nomModule = listeMots[0];
				creditModule = Integer.valueOf(listeMots[1]);
				modules.add(new Module(nomModule, creditModule));
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


