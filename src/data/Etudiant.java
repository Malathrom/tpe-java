package data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Etudiant {
	
	private String nom;
	
	private String prenom;
	
	private int creditTotal;
	
	private int nbSemestres;
	
	private int semestreParcours;
	
	/**liste des modules fait par l'etudiant*/
	private List<Module> modules = new ArrayList<Module>();
	
	public Etudiant(String nom, String prenom, List<Module> modules, int creditTotal, int semestres) {
		setNom(nom);
		setPrenom(prenom);
		setModules(modules);
		setCreditTotal(creditTotal);
		setNbSemestres(semestres);
	}

	/**
	 * Getter nom etudiant
	 * @return le nom de l'etudiant
	 */
	public String getNom() {
		return nom;
	}
	
	/**
	 * Setter nom de l' etudiant
	 * @param nom le nom de l'etudiant
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	/**
	 * Getter prenom etudiant
	 * @return le prenom de l'etudiant
	 */
	public String getPrenom() {
		return prenom;
	}
	
	/**
	 * Setter prenom de l' etudiant
	 * @param prenom le prenom de l'etudiant
	 */
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	
	/**
	 * Getter modules etudiant
	 * @return les modules de l'etudiant
	 */
	public List<Module> getModules() {
		return modules;
	}
	
	/**
	 * Setter liste des modules de l' etudiant
	 * @param modules les modules de l'etudiant
	 */
	public void setModules(List<Module> modules) {
		this.modules = modules;
	}
	
	/**
	 * Getter creditTotal de l'etudiant
	 * @return le nombre de credit de l'etudiant
	 */
	public int getCreditTotal() {
		return creditTotal;
	}
	
	/**
	 * Setter creditTotal de l' etudiant
	 * @param creditTotal le nombre de semestre de l'etudiant
	 */
	public void setCreditTotal(int creditTotal) {
		this.creditTotal = creditTotal;
	}
	
	/**
	 * Getter nombre de semestre de l' etudiant
	 * @return le nombre de semestre de l'etudiant
	 */
	public int getNbSemestres() {
		return nbSemestres;
	}

	/**
	 * Setter nombre de semestre de l' etudiant
	 * @param nbSemestres le nombre de semestre de l'etudiant
	 */
	public void setNbSemestres(int nbSemestres) {
		this.nbSemestres = nbSemestres;
	}
	
	@Override
	public String toString(){
		Iterator<Module> it = modules.iterator();
		String chaine = nom + " "+ prenom + " total credit:" + creditTotal + " Semestre total:" + nbSemestres + "\n";
		while (it.hasNext()) {
			Module module = (Module) it.next();
			chaine+= module + "\n";
		}
		return chaine;
	}
}
