package operation;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.Filtrage;
import io.LectureModules;
import operation.data.Etudiant;
import operation.data.Module;
/**GestionData gere les donnees des fichiers des etudiants*/
public class GestionData {

	/**
	 * LIGNE représente le nombre lignes necessaires dans une matrice pour stocker les données d'un seul etudiant
	 */
	private final static int LIGNE = 100;

	/**
	 * COLONNE représente le nombre colonne necessaires dans une matrice pour stocker les donnees d'un seul etudiant
	 */
	private final static int COLONNE = 50;

	/**
	 * delimiter représente le delimiteur entre 2 etudiants dans le fichier texte
	 */
	private final static String delimiter = " ";

	/**
	 * file represente le fichier qui va etre traité
	 */
	private File file = null;

	/**
	 * modules contient la liste des modules existant dans le fichier modules pour filtrer les modules
	 */
	private static List<Module> modules = new ArrayList<Module>();

	/**
	 * etudiants est la liste des objets de type Etudiant que nous sommes en train de traiter dans le fichier
	 */
	private List<Etudiant> etudiants = new ArrayList<Etudiant>();

	/**
	 * dataEtudiant contient les données texte nom formates sur un etudiant (nom, prenomm Ues, notes)
	 */
	private String[][] dataEtudiant = new String[LIGNE][COLONNE];

	/**
	 * nbEtudiant représente le nombre d'etudiant present dans le fichier
	 */
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

	/** 
	 * Constructeur : Lance la lecture des modules existant en ISI qui se trouve dans le fichier /files/module.txt
	 * @param file chemin du fichier qui traité
	 */
	public GestionData(String file){
		this.file = new File(file);
		modules = LectureModules.lireModules();
	}
	/** 
	 * Constructeur : Lance la lecture des modules existant en ISI qui se trouve dans le fichier /files/module.txt
	 * @param file le fichier qui va etre traité
	 */
	public GestionData(File file){
		this.file = file;
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

	/**
	 * lireFichier traite le fichier txt des etudiants et permet d;instncier les etudaints et les modules pour pouvoir les manipuler en java
	 */
	public void lireFichier(){
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
					System.out.println("New etudiant");// TODO a enlever
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
			System.out.println(nbEtudiants);//TODO A enlever un moment
		}
		catch(FileNotFoundException exc) {
			System.out.println("Erreur d'ouverture");
		} 
		catch (IOException e) {e.printStackTrace();}
	}

	/**
	 * IsNouvelEtudiant permet de savoir quand on passe a un nouvel etudiant dans le fichier txt
	 * @param contenu correspond a la ligne en cours dans le fichier
	 * @param delimiter correspond à la chaine qui delimite 2 etudiants dans le fichier
	 * @return true si on passe a un nouvel etudiant dans le fichier sinon false
	 */
	private boolean isNouvelEtudiant(String contenu, String delimiter) {
		if (contenu.equals(delimiter))
			return true;
		return false;
	}

	/**
	 * creationEtudiant permet de créer l'etudiant en objet Java et de l'ajouter à la liste des etudiants deja instanciées 
	 */
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

	/**
	 * recupereNom recupere le nom de l'etudiant dans la matrice data
	 * @return retourne le nom de l'etudiant
	 */
	private String recupereNom(){
		return dataEtudiant[1][2];
	}

	/**
	 * recuperePrenom recupere le prenom de l'etudiant dans la matrice data
	 * @return retourne le prenom de l'etudiant
	 */
	private String recuperePrenom(){
		return dataEtudiant[1][1];
	}

	/**
	 * recuperUEs recupere la liste des UEs de l'etudiant dans la matrice data
	 * @return retourne la liste des UEs de l'etudiant
	 */
	private List<String> recupereUEs(){
		List<String> semestresEtudiant = recuperesDonneesSemestres();

		int semestre = recupereSemestre();
		String typeSemestre = recupereTypeSemestre();

		System.out.println("typesemestre "+ typeSemestre);
		System.out.println("semestre "+ semestre);
		return null;
	}

	//TODO a commenter //on stocke les donnees des semestres uniquement
	private List<String> recuperesDonneesSemestres() {
		List<String> semestresUE = new ArrayList<String>();
		int i, j;
		boolean enSemestre = false;//permet desavoir si on parcourt les semestres

		//on parcourt les donnees
		for(i=0; i<dataEtudiant.length; i++) {
			for(j=0; j<dataEtudiant[i].length; j++) {
				if(dataEtudiant[i][j]!=null) {//si le champ n'est pas null
					if(dataEtudiant[i][j].equals("Observations")){//le mot Observations est le dernier mot avant les semestres
						enSemestre=true;
						break;
					}

					else if(enSemestre){//si on est dans la partie semestres
						if(dataEtudiant[i][j]!=null) {//si le champ n'est pas null//si il y a plus rien sur la ligne on passe a la suite
							if(!dataEtudiant[i][j].equals("TOTAUX")){//si on a pas fini les semestres
								semestresUE.add(dataEtudiant[i][j]);
								//TODO creer les semestres ICI
							}
						}
					}
					else //si on est a la ligne TOTAUX on a fini les semestres
						enSemestre=false;
				}
			}
		}
		
		//TODO faire des sous ArrayList pour chque semestres ils sont separes par Total semestre
		//TODO creer un matrice UE en testant si la ligne est P+un nombre ou A+plus un nombre on met donc la suite dans la matrice
		//TODO recuperer les donnees sur les UVs, note categorie etc
		//ArrayList<Module> module = ueEtudiant()
		

		semestresUE = suppressionElements(semestresUE);//Suppression des elements vide de l'ArrayList

		System.out.println("debut");
		Iterator<String> it = semestresUE.iterator();
		while (it.hasNext()) {
			String str = it.next();

			if (str.equals("■")) {
				System.out.print("carre");
			}
			System.out.println(semestresUE.indexOf(str) + " "+ str);
		}
		System.out.println(semestresUE.size());
		System.out.println("fin");
		return null;
	}

	//TODO a commenter //Suppression des elements inutiles (vide, ■) se trouvant dans la liste
	public List<String> suppressionElements(List<String> liste){
		String vide = "";
		String carre = "■";
		while (liste.contains(vide)) {//on supprime les chaines vide
			liste.remove(vide);
		}
		while (liste.contains(carre)) {//on supprime les carre
			liste.remove(carre);
		}
		return liste;
	}

	/**
	 * recupereTypeSemestre recupere le type de semestre de l'etudiant dans la matrice data entre ISI ou TC ou MASTER
	 * @return retourne le type de semestre de l'etudiant
	 */
	private String recupereTypeSemestre(){
		return dataEtudiant[7][1];
	}

	/**
	 * recupereSemestre recupere le numero de semestre de l'etudiant dans la matrice data
	 * @return retourne le numero de semestre de l'etudiant
	 */
	private int recupereSemestre(){
		return Integer.valueOf(dataEtudiant[7][2]);
	}

	/**
	 * Teste si un string est contenu dans une enum
	 * @param value //TODO a definir value
	 * @param enumClass //TODO a definir enumClass
	 * @return
	 */
	public <E extends Enum<E>> boolean isInEnum(String value, Class<E> enumClass) {
		for (E e : enumClass.getEnumConstants()) {
			if(e.name().equals(value)) { return true; }
		}
		return false;
	}

	/**
	 * renvois une liste constitué des ue d'un étudiant
	 * @param tabMots //TODO a definir tabmots
	 * @return //TODO dire ce que ca retourne
	 */
	public void ueEtudiant(String tabMots[], Etudiant etu){
		int i;
		i=0;
		int semestre=1;
		while (i<tabMots.length){
			if (isInEnum(tabMots[i], Note.class)&& Filtrage.isNumeric(tabMots[i+3])){
				Module mod = new Module(tabMots[i-2], Integer.valueOf(tabMots[i+3]), semestre, Note.valueOf(tabMots[i]));
				etu.getModules().add(mod);
			}
			i++;
		}	
		System.out.println(etu.getModules());
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