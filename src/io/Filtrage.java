package io;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import modules.LectureModules;
import operation.data.Module;

/**
 * Filtrage est une classe Java qui permet d'écrire dans un fichier CSV des propositions de décisions de Jury du département ISI.
 * Elle se base sur un PV de jury écrit au format texte TXT
 * @author nigro
 * @version 1.0
 */
public class Filtrage {

	//TODO faire des sysout pour voir si la sepeartion filtrage/conversion est bonne

	/**
	 * modules contient la liste des modules existant dans le fichier modules pour filtrer les modules
	 * */
	private static List<Module> modules = new ArrayList<Module>();

	private String rechercheStage;
	/**
	 * nomPrenom contient une chaîne de caractères contenant le nom et prénom de l'étudiant ou "" si le nom et prénom de l'étudiant ne sont pas encore trouvés
	 */
	private String nomPrenom;
	/**
	 * nomZone contient une chaine de caractéres indiquant la zone du PV de jury où le filtrage est en train de travailler
	 * "inconnue" : signifie qu'on se trouve dans la zone de description de l'étudiant
	 * "TC" : signifie qu'on se trouve dans la zone formation Tronc Commun de l'étudiant
	 * "ISI" : signifie qu'on se trouve dans la zone formation ISI de l'étudiant
	 * "Master" : signifie qu'on se trouve dans la zone formation Master de l'étudiant
	 */
	private String nomZone;
	/**
	 * st09 est vrai lorsque l'étudiant a validé son ST09
	 */
	private boolean st09;
	/**
	 * st10 est vrai lorsque l'étudiant a validé son ST10
	 */
	private boolean st10;
	/**
	 * st30 est vrai lorsque l'étudiant a validé son ST30
	 */
	private boolean st30;
	/**
	 * estPasseParTC est vrai lorsque l'étudiant a fait un TC
	 */
	private boolean estPasseParTC;
	/**
	 * estPasseParISI est vrai lorsque l'étudiant a fait le cursus ISI
	 */
	private boolean estPasseParISI;
	/**
	 * estPasseParMaster est vrai lorsque l'étudiant a fait le cursus Master
	 */
	private boolean estPasseParMaster;
	/**
	 * universiteChinoise est vrai lorsque l'étudiant est un étudiant Chinois (gestion particuliére)
	 */
	private boolean universiteChinoise;
	/**
	 * st09_st10_st30 contient "st09", "st10" ou "st30" si le prochain stage à valider est respectivement un stage "st09", "st10" ou "st30"
	 */
	private String st09_st10_st30;
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
	public Filtrage(){
		modules = LectureModules.lireModules();
		System.out.println(modules);//TODO a enlever
	}

	/**
	 * Initialisation des attributs de la classe.
	 */
	public void initialiseRecherche(){
		totalCSTM=0;
		st09=false;
		st10=false;
		st30=false;
		universiteChinoise = false;
		estPasseParTC     = false;
		estPasseParISI    = false;
		estPasseParMaster = false;
		st09_st10_st30="";
		nomPrenom="";
		nomZone="inconnue";
		nbA=0;
	}

	/**
	 * Codage du raisonnement du jury
	 * Le résultat est stocké dans la chaine de caractéres sortieExcelCSV au format CSV
	 * @return 
	 */
	public String decisionsJury(){
		if (totalCSTM<=12) {
			System.out.println("Pas de recherche de stage-->"+nomPrenom);
			rechercheStage="non";
		}
		else rechercheStage="oui";

		if (st10 || st30) {
			st09_st10_st30="Stages déjà effectués";
			rechercheStage="---";
		}
		else {
			if (st09) {
				if (universiteChinoise){
					st09_st10_st30="---";
					rechercheStage="Université chinoise";
				} else if (estPasseParMaster) {
					st09_st10_st30="ST30";
				}
				else {st09_st10_st30="ST10";}
			}
			else st09_st10_st30="ST09";
		}

		// Formatage de la ligne à écrire. Puis on l'écrit
		return totalCSTM+";"+st09_st10_st30+";"+rechercheStage+nomPrenom+";"+nbA+" A obtenus"+"\n";
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

	/**
	 * Comptabilise les crédits des UE TC de branche ISI (pour une ligne de texte)
	 * @param tabMots tableau de mots composant une ligne de texte à analyser
	 * @return le total des CS+TM trouvés dans tabMots
	 */
	public int compteCSTM (String tabMots[]){
		int i, credits;
		credits=0;
		i=0;
		Iterator<Module> it = modules.iterator();
		while (i<tabMots.length){

			/**			while (it.hasNext()){
				Module mod = it.next();
				if(mod.getNom().equals(tabMots[i])){
					if ((i+5<tabMots.length) && (tabMots[i+5].equals("6")))
						System.out.println("c'est equal");
					credits=credits+6;
					i=i+6;
				}
				else i=i+1;
			}*/


			if ((tabMots[i].equals("NF16")) || (tabMots[i].equals("EG23")) ||
					(tabMots[i].equals("GL02")) || (tabMots[i].equals("NF20")) ||
					(tabMots[i].equals("IF07")) || (tabMots[i].equals("IF09")) ||
					(tabMots[i].equals("IF14")) || (tabMots[i].equals("LO02")) ||
					(tabMots[i].equals("IF02")) || (tabMots[i].equals("LO12")) ||
					(tabMots[i].equals("RE04")) || (tabMots[i].equals("IF03")) ||
					(tabMots[i].equals("LO07")) || (tabMots[i].equals("NF19"))
					) {
				if ((i+5<tabMots.length) && (tabMots[i+5].equals("6")))
					//System.out.println("tab" + tabMots);/*TODO a enlever*/
					//System.out.println(nomPrenom + " " +totalCSTM);/*TODO a enlever*/
				credits=credits+6;
				i=i+6;
			}
			else i=i+1;
		}
		return credits;
	}

	/**
	 * Indique si le mot clé "Jiaotong" se trouve dans le tableau
	 * Ces étudiants chinois ne font qu'un ST09 et pas de ST10
	 * @param tabMots tableau de mots composant une ligne de texte à analyser
	 * @return true si le tableau tabMots contient une université Chinoise (Jiaotong), sinon false
	 */
	public boolean trouveUniversiteChinoise (String tabMots[]){
		int i;
		i=0;
		while ((i<tabMots.length) && (! (tabMots[i].equals("Jiaotong"))) ) {
			i=i+1;
		}
		return (i<tabMots.length);	
	}

	/**
	 * Indique si le mot clé "ST09" ou "TN09" (stage intermédiaire) se trouve dans le tableau
	 * @param tabMots tableau de mots composant une ligne de texte å analyser
	 * @return true si le tableau tabMots contient le stage "st09", sinon false
	 */
	public boolean trouveST09 (String tabMots[]){
		int i;
		i=0;
		while ((i<tabMots.length) && (! (tabMots[i].equals("ST09"))) && (!(tabMots[i].equals("TN09")))) {
			i=i+1;
		}
		return (i<tabMots.length);	
	}

	/**
	 * Indique si le mot clé "ST10" ou "TN10" (stage Fin d'étude) se trouve dans le tableau
	 * @param tabMots tableau de mots composant une ligne de texte å analyser
	 * @return true si le tableau tabMots contient le stage "st10", sinon false
	 */
	public boolean trouveST10 (String tabMots[]){
		int i;
		i=0;
		while ((i<tabMots.length) && (!tabMots[i].equals("ST10")) && (!tabMots[i].equals("TN10"))) {
			i=i+1;
		}
		return (i<tabMots.length);	
	}

	/**
	 * Indique si le mot clé "ST30" ou "TN30" (stage Master) se trouve dans le tableau
	 * @param tabMots tableau de mots composant une ligne de texte à analyser
	 * @return true si le tableau tabMots contient le stage "st10", sinon false
	 */
	public boolean trouveST30 (String tabMots[]){
		int i;
		i=0;
		while ((i<tabMots.length) && (!tabMots[i].equals("ST30")) && (!tabMots[i].equals("TN30"))) {
			i=i+1;
		}
		return (i<tabMots.length);	
	}

	/**TODO A commenter mieux Methode qui recherche si l'etudiant est en stage ST09, ST10, ST30*/
	public boolean enStage(String[] listeMots){
		if(trouveST09(listeMots) || isSt09())
			return true;
		if(trouveST10(listeMots) || isSt10())
			return true;
		if(trouveST30(listeMots) || isSt30())
			return true;
		return false;
	}

	/*TODO a commeneter en gros la methode recherche dans quelle zone on est*/
	public void rechercheZone(String[] listeMots){
		if (enZoneInconnue(listeMots)){
			setNomZone("inconnue");
		}
		if (enZoneMaster(listeMots)){
			setNomZone("Master");
			setEstPasseParMaster(true);
		}
		if (enZoneISI(listeMots)){
			setNomZone("ISI");
			setEstPasseParISI(true);
		}
		if (enZoneTC(listeMots)){
			setNomZone("TC");
			setEstPasseParTC(true);
		}
	}

	/**
	 * Indique si le tableau de mots contient "ISI"
	 * Cela permet de repérer que l'on est dans la partie "ISI" du PV d'un étudiant
	 * @param tabMots tableau de mots composant une ligne de texte à analyser
	 * @return true si le tableau tabMots contient le mot clé "ISI", sinon false
	 */
	public boolean enZoneISI (String tabMots[]){
		int i;
		i=0;
		while ((i<tabMots.length) && (!tabMots[i].equals("ISI"))) {
			i=i+1;
		}

		return (i<tabMots.length)&&(i<4);	
	}

	/**
	 * Indique si le tableau de mots contient "TC"
	 * Cela permet de repérer que l'on est dans la partie "Tronc commun" du PV d'un étudiant
	 * @param tabMots tableau de mots composant une ligne de texte à analyser
	 * @return true si le tableau tabMots contient le mot clé "TC", sinon false
	 */
	public boolean enZoneTC (String tabMots[]){
		int i;
		i=0;
		while ((i<tabMots.length) && (!tabMots[i].equals("TC"))) {
			i=i+1;
		}
		return (i<tabMots.length)&&(i<4);	
	}

	/**
	 * Indique si le tableau de mots contient "Master"
	 * Cela permet de repérer que l'on est dans la partie "Master" du PV d'un étudiant
	 * @param tabMots tableau de mots composant une ligne de texte à analyser
	 * @return true si le tableau tabMots contient le mot clé "Master", sinon false
	 */
	public boolean enZoneMaster (String tabMots[]){
		int i;
		i=0;
		while ((i<tabMots.length) && (!tabMots[i].equals("Master"))) {
			i=i+1;
		}
		return (i<tabMots.length)&&(i<4);	
	}

	/**
	 * Indique si le tableau de mots contient "établissement"
	 * Cela permet de repérer que l'on démarre l'analyse d'un autre étudiant
	 * @param tabMots tableau de mots composant une ligne de texte à analyser
	 * @return true si le tableau tabMots contient le mot clé "établissement", sinon false
	 */
	public boolean enZoneInconnue (String tabMots[]){
		int i;
		i=0;
		while ((i<tabMots.length) && (!tabMots[i].equals("établissement"))) {
			i=i+1;
		}
		return (i<tabMots.length);	
	}
	/**
	 * indique si str est une chaine de charactères numériques.
	 * @param str
	 * @return true si str est un nombre, false sinon
	 */
	public static boolean isNumeric(String str)  
	{  
	  try  
	  {  
	    double d = Double.parseDouble(str);  
	  }  
	  catch(NumberFormatException nfe)  
	  {  
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

	public String getRechercheStage() {
		return rechercheStage;
	}

	public void setRechercheStage(String rechercheStage) {
		this.rechercheStage = rechercheStage;
	}

	public String getNomPrenom() {
		return nomPrenom;
	}

	public void setNomPrenom(String nomPrenom) {
		this.nomPrenom = nomPrenom;
	}

	public String getNomZone() {
		return nomZone;
	}

	public void setNomZone(String nomZone) {
		this.nomZone = nomZone;
	}

	public boolean isSt09() {
		return st09;
	}

	public void setSt09(boolean st09) {
		this.st09 = st09;
	}

	public boolean isSt10() {
		return st10;
	}

	public void setSt10(boolean st10) {
		this.st10 = st10;
	}

	public boolean isSt30() {
		return st30;
	}

	public void setSt30(boolean st30) {
		this.st30 = st30;
	}

	public boolean isEstPasseParTC() {
		return estPasseParTC;
	}

	public void setEstPasseParTC(boolean estPasseParTC) {
		this.estPasseParTC = estPasseParTC;
	}

	public boolean isEstPasseParISI() {
		return estPasseParISI;
	}

	public void setEstPasseParISI(boolean estPasseParISI) {
		this.estPasseParISI = estPasseParISI;
	}

	public boolean isEstPasseParMaster() {
		return estPasseParMaster;
	}

	public void setEstPasseParMaster(boolean estPasseParMaster) {
		this.estPasseParMaster = estPasseParMaster;
	}

	public boolean isUniversiteChinoise() {
		return universiteChinoise;
	}

	public void setUniversiteChinoise(boolean universiteChinoise) {
		this.universiteChinoise = universiteChinoise;
	}


	public String getSt09_st10_st30() {
		return st09_st10_st30;
	}

	public void setSt09_st10_st30(String st09_st10_st30) {
		this.st09_st10_st30 = st09_st10_st30;
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
