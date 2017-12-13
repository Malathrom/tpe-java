package operation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.pdfbox.pdfwriter.ContentStreamWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.PrintWriter;

import data.Etudiant;
import data.Module;
import io.LectureModules;

/**
 * Classe qui gere les decisions de jury des etudiants 
 */
public abstract class DecisionJury{

	/*
	 * fichierTexte represente le fichier qui contient les donnees des etudiants
	 */
	private static String fichierTexte;

	/*
	 * fichierCsv represente le fichier qui contiendra les decisionsJury en csv
	 */
	private static String fichierCsv;

	/*
	 * fichierPDF represente le fichier qui contiendra les decisionsJury en pdf
	 */
	private static String fichierPdf;
	/**
	 * Liste des etudiants qui sont entrain d'etre traités
	 */
	List<Etudiant> etudiants = new ArrayList<Etudiant>();

	/**
	 * Méthode qui ecrit les decisions de jury dans le fichier PDF et CSV a partir des donnes du fichier Texte
	 * @param nomFichierTexte le fichier texte à analyser contenant les donnees des etudiants
	 * @param nomFichierPDF  le fichier PDF de sortie (résultat)
	 * @param nomFichierCSV le fichier CSV de sortie (résultat)
	 */
	public static void ecritureDecisionJury (String nomFichierTexte, String nomFichierPDF, String nomFichierCSV){
		fichierPdf=nomFichierPDF;
		fichierTexte=nomFichierTexte;
		fichierCsv=nomFichierCSV;
		ecritureDecisionJuryCSV();//ecriture des decisions dans les CSV
		ecritureDecisionJuryPDF();//ecriture des decisions dans les PDF
	}

	/**
	 * Methode qui écrit les decisions de jury dans un fichier CSV
	 */
	public static void ecritureDecisionJuryCSV(){
		File file = new File(fichierCsv);
		FileWriter fw = null;
		try {
			fw = new FileWriter(file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		BufferedWriter bw;
		PrintWriter pw;
		bw = new BufferedWriter(fw);
		pw = new PrintWriter(bw);

		List<Etudiant> etudiants = GestionData.listeEtudiant(new File(fichierTexte));
		Iterator<Etudiant> it = etudiants.iterator();

		String entete="";
		entete+="Nom;Prenom;Stage;";
		int semSize= DecisionJury.avisJury(etudiants.get(0)).size(), j=0;
		while (j<semSize){
			entete+="Avis Semestre "+(j+1)+";";
			j++;
		}
		entete+="\n";
		pw.print(entete);

		while (it.hasNext()) {
			Etudiant etudiant = it.next();
			String out="";
			out=out+etudiant.getNom()+";"+etudiant.getPrenom()+";";
			out+=DecisionJury.dernierStage(etudiant)+";";
			int i=0;
			List<String> avisSem= DecisionJury.avisJury(etudiant);
			while(i<avisSem.size()){
				out+= avisSem.get(i)+";";
				i++;
			}
			pw.println(out);
		}
		pw.close();	
	}

	/**
	 * Methode qui ecrit les decisions de jury dans un fichier PDF
	 */
	public static void ecritureDecisionJuryPDF(){
		PDDocument doc;
		try {
			doc = new PDDocument();
			doc.save(fichierPdf);//sauvegarde du pdf dans le dossier qu'on a choisit
			doc.addPage(new PDPage());//on creer la premiere page

			PDFont font = PDType1Font.HELVETICA_BOLD;
			float fontSize = 7.0f;
			int marginLeft = 10;
			int marginBottom = 650;
			PDPageContentStream contentStream = null;

			int nbEtudiantPage = 25;
			int numPage = 0;
			List<Etudiant> etudiants = GestionData.listeEtudiant(new File(fichierTexte));
			Iterator<Etudiant> it = etudiants.iterator();
			List<Etudiant> etudiantsPage = new ArrayList<Etudiant>();
			List<String> decisionTab = new ArrayList<String>();

			//Ajout du titre
			String Titre = "Avis Etudiant ";
			float fontSizeTitle = 22.0f;
			PDPage page = doc.getPage(numPage);
			contentStream = new PDPageContentStream(doc, page);
			contentStream.beginText();// on commence l'ecriture des decision
			contentStream.newLineAtOffset(200, 700);
			contentStream.setFont(font, fontSizeTitle);
			contentStream.showText(Titre);//Ajout du titre
			contentStream.endText();
			
			//on prepare le text de decision
			contentStream.beginText();// on commence l'ecriture des decision
			contentStream.newLineAtOffset(marginLeft, marginBottom);
			contentStream.setFont(font, fontSize);
			contentStream.setLeading(20);
			
			int cptEtudiant = 0, cptTotal = 0;
			while(it.hasNext()){				
				Etudiant etudiant = it.next();
				cptEtudiant++;
				cptTotal++;
				if(cptEtudiant >= nbEtudiantPage){//on on ajoute les etudiants sur la page courante
					contentStream.endText();//on ferme le flux sur l'ancienne page 
					contentStream.close();
					doc.addPage(new PDPage());//on ajoute une nouvelle page
					cptEtudiant=0;//le compteur sur cette page est donc remis a zero
					page = doc.getPage(++numPage);// on change de page
					
					contentStream = new PDPageContentStream(doc, page, AppendMode.APPEND, true, true);//on ajoute du contenu
					contentStream.beginText();// on commence l'ecriture des decision
					contentStream.newLineAtOffset(marginLeft, marginBottom);
					contentStream.setFont(font, fontSize);
					contentStream.setLeading(20);
				}
				System.out.println(etudiant.getNom());
				List<String> avisSem = DecisionJury.avisJury(etudiant);
				String lastAvis = avisSem.get(avisSem.size()-1);//on recupere le dernier avis de l'etudiant
				contentStream.showText(cptTotal+" : "+etudiant.getNom() + " " + etudiant.getPrenom() + " : " + lastAvis);	
				contentStream.newLine();
			}
			contentStream.endText();
			contentStream.close();
			doc.save(fichierPdf);//sauvegarde du pdf dans le dossier qu'on a choisit
			doc.close();//fermeture di document
		}catch (IOException e){
			e.printStackTrace();
		}
	}

	/**
	 * Retourne le nombre de note obtenues pour un étudiant et semestre donné.
	 * @param etu l'etudiant a traiter
	 * @param note la note que l'on veut compter
	 * @param semestre le semestre
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
	 * @param etu l'etudiant a traiter
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
	 * @param mod le module qui est tester
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
	 * @param etu l'etudiant traité
	 * @param sem le semestre visée
	 * @return le nombre d'UE de l'etudiant ce semestre
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
			if (etu.getModules().get(i).getNom().equals("TN09")){
				tn09=true;
			}
			if (etu.getModules().get(i).getNom().equals("TN10")){
				tn10=true;
			}
			if (etu.getModules().get(i).getNom().equals("TN30")){
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
	 * Methode qui compte les credits des modules ISI de l'etudiant
	 * @return le nombre de credit pour les module ISI
	 */
	public int compteCreditsISI (){
		int credits = 0;
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
	 * Retourne si oui ou non il faut afficher un avertissement pour le NPML
	 * @param etu l'etudiant choisis
	 * @param sem le semestre 
	 * @return un booleen 
	 */
	public static boolean avertissementNPML(Etudiant etu, int sem){
		boolean avertissementNPML=false;
		int i=0;
		if (sem==1 || sem==2){
			while(i<etu.getModules().size()){
				if (etu.getModules().get(i).getSemestre()==1 && etu.getModules().get(i).getParcours().equals("ISI") && (etu.getModules().get(i).getNom().equals("LE01"))){
					avertissementNPML=true;
				}/* LE01(validé ou non) en ISI 1  */
				if (etu.getModules().get(i).getSemestre()==2 && etu.getModules().get(i).getParcours().equals("ISI") && (etu.getModules().get(i).getNom().equals("LE01") || etu.getModules().get(i).getNom().equals("LE02"))){
					avertissementNPML=true;
				}/* LE01 ou LE02 (validé ou non) en ISI 2  */
				i++;
			}
		}
		if (sem==3){
			boolean LE03=false;
			i=0;
			while(i<etu.getModules().size()){
				if (etu.getModules().get(i).getSemestre()==3 && etu.getModules().get(i).getParcours().equals("ISI") && etu.getModules().get(i).getNom().equals("LE03")){
					LE03=true;
				}
				if (etu.getModules().get(i).getSemestre()==3 && !etu.getModules().get(i).getParcours().equals("ISI")){
					LE03=true;
				}
				i++;
			}
			if (LE03==false){
				avertissementNPML=true;
			}
		}
		return avertissementNPML;
	}

	/**
	 * Scan les modules d'un étudiant et affiche en réponse un string contenant l'avis jury.
	 * @param etu l'etudiant choisis.
	 * @return l'avis jury en string.
	 */
	public static List<String> avisJury(Etudiant etu){
		ArrayList<String> out= new ArrayList<String>();
		int maxSem=maxSemestre(etu)+1;
		int sem=1;
		boolean buleadm=false;
		while(sem<maxSem){
			String str="";
			int nbA=compteNote(etu, Note.A, sem);
			int nbB=compteNote(etu, Note.B, sem);
			int nbD=compteNote(etu, Note.D, sem);
			int nbE=compteNote(etu, Note.E, sem);
			int nbUe=nombreUeSemestre(etu, sem);
			int nbUeRatees=0, i=0, nbUeRateesCSTM=0;

			boolean avertissementNPML=avertissementNPML(etu, sem);
			boolean LE03valide=false;
			while(i<etu.getModules().size()){
				if (estRatee(etu.getModules().get(i)) && etu.getModules().get(i).getSemestre()==sem && !(etu.getModules().get(i).getCategorie().equals("CS") || etu.getModules().get(i).getCategorie().equals("TM"))){
					nbUeRatees++;
				}
				if (estRatee(etu.getModules().get(i)) && etu.getModules().get(i).getSemestre()==sem && (etu.getModules().get(i).getCategorie().equals("CS") || etu.getModules().get(i).getCategorie().equals("TM"))){
					nbUeRateesCSTM++;
				}
				if (!estRatee(etu.getModules().get(i)) && etu.getModules().get(i).getSemestre()==sem && etu.getModules().get(i).getNom().equals("LE08")){
					buleadm=true;
				}
				if(!estRatee(etu.getModules().get(i)) && etu.getModules().get(i).getSemestre()==(sem-1) && etu.getModules().get(i).getNom().equals("LE03")){
					LE03valide=true;
				}/* LE03 a été validé au semestre précédent*/
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
			if (buleadm==false){
				if(avertissementNPML){
					str+=", Vos résultats en langues sont insuffisants pour obtenir le NPML en temps voulu, réagissez.";
				}else if (LE03valide){
					str+=", Attention, vous n'avez toujours pas validé votre NPML, indispensable pour être diplômé(e).";
				}
			}

			sem++;
			out.add(str);
		}
		return out;
	}
}

