package operation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import data.Etudiant;
import data.Module;
import io.LectureModules;

public abstract class DecisionJury {

	List<Etudiant> etudiants = new ArrayList<Etudiant>();

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

	/** TODO mettre un bollen si l'etudiant a fait en dernier semestre un stage
	/**
	 * enStage recherche si l'etudiant est soit en stage ST09, soit en ST10, soit ST30
	 * tabMots tableau de mots composant une ligne de texte à analyser
	 * @return true si le tableau tabMots contient le stage "st09" ou "st10" ou "st30", sinon false
	public boolean enStage(String[] tabMots){
		if(trouveST09(tabMots) || isSt09())
			return true;
		if(trouveST10(tabMots) || isSt10())
			return true;
		if(trouveST30(tabMots) || isSt30())
			return true;
		return false;
	}

	/*TODO A Appeler quelque part et a commenter
	 * 
	 */
	public int compteCreditsISIOuTC (){
		int i, credits;
		credits=0;
		i=0;
		List<Module> modules = LectureModules.lireModules();
		Iterator<Module> it = modules.iterator();
		while(it.hasNext()){
			Module mod =it.next();
			if(mod.getParcours().equals("TC") || mod.getParcours().equals("ISI")){//si l'UV est ISI ou TC on ajoute ces credits
				credits+=mod.getCredit();
			}
		}
		return credits;
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
}
