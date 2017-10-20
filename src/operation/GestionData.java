package operation;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import io.Filtrage;
import io.LectureModules;
import operation.data.Etudiant;
import operation.data.Module;
/**GestionData gere les donnees des fichiers des etudiants*/
public class GestionData {

	//TODO si on a le temps on modifie le type de DataETudian en ArrayLIST

	/**
	 * LIGNE représente le nombre lignes necessaires dans une matrice pour stocker les données d'un seul etudiant
	 */
	private final static int LIGNE = 100;

	/**
	 * COLONNE représente le nombre colonne necessaires dans une matrice pour stocker les donnees d'un seul etudiant
	 */
	private final static int COLONNE = 50;

	/**
	 * sepSemestre est une regex qui permet de savoir quand dans le fichier on est dans un nouveau semestres
	 */
	private final String sepSemestre = "(A|P)[0-9]{2}";

	/**
	 * file represente le fichier qui va etre traité
	 */
	private File file = null;

	/**
	 * modules contient la liste des modules existant dans le fichier modules pour filtrer les modules
	 */
	private static List<Module> modules = new ArrayList<Module>();

	/**
	 * etudiants est la liste des objets de type Etudiant que nous sommes en train de traiter dans le fichier
	 */
	private List<Etudiant> etudiants = new ArrayList<Etudiant>();

	/**
	 * nomPrenom contient une chaîne de caractères contenant le nom et prénom de l'étudiant ou "" si le nom et prénom de l'étudiant ne sont pas encore trouvés
	 */
	private String nomPrenom;

	/**
	 * dataEtudiant va contenir les donnees de l'etudiant a traiter
	 */
	List<String> dataEtudiant = new ArrayList<String>();

	//TODO a commenter
	private int nbEtudiant = 0;

	/**
	 * totalCSTM compte le nombre de CS+TM de TC de branche obtenus par l'étudiant au cours du TC ou du TC de branche ISI.  
	 */
	private int totalCSTM;
	/**
	 * nbA désigne le nombre de A obtenus par un élève.
	 */
	private int nbA;

	/** 
	 * Constructeur : Lance la lecture des modules existant en ISI qui se trouve dans le fichier /files/module.txt
	 * @param file chemin du fichier qui traité
	 */
	public GestionData(String file){
		this.file = new File(file);
		modules = LectureModules.lireModules();
	}
	/** 
	 * Constructeur : Lance la lecture des modules existant en ISI qui se trouve dans le fichier /files/module.txt
	 * @param file le fichier qui va etre traité
	 */
	public GestionData(File file){
		this.file = file;
		modules = LectureModules.lireModules();
	}

	/**
	 * Reset des données pour un nouvel etudiant
	 */
	public void reset(){
		totalCSTM=0;
		nomPrenom="";
		nbA=0;
		dataEtudiant = new ArrayList<String>();//on remet les donnes de l'etudiant a 0
	}

	/**
	 * lireFichier traite le fichier txt des etudiants et permet d;instncier les etudaints et les modules pour pouvoir les manipuler en java
	 */
	public void lireFichier(){
		BufferedReader lecteurAvecBuffer;
		List<String> datas = new ArrayList<String>();//TOUTES LES DONNEES DU FICHIER
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
		creationListeEtudiants(datas);
	}

	//TODO a commenter on va traite etudiant par etudiant
	private void creationListeEtudiants(List<String> datas){
		datas = suppressionElements(datas); //On supprime les elements inutile

		Iterator<String> it = datas.iterator();
		while (it.hasNext()) {//on parcourt les donnees
			String data = it.next();
			if(startDataEtudiant(data)){	//si on change d'etudiant on reste les donnees concernant l'ancien etudiant
				reset();
				nbEtudiant++;
			}
			else{//si on a pas changer d'etudiant on stocke ces donnees
				if(endDataEtudiant(data)){//si on est a la fin des donnees on creer l'etudiant
					creationEtudiant();
					reset();
					//etudiants.add(etudiant);//TODO on ajoute l'ancien etudiant dans la liste
				}
				else{
					dataEtudiant.add(data);//on est pas a la fin on attemd
				}
			}
			//TODO a voir si il faut appellet creationEtuidant pour le dernier etudiant de la liste
			//System.out.println(dataEtudiant.size()+1);
		}
		//System.out.println(nbEtudiant);//TODO a enlever
	}

	/**
	 * creationEtudiant permet de créer l'etudiant en objet Java et de l'ajouter à la liste des etudiants deja instanciées 
	 */
	private void creationEtudiant(){

		//on recupere le nom et prenom de l'etudiant traité
		String nom = recupereNom();
		String prenom = recuperePrenom();
		//System.out.println("nom "+ nom);//TODO a enlever
		//System.out.println("prenom "+ prenom);//TODO a enlever

		List<Module> modulesEtu = recupereModules();//on recupere les Ue d'un etudiant

		//TODO recuperer les donnees sur l'etudiant et l'instancier et le mettre dans la liste etudiant;
		//TODO envoyer ces donnees a une classe GestionNotes
		//Etudiant etudiant = new Etudiant(nom, prenom, modules);

	}

	/**
	 * recupereNom recupere le nom de l'etudiant dans la matrice data
	 * @return retourne le nom de l'etudiant
	 */
	private String recupereNom(){
		return dataEtudiant.get(8);
	}

	/**
	 * recuperePrenom recupere le prenom de l'etudiant dans la matrice data
	 * @return retourne le prenom de l'etudiant
	 */
	private String recuperePrenom(){
		return dataEtudiant.get(7);
	}

	/**
	 * recuperUEs recupere la liste des UEs de l'etudiant dans la matrice data
	 * @return retourne la liste des UEs de l'etudiant
	 */
	private List<Module> recupereModules(){
		List<Module> modules = new ArrayList<Module>();//liste de tous les modules de l'etudiant
		List<String> dataSemestreEtudiant = new ArrayList<String>(); //donnees d'un semestre de l'etudiant
		int nbSemestre = 0; // indique le nombre de semestres qu'a fait l'etudiant
		int semestre = 0; //indique le semestre dans lequel l'etudiant a fait un module

		Iterator<String> it = dataEtudiant.iterator();
		while (it.hasNext()) {//on parcourt les donnees
			String data = it.next();
			
			if(startDataSemestre(data)){	//si on change de semestre on creer les modules de l'ancien semestre
				reset();
				dataSemestreEtudiant = new ArrayList<String>();//on reset les donnees pour le nouveau semestres
				dataSemestreEtudiant.add(data);//on ajoute le type de semestre (P15 ou A17)
				semestre++;
				nbSemestre++;
			}
			else{//si on a pas changer d'etudiant on stocke ces donnees
				if(endDataSemestre(data)){//si on est a la fin d'un smeestre on creer les modules de ce semestres
					creationModules(dataSemestreEtudiant, semestre);//Creation des modules du semestre en cours de traitement
					//modules.add();//TODO on ajoute les modules a la liste total des modules
				}
				else{
					dataSemestreEtudiant.add(data);//on est pas a la fin du semestre on ajoute le reste des donnees dans ce semestres
				}
			}
		}
		return null;
	}

	//TODO a commenter Creation des Uv de l'etudiant pour un semestre
	private List<Module> creationModules(List<String> modulesData, int semestre) {
		List<Module> modulesSemestre = new ArrayList<Module>();
		Iterator<String> it = modulesData.iterator();
		
		Module mod1 = new Module(modulesData.get(3), Note.getNote(modulesData.get(4)), Integer.valueOf(modulesData.get(5)), semestre);
		Module mod2 = new Module(modulesData.get(6), Note.getNote(modulesData.get(7)), Integer.valueOf(modulesData.get(8)), semestre);
		Module mod3 = new Module(modulesData.get(9), Note.getNote(modulesData.get(10)), Integer.valueOf(modulesData.get(11)),  semestre);
		Module mod4 = new Module(modulesData.get(12), Note.getNote(modulesData.get(13)), Integer.valueOf(modulesData.get(14)), semestre);
		Module mod5 = new Module(modulesData.get(15), Note.getNote(modulesData.get(16)),  Integer.valueOf(modulesData.get(17)), semestre);
		System.out.println("MOD1 :"+mod1);
		System.out.println("MOD2 :"+mod2);
		System.out.println("MOD3 :"+mod3);
		System.out.println("MOD4 :"+mod4);
		System.out.println("MOD5 :"+mod5);
		
		
		//On fait un pattern.MAtch pour chaque donne qu\on parcourt des que ca colle avec un UV on parcourt suite 
		while (it.hasNext()) {//on parcourt la liste qui contient les semestres
			String data = (String) it.next();
			//System.out.println("Semestre" + semestre + " " +modulesData.indexOf(data)+" "+data);
		}
		//Utiliser la fonctions matches pour retourevr A15 ou P22
		//TODO faire des sous ArrayList pour chque semestres ils sont separes par Total semestre
		//TODO creer un matrice UE en testant si la ligne est P+un nombre ou A+plus un nombre on met donc la suite dans la matrice
		//TODO recuperer les donnees sur les UVs, note categorie etc
		//ArrayList<Module> module = ueEtudiant()
		return null;
	}


	//TODO a commenter //on stocke les donnees des semestres uniquement
	/*private List<String> recuperesDonneesSemestres(List<String> dataEtudiant) {
		List<String> semestresUE = new ArrayList<String>();
		boolean enSemestre = false;//permet desavoir si on parcourt les semestres

		//on parcourt les donnees
		Iterator<String> it = dataEtudiant.iterator();
		int debut = dataEtudiant.indexOf("Observations");//indique la position du debut des semestres
		int fin = dataEtudiant.indexOf("TOTAUX");//indique la position de la fin des semestres
		System.out.println(debut + "  " + fin);//TODO a enlever
		int index = ++debut;
		while (index < fin) {
			semestresUE.add(dataEtudiant.get(index));
			index++;
		}

		//verification
		System.out.println("verification" + semestresUE.get(0));
		System.out.println("verification2" + semestresUE.get(semestresUE.size()-1));

		//Suppression des elements vide de l'ArrayList
		semestresUE = suppressionElements(semestresUE);
		return semestresUE;
	}*/

	/**
	 * recupereTypeSemestre recupere le type de semestre de l'etudiant dans la la liste des semestres
	 * @return retourne le type de semestre de l'etudiant
	 */
	private String recupereTypeSemestre(List<String> liste){
		return liste.get(1);
	}

	/**
	 * recupereSemestre recupere le numero de semestre de l'etudiant dans la matrice data
	 * @return retourne le numero de semestre de l'etudiant
	 */
	private int recupereSemestre(List<String> liste){
		return Integer.valueOf(liste.get(2));
	}

	//TODO a commenter //Suppression des elements inutiles (vide, ■) se trouvant dans la liste
	public List<String> suppressionElements(List<String> liste){
		String vide = "";
		String carre = "■";
		String triangle = "▲";
		while (liste.contains(vide)) {//on supprime les chaines vide
			liste.remove(vide);
		}
		while (liste.contains(carre)) {//on supprime les carre
			liste.remove(carre);
		}
		while (liste.contains(triangle)) {//on supprime les carre
			liste.remove(triangle);
		}
		return liste;
	}	

	/*TODO a commenter methode pour s'avoir quand commence les donnees d'un etudiant*/
	private boolean startDataEtudiant(String contenu) {
		if ("PV".equals(contenu))//si c'est un nouveau etudiant
			return true;
		return false;
	}

	/*TODO a commenter methode pour s'avoir quand se termine les donnees d'un etudiant*/
	private boolean endDataEtudiant(String contenu) {
		if ("Global".equals(contenu))
			return true;
		return false;
	}

	/*TODO a commenter methode pour s'avoir quand commence les donnees d'un semestre*/
	private boolean startDataSemestre(String contenu) {
		if (Pattern.matches(sepSemestre, contenu))
			return true;
		return false;
	}

	/*TODO a commenter methode pour s'avoir quand se termine les donnees d'un semestre*/
	private boolean endDataSemestre(String contenu) {
		if ("Total".equals(contenu))
			return true;
		return false;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Teste si un string est contenu dans une enum
	 * @param value //TODO a definir value
	 * @param enumClass //TODO a definir enumClass
	 * @return
	 */
	public <E extends Enum<E>> boolean isInEnum(String value, Class<E> enumClass) {
		for (E e : enumClass.getEnumConstants()) {
			if(e.name().equals(value)) { return true; }
		}
		return false;
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * renvois une liste constitué des ue d'un étudiant
	 * @param tabMots //TODO a definir tabmots
	 * @return //TODO dire ce que ca retourne
	 */
	public void ueEtudiant(String tabMots[], Etudiant etu){
		int i;
		i=0;
		int semestre=1;
		while (i<tabMots.length){
			if (isInEnum(tabMots[i], Note.class)&& Filtrage.isNumeric(tabMots[i+3])){
				Module mod = new Module(tabMots[i-2], Note.getNote(tabMots[i]), Integer.valueOf(tabMots[i+3]), semestre);
				etu.getModules().add(mod);
			}
			i++;
		}	
		System.out.println(etu.getModules());
	}
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * retourne le nombre de note obtenues pour un étudiant et semestre donné.
	 * @param etu
	 * @param la note que l'on veut compter
	 * @param le semestre
	 * @return nbNote
	 */
	public int compteNote(Etudiant etu, Note note, int semestre){
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
	public int maxSemestre(Etudiant etu){
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
	public boolean estRatee(Module mod){
		boolean out=false;
		if (mod.getCredit()==0){
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
	public int nombreUeSemestre(Etudiant etu, int sem){
		int nbUe=0, i=0;
		while(i<etu.getModules().size()){
			if(etu.getModules().get(i).getSemestre()==sem){
				nbUe++;
				System.out.println(etu.getModules().get(i));
			}
			i++;
		}
		return nbUe;
	}
	
	/**
	 * Retourne l'avis du jury pour un élève en particulier sous forme de string
	 * @param l'étudiant choisis
	 * @return l'avis de chaque semestre dans un tableau, chaque case représentant un semestre
	 */
	public ArrayList<String> avisJury(Etudiant etu){
		ArrayList<String> out= new ArrayList<String>();
		int maxSem=maxSemestre(etu);
		System.out.println("max=" +maxSem);
		int sem=1;
		while(sem<maxSem){
			String str="";
			int nbA=compteNote(etu, Note.A, sem);
			int nbB=compteNote(etu, Note.B, sem);
			int nbD=compteNote(etu, Note.D, sem);
			int nbE=compteNote(etu, Note.E, sem);
			int nbUe=nombreUeSemestre(etu, sem);
			System.out.println(etu.getModules().get(3));
			System.out.println(nbUe);
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
				if ((nbA+nbB)/nbUe>0.7){
					str+=", Excellent Semestre";
				}
				else if ((nbA+nbB)/nbUe>0.6){
					str+=", Très Bon Semestre";
				}
				else if ((nbA+nbB)/nbUe>0.5){
					str+=", Bon Semestre";
				}
				else if ((nbE+nbD)/nbUe<0.5){
					str+=", Assez Bon Semestre";
				}
				else{
					str+=", Semestre Moyen";
				}		
			}
			if (nbUeRateesTotal==1){
				str+="Poursuite avec Conseil";
				if ((nbE+nbD)/nbUe<0.5){
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
		ArrayList<String> a= new ArrayList<String>();
		a.add("bleh");
		return a;
	}
	
	public static List<Module> getModules() {
		return modules;
	}

	public static void setModules(List<Module> modules) {
		GestionData.modules = modules;
	}

	public List<Etudiant> getEtudiants() {
		return etudiants;
	}

	public void setEtudiants(List<Etudiant> etudiants) {
		this.etudiants = etudiants;
	}

	public String getNomPrenom() {
		return nomPrenom;
	}

	public void setNomPrenom(String nomPrenom) {
		this.nomPrenom = nomPrenom;
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

	public static int getLigne() {
		return LIGNE;
	}

	public static int getColonne() {
		return COLONNE;
	}
	
	
}
