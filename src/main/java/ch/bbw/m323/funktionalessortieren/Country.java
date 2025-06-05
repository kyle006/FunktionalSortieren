package ch.bbw.m323.funktionalessortieren;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Comparator;

public class Country implements Comparable<Country> {

    private String name;
    private Continent continent;
    private long population;
    private double areaInSquareKm;
    private LocalDate accessionToUN;
    private List<String> nationalLanguages;

    // Statische Comparatoren
    /**
     * Comparator für die ASC Sortierung von Ländern nach Bevölkerungszahl.
     */
    public static final Comparator<Country> BY_POPULATION_ASC =
            Comparator.comparingLong(Country::getPopulation);

    /**
     * Comparator für die DESC Sortierung von Ländern nach Fläche.
     */
    public static final Comparator<Country> BY_AREA_DESC =
            Comparator.comparingDouble(Country::getAreaInSquareKm).reversed();

    /**
     * Comparator für die Sortierung von Ländern nach UN-Beitrittsdatum.
     */
    public static final Comparator<Country> BY_UN_ACCESSION_DATE_ASC =
            Comparator.comparing(Country::getAccessionToUN, Comparator.nullsLast(LocalDate::compareTo));

    /**
     * Comparator für die Sortierung von Ländern nach der Anzahl ihrer Nationalsprachen.
     */
    public static final Comparator<Country> BY_NUMBER_OF_LANGUAGES_ASC =
            Comparator.comparingInt(country -> country.getNationalLanguages() != null ? country.getNationalLanguages().size() : 0);


    /**
     * Konstruktor zum Erstellen eines neuen {@code Country}-Objekts.
     *
     * @param name             Der Name des Landes.
     * @param continent        Der Kontinent, zu dem das Land gehört.
     * @param population       Die Bevölkerungszahl des Landes.
     * @param areaInSquareKm   Die Fläche des Landes in Quadratkilometern.
     * @param accessionToUN    Das Beitrittsdatum zur UN.
     * @param nationalLanguages Eine Liste der Nationalsprachen.
     */
    public Country(String name, Continent continent, long population, double areaInSquareKm, LocalDate accessionToUN, List<String> nationalLanguages) {
        this.name = name;
        this.continent = continent;
        this.population = population;
        this.areaInSquareKm = areaInSquareKm;
        this.accessionToUN = accessionToUN;
        this.nationalLanguages = nationalLanguages;
    }

    // Getter und Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Continent getContinent() {
        return continent;
    }

    public void setContinent(Continent continent) {
        this.continent = continent;
    }

    public long getPopulation() {
        return population;
    }

    public void setPopulation(long population) {
        this.population = population;
    }

    public double getAreaInSquareKm() {
        return areaInSquareKm;
    }

    public void setAreaInSquareKm(double areaInSquareKm) {
        this.areaInSquareKm = areaInSquareKm;
    }

    public LocalDate getAccessionToUN() {
        return accessionToUN;
    }

    public void setAccessionToUN(LocalDate accessionToUN) {
        this.accessionToUN = accessionToUN;
    }

    public List<String> getNationalLanguages() {
        return nationalLanguages;
    }

    public void setNationalLanguages(List<String> nationalLanguages) {
        this.nationalLanguages = nationalLanguages;
    }

    @Override
    public String toString() {
        return "Country{" +
               "name='" + name + '\'' +
               ", continent=" + (continent != null ? continent.getDisplayName() : "N/A") +
               ", population=" + population +
               ", areaInSquareKm=" + areaInSquareKm + " km²" +
               ", accessionToUN=" + accessionToUN +
               ", nationalLanguages=" + (nationalLanguages != null ? nationalLanguages : "N/A") +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Country country = (Country) o;
        return population == country.population &&
               Double.compare(country.areaInSquareKm, areaInSquareKm) == 0 &&
               Objects.equals(name, country.name) &&
               continent == country.continent &&
               Objects.equals(accessionToUN, country.accessionToUN) &&
               Objects.equals(nationalLanguages, country.nationalLanguages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, continent, population, areaInSquareKm, accessionToUN, nationalLanguages);
    }

    @Override
    public int compareTo(Country other) {
        if (this.name == null && other.name == null) {
            return 0;
        }
        if (this.name == null) {
            return -1;
        }
        if (other.name == null) {
            return 1;
        }
        return this.name.compareToIgnoreCase(other.name);
    }
} 