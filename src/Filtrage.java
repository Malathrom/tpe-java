import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Filtrage est une classe Java qui permet d'écrire dans un fichier CSV des propositions de décisions de Jury du département ISI.
 * Elle se base sur un PV de jury écrit au format texte TXT
 * @author nigro
 * @version 1.0
 */
public class Filtrage {
	
	/**
	 * sortieExcelCSV contient le nom du fichier CVS contenant les propositions de décisions 
	 */
	String sortieExcelCSV;
	/**
	 * rechercheStage contient les valeurs "oui" ou "non" indiquant si l'étudiant peut rechercher un stage au prochain semestre
	 */
	String rechercheStage;
	/**
	 * nomPrenom contient une chaîne de caractères contenant le nom et prénom de l'étudiant ou "" si le nom et prénom de l'étudiant ne sont pas encore trouvés
	 */
	String nomPrenom;
	/**
	 * nomZone contient une chaine de caractères indiquant la zone du PV de jury où le filtrage est en train de travailler
	 * "inconnue" : signifie qu'on se trouve dans la zone de description de l'étudiant
	 * "TC" : signifie qu'on se trouve dans la zone formation Tronc Commun de l'étudiant
	 * "ISI" : signifie qu'on se trouve dans la zone formation ISI de l'étudiant
	 * "Master" : signifie qu'on se trouve dans la zone formation Master de l'étudiant
	 */
	String nomZone;
	/**
	 * st09 est vrai lorsque l'étudiant a validé son ST09
	 */
	boolean st09;
	/**
	 * st10 est vrai lorsque l'étudiant a validé son ST10
	 */
	boolean st10;
	/**
	 * st30 est vrai lorsque l'étudiant a validé son ST30
	 */
	boolean st30;
	/**
	 * estPasseParTC est vrai lorsque l'étudiant a fait un TC
	 */
	boolean estPasseParTC;
	/**
	 * estPasseParISI est vrai lorsque l'étudiant a fait le cursus ISI
	 */
	boolean estPasseParISI;
	/**
	 * estPasseParMaster est vrai lorsque l'étudiant a fait le cursus Master
	 */
	boolean estPasseParMaster;
	/**
	 * universiteChinoise est vrai lorsque l'étudiant est un étudiant Chinois (gestion particulière)
	 */
	boolean universiteChinoise;
	/**
	 * st09_st10_st30 contient "st09", "st10" ou "st30" si le prochain stage à valider est respectivement un stage "st09", "st10" ou "st30"
	 */
	String st09_st10_st30;
	/**
	 * totalCSTM compte le nombre de CS+TM de TC de branche obtenus par l'étudiant au cours du TC ou du TC de branche ISI.  
	 */
	int totalCSTM;

	/**
	 * Constructeur : Lance la lecture du fichier texte 
	 * @param nomFichierTexte Chaine de caractères représentant le fichier texte à analyser
	 * @param nomFichierCSV Chaine de caractères représentant le fichier CSV de sortie (résultat)
	 * @param niveauIsi entier représentant le niveau de l'étudiant dans le formation ISI
	 */
	public Filtrage (String nomFichierTexte, String nomFichierCSV, int niveauIsi){
		try {
			lireFichier(nomFichierTexte, nomFichierCSV, niveauIsi);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


    /**
    * Renvoie une chaine de caractères avec le nom et prénom, s'il ne le trouve pas renvoie la chaine vide ""
    * @param tabMots tableau de mots composant une ligne de texte à analyser
    * @return une chaine de caractères contenant le nom suivi des prénoms de l'étudiant. Sinon renvoie ""
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
		while (i<tabMots.length){
			if ((tabMots[i].equals("NF16")) || (tabMots[i].equals("EG23")) ||
				(tabMots[i].equals("GL02")) || (tabMots[i].equals("NF20")) ||
				(tabMots[i].equals("IF07")) || (tabMots[i].equals("IF09")) ||
				(tabMots[i].equals("IF14")) || (tabMots[i].equals("LO02")) ||
				(tabMots[i].equals("IF02")) || (tabMots[i].equals("LO12")) ||
				(tabMots[i].equals("RE04")) || (tabMots[i].equals("IF03")) ||
				(tabMots[i].equals("LO07")) || (tabMots[i].equals("NF19"))
			   ) {
				if ((i+5<tabMots.length) && (tabMots[i+5].equals("6")))
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
	 * @param tabMots tableau de mots composant une ligne de texte à analyser
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
	 * @param tabMots tableau de mots composant une ligne de texte à analyser
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
	 * Initialisation des attributs de la classe.
	 */
	public void initialiseRecherche(){
		 totalCSTM=0;
	     st09=false;
	     st10=false;
	     st30=false;
	     universiteChinoise=false;
	     estPasseParTC     =false;
	     estPasseParISI    =false;
	     estPasseParMaster =false;
	     st09_st10_st30="";		     
	     sortieExcelCSV="";
	     nomPrenom="";
	     nomZone="inconnue";
	}
	
	/**
	 * Codage du raisonnement du jury
	 * Le résultat est stocké dans la chaine de caractères sortieExcelCSV au format CSV
	 */
	public void decisionsJury(){
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
   	    sortieExcelCSV=sortieExcelCSV+totalCSTM+";"+st09_st10_st30+";"+rechercheStage+nomPrenom+"\n";
	}
	

	/**
	 * Prend en compte les nouvelles règles A17 pour les stages : pour les étudiant ISI 1, on compte les CS+TM, ils peuvent chercher un stage que si CS+TM (Hors equivalence) est supérieur strictement à 2
	 * La méthode lit le fichier TXT ligne par ligne pour extraire les informations relatives aux étudiants et utiles pour le jury
	 * @param nomFichierTexte Chaine de caractères représentant le fichier texte à analyser
	 * @param nomFichierCSV Chaine de caractères représentant le fichier CSV de sortie (résultat)
	 * @param niveauIsi  Entier représentant le niveau de l'étudiant dans le formation ISI
	 * @throws IOException Erreur d'ouverture de fichier
	 */
	public void lireFichier (String nomFichierTexte, String nomFichierCSV, int niveauIsi) throws IOException{
		BufferedReader lecteurAvecBuffer = null;
		BufferedWriter ecritureAvecBuffer= null;
		String ligne;
		String listeMots[];
		
	    try {lecteurAvecBuffer = new BufferedReader(new FileReader(nomFichierTexte));
		     ecritureAvecBuffer = new BufferedWriter(new FileWriter(nomFichierCSV));
		     ecritureAvecBuffer.write("Contrainte ISI 1 : étudiant ne cherchant pas de stage au prochain semestre = (CS+TM<=12)\n");// la légende
		     ecritureAvecBuffer.write("CS+TM<=12;Niveau stage;Recherche stage;Nom;Prénom1;Prénom2;prénom3\n");// la légende
		     
             // Initialisation
		     initialiseRecherche();

    	     // Parcours toutes les lignes du fichier texte
		     while ((ligne = lecteurAvecBuffer.readLine()) != null){
		     listeMots=ligne.split(" ");
		     
		     if (enZoneInconnue(listeMots)) {nomZone="inconnue";}
		     if (enZoneMaster(listeMots))   {nomZone="Master";estPasseParMaster=true;}
		     if (enZoneISI(listeMots))      {nomZone="ISI";estPasseParISI=true;}
		     if (enZoneTC(listeMots))       {nomZone="TC";estPasseParTC=true;}
		     
		     totalCSTM=totalCSTM+compteCSTM(listeMots);
		     
		     st10=trouveST10(listeMots)||st10;
		     st09=trouveST09(listeMots)||st09;
		     st30=trouveST30(listeMots)||st30;
		     universiteChinoise=trouveUniversiteChinoise(listeMots)||universiteChinoise;

		     // Mémorisation du nom et prénom de l'étudiant
		     if (nomPrenom.equals("")) {
		    	 nomPrenom=trouveNomPrenom(listeMots);
		     }
     
		     // On a traité toutes les lignes du PV d'un étudiant. On déclenche la décision
		     if (!nomPrenom.equals("") && nomZone.equals("inconnue")){
		    	 System.out.println(nomPrenom + "=" + Integer.toString(totalCSTM));
		    	 
		        decisionsJury();
 	           	ecritureAvecBuffer.write(sortieExcelCSV);

		    	// Ré-initialisation des variables pour le traitement
		    	initialiseRecherche();
		     }
		            	
		    }
		    // Traitement du dernier étudiant du fichier
			   decisionsJury();
		       ecritureAvecBuffer.write(sortieExcelCSV); 
	    }
	    catch(FileNotFoundException exc) {
		    System.out.println("Erreur d'ouverture");
	    }
	    finally {
		    lecteurAvecBuffer.close();
	    	ecritureAvecBuffer.close();
	    }
	  }
}
