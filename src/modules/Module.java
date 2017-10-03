package modules;

/*TODO a commenter*/
public class Module {
	private String nom = "";
	private int credit = 0;
	
	public Module(String nom, int credit){
		this.nom = nom;
		this.credit = credit;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public int getCredit() {
		return credit;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}
}
