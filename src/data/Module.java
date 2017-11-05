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
	
	//TODO a commenter	
	public String getNom() {
		return nom;
	}
	
	//TODO a commenter	
	public void setNom(String nom) {
		this.nom = nom;
	}

	//TODO a commenter	
	public String getCategorie() {
		return categorie;
	}

	//TODO a commenter	
	public void setCategorie(String categorie) {
		this.categorie = categorie;
	}

	//TODO a commenter	
	public String getParcours() {
		return parcours;
	}

	//TODO a commenter	
	public void setParcours(String parcours) {
		this.parcours = parcours;
	}

	//TODO a commenter	
	public int getSemestre() {
		return semestre;
	}

	//TODO a commenter	
	public void setSemestre(int semestre) {
		this.semestre = semestre;
	}

	//TODO a commenter	
	public Note getNote() {
		return note;
	}

	//TODO a commenter	
	public void setNote(Note note) {
		this.note = note;
	}

	//TODO a commenter	
	public int getCredit() {
		return credit;
	}

	//TODO a commenter	
	public void setCredit(int credit) {
		this.credit = credit;
	}
}
