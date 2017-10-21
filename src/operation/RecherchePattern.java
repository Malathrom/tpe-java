package operation;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

//TODO a commenter Classe qui traite les pattern de fichier
public class RecherchePattern {
	
	//TODO a commenter
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

	//TODO a commenter
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

	//TODO a commenter
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

	/*TODO a commenter methode pour s'avoir quand commence les donnees d'un etudiant*/
	public static boolean rechercheDebutEtudiant(String contenu) {
		String regex = "[0-9]{2}/[0-9]{2}/20[0-9]{2}";
		if (Pattern.matches(regex, contenu))//si c'est un nouveau etudiant
			return true;
		return false;
	}

	/*TODO a commenter methode pour s'avoir quand se termine les donnees d'un etudiant*/
	public static boolean rechercheFinEtudiant(String contenu) {
		String regex = "Global";
		if (Pattern.matches(regex, contenu))
			return true;
		return false;
	}

	/*TODO a commenter methode pour s'avoir quand commence les donnees d'un semestre*/
	public static boolean rechercheDebutSemestre(String contenu) {
		String regex = "(A|P)[0-9]{2}";
		if (Pattern.matches(regex, contenu))
			return true;
		return false;
	}

	/*TODO a commenter methode pour s'avoir quand se termine les donnees d'un semestre*/
	public static boolean rechercheFinSemestre(String contenu) {
		String regex = "[0-9]{2}/[0-9]{2}";//Total
		if (Pattern.matches(regex, contenu))
			return true;
		return false;
	}
}
