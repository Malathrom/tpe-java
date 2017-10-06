package operation.data;

import java.util.Comparator;

public class Element implements Comparator<Element> {
	protected String sigle;
	protected String categorie;
	protected int credit;

	public Element(String sigle, String categorie, int credit) {
		this.sigle = sigle;
		this.categorie = categorie;
		this.credit = credit;
	}

	public String getSigle() {
		return sigle;
	}

	public void setSigle(String sigle) {
		this.sigle = sigle;
	}

	public String getCategorie() {
		return categorie;
	}

	/**
	 *
	 * @return le nombre de crédit
	 */
	public int getCredit() {
		return credit;
	}

	/**
	 * Cette méthode setCredit permet de modifier l'attribut de la classe
	 * @param credit
	 */
	public void setCredit(int credit) {
		this.credit = credit;
	}


	@Override
	public int compare(Element elt1, Element elt2) {
		Integer credit1 = elt1.getCredit();
		Integer credit2 = elt2.getCredit();
		int result = credit1.compareTo(credit2);
		if(result == 0){
			return credit1.compareTo(credit2);
		}
		return result;
	}
}