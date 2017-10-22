package io;

import java.io.BufferedReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.io.PrintWriter;
import javax.swing.JOptionPane;

import operation.GestionData;
import operation.data.Etudiant;

public class Conversion {
	FileWriter fw = null;
	//TODO tester la classe pour voir si le fichier final est ok
	//TODO finir la méthode CompteCSTM avec l’arrayList modules

	/**
	 * filtre pour filtrer les decisions de jury
	 */
	private Filtrage filtre = new Filtrage();
	

	/**
	 * decisionCSV contient le nom du fichier CVS contenant les propositions de décisions 
	 */
	private String decisionCSV = "";

	/**
	 * Constructeur : Lance la lecture du fichier texte 
	 * @param nomFichierTexte Chaine de caractéres représentant le fichier texte à analyser
	 * @param nomFichierCSV Chaine de caractéres représentant le fichier CSV de sortie (résultat)
	 * @param niveauIsi entier représentant le niveau de l'étudiant dans la formation ISI
	 */
	public Conversion (String nomFichierTexte, String nomFichierCSV){
		lireFichier(nomFichierTexte, nomFichierCSV);
	}

	/**
	 * Prend en compte les nouvelles régles A17 pour les stages : pour les étudiant ISI 1, on compte les CS+TM, 
	 * ils peuvent chercher un stage que si CS+TM (Hors equivalence) est supérieur strictement à 2
	 * La méthode lit le fichier TXT ligne par ligne pour extraire les informations relatives aux étudiants et utiles pour le jury
	 * @param nomFichierTexte Chaine de caractères représentant le fichier texte à analyser
	 * @param nomFichierCSV Chaine de caractéres représentant le fichier CSV de sortie (résultat)
	 * @param niveauIsi  Entier représentant le niveau de l'étudiant dans le formation ISI
	 * @throws IOException Erreur d'ouverture de fichier
	 */
	public void lireFichier (String fileTxt, String nomFichierCSV){
		File file = new File(nomFichierCSV);
		try {
			fw = new FileWriter(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		BufferedWriter bw;
		PrintWriter pw;
		bw = new BufferedWriter(fw);
		pw = new PrintWriter(bw);
		
		//GestionData g = new GestionData(new File(fileTxt));
		List<Etudiant> etudiants = GestionData.listeEtudiant(new File(fileTxt));
		Iterator<Etudiant> it = etudiants.iterator();
		String entete="";
		entete+="Nom;Prenom;";
		int semSize= GestionData.avisJury(etudiants.get(0)).size(), j=0;
		while (j<semSize){
			entete+="Avis Semestre "+(j+1)+";";
			j++;
		}
		entete+="\n";
		pw.print(entete);
		
		while (it.hasNext()) {
			Etudiant etudiant = it.next();
			String out="";
			out=out+etudiant.getNom()+";"+etudiant.getPrenom()+";";
			int i=0;
			List<String> avisSem= GestionData.avisJury(etudiant);
			while(i<avisSem.size()){
				out+= avisSem.get(i)+";";
				i++;
			}
			pw.println(out);
		}
		pw.println(etudiants);
		pw.close();
		
	}
	
	public Filtrage getFiltre() {
		return filtre;
	}

	public void setFiltre(Filtrage filtre) {
		this.filtre = filtre;
	}

	public String getDecisionCSV() {
		return decisionCSV;
	}

	public void setDecisionCSV(String sortieExcelCSV) {
		this.decisionCSV = sortieExcelCSV;
	}
}
