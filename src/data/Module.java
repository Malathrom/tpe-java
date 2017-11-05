package data;

import operation.Note;

/**La classe module représente un module de l'UTT*/
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

	//TODO a commenter
	public Module(String nom, Note note, int credit, int semestre, String parcours, String categorie) {
		this.nom = nom;
		this.categorie = categorie;
		this.semestre = semestre; 
		this.note = note;
		this.parcours = parcours;
		this.credit = credit;
	}
	//TODO a commenter	
	public Module(String nom, int credit, String categorie, String parcours){
		this.nom = nom;
		this.credit = credit;
		this.categorie = categorie;
		this.parcours = parcours;
	}
	
	//TODO a commenter
	public Module(String nom){
		this.nom = nom;
	}
	
	@Override
	public String toString() {
		return "Module{ nom=" + nom + " parcours=" + parcours + " semestre: " + semestre + " Note=" + note + " credit=" + credit + " categorie=" + categorie + "}";
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
}
