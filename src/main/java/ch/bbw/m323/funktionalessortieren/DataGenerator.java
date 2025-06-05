package ch.bbw.m323.funktionalessortieren;

import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.Select;
import org.instancio.settings.Keys;
import org.instancio.settings.Settings;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;

/**
 * Eine Hilfsklasse zum Generieren von Testdaten.
 * Verwendet die Instancio Bibliothek, um zufällige, aber reproduzierbare Daten zu erzeugen.
 */
public class DataGenerator {

    private static final Random RANDOM_GENERATOR = new Random();
    private static final long FIXED_SEED = 12345L; 

    // Vordefinierte Liste von möglichen Sprachen zur Auswahl
    private static final List<String> POSSIBLE_LANGUAGES = Arrays.asList(
            "Englisch", "Spanisch", "Französisch", "Deutsch", "Chinesisch (Mandarin)",
            "Hindi", "Arabisch", "Portugiesisch", "Russisch", "Japanisch", "Bengalisch",
            "Lahnda", "Koreanisch", "Italienisch", "Türkisch", "Niederländisch", "Polnisch"
    );

    // Ergänzung zu DataGenerator.java

    private static final String[] SILBEN = {"Al", "ba", "ri", "on", "ta", "la", "ni", "ra", "do", "va", "lo", "sa"};
    private static final String[] ENDUNGEN = {"ien", "land", "stan", "tan", "mark"};

    private static String generiereSchoenenNamen(Random random) {
        String name = SILBEN[random.nextInt(SILBEN.length)] +
                SILBEN[random.nextInt(SILBEN.length)] +
                ENDUNGEN[random.nextInt(ENDUNGEN.length)];
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    static {
        RANDOM_GENERATOR.setSeed(FIXED_SEED);
    }

    /**
     * Generiert eine Liste von Objekten mit zufälligen, aber reproduzierbaren Daten.
     */
    public static List<Country> generateCountries(int count) {
        Settings instancioSettings = Settings.create().set(Keys.SEED, FIXED_SEED);

        Model<Country> countryModel = Instancio.of(Country.class)
                .withSettings(instancioSettings)
                .supply(Select.field(Country::getName), random -> generiereSchoenenNamen(RANDOM_GENERATOR))
                .supply(Select.field(Country::getContinent), random -> Continent.values()[RANDOM_GENERATOR.nextInt(Continent.values().length)])
                .generate(Select.field(Country::getPopulation), gen -> gen.longs().min(500_000L).max(1_400_000_000L))
                .supply(Select.field(Country::getAreaInSquareKm), random -> (double) (RANDOM_GENERATOR.nextLong(100_000, 17_000_000_000L)) / 100.0)
                .supply(Select.field(Country::getAccessionToUN), random -> {
                    long minDay = LocalDate.of(1945, 10, 24).toEpochDay();
                    long maxDay = LocalDate.of(2023, 12, 31).toEpochDay();
                    if (RANDOM_GENERATOR.nextDouble() < 0.1) {
                        return null;
                    }
                    long randomEpochDay = minDay + RANDOM_GENERATOR.nextLong(maxDay - minDay + 1);
                    return LocalDate.ofEpochDay(randomEpochDay);
                })
                .supply(Select.field(Country::getNationalLanguages), random -> {
                    int numberOfLanguages = RANDOM_GENERATOR.nextInt(1, 4);
                    List<String> languages = new ArrayList<>();
                    List<String> availableLanguages = new ArrayList<>(POSSIBLE_LANGUAGES);
                    Collections.shuffle(availableLanguages, RANDOM_GENERATOR);
                    for (int i = 0; i < numberOfLanguages && i < availableLanguages.size(); i++) {
                        languages.add(availableLanguages.get(i));
                    }
                    return languages.isEmpty() ? null : languages;
                })
                .toModel();

        return Instancio.of(countryModel).withSettings(instancioSettings).stream().limit(count).toList();
    }

    /**
     * Hauptmethode zum Testen des Datengenerators für Länder.
     * Generiert 100 Länder und gibt sie auf der Konsole aus.
     */
    public static void main(String[] args) {
        System.out.println("--- Generierte Länder (Reproduzierbar mit Seed: " + FIXED_SEED + ") ---");
        List<Country> countries = generateCountries(100);
        countries.forEach(System.out::println);
        System.out.println("\nEs wurden " + countries.size() + " Länder generiert.");

        long nullUNAccessions = countries.stream().filter(c -> c.getAccessionToUN() == null).count();
        System.out.println("Anzahl Länder mit fehlendem UN-Beitrittsdatum: " + nullUNAccessions);

        countries.stream().limit(5).forEach(c -> System.out.println("Sprachen für " + c.getName() + ": " + c.getNationalLanguages()));
    }
}
