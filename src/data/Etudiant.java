package data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Etudiant {
	
	private String nom;
	
	private String prenom;
	
	private int creditTotal;
	
	/**liste des modules fait par l'etudiant*/
	private List<Module> modules = new ArrayList<Module>();

	//TODO A SUPPRIMER UNE FOIS QUE L;autre marche
	public Etudiant(String nom, String prenom, List<Module> modules) {
		this.nom = nom;
		this.prenom = prenom;
		this.modules = modules;
	}
	
	public Etudiant(String nom, String prenom, List<Module> modules, int creditTotal) {
		this.nom = nom;
		this.prenom = prenom;
		this.modules = modules;
		this.setCreditTotal(creditTotal);
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
	public String getPrenom() {
		return prenom;
	}
	//TODO a commenter
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	//TODO a commenter
	public List<Module> getModules() {
		return modules;
	}
	//TODO a commenter
	public void setModules(List<Module> modules) {
		this.modules = modules;
	}
	//TODO a commenter
	public int getCreditTotal() {
		return creditTotal;
	}
	//TODO a commenter
	public void setCreditTotal(int creditTotal) {
		this.creditTotal = creditTotal;
	}
	
	public String toString(){
		Iterator<Module> it = modules.iterator();
		String chaine = nom + " "+ prenom + " " + creditTotal + "\n";
		while (it.hasNext()) {
			Module module = (Module) it.next();
			chaine+= module + "\n";
		}
		return chaine;
	}
}