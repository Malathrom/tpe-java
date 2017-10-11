package io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import operation.CalculNote;

public class Conversion {
	
	//TODO tester la classe pour voir si le fichier final est ok
	//TODO Mettre les différents répertoires dans une pile qui recupera l’ensemble des chemins qui sauvegardés dans un fichier texte
	//TODO finir la méthode CompteCSTM avec l’arrayList modules
	
	/**TODO a commenter mise en place du filtre de la decision de jury*/
	private Filtrage filtre = new Filtrage();

	/**
	 * decisionCSV contient le nom du fichier CVS contenant les propositions de décisions 
	 */
	private String decisionCSV = "";

	/**
	 * Constructeur : Lance la lecture du fichier texte 
	 * @param nomFichierTexte Chaine de caractéres représentant le fichier texte à analyser
	 * @param nomFichierCSV Chaine de caractéres représentant le fichier CSV de sortie (résultat)
	 * @param niveauIsi entier représentant le niveau de l'étudiant dans la formation ISI
	 */
	public Conversion (String nomFichierTexte, String nomFichierCSV, int niveauIsi){
		try {
			lireFichier(nomFichierTexte, nomFichierCSV, niveauIsi);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Prend en compte les nouvelles régles A17 pour les stages : pour les étudiant ISI 1, on compte les CS+TM, 
	 * ils peuvent chercher un stage que si CS+TM (Hors equivalence) est supérieur strictement à 2
	 * La méthode lit le fichier TXT ligne par ligne pour extraire les informations relatives aux étudiants et utiles pour le jury
	 * @param nomFichierTexte Chaine de caractéres représentant le fichier texte à analyser
	 * @param nomFichierCSV Chaine de caractéres représentant le fichier CSV de sortie (résultat)
	 * @param niveauIsi  Entier représentant le niveau de l'étudiant dans le formation ISI
	 * @throws IOException Erreur d'ouverture de fichier
	 */
	public void lireFichier (String nomFichierTexte, String nomFichierCSV, int niveauIsi) throws IOException{
		BufferedReader lecteurAvecBuffer = null;
		BufferedWriter ecritureAvecBuffer= null;
		String ligne;
		String listeMots[];

		try {
			lecteurAvecBuffer = new BufferedReader(new FileReader(nomFichierTexte));
			ecritureAvecBuffer = new BufferedWriter(new FileWriter(nomFichierCSV));
			ecritureAvecBuffer.write("Contrainte ISI 1 : étudiant ne cherchant pas de stage au prochain semestre = (CS+TM<=12)\n");// la légende
			ecritureAvecBuffer.write("CS+TM<=12;Niveau stage;Recherche stage;Nom;Prénom1;Prénom2;prénom3\n");// la légende
			
			filtre.initialiseRecherche();// Initialisation

			// Parcours toutes les lignes du fichier texte
			while ((ligne = lecteurAvecBuffer.readLine()) != null){
				listeMots=ligne.split(" ");
				filtre.rechercheZone(listeMots);//recherche la zone de l'etudiant
				filtre.enStage(listeMots);// recherche si l'etudnaint est en stage
				filtre.setUniversiteChinoise(filtre.trouveUniversiteChinoise(listeMots)||filtre.isUniversiteChinoise()); //recherche si l'etudiant est chinois
				filtre.setTotalCSTM(filtre.getTotalCSTM() + filtre.compteCSTM(listeMots));//Compte les credits de l'etudiants
				filtre.setNbA(filtre.trouveNbA(listeMots)+filtre.getNbA());

				// Mémorisation du nom et prénom de l'étudiant
				if (filtre.getNomPrenom().equals("")) {
					filtre.setNomPrenom(filtre.trouveNomPrenom(listeMots));
				}

				// On a traité toutes les lignes du PV d'un étudiant. On déclenche la décision
				if (!filtre.getNomPrenom().equals("") && filtre.getNomZone().equals("inconnue")){
					setDecisionCSV(getDecisionCSV()+filtre.decisionJury()); //On stocke la decision final
					// Ré-initialisation des variables pour le traitement de l'etudiant suivant
					filtre.initialiseRecherche();
				}
			}
			// Traitement du dernier étudiant du fichier
			System.out.println("lectur2 "+filtre.getNomPrenom() + "=" + Integer.toString(filtre.getTotalCSTM()));//TODO a enlever
			setDecisionCSV(getDecisionCSV()+filtre.decisionJury()); //On stocke la decision final
			ecritureAvecBuffer.write(getDecisionCSV()); //on ecrit la decicion final de l'etudiant
			
			///////////////////////////////////////////////////////////METHODE OU On lance les calculs/////////////////////////////////////////////
			CalculNote.calculsemestre(decisionCSV);
			ecritureAvecBuffer.write(decisionCSV); 
		}
		catch(FileNotFoundException exc) {
			System.out.println("Erreur d'ouverture");
		}
		finally {
			lecteurAvecBuffer.close();
			ecritureAvecBuffer.close();
		}
	}
	
	public Filtrage getFiltre() {
		return filtre;
	}

	public void setFiltre(Filtrage filtre) {
		this.filtre = filtre;
	}

	public String getDecisionCSV() {
		return decisionCSV;
	}

	public void setDecisionCSV(String sortieExcelCSV) {
		this.decisionCSV = sortieExcelCSV;
	}

}
