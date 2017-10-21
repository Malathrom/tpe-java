package operation;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.LectureModules;
import operation.data.Etudiant;
import operation.data.Module;
/**GestionData gere les donnees des fichiers des etudiants*/
public class GestionData {

	//TODO si on a le temps on modifie le type de DataETudian en ArrayLIST

	/**
	 * LIGNE représente le nombre lignes necessaires dans une matrice pour stocker les données d'un seul etudiant
	 */
	private final static int LIGNE = 100;

	/**
	 * COLONNE représente le nombre colonne necessaires dans une matrice pour stocker les donnees d'un seul etudiant
	 */
	private final static int COLONNE = 50;

	/**
	 * file represente le fichier qui va etre traité
	 */
	private File file = null;

	/**
	 * modules contient la liste des modules existant dans le fichier modules pour filtrer les modules
	 */
	private static List<Module> modulesExistant = new ArrayList<Module>();

	/**
	 * etudiants est la liste des objets de type Etudiant que nous sommes en train de traiter dans le fichier
	 */
	private List<Etudiant> etudiants = new ArrayList<Etudiant>();

	/**
	 * dataEtudiant va contenir les donnees de l'etudiant a traiter
	 */
	private List<String> dataEtudiant = new ArrayList<String>();

	/**
	 * nomPrenom contient une chaîne de caractères contenant le nom et prénom de l'étudiant ou "" si le nom et prénom de l'étudiant ne sont pas encore trouvés
	 */
	private String nomPrenom;

	/*
	 * nbSemestre indique le nombre de semestre qu'a fait l'etudiant
	 */
	private int nbSemestreEtudiant = 0; 

	/*
	 * nbEtudiant indique le nombre d'etudiant que nous avons traité
	 */
	private int nbEtudiant = 0;

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
	 * @param file le fichier qui va etre traité
	 */
	public GestionData(File file){
		this.file = file;
		modulesExistant = LectureModules.lireModules();
	}

	/**
	 * Reset des données pour un nouvel etudiant
	 */
	public void reset(){
		totalCSTM=0;
		nomPrenom="";
		nbA=0;
		dataEtudiant = new ArrayList<String>();//on remet les donnees de l'etudiant a 0
		nbSemestreEtudiant = 0;
	}

	/**
	 * lireFichier traite le fichier txt des etudiants et permet d;instncier les etudaints et les modules pour pouvoir les manipuler en java
	 */
	public void lireFichier(){
		BufferedReader lecteurAvecBuffer;
		List<String> datas = new ArrayList<String>();//TOUTES LES DONNEES DU FICHIER
		reset();// Initialisation
		try {
			lecteurAvecBuffer = new BufferedReader(new FileReader(file));
			String contenu;
			String listeMots[];
			while ((contenu = lecteurAvecBuffer.readLine()) != null){
				listeMots = contenu.split(" ");

				//Ajout des donnees du fichier dans une liste
				for (String string : listeMots) {
					datas.add(string);
				}
			}
		}
		catch(FileNotFoundException exc) {
			System.out.println("Erreur d'ouverture");
		} 
		catch (IOException e) {e.printStackTrace();}
		//On creer les etudiants
		creationListeEtudiants(datas);
	}

	//TODO a commenter on va traite etudiant par etudiant
	private void creationListeEtudiants(List<String> datas){
		datas = suppressionElements(datas); //On supprime les elements inutile

		Iterator<String> it = datas.iterator();
		while (it.hasNext()) {//on parcourt les donnees
			String data = it.next();
			if(RecherchePattern.rechercheDebutEtudiant(data)){	//si on change d'etudiant on reste les donnees concernant l'ancien etudiant
				reset();
				nbEtudiant++;
			}
			else{//si on a pas changer d'etudiant on stocke ces donnees
				if(RecherchePattern.rechercheFinEtudiant(data)){//si on est a la fin des donnees on creer l'etudiant
					etudiants.add(ajoutEtudiant());
					reset();
					//etudiants.add(etudiant);//TODO on ajoute l'ancien etudiant dans la liste
				}
				else{
					dataEtudiant.add(data);//on est pas a la fin on attend
				}
			}
			//TODO a voir si il faut appellet creationEtuidant pour le dernier etudiant de la liste
			//System.out.println(dataEtudiant.size()+1);
		}
		AffichageEtudiant();//TODO a enlever affichage pour voir si ca marche
	}

	/**
	 * ajoutEtudiant permet de créer l'etudiant en objet Java et de l'ajouter à la liste des etudiants deja instanciées 
	 * @param dataEtudiant 
	 */
	private Etudiant ajoutEtudiant(){
		String nom = RecherchePattern.recupereNom(dataEtudiant);//on recupere le nom 
		String prenom = RecherchePattern.recuperePrenom(dataEtudiant);// on recupere le prenom
		List<Module> modulesEtudiant = ajoutModulesEtudiant();//on recupere les UE
		return new Etudiant(nom, prenom, modulesEtudiant);
	}

	/**TODO a commenter*/
	private List<Module> ajoutModulesEtudiant(){
		List<Module> modules = new ArrayList<Module>();
		List<String> dataSemestreEtudiant = new ArrayList<String>(); //donnees d'un semestre de l'etudiant
		boolean enSemestre = false;
		Iterator<String> it = dataEtudiant.iterator();
		while (it.hasNext()) {//on parcourt les donnees
			String data = it.next();

			if(RecherchePattern.rechercheDebutSemestre(data)){	//pour le premier semestre
				enSemestre = true;
			}
			if(enSemestre){//si on est dans la zone de semestres
				if(RecherchePattern.rechercheFinSemestre(data)){//si on est a la fin du semestre 
					//ajoutModules(dataSemestreEtudiant);//Ajout des modules du semestre
					modules.addAll(ajoutModules(dataSemestreEtudiant));
					dataSemestreEtudiant = new ArrayList<String>();// on reset les donnees
					enSemestre=false;
				}
				else{//si on est dans la zone semestre
					dataSemestreEtudiant.add(data);
				}
			}
		}
		return modules;
	}

	/**TODO a recommenter
	 * Creation des UEs de l'etudiant pour un semestre
	 * @param modulesData les donnees sur les UEs
	 * @param semestre le semestre des UV que l'etudiant a fair
	 * @return 
	 */
	private List<Module> ajoutModules(List<String> modulesData) {
		List<Module> mods = new ArrayList<Module>();
		nbSemestreEtudiant++;
		String parcours = RecherchePattern.recupereParcours(modulesData);
		int semestre = RecherchePattern.recupereSemestreModule(modulesData);
		String nomModule = null;
		Note note = null;
		int credit = 0;

		boolean premierModule = false;
		Iterator<String> it = modulesData.iterator();
		while (it.hasNext()) {
			String str = it.next();

			if(RecherchePattern.recupereNomModule(str) != null){//des qu'on a le premier module 
				premierModule=true;
			}
			if (premierModule) {
				if(RecherchePattern.recupereNomModule(str) != null)//tant qu'on a pas trouve un nom de module correct 
					nomModule=RecherchePattern.recupereNomModule(str);
				if(RecherchePattern.recupereNoteModule(str) != null)//tant qu'on a pas trouve une note de module correct 
					note=RecherchePattern.recupereNoteModule(str);	
				if(RecherchePattern.recupereCreditModule(str) != 0) //tant qu'on a pas trouve une note de module correct 
					credit=RecherchePattern.recupereCreditModule(str);
				
				//si toutes les valeurs sont ok alors on creer le module
				if (nomModule != null && note != null && credit != 0 && parcours != null && semestre != 0) {
					Module module = new Module(nomModule, note, credit, semestre, parcours, null);
					mods.add(module);
					nomModule = null;//on reset les donnees
					note = null;//on reset les donnees
					credit = 0;//on reset les donnees
				}
			}
		}
		return mods;
	}

	//TODO a commenter //Suppression des elements inutiles (vide, ■) se trouvant dans la liste
	public List<String> suppressionElements(List<String> liste){
		String vide = "";
		String carre = "■";
		String triangle = "▲";
		while (liste.contains(vide)) {//on supprime les chaines vide
			liste.remove(vide);
		}
		while (liste.contains(carre)) {//on supprime les carre
			liste.remove(carre);
		}
		while (liste.contains(triangle)) {//on supprime les carre
			liste.remove(triangle);
		}
		return liste;
	}	

	//TODO a commenter
	private void AffichageEtudiant() {
		for (Iterator<Etudiant> iterator = etudiants.iterator(); iterator.hasNext();) {
			Etudiant etu = (Etudiant) iterator.next();
			System.out.println(etu);
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
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
	 * retourne le nombre de note obtenues pour un étudiant et semestre donné.
	 * @param etu
	 * @param la note que l'on veut compter
	 * @param le semestre
	 * @return nbNote
	 */
	public int compteNote(Etudiant etu, Note note, int semestre){
		int nbNote=0, i=0;
		while(i<etu.getModules().size()){
			if (etu.getModules().get(i).getNote()==note && etu.getModules().get(i).getSemestre()==semestre){
				nbNote++;
			}
			i++;
		}
		return nbNote;
	}

	/**
	 * retourne le numero de semestre effectués pour un étudiant
	 * @param etu
	 * @return le nombre de semestres effectués
	 */
	public int maxSemestre(Etudiant etu){
		int maxSem=0, i=0;
		while(i<etu.getModules().size()){
			if(etu.getModules().get(i).getSemestre()>maxSem){
				maxSem=etu.getModules().get(i).getSemestre();
			}
			i++;
		}		
		return maxSem;
	}
	/**
	 * indique si une UE est ratée
	 * @param mod
	 * @return true si c'est le cas, false sinon
	 */
	public boolean estRatee(Module mod){
		boolean out=false;
		if (mod.getCredit()==0){
			out=true;
		}
		return out;
	}
	/**
	 * retourne le nombre d'ue pour un étudiant et semestre donné
	 * @param etu
	 * @param sem
	 * @return
	 */
	public int nombreUeSemestre(Etudiant etu, int sem){
		int nbUe=0, i=0;
		while(i<etu.getModules().size()){
			if(etu.getModules().get(i).getSemestre()==sem){
				nbUe++;
				System.out.println(etu.getModules().get(i));
			}
			i++;
		}
		return nbUe;
	}

	/**
	 * Retourne l'avis du jury pour un élève en particulier sous forme de string
	 * @param l'étudiant choisis
	 * @return l'avis de chaque semestre dans un tableau, chaque case représentant un semestre
	 */
	public ArrayList<String> avisJury(Etudiant etu){
		ArrayList<String> out= new ArrayList<String>();
		int maxSem=maxSemestre(etu);
		System.out.println("max=" +maxSem);
		int sem=1;
		while(sem<maxSem){
			String str="";
			int nbA=compteNote(etu, Note.A, sem);
			int nbB=compteNote(etu, Note.B, sem);
			int nbD=compteNote(etu, Note.D, sem);
			int nbE=compteNote(etu, Note.E, sem);
			int nbUe=nombreUeSemestre(etu, sem);
			System.out.println(etu.getModules().get(3));
			System.out.println(nbUe);
			int nbUeRatees=0, i=0, nbUeRateesCSTM=0;
			while(i<etu.getModules().size()){
				if (estRatee(etu.getModules().get(i)) && etu.getModules().get(i).getSemestre()==sem && !(etu.getModules().get(i).getCategorie()=="CS" || etu.getModules().get(i).getCategorie()=="TM")){
					nbUeRatees++;
				}
				if (estRatee(etu.getModules().get(i)) && etu.getModules().get(i).getSemestre()==sem && (etu.getModules().get(i).getCategorie()=="CS" || etu.getModules().get(i).getCategorie()=="TM")){
					nbUeRateesCSTM++;
				}
				i++;
			}
			int nbUeRateesTotal=nbUeRatees+nbUeRateesCSTM;
			if (nbUeRateesTotal==0){
				str+="Poursuite Normale";
				if ((nbA+nbB)/nbUe>0.7){
					str+=", Excellent Semestre";
				}
				else if ((nbA+nbB)/nbUe>0.6){
					str+=", Très Bon Semestre";
				}
				else if ((nbA+nbB)/nbUe>0.5){
					str+=", Bon Semestre";
				}
				else if ((nbE+nbD)/nbUe<0.5){
					str+=", Assez Bon Semestre";
				}
				else{
					str+=", Semestre Moyen";
				}		
			}
			if (nbUeRateesTotal==1){
				str+="Poursuite avec Conseil";
				if ((nbE+nbD)/nbUe<0.5){
					str+=", Semestre Moyen";
				}
				else{
					str+=", Semestre Médiocre";
				}
			}
			if (nbUeRateesTotal>=2){
				str+="Poursuite avec Réserve";
				if (nbUeRateesCSTM<=1){
					str+=", Mauvais Semestre";
				}
				else{
					str+=", Très Mauvais Semestre";
				}
			}

			sem++;
			out.add(str);
		}
		ArrayList<String> a= new ArrayList<String>();
		a.add("bleh");
		return a;
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

	public static int getLigne() {
		return LIGNE;
	}

	public static int getColonne() {
		return COLONNE;
	}


}
