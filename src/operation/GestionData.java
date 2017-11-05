package operation;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import data.Etudiant;
import data.Module;
import io.LectureModules;
/**GestionData gere les donnees des fichiers des etudiants*/
public class GestionData {

	/**
	 * etudiants est la liste des objets de type Etudiant que nous sommes en train de traiter dans le fichier
	 */
	private List<Etudiant> etudiants = new ArrayList<Etudiant>();

	/*
	 * nbSemestre indique le nombre de semestre qu'a fait l'etudiant
	 */
	private static int nbSemestres = 0; 

	/*
	 * nbEtudiant indique le nombre d'etudiant que nous avons traité
	 */
	private static int nbEtudiant = 0;

	/**
	 * Reset des données pour un nouvel etudiant
	 */
	public static void reset(){
		setNbSemestres(0);
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
				setNbEtudiant(getNbEtudiant() + 1);
			}
			else{//si on a pas changer d'etudiant on stocke ces donnees
				if(RecherchePattern.rechercheFinEtudiant(data)){//si on est a la fin des donnees on creer l'etudiant
					dataEtudiant.add(data); //on ajoute le dernier element au donnee
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
		int credit = RecherchePattern.recupereTotalCredit(dataEtudiant);
		nbEtudiant++;
		return new Etudiant(nom, prenom, modulesEtudiant, credit, nbSemestres);
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
					modules.addAll(creationModulesSemestre(dataSemestreEtudiant));
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
	 * creationModulesSemestre creer les modules de l'etudiant d'un semestre
	 * @param modulesData les donnees sur les UEs d'un semestre
	 * @return la liste des modules du semestre
	 */
	private static List<Module> creationModulesSemestre(List<String> modulesData) {
		List<Module> mods = new ArrayList<Module>();
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
			System.out.println("chaine " +str);

			if(RecherchePattern.recupereNomModule(str) != null){//des qu'on a le premier module 
				nomModule=RecherchePattern.recupereNomModule(str);//on recupere le nom du module
				categorie=RecherchePattern.recupereCategorie(nomModule);// la categorie
				credit=RecherchePattern.recupereCredit(str); //le nombre de credit
				System.out.println(1);
			}
			
			if(RecherchePattern.recupereNote(str) != null){
				System.out.println(5);
				note=RecherchePattern.recupereNote(str);	
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
		nbSemestres++; //on ajute un semestre a l'etudiant
		return mods;
	}

	//TODO a commenter
	public List<Etudiant> getEtudiants() {
		return etudiants;
	}

	//TODO a commenter
	public void setEtudiants(List<Etudiant> etudiants) {
		this.etudiants = etudiants;
	}

	//TODO a commenter
	public static int getNbEtudiant() {
		return nbEtudiant;
	}

	//TODO a commenter
	public static void setNbEtudiant(int nbEtudiant) {
		GestionData.nbEtudiant = nbEtudiant;
	}

	//TODO a commenter
	public static int getNbSemestres() {
		return nbSemestres;
	}

	//TODO a commenter
	public static void setNbSemestres(int nbSemestreEtudiant) {
		GestionData.nbSemestres = nbSemestreEtudiant;
	}
}
