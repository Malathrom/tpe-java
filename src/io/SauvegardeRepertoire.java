package io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;

/**classe qui sauvegarder les repertoires parcouru*/
public class SauvegardeRepertoire {

	/**Ensemble des chemins deja parcouru*/
	private static List<String> paths = new ArrayList<String>();

	/**chemin vers le fichier contenant les paths*/
	private static File pathFile = new File("src/files/paths.txt");

	/**TODO A commenter methode qui sotcke dans un fichier les chemins visites*/
	public SauvegardeRepertoire(JFileChooser chooser){
		String ligne;
		BufferedReader br = null;
		PrintWriter pw = null;

		try {
			br = new BufferedReader(new FileReader(pathFile));

			paths.add(chooser.getSelectedFile().getParent());//on recupere le rep selectionne
			while ((ligne = br.readLine()) != null){
				if (!paths.contains(ligne)) {
					paths.add(ligne);//on recupere les anciens repertoires si il n'existe pas deja		
				}
			}
			br.close();

			pw = new PrintWriter(new BufferedWriter(new FileWriter(pathFile)));
			for (String string : paths) {
				pw.println(string);//on reecrit la nouvelle liste
			}
			pw.close();
		} catch (IOException e) {
			System.out.println ("Erreur lors de la lecture : " + e.getMessage());
		}
	}

	public static List<String> getPaths() {
		return paths;
	}

	public static void setPaths(List<String> paths) {
		SauvegardeRepertoire.paths = paths;
	}

	public static File getPathFile() {
		return pathFile;
	}

	public static void setPathFile(File pathFile) {
		SauvegardeRepertoire.pathFile = pathFile;
	}
}

