package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import operation.data.Etudiant;
import operation.data.Module;

public class GestionEtudiants {

	//TODO faire des sysout pour voir si la sepeartion filtrage/conversion est bonne

	/**
	 * modules contient la liste des modules existant dans le fichier modules pour filtrer les modules
	 * */
	private static List<Module> modules = new ArrayList<Module>();

	/**TODO a commener*/
	private List<Etudiant> etudiants = new ArrayList<Etudiant>();

	/**
	 * nomPrenom contient une chaîne de caractères contenant le nom et prénom de l'étudiant ou "" si le nom et prénom de l'étudiant ne sont pas encore trouvés
	 */
	private String nomPrenom;

	/**
	 * totalCSTM compte le nombre de CS+TM de TC de branche obtenus par l'étudiant au cours du TC ou du TC de branche ISI.  
	 */
	private int totalCSTM;
	/**
	 * nbA désigne le nombre de A obtenus par un élève.
	 */
	private int nbA;

	/** TODO commentaire A modifier
	 * Constructeur : Lance la lecture du fichier texte 
	 * @param niveauIsi entier représentant le niveau de l'étudiant dans la formation ISI
	 */
	public GestionEtudiants(){
		modules = LectureModules.lireModules();
	}

	/**
	 * Initialisation des attributs de la classe.
	 */
	public void initialiseRecherche(){
		totalCSTM=0;
		nomPrenom="";
		nbA=0;
	}

	/**
	 * Renvoie une chaine de caractères avec le nom et prénom, s'il ne le trouve pas renvoie la chaine vide ""
	 * @param tabMots tableau de mots composant une ligne de texte à analyser
	 * @return une chaine de caractéres contenant le nom suivi des prénoms de l'étudiant. Sinon renvoie ""
	 */
	public String trouveNomPrenom(String tabMots[]){
		String nomPrenom="";
		if (tabMots.length>5 && tabMots[0].equals("TOTAUX")){
			int i=tabMots.length-1;
			while (!tabMots[i].equals("M.") && !tabMots[i].equals("Mme")){
				nomPrenom=nomPrenom+";"+tabMots[i];
				i-=1;
			}
		}
		return nomPrenom;
	}

	public void traitement(){
		File file = new File("src/test/etudiant_test.txt");

		initialiseRecherche();// Initialisation
		try {
			BufferedReader lecteurAvecBuffer = new BufferedReader(new FileReader(file));
			String ligne;
			String listeMots[];
			while ((ligne = lecteurAvecBuffer.readLine()) != null){
				listeMots=ligne.split(" ");
				setTotalCSTM(getTotalCSTM() + compteCSTM(listeMots));//Compte les credits de l'etudiants
				setNbA(trouveNbA(listeMots)+getNbA());

				lecteurAvecBuffer = new BufferedReader(new FileReader(file));

				setNomPrenom(trouveNomPrenom(listeMots));

				// Parcours toutes les lignes du fichier texte
				while ((ligne = lecteurAvecBuffer.readLine()) != null){
					listeMots=ligne.split(" ");
					for (String string : listeMots) {
						System.out.println("String "+string);

						//TODO recuperer le donnees sur l'etudiant et l'instancier et le mettre dans la liste etudiant;
						//TODO recuperer les donnees sur les UVs, note categorie etc
						//TODO envoyer ces donnes a une classe GestionNotes
					}
				}
			}
		}
		catch(FileNotFoundException exc) {
			System.out.println("Erreur d'ouverture");
		} 
		catch (IOException e) {e.printStackTrace();}
	}

	/**
	 * Comptabilise les crédits des UE TC de branche ISI (pour une ligne de texte)
	 * @param tabMots tableau de mots composant une ligne de texte à analyser
	 * @return le total des CS+TM trouvés dans tabMots
	 */
	public int compteCSTM (String tabMots[]){
		int i, credits;
		credits=0;
		Module module = null;
		i=0;
		Iterator<Module> it = modules.iterator();
		while (i<tabMots.length){
			while(it.hasNext()){
				module = it.next();
				if (tabMots[i].equals(module.getNom())) {
					credits+=module.getCredit();
				}
				else{
				}
			}
			i+=1;
		}
		return credits;
	}	

	/**
	 * indique si str est une chaine de charactères numériques.
	 * @param str
	 * @return true si str est un nombre, false sinon
	 */
	public static boolean isNumeric(String str){  
		try{  
			Double.parseDouble(str);  
		}  
		catch(NumberFormatException nfe){  
			return false;  
		}  
		return true;  
	}
	/**
	 * Compte le nombre de A
	 * @param tabMots tableau de mots composant une ligne de texte à analyser
	 * @return le nombre de A de l'élève
	 */
	public int trouveNbA (String tabMots[]){
		int i=0;
		int compteur=0;
		while(i<tabMots.length){
			if (tabMots[i].equals("A") && isNumeric(tabMots[i+3])){
				compteur++;

			}
			i++;
		}

		return compteur;
	}


	public static List<Module> getModules() {
		return modules;
	}



	public static void setModules(List<Module> modules) {
		GestionEtudiants.modules = modules;
	}



	public List<Etudiant> getEtudiants() {
		return etudiants;
	}



	public void setEtudiants(List<Etudiant> etudiants) {
		this.etudiants = etudiants;
	}



	public String getNomPrenom() {
		return nomPrenom;
	}



	public void setNomPrenom(String nomPrenom) {
		this.nomPrenom = nomPrenom;
	}



	public int getTotalCSTM() {
		return totalCSTM;
	}



	public void setTotalCSTM(int totalCSTM) {
		this.totalCSTM = totalCSTM;
	}



	public int getNbA() {
		return nbA;
	}



	public void setNbA(int nbA) {
		this.nbA = nbA;
	}
}
