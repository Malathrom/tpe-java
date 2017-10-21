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
		String regex = "Total";//Total
		if (Pattern.matches(regex, contenu))
			return true;
		return false;
	}

	public static String recupereNomModule(String contenu) {
		String regex = "[A-Z]{2,}[0-9]{2}";//un nom de module
		if (Pattern.matches(regex, contenu))
			return contenu;
		return null;
	}

	public static Note recupereNoteModule(String contenu) {
		String regex = "[A-F]|FX|EQU|ABS|NULL";//juste une lettre
		if (Pattern.matches(regex, contenu))
			return Note.getNote(contenu);
		return null;
	}

	public static int recupereCreditModule(String contenu) {
		String regex = "[0-9]";//juste un chiffre
		if (Pattern.matches(regex, contenu))
			return Integer.valueOf(contenu);
		return 0;
	}

	public static int recupereSemestreModule(List<String> modulesData) {
		String recupereParcours = recupereParcours(modulesData);// on recupere le parcours qui se trouve juste avant le semestre
		int pos = modulesData.indexOf(recupereParcours);//on recupere la position
		String semestre = modulesData.get(pos+1);//on recupere le semestre
		return Integer.valueOf(semestre);
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
}
