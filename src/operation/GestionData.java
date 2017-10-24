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

	/**
	 * etudiants est la liste des objets de type Etudiant que nous sommes en train de traiter dans le fichier
	 */
	private List<Etudiant> etudiants = new ArrayList<Etudiant>();

	/*
	 * nbSemestre indique le nombre de semestre qu'a fait l'etudiant
	 */
	private static int nbSemestreEtudiant = 0; 

	/*
	 * nbEtudiant indique le nombre d'etudiant que nous avons traité
	 */
	private static int nbEtudiant = 0;

	/**
	 * Reset des données pour un nouvel etudiant
	 */
	public static void reset(){
		nbSemestreEtudiant = 0;
	}

	/**
	 * listeEtudiant traite le fichier txt des etudiants et permet d;instncier les etudaints et les modules pour pouvoir les manipuler en java
	 * @return la liste des etudiants
	 */
	public static List<Etudiant> listeEtudiant(File file){
		BufferedReader lecteurAvecBuffer;
		List<String> datas = new ArrayList<String>();//donnes du fichier
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
		return creationListeEtudiants(datas);
	}

	/**
	 * methode qui instancie la liste des etudiants a partir du fichier texte
	 * @param datas les donnees dans le fichier
	 * @return la liste des etudiants
	 */
	private static List<Etudiant> creationListeEtudiants(List<String> datas){
		List<String> dataEtudiant = new ArrayList<String>();
		List<Etudiant> etudiants = new ArrayList<Etudiant>();
		Iterator<String> it = datas.iterator();
		while (it.hasNext()) {//on parcourt les donnees
			String data = it.next();
			if(RecherchePattern.rechercheDebutEtudiant(data)){	//si on change d'etudiant on reste les donnees concernant l'ancien etudiant
				reset();
				dataEtudiant = new ArrayList<String>();//on reset les donnees
				nbEtudiant++;
			}
			else{//si on a pas changer d'etudiant on stocke ces donnees
				if(RecherchePattern.rechercheFinEtudiant(data)){//si on est a la fin des donnees on creer l'etudiant
					etudiants.add(ajoutEtudiant(dataEtudiant));
					reset();
					//etudiants.add(etudiant);//TODO on ajoute l'ancien etudiant dans la liste
				}
				else{
					dataEtudiant.add(data);//on est pas a la fin on attend
				}
			}
		}
		return etudiants;
	}

	/**
	 * ajoutEtudiant permet de créer l'etudiant en objet Java et de l'ajouter à la liste des etudiants deja instanciées 
	 * @param dataEtudiant les donnees de l'etudiant
	 * @return l'etudiant
	 */
	private static Etudiant ajoutEtudiant(List<String> dataEtudiant){
		String nom = RecherchePattern.recupereNom(dataEtudiant);//on recupere le nom 
		String prenom = RecherchePattern.recuperePrenom(dataEtudiant);// on recupere le prenom
		List<Module> modulesEtudiant = ajoutModulesEtudiant(dataEtudiant);//on recupere les UE
		return new Etudiant(nom, prenom, modulesEtudiant);
	}

	/**
	 * ajoutModulesEtudiant methode qui recupere tous les modules qu'a fait l'etudiant
	 * @param dataEtudiant  les donnees de l'etudiant
	 * @return la liste des modules
	 */
	private static List<Module> ajoutModulesEtudiant(List<String> dataEtudiant){
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
					modules.addAll(creationModules(dataSemestreEtudiant));
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

	/**
	 * creationModules creer les modules de l'etudiant
	 * @param modulesData les donnees sur les UEs
	 * @return la liste des modules du semestres
	 */
	private static List<Module> creationModules(List<String> modulesData) {
		List<Module> mods = new ArrayList<Module>();
		nbSemestreEtudiant++;
		String parcours = RecherchePattern.recupereParcours(modulesData);
		int semestre = RecherchePattern.recupereSemestre(modulesData);
		String nomModule = null;
		Note note = null;
		String categorie = null;
		int credit = 0;

		boolean premierModule = false;
		Iterator<String> it = modulesData.iterator();
		while (it.hasNext()) {
			String str = it.next();

			if(RecherchePattern.recupereNomModule(str) != null){//des qu'on a le premier module 
				nomModule=RecherchePattern.recupereNomModule(str);
				premierModule=true;
			}
			if (premierModule) {
				if(RecherchePattern.recupereNomModule(str) != null){
					nomModule=RecherchePattern.recupereNomModule(str);
					if(RecherchePattern.recupereCategorie(nomModule) != null){ //une fois qu'on a le nom du module si il est de type ISI on essaye de savoir si c'est CS OU TM
						categorie=RecherchePattern.recupereCategorie(nomModule);
					}
				}
				if(RecherchePattern.recupereNote(str) != null){
					note=RecherchePattern.recupereNote(str);	
				}
				if(RecherchePattern.recupereCredit(str) != 0){ 
					credit=RecherchePattern.recupereCredit(str);
				}
				
				
				//si toutes les valeurs sont ok alors on creer le module
				if (nomModule != null && note != null && credit != 0 && parcours != null && semestre != 0) {
					Module module = new Module(nomModule, note, credit, semestre, parcours, categorie);
					mods.add(module);
					//on reset les donnees pour le prochain module
					nomModule = null;
					note = null;
					credit = 0;
					categorie = null;
				}
			}
		}
		return mods;
	}

	/**
	 * retourne le nombre de note obtenues pour un étudiant et semestre donné.
	 * @param etu
	 * @param la note que l'on veut compter
	 * @param le semestre
	 * @return nbNote
	 */
	public static int compteNote(Etudiant etu, Note note, int semestre){
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
	public static int maxSemestre(Etudiant etu){
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
	public static boolean estRatee(Module mod){
		boolean out=false;
		if (mod.getNote()==Note.F || mod.getNote()==Note.FX || mod.getNote()==Note.ABS){
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
	public static int nombreUeSemestre(Etudiant etu, int sem){
		int nbUe=0, i=0;
		while(i<etu.getModules().size()){
			if(etu.getModules().get(i).getSemestre()==sem){
				nbUe++;
			}
			i++;
		}
		return nbUe;
	}
	
	/**
	 * renvois le nom du dernier stage effectué par l'étudiant
	 * @param etu l'étudiant en question
	 * @return "TN30" si tn30, etc... puis "pas de stage" si aucun stage n'a été trouvé
	 */
	public static String dernierStage(Etudiant etu){
		int i=0;
		boolean tn09=false, tn10=false, tn30=false;
		while(i<etu.getModules().size()){
			if(etu.getModules().get(i).getCategorie()=="ST"){
				System.out.println(etu.getModules().get(i));
			}
			if (etu.getModules().get(i).getNom()=="TN09"){
				tn09=true;
			}
			if (etu.getModules().get(i).getNom()=="TN10"){
				tn10=true;
			}
			if (etu.getModules().get(i).getNom()=="TN30"){
				tn30=true;
			}
			i++;
		}
		if(tn30==true){
			return "TN30";
		}
		else if(tn10==true){
			return "TN10";
		}
		else if(tn09==true){
			return "TN09";
		}
		else {
			return "Pas de stage";
		}
		
	}

	/**
	 * Retourne l'avis du jury pour un élève en particulier sous forme de string
	 * @param l'étudiant choisis
	 * @return l'avis de chaque semestre dans un tableau, chaque case représentant un semestre
	 */
	public static List<String> avisJury(Etudiant etu){
		ArrayList<String> out= new ArrayList<String>();
		int maxSem=maxSemestre(etu)+1;
		int sem=1;
		while(sem<maxSem){
			String str="";
			int nbA=compteNote(etu, Note.A, sem);
			int nbB=compteNote(etu, Note.B, sem);
			int nbD=compteNote(etu, Note.D, sem);
			int nbE=compteNote(etu, Note.E, sem);
			int nbUe=nombreUeSemestre(etu, sem);
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
				if (((float)nbA+nbB)/nbUe>0.7){
					str+=", Excellent Semestre";
				}
				else if (((float)nbA+nbB)/nbUe>0.6){
					str+=", Très Bon Semestre";
				}
				else if (((float)nbA+nbB)/nbUe>0.5){
					str+=", Bon Semestre";
				}
				else if (((float)nbE+nbD)/nbUe<0.5){
					str+=", Assez Bon Semestre";
				}
				else{
					str+=", Semestre Moyen";
				}
			}
			if (nbUeRateesTotal==1){
				str+="Poursuite avec Conseil";
				if (((float)nbE+nbD)/nbUe<0.5){
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
		return out;
	}

	public List<Etudiant> getEtudiants() {
		return etudiants;
	}

	public void setEtudiants(List<Etudiant> etudiants) {
		this.etudiants = etudiants;
	}
}
