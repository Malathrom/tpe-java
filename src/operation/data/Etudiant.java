package operation.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Etudiant {
	
	private String nom;
	
	private String prenom;
	
	/**liste des modules fait par l'etudiant*/
	private List<Module> modules = new ArrayList<Module>();

	public Etudiant(String nom, String prenom, List<Module> modules) {
		this.nom = nom;
		this.prenom = prenom;
		this.modules = modules;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public List<Module> getModules() {
		return modules;
	}

	public void setModules(List<Module> modules) {
		this.modules = modules;
	}
	
	public String toString(){
		Iterator<Module> it = modules.iterator();
		String chaine = nom + " "+ prenom + "\n";
		while (it.hasNext()) {
			Module module = (Module) it.next();
			chaine+= module + "\n";
		}
		return chaine;
	}

	
}
