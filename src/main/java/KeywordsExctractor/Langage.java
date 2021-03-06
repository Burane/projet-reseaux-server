package KeywordsExctractor;

public enum Langage {
	FRENCH("fr"),
	ENGLISH("en");

	private final String libelle;

	Langage(String libelle) {
		this.libelle = libelle;
	}

	@Override
	public String toString() {
		return libelle;
	}
}
