package ch.bbw.m323.funktionalessortieren;

import java.util.Comparator;

/**
 * Ein Comparator, der Objekte zuerst nach ihrem Kontinent (aufsteigend nach Anzeigenamen)
 * und dann innerhalb jedes Kontinents nach ihrer Bevölkerung (absteigend) sortiert.
 */
public class SortByContinentThenPopulationDesc implements Comparator<Country> {

    @Override
    public int compare(Country c1, Country c2) {
        if (c1 == null && c2 == null) return 0;
        if (c1 == null) return 1; // c1 (null) ist "grösser" und kommt nach c2
        if (c2 == null) return -1; // c2 (null) ist "grösser" und kommt nach c1

        Continent cont1 = c1.getContinent();
        Continent cont2 = c2.getContinent();

        // Vergleich der Kontinente (Anzeigenamen, nulls last)
        int continentCompare;
        if (cont1 == null && cont2 == null) {
            continentCompare = 0;
        } else if (cont1 == null) {
            continentCompare = 1; // null Kontinente ans Ende
        } else if (cont2 == null) {
            continentCompare = -1; // null Kontinente ans Ende
        } else {
            continentCompare = cont1.getDisplayName().compareToIgnoreCase(cont2.getDisplayName());
        }

        if (continentCompare != 0) {
            return continentCompare;
        }

        // Wenn Kontinente gleich sind, nach Bevölkerung absteigend sortieren
        return Long.compare(c2.getPopulation(), c1.getPopulation()); // c2 vs c1 für absteigend
    }
} 