package modules;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class LectureModules {

	private List<Module> modules;
	private String file = "modules.txt";

	public LectureModules(){

		BufferedReader lecteurAvecBuffer = null;
		String ligne;
		String listeMots[];

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File("tonfichier")));
			// normalement si le fichier n'existe pas, il est crée à la racine du projet
			writer.write("....");
			writer.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		// Parcours toutes les lignes du fichier texte
		try {
			lecteurAvecBuffer = new BufferedReader(new FileReader(new File(file)));
			while ((ligne = lecteurAvecBuffer.readLine()) != null){
				listeMots=ligne.split(" ");
				System.out.println("lignes :"+ ligne);
				System.out.println("lignesMots :"+ listeMots);


				
				
				lecteurAvecBuffer.close();
			}
		} catch (IOException e) {
			System.out.println("Erreur de fichier");
			e.printStackTrace();
		}	
	}
}


