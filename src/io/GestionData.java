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
//Classe qui gere les donnees des fichiers
public class GestionData {

	//TODO commenter tosu les getters et setters de cette classe
	//TODO qui va lire le fichier texte et créer les étudiants et de cette classe on créer les méthodes de comptage de notes par semestre, par type etc

	//TODO A commenter represente le nombre lignes necessaires dans une matrice pour stocker les donnees d'un etudiant
	private final static int LIGNE = 69;

	//TODO A commenter represente le nombre colonne necessaires dans une matrice pour stocker les donnees d'un etudiant
	private final static int COLONNE = 33;
	
	//TODO a commenter represente le delimieter entre 2 etudiants dans le fichier
	private final static String delimiter = " ";

	/**
	 * modules contient la liste des modules existant dans le fichier modules pour filtrer les modules
	 * */
	private static List<Module> modules = new ArrayList<Module>();

	/**TODO a commener*/
	private List<Etudiant> etudiants = new ArrayList<Etudiant>();

	//TODO attribut qui conetient toutes les donnees non fomattees sur un etudiant
	private String[][] dataEtudiant = new String[LIGNE][COLONNE];

	//TODO a commenter compte le nombre d'etudiant present dans le fichier
	private int nbEtudiants = 1;

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
	public GestionData(){
		modules = LectureModules.lireModules();
	}

	/**
	 * Ireirnitailisation des donnees pour un nouvel etudiant
	 */
	public void reset(){
		totalCSTM=0;
		nomPrenom="";
		nbA=0;
		dataEtudiant = new String[LIGNE][COLONNE];
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

	//TODO a commenter
	public void lireFichier(){
		//File file = new File("src/test/etudiant_test.txt");//Fichier de test
		File file = new File("src/test/4etudiants.txt");//Fichier de test 2
		BufferedReader lecteurAvecBuffer;
		reset();// Initialisation
		try {
			lecteurAvecBuffer = new BufferedReader(new FileReader(file));
			String contenu;
			String listeMots[];
			int i = 0, j = 0;
			while ((contenu = lecteurAvecBuffer.readLine()) != null){
				listeMots = contenu.split(" ");
				if (i==67) {
				
				
					if(contenu.equals(" "))
						System.out.println("slaut3");
				}
				if(isNouvelEtudiant(contenu, delimiter)){//permet de savoir si on change d'etudiant
					reset();
					System.out.println("New etudiant");
					i = 0;
					////////TODO on lance les methodes pour instancier les UV et l'etudiant
					nbEtudiants++;
				}
				j = 0;
				for (String string : listeMots) {
					System.out.println(i+ ":" + j +" " + string);
					dataEtudiant[i][j++]=string;
				}
				
				i++; //on passe a la ligne suivante

				//setTotalCSTM(getTotalCSTM() + compteCSTM(listeMots));//Compte les credits de l'etudiants
				//setNbA(trouveNbA(listeMots)+getNbA());
				//setNomPrenom(trouveNomPrenom(listeMots));

				//TODO METHDOE A CHANGER IL FAUT SAVOIR COMMENT DETECTER LES SAUTS DE LIGNES DANS UN FICHIER
				//TODO Recuperer la ligne et l'index de listeMots pour trouver les infos
				//TODO recuperer les donnees sur l'etudiant et l'instancier et le mettre dans la liste etudiant;
				//TODO recuperer les donnees sur les UVs, note categorie etc
				//TODO envoyer ces donnes a une classe GestionNotes
			}
			System.out.println(nbEtudiants);
		}
		catch(FileNotFoundException exc) {
			System.out.println("Erreur d'ouverture");
		} 
		catch (IOException e) {e.printStackTrace();}
	}

	//TODO a commenter permet de savoir quand on passe a un nouvel etudiant dans le fichier
	//contenu correspond a la ligne en cours dans le fichier
	//delimiter correspond a la chaine qui delimite 2 etudiants dans le fichier
	private boolean isNouvelEtudiant(String contenu, String delimiter) {
		if (contenu.equals(delimiter))
			return true;
		return false;
	}

	//TODO methode a faire qui permet de creer un etudiant a appeler quand la methode ligneNull retourne true et que la variable 
	private void creationEtudiant(){
		//Etudiant etudiant = new Etudiant(nom, prenom, modules);
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
		GestionData.modules = modules;
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
