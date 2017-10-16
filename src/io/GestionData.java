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
//TODO a commenter Classe qui gere les donnees des fichiers
public class GestionData {

	//TODO commenter tous les getters et setters de cette classe

	//TODO A commenter represente le nombre lignes necessaires dans une matrice pour stocker les donnees d'un etudiant
	private final static int LIGNE = 100;

	//TODO A commenter represente le nombre colonne necessaires dans une matrice pour stocker les donnees d'un etudiant
	private final static int COLONNE = 50;

	//TODO a commenter represente le delimieter entre 2 etudiants dans le fichier
	private final static String delimiter = " ";

	/**
	 * modules contient la liste des modules existant dans le fichier modules pour filtrer les modules
	 * */
	private static List<Module> modules = new ArrayList<Module>();

	/**TODO a commenter*/
	private List<Etudiant> etudiants = new ArrayList<Etudiant>();

	//TODO attribut qui conetient toutes les donnees non fomattees sur un etudiant
	private String[][] dataEtudiant = new String[LIGNE][COLONNE];

	//TODO a commenter compte le nombre d'etudiant present dans le fichier
	private int nbEtudiants = 0;

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
	 * Reset des données pour un nouvel etudiant
	 */
	public void reset(){
		totalCSTM=0;
		nomPrenom="";
		nbA=0;
		dataEtudiant = new String[LIGNE][COLONNE];
	}

	//TODO a commenter
	public void lireFichier(){
		File file = new File("src/test/etudiant_test.txt");//Fichier de test//TODO a enlever
		//File file2 = new File("src/test/4etudiants.txt");//Fichier de test 2//TODO a enlever
		//File file = new File("src/test/PV ISI 2.txt");//Fichier de test 3//TODO a enlever
		BufferedReader lecteurAvecBuffer;
		reset();// Initialisation
		try {
			lecteurAvecBuffer = new BufferedReader(new FileReader(file));
			String contenu;
			String listeMots[];
			int i = 0, j = 0;
			while ((contenu = lecteurAvecBuffer.readLine()) != null){
				listeMots = contenu.split(" ");
				
				if(isNouvelEtudiant(contenu, delimiter)){//permet de savoir si on change d'etudiant
					creationEtudiant();//on creer l'ancien etudiant
					reset();
					System.out.println("New etudiant");
					i = 0;
					////////TODO on lance les methodes pour instancier les UV et l'etudiant
					//TODO qui va lire le fichier texte et créer les étudiants et de cette classe on créer les méthodes de comptage de notes par semestre, par type etc
					//TODO quand on recuperera les matieres il faudra tester si elles font parties de l'ISI
					nbEtudiants++;
				}
				j = 0;
				for (String string : listeMots) {
					//System.out.println(i+ ":" + j +" " + string);//TODO a enlever
					dataEtudiant[i][j++]=string;
				}
				i++; //on passe a la ligne suivante
			}
			System.out.println(nbEtudiants);//A enlever un moment
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

	//TODO methode a faire qui permet de creer un etudiant 
	private void creationEtudiant(){
		//on recupere les donnees
		String nom = recupereNom();
		String prenom = recuperePrenom();
		System.out.println("nom "+ nom);
		System.out.println("prenom "+ prenom);
		recupereUEs();
		//TODO recuperer les donnees sur l'etudiant et l'instancier et le mettre dans la liste etudiant;
		//TODO envoyer ces donnees a une classe GestionNotes
		//Etudiant etudiant = new Etudiant(nom, prenom, modules);
	}

	//TODO a commenter recupere le nom de l'etudiant dans la matrice data
	private String recupereNom(){
		return dataEtudiant[1][2];
	}

	//TODO a commenter recupere le prenom de l'etudiant dans la matrice data
	private String recuperePrenom(){
		return dataEtudiant[1][1];
	}

	//TODO methode a faire qui permet de creer une Ue
	private List<String> recupereUEs(){
		List<String> ues = new ArrayList<String>();
		String[][] semestreUE = new String[10][5];
		//pour recuperer les semstres ca commenrce par CS TM ST EC ME CT HP NPML Observations
		// entre les semestres il y a Total semestre
		// la fin commence par TOTAUX
		//TODO creer un matrice UE en testant si la ligne est P+un nombre ou A+plus un nombre on met donc la suite dans la matrice
		//TODO recuperer les donnees sur les UVs, note categorie etc
		int semestre = recupereSemestre();
		String typeSemestre = recupereTypeSemestre();
		System.out.println("typesemestre "+ typeSemestre);
		System.out.println("semestre "+ semestre);
		return null;
	}
	
	//TODO a commenter recupere ISI ou TC ou MASTER
	private String recupereTypeSemestre(){
		return dataEtudiant[7][1];
	}
	
	private int recupereSemestre(){
		return Integer.valueOf(dataEtudiant[7][2]);
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

	public String[][] getDataEtudiant() {
		return dataEtudiant;
	}

	public void setDataEtudiant(String[][] dataEtudiant) {
		this.dataEtudiant = dataEtudiant;
	}

	public int getNbEtudiants() {
		return nbEtudiants;
	}

	public void setNbEtudiants(int nbEtudiants) {
		this.nbEtudiants = nbEtudiants;
	}

	public static int getLigne() {
		return LIGNE;
	}

	public static int getColonne() {
		return COLONNE;
	}

	public static String getDelimiter() {
		return delimiter;
	}
}
