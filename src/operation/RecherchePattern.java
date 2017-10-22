package operation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import io.LectureModules;
import operation.data.Module;

/**RecherchePattern traite les pattern dans les fichiers texte pour recuperer des donnees senesible*/
public class RecherchePattern {

	/**
	 * modules contient la liste des modules existant dans le fichier modules pour filtrer les modules
	 */
	private static List<Module> modulesExistant = LectureModules.lireModules();

	/**
	 * recupereNom recherche avec un systeme de regex le nom de l'etudiant dans le fichier
	 * @param dataEtudiant la liste des donnes dans laquelle chercher le nom
	 * @return le nom de l'etudiant
	 */
	public static String recupereNom(List<String> dataEtudiant){
		Iterator<String> it = dataEtudiant.iterator();
		String regex = "N°[0-9]{5}";
		int position = 0;
		String contenu;
		while(it.hasNext()){
			contenu = it.next();
			if (Pattern.matches(regex, contenu)){
				position = dataEtudiant.indexOf(contenu)-1;// on recupere la position du nom
				return dataEtudiant.get(position); //le nom se trouve juste avant le numero etudiant
			}
		}
		return null;
	}

	/**
	 * recuperPrenom recherche avec un systeme de regex le prenom de l'etudiant dans le fichier
	 * @param dataEtudiant la liste des donnes dans laquelle chercher le prenom
	 * @return le prenom de l'etudiant
	 */
	public static String recuperePrenom(List<String> dataEtudiant){
		Iterator<String> it = dataEtudiant.iterator();
		//String regex = "M.|Mme"; //Mme Ruolin ZHENG
		String regex = "N°[0-9]{5}";
		int position = 0;
		String contenu;
		while(it.hasNext()){
			contenu = it.next();
			if (Pattern.matches(regex, contenu)){
				position = dataEtudiant.indexOf(contenu)-2; // on recupere la position du prenom
				return dataEtudiant.get(position);// le prenom se trouve 2 cran avant le numero etudiant
			}
		}
		return null;
	}

	/**
	 * rechercheDebutEtudiant indique avec un systeme de regex quand commence les donnees d'un etudiant
	 * @param contenu la chaine a verifier 
	 * @return true si le conetnu indique que c'est un nouvelle etudiant
	 */
	public static boolean rechercheDebutEtudiant(String contenu) {
		String regex = "[0-9]{2}/[0-9]{2}/20[0-9]{2}";
		if (Pattern.matches(regex, contenu))//si c'est un nouveau etudiant
			return true;
		return false;
	}

	/**
	 * rechercheFinEtudiant indique avec un systeme de regex quand fini les donnees d'un etudiant
	 * @param contenu la chaine a verifier 
	 * @return true si le contenu indique que c'est la fin d'un etudiant
	 */
	public static boolean rechercheFinEtudiant(String contenu) {
		String regex = "Global";
		if (Pattern.matches(regex, contenu))
			return true;
		return false;
	}

	/**
	 * rechercheDebutSemestre indique avec un systeme de regex quand debute les donnees d'un semestre d'un etudiant
	 * @param contenu la chaine a verifier
	 * @return true si le contenu indique que c'est le debut d'un semestre
	 */
	public static boolean rechercheDebutSemestre(String contenu) {
		String regex = "(A|P)[0-9]{2}";
		if (Pattern.matches(regex, contenu))
			return true;
		return false;
	}

	/**
	 * rechercheFinSemestre indique avec un systeme de regex quand fini les donnees d'un semestre d'un etudiant
	 * @param contenu la chaine a verifier
	 * @return true si le contenu indique que c'est le debut d'un semestre
	 */
	public static boolean rechercheFinSemestre(String contenu) {
		String regex = "Total";//Total
		if (Pattern.matches(regex, contenu))
			return true;
		return false;
	}

	/**
	 * recupereNomModule recherche avec un systeme de regex le nom du module
	 * @param contenu la chaine a verifier
	 * @return le nom du module
	 */
	public static String recupereNomModule(String contenu) {
		String regex = "([A-Z]{3,5})|([A-Z]{2,}[0-9]{2})";//un nom de module
		if (Pattern.matches(regex, contenu))
			return contenu;
		return null;
	}

	/**
	 * recupereNote recherche avec un systeme de regex la note du module
	 * @param contenu la chaine a verifier
	 * @return la note du module
	 */
	public static Note recupereNote(String contenu) {
		String regex = "[A-F]|FX|EQU|ABS|NULL";//juste une lettre
		if (Pattern.matches(regex, contenu))
			return Note.getNote(contenu);
		return null;
	}

	/**
	 * recupereCredit recherche avec un systeme de regex le credit du module
	 * @param contenu la chaine a verifier
	 * @return le credit du module
	 */
	public static int recupereCredit(String nomModule) {
		Iterator it = modulesExistant.iterator();
		while (it.hasNext()) {
			Module mod = (Module) it.next();
			if(mod.getNom().equals(nomModule)){//si on a trouve un module correspondant
				return mod.getCredit();//on retourne sa categorie
			}
		}
		return 0;
	}

	/**
	 * recupereSemestreModule recherche avec un systeme de regex le semestre du module
	 * @param contenu la chaine a verifier
	 * @return le semestre du module
	 */
	public static int recupereSemestre(List<String> modulesData) {
		String semestre = null;
		try{
			String recupereParcours = recupereParcours(modulesData);// on recupere le parcours qui se trouve juste avant le semestre
			int pos = modulesData.indexOf(recupereParcours);//on recupere la position
			semestre = modulesData.get(pos+1);//on recupere le semestre
			return Integer.valueOf(semestre);
		}catch(NumberFormatException e){
			System.out.println(semestre);
		}
		return 0;
	}

	/**
	 * recupereParcours recherche avec un systeme de regex le parcours du module
	 * @param contenu la chaine a verifier
	 * @return le parcours du module
	 */
	public static String recupereParcours(List<String> modulesData) {
		Iterator<String> it = modulesData.iterator();
		String regex = "ISI|TC|MASTER";
		String contenu;
		while(it.hasNext()){
			contenu = it.next();
			if (Pattern.matches(regex, contenu))//si c'est un nouveau etudiant
				return contenu;
		}
		return null;
	}

	//TODO a commneter on recupere la categorie du module a partir de son nom et en utilisant modulesExistant
	public static String recupereCategorie(String nomModule){
		Iterator it = modulesExistant.iterator();
		while (it.hasNext()) {
			Module mod = (Module) it.next();
			if(mod.getNom().equals(nomModule)){//si on a trouve un module correspondant
				return mod.getCategorie();//on retourne sa categorie
			}
		}
		return "Inconnue";
	}
}
