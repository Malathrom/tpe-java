package operation;

import java.util.Iterator;
import java.util.List;
import data.Etudiant;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
//TODO a commenter
public abstract class Statistiques {	
	
	//TODO a commenter
		public static void ecritureStatistiques(String nomFichierTexte, String nomFichierStat){
			File fileStat = new File(nomFichierStat);// on recupere le fichier de stat
			FileWriter fw = null;
			try {
				fw = new FileWriter(fileStat);
			} catch (IOException e) {
				e.printStackTrace();
			}

			BufferedWriter bw;
			PrintWriter pw;
			bw = new BufferedWriter(fw);
			pw = new PrintWriter(bw);

			List<Etudiant> etudiants = GestionData.listeEtudiant(new File(nomFichierTexte));
			Iterator<Etudiant> it = etudiants.iterator();//TODO a enelever
			
			//TODO APPELLE LES METHODES POUR LES STATS----------------------------------------------------------------------------------------
			pw.println(etudiants);
			pw.println("test");//TODO a enelever
			pw.close();	//TODO a enelever

		}
	
	
	//TODO a commenter
	public static int totalNote(List<Etudiant> etudiants, Note note){
		int totalNote=0;
		Iterator<Etudiant> it = etudiants.iterator();
		while (it.hasNext()) {
			Etudiant etu = it.next();
			int i=0;
			while(i<etu.getModules().size()){
				if (etu.getModules().get(i).getNote()==note){
					totalNote++;
				}
				i++;
			}
		}
		return totalNote;
	}

	//TODO a commenter
	public static ArrayList<Object> pourcentUe(List<Etudiant> etudiants, String nomUe){
		float pourcentReussite;
		int nbReussie=0, nbRate=0, nbA=0, nbB=0, nbC=0, nbD=0, nbE=0, nbF=0, nbElse=0;
		Iterator<Etudiant> it = etudiants.iterator();
		while (it.hasNext()) {
			Etudiant etu = it.next();
			int i=0;
			while(i<etu.getModules().size()){
				if (etu.getModules().get(i).getNom()==nomUe && DecisionJury.estRatee(etu.getModules().get(i))){
					nbRate++;
				}
				else{
					nbReussie++;
				}
				switch(etu.getModules().get(i).getNote()){
				case A:
					nbA++;
					break;
				case B:
					nbB++;
					break;
				case C:
					nbC++;
					break;
				case D:
					nbD++;
					break;
				case E:
					nbE++;
					break;
				case F:
				case FX:
					nbF++;
					break;
				default:
					nbElse++;
					break;
				}
				i++;
			}
		}
		int nbTotal = nbRate+nbReussie;
		pourcentReussite=(float)nbReussie/nbTotal;
		ArrayList<Object> out = new ArrayList<Object>();
		out.addAll(Arrays.asList(nomUe, nbTotal, nbReussie, nbRate, pourcentReussite, nbA, nbB, nbC, nbD, nbE, nbF, nbElse));
		return out;
	}
}
