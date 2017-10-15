package operation.data;

import operation.Note;

/**La classe module représente un module de l'UTT
 * @auteur Noga Lucas
 * @version 2017
 */

/*TODO se servir de la javadoc de cette classe*/
public class Module{
	
	/**Nom du module*/
	private String nom;

	/**CS ou TM*/
	private String categorie;

	/**ISI RT GI etc*/
	private String parcours;

	/**Semestre a laquel l'UV est fait par l'etudiant*/
	private int semestre;

	/**Note que l'etudiant a eu a cette UV*/
	private Note note;

	/**Credit de l'UV*/
	private int credit;

	public Module(String nom, String categorie, String parcours, int credit, int semestre, Note note) {
		this.nom = nom;
		this.categorie = categorie;
		this.semestre = semestre; 
		this.note = note;
		this.parcours = parcours;
		this.credit = credit;
	}
	
	public Module(String nom, int credit, int semestre, Note note){
		this.nom = nom;
		this.semestre = semestre; 
		this.note = note;
		this.credit = credit;
	}
	
	public String getNom() {
		return nom;
	}
	
	
	public void setNom(String nom) {
		this.nom = nom;
	}


	public String getCategorie() {
		return categorie;
	}


	public void setCategorie(String categorie) {
		this.categorie = categorie;
	}


	public String getParcours() {
		return parcours;
	}


	public void setParcours(String parcours) {
		this.parcours = parcours;
	}


	public int getSemestre() {
		return semestre;
	}


	public void setSemestre(int semestre) {
		this.semestre = semestre;
	}


	public Note getNote() {
		return note;
	}


	public void setNote(Note note) {
		this.note = note;
	}


	public int getCredit() {
		return credit;
	}


	public void setCredit(int credit) {
		this.credit = credit;
	}

	@Override
	public String toString() {
		return "Module{" + "nom=" + nom + ", categorie=" + categorie + ", parcours=" + parcours + ",  semestre=" + semestre + ", Note=" + note + ", credit=" + credit + "}";
	}
}
