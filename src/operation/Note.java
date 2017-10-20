package operation;
/** Enumeration represenatnt les notes possibles pour une UV*/
//TODO a commenter
public enum Note{
	A("A"),
	B("B"),
	C("C"),
	D("D"),
	E("E"),
	F("F"),
	FX("FX"),
	EQU("EQU"),
	ABS("ABS"),
	NULL("NULL");

	private String note;

	Note(String note){
		this.note = note;
	}

	public String getNote() {
		return note;
	}

	public static Note getNote(String note) {
		if(note.equals("A"))
				return A;
		if(note.equals("B"))
				return B;
		if(note.equals("C"))
				return C;
		if(note.equals("D"))
				return D;
		if(note.equals("E"))
				return E;
		if(note.equals("F"))
				return F;
		if(note.equals("FX"))
				return FX;
		if(note.equals("EQU"))
				return EQU;
		if(note.equals("ABS"))
				return ABS;
		if(note.equals("NULL"))
				return NULL;
		return null;
	}
}
