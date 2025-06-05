package ch.bbw.m323.funktionalessortieren;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * MainApp zur Demonstration verschiedener funktionaler Sortiertechniken
 */
public class MainApp {

    private static final int ANZAHL_LAENDER_FUER_DEMO = 25; // Reduzierte Anzahl für übersichtlichere Konsolenausgabe kann auf 100 erhöht werden

    public static void main(String[] args) {
        System.out.println("Starte die Demonstration der funktionalen Sortierung für Länder...");

        // Schritt 1: Daten generieren
        List<Country> alleLaender = DataGenerator.generateCountries(100);
        List<Country> laenderFuerDemo = new ArrayList<>(alleLaender.subList(0, Math.min(alleLaender.size(), ANZAHL_LAENDER_FUER_DEMO)));

        System.out.println("\n--- Ursprüngliche (unsortierte) Liste der ersten " + laenderFuerDemo.size() + " Länder ---");
        laenderFuerDemo.forEach(System.out::println);

        // 1. Natürliche Sortierung (Comparable: nach Name)
        List<Country> sortiertNachName = new ArrayList<>(laenderFuerDemo);
        Collections.sort(sortiertNachName);
        System.out.println("\n--- 1. Sortiert nach Name (Natürliche Ordnung, Comparable) ---");
        sortiertNachName.forEach(System.out::println);

        // 2. Sortierung mit abgeleiteter Comparator-Klasse (SortByContinentThenPopulationDesc)
        List<Country> sortiertNachKontinentDannBevoelkerung = new ArrayList<>(laenderFuerDemo);
        sortiertNachKontinentDannBevoelkerung.sort(new SortByContinentThenPopulationDesc());
        System.out.println("\n--- 2. Sortiert nach Kontinent (aufst.) dann Bevölkerung (abst.) (Abgeleitete Comparator-Klasse) ---");
        sortiertNachKontinentDannBevoelkerung.forEach(System.out::println);

        // 3. Sortierung mit anonymer Comparator-Klasse (z.B. nach Fläche, aufsteigend)
        List<Country> sortiertNachFlaecheAnon = new ArrayList<>(laenderFuerDemo);
        sortiertNachFlaecheAnon.sort(new Comparator<Country>() {
            @Override
            public int compare(Country c1, Country c2) {
                return Double.compare(c1.getAreaInSquareKm(), c2.getAreaInSquareKm());
            }
        });
        System.out.println("\n--- 3. Sortiert nach Fläche (aufsteigend) (Anonyme Comparator-Klasse) ---");
        sortiertNachFlaecheAnon.forEach(System.out::println);

        // 4. Sortierung mit Lambda-Ausdruck (z.B. nach UN-Beitrittsdatum, älteste zuerst, nulls last)
        List<Country> sortiertNachUNBeitrittLambda = new ArrayList<>(laenderFuerDemo);
        sortiertNachUNBeitrittLambda.sort((c1, c2) -> {
            if (c1.getAccessionToUN() == null && c2.getAccessionToUN() == null) return 0;
            if (c1.getAccessionToUN() == null) return 1; // nulls last
            if (c2.getAccessionToUN() == null) return -1; // nulls last
            return c1.getAccessionToUN().compareTo(c2.getAccessionToUN());
        });
        System.out.println("\n--- 4. Sortiert nach UN-Beitrittsdatum (älteste zuerst, nulls last) (Lambda-Ausdruck) ---");
        sortiertNachUNBeitrittLambda.forEach(System.out::println);

        // 5. Sortierung mit Comparator-Kette (z.B. nach Anzahl Sprachen (aufst.), dann Name (aufst.))
        List<Country> sortiertNachSprachenzahlDannName = new ArrayList<>(laenderFuerDemo);
        Comparator<Country> sprachenComparator = Comparator.comparingInt(c -> c.getNationalLanguages() != null ? c.getNationalLanguages().size() : 0);
        Comparator<Country> nameComparator = Comparator.comparing(
                Country::getName, 
                Comparator.nullsFirst(String.CASE_INSENSITIVE_ORDER)
        );
        sortiertNachSprachenzahlDannName.sort(sprachenComparator.thenComparing(nameComparator));
        System.out.println("\n--- 5. Sortiert nach Anzahl Sprachen (aufst.), dann Name (aufst.) (Comparator-Kette) ---");
        sortiertNachSprachenzahlDannName.forEach(System.out::println);

        // 6. Sortierung mit statischen Comparator-Attributen aus der Datenklasse
        // 6a. Nach Bevölkerung aufsteigend (Country.BY_POPULATION_ASC)
        List<Country> sortiertNachBevoelkerungStatisch = new ArrayList<>(laenderFuerDemo);
        sortiertNachBevoelkerungStatisch.sort(Country.BY_POPULATION_ASC);
        System.out.println("\n--- 6a. Sortiert nach Bevölkerung (aufsteigend) (Statischer Comparator: BY_POPULATION_ASC) ---");
        sortiertNachBevoelkerungStatisch.forEach(System.out::println);

        // 6b. Nach Fläche absteigend (Country.BY_AREA_DESC)
        List<Country> sortiertNachFlaecheStatischDesc = new ArrayList<>(laenderFuerDemo);
        sortiertNachFlaecheStatischDesc.sort(Country.BY_AREA_DESC);
        System.out.println("\n--- 6b. Sortiert nach Fläche (absteigend) (Statischer Comparator: BY_AREA_DESC) ---");
        sortiertNachFlaecheStatischDesc.forEach(System.out::println);

        // 6c. Nach UN-Beitrittsdatum aufsteigend (Country.BY_UN_ACCESSION_DATE_ASC)
        List<Country> sortiertNachUNBeitrittStatisch = new ArrayList<>(laenderFuerDemo);
        sortiertNachUNBeitrittStatisch.sort(Country.BY_UN_ACCESSION_DATE_ASC);
        System.out.println("\n--- 6c. Sortiert nach UN-Beitrittsdatum (aufsteigend, nulls last) (Statischer Comparator: BY_UN_ACCESSION_DATE_ASC) ---");
        sortiertNachUNBeitrittStatisch.forEach(System.out::println);

        // 6d. Nach Anzahl Sprachen aufsteigend (Country.BY_NUMBER_OF_LANGUAGES_ASC)
        List<Country> sortiertNachAnzahlSprachenStatisch = new ArrayList<>(laenderFuerDemo);
        sortiertNachAnzahlSprachenStatisch.sort(Country.BY_NUMBER_OF_LANGUAGES_ASC);
        System.out.println("\n--- 6d. Sortiert nach Anzahl Sprachen (aufsteigend) (Statischer Comparator: BY_NUMBER_OF_LANGUAGES_ASC) ---");
        sortiertNachAnzahlSprachenStatisch.forEach(System.out::println);


        // 7. Umgekehrte Sortierung
        // 7a. Umkehrung der natürlichen Ordnung (Name absteigend)
        List<Country> sortiertNachNameAbsteigend = new ArrayList<>(laenderFuerDemo);
        Collections.sort(sortiertNachNameAbsteigend, Collections.reverseOrder());
        System.out.println("\n--- 8a. Sortiert nach Name (absteigend) (Umgekehrte natürliche Ordnung) ---");
        sortiertNachNameAbsteigend.forEach(System.out::println);

        // 7b. Umkehrung eines spezifischen Comparators (z.B. Bevölkerung absteigend mit .reversed())
        List<Country> sortiertNachBevoelkerungAbsteigend = new ArrayList<>(laenderFuerDemo);
        sortiertNachBevoelkerungAbsteigend.sort(Country.BY_POPULATION_ASC.reversed());
        System.out.println("\n--- 8b. Sortiert nach Bevölkerung (absteigend) (Umgekehrter statischer Comparator) ---");
        sortiertNachBevoelkerungAbsteigend.forEach(System.out::println);

        // 8. Sortieren mit Java Streams
        System.out.println("\n--- 10. Sortieren mit Java Streams ---");

        // 8a. Stream-Sortierung mit natürlicher Ordnung (nach Name)
        System.out.println("\n--- 10a. Stream sortiert nach Name (natürliche Ordnung) ---");
        List<Country> streamSortiertNachName = laenderFuerDemo.stream()
                .sorted()
                .collect(java.util.stream.Collectors.toList());
        streamSortiertNachName.forEach(System.out::println);

        // 8b. Stream-Sortierung mit einem Comparator (nach Bevölkerung absteigend)
        System.out.println("\n--- 10b. Stream sortiert nach Bevölkerung (absteigend) mit Comparator ---");
        List<Country> streamSortiertNachBevoelkerungDesc = laenderFuerDemo.stream()
                .sorted(Country.BY_POPULATION_ASC.reversed()) 
                .collect(java.util.stream.Collectors.toList());
        streamSortiertNachBevoelkerungDesc.forEach(System.out::println);

        // 8c. Stream-Sortierung mit Comparator-Kette (Kontinent aufst., dann Fläche abst.)
        System.out.println("\n--- 10c. Stream sortiert: Kontinent (aufst.), dann Fläche (abst.) ---");
        List<Country> streamSortiertMehrstufig = laenderFuerDemo.stream()
                .sorted(Comparator.comparing((Country c) -> c.getContinent() != null ? c.getContinent().getDisplayName() : "ZZZ", Comparator.nullsLast(String::compareToIgnoreCase))
                                .thenComparing(Country::getAreaInSquareKm, Comparator.reverseOrder()))
                .collect(java.util.stream.Collectors.toList());
        streamSortiertMehrstufig.forEach(System.out::println);

        System.out.println("\nDemonstration der funktionalen Sortierung abgeschlossen.");
    }
} 