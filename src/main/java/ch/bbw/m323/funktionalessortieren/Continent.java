package ch.bbw.m323.funktionalessortieren;

/**
 * Definiert die Kontinente der Welt.
 * Wird verwendet, um Länder einem Kontinent zuzuordnen.
 */
public enum Continent {
    AFRIKA("Afrika"),
    ASIEN("Asien"),
    EUROPA("Europa"),
    NORDAMERIKA("Nordamerika"),
    SUEDAMERIKA("Südamerika"),
    OZEANIEN("Ozeanien"),
    ANTARKTIS("Antarktis");

    private final String displayName;

    Continent(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gibt den Anzeigenamen des Kontinents zurück.
     */
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
} 