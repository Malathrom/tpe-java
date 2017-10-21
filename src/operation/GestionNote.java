package operation;

import java.util.ArrayList;
import java.util.List;

import operation.data.Etudiant;
import operation.data.Module;

public class GestionNote {

	List<Etudiant> etudiants = new ArrayList<Etudiant>();
	
	public GestionNote(){
		Module mod1 = new Module("IF26", Note.A, 6, 1);
		Module mod2 = new Module("NF16", Note.D, 6, 2);
		Module mod3 = new Module("LE08", Note.FX, 6, 1);
		Module mod4 = new Module("LO12", Note.B, 6, 1);
		Module mod5 = new Module("LO02", Note.C, 6, 3);
		Module mod6 = new Module("SY04", Note.D, 6, 2);
		Module mod7 = new Module("MO54", Note.E, 6, 1);
		Module mod8 = new Module("GE31", Note.A, 4, 3);
		Module mod9 = new Module("MATH01", Note.B, 6, 1);
		Module mod10 = new Module("IF26", Note.C, 6, 2);
		Module mod11 = new Module("JP00", Note.D, 4, 3);
		Module mod12 = new Module("PO03", Note.E, 6, 2);
		Module mod14 = new Module("GE43", Note.A, 6, 1);
		Module mod18 = new Module("IF10", Note.E, 6, 1);
		Module mod19 = new Module("TX10", Note.F, 4, 3);
		Module mod20 = new Module("NF20", Note.A, 6, 2);
		
		List<Module> modules1 = new ArrayList<Module>();
		List<Module> modules2 = new ArrayList<Module>();
		List<Module> modules3 = new ArrayList<Module>();
		
		modules1.add(mod1);
		modules1.add(mod3);
		modules1.add(mod5);
		modules1.add(mod7);
		modules1.add(mod9);
		modules1.add(mod11);
		modules2.add(mod2);
		modules2.add(mod4);
		modules2.add(mod6);
		modules2.add(mod8);
		modules2.add(mod10);
		modules2.add(mod12);
		modules3.add(mod10);
		modules3.add(mod7);
		modules3.add(mod14);
		modules3.add(mod20);
		modules3.add(mod19);
		modules3.add(mod18);
		etudiants.add(new Etudiant("De Smedt", "Tom", modules1));
		etudiants.add(new Etudiant("Genin", "Adrian", modules2));
		etudiants.add(new Etudiant("Noga", "Lucas", modules3));
	}
	
	//TODO gestion de note par semestres
	
	public void traitement(){
		
	}
	
}
