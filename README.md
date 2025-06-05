# Funktionales Sortieren in Java - Country Example

## Überblick
Dieses Projekt dient als praktische Demonstration verschiedener Sortierstrategien in Java. Im Mittelpunkt steht die Klasse `Country`, welche Länder anhand diverser Merkmale wie Name, Kontinent, Einwohnerzahl, Fläche, UN-Beitrittsdatum und Landessprachen abbildet. Die Ausführungen illustrieren die Anwendung der natürlichen Sortierreihenfolge (`Comparable`), der umgekehrten Sortierung sowie mannigfaltiger `Comparator`-Implementierungen.

Zur Erzeugung der notwendigen Testdaten für die `Country`-Instanzen kommt die Klasse `DataGenerator` zum Einsatz. Diese bedient sich der [Instancio](https://www.instancio.org/) Bibliothek, um eine flexible und nachvollziehbare Generierung von Datensätzen zu gewährleisten. Zum die App laufen zu lassen kann man in IntelliJ einfach in der MainApp.Java Klasse die applikation starten. Die Daten werden generiert und auch sortiert dann in der Konsole ausgegeben.

## Wie funktioniert Java's Sortierung im Detail?

### 1. Natural Order (Natürliche Ordnung) - Comparable Interface

#### Konzept und Implementierung
Das Fundament der natürlichen Sortierung in Java bildet das `java.lang.Comparable<T>` Interface. Implementiert eine Klasse dieses Interface, so legt sie eine standardmässige, "natürliche" Reihenfolge für ihre Instanzen fest.

Im vorliegenden Projekt realisiert die `Country`-Klasse `Comparable<Country>`. Die natürliche Sortierung ist hierbei auf den Ländernamen ausgerichtet, wobei Gross-/Kleinschreibung ignoriert wird und `null`-Werte am Anfang der sortierten Liste erscheinen:

```java
package ch.bbw.m323.funktionalessortieren;

import java.util.Objects;

public class Country implements Comparable<Country> {
    private String name;
    // ... andere Attribute wie continent, population, areaInSquareKm ...

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
    // ... Getter, Setter, Konstruktor, equals, hashCode, toString ...
}
```

#### Anwendung der natürlichen Sortierung

1.  **Aufruf von `Collections.sort()`**:
    Wird `Collections.sort(list)` auf eine Liste von `Country`-Objekten angewendet, greift Java intern auf die implementierte `compareTo()`-Methode zurück.
    ```java
    // In MainApp.java:
    List<Country> sortiertNachName = new ArrayList<>(laenderFuerDemo);
    Collections.sort(sortiertNachName);
    ```

2.  **Interner Verarbeitungsmechanismus**:
    *   Java verifiziert, ob die Elemente der Liste das `Comparable`-Interface implementieren.
    *   Ist dies der Fall, wird die `compareTo()`-Methode für die paarweisen Vergleiche der Elemente herangezogen, um deren korrekte Position in der sortierten Sequenz zu bestimmen.
    *   Fehlt die `Comparable`-Implementierung, resultiert dies in einer `ClassCastException` zur Laufzeit.

3.  **Interpretation der `compareTo()`-Rückgabewerte**:
    *   Ein **negativer Wert** (< 0) signalisiert, dass das aktuelle Objekt (`this`) in der Sortierreihenfolge vor dem verglichenen Objekt (`other`) steht.
    *   Der Wert **Null** (= 0) bedeutet, dass beide Objekte hinsichtlich des Sortierkriteriums als gleichwertig betrachtet werden.
    *   Ein **positiver Wert** (> 0) zeigt an, dass das aktuelle Objekt (`this`) nach dem verglichenen Objekt (`other`) einzuordnen ist.

4.  **Detailbetrachtung: `String.compareToIgnoreCase()`**:
    Die Methode `compareToIgnoreCase()` der Klasse `String` führt einen lexikographischen Vergleich zweier Zeichenketten durch, wobei die Gross- und Kleinschreibung unberücksichtigt bleibt. Wie im Codebeispiel der `Country`-Klasse ersichtlich, ist eine explizite Behandlung von `null`-Werten unerlässlich, um `NullPointerExceptions` vorzubeugen und eine definierte Sortierposition für solche Fälle sicherzustellen.

### 2. Reverse Order (Umgekehrte Ordnung)

Java stellt verschiedene Ansätze zur Verfügung, um eine etablierte Sortierreihenfolge umzukehren:

1.  **Mittels `Collections.reverseOrder()`**:
    *   Diese statische Hilfsmethode liefert einen `Comparator`, welcher die Umkehrung der natürlichen Sortierordnung für `Comparable`-Objekte bewirkt.
    ```java
    // In MainApp.java, zur absteigenden Sortierung von Ländern nach Namen:
    List<Country> sortiertNachNameAbsteigend = new ArrayList<>(laenderFuerDemo);
    Collections.sort(sortiertNachNameAbsteigend, Collections.reverseOrder());
    ```
    *   Intern bedient sich dieser `Comparator` der `compareTo()`-Methode der Objekte, negiert jedoch deren Ergebnis (vereinfacht ausgedrückt, er vergleicht `b.compareTo(a)` anstelle von `a.compareTo(b)`).

2.  **Durch `Comparator.reversed()`**:
    *   Seit Java 8 ist die `default`-Methode `reversed()` Teil des `Comparator`-Interfaces.
    *   Besitzt man bereits einen `Comparator` (z.B. den statischen `Country.BY_POPULATION_ASC`), so lässt sich durch Aufruf von `Country.BY_POPULATION_ASC.reversed()` mühelos ein neuer `Comparator` mit invertierter Sortierlogik erzeugen.
    ```java
    // In MainApp.java, zur Sortierung von Ländern nach absteigender Bevölkerungszahl:
    List<Country> sortiertNachBevoelkerungAbsteigend = new ArrayList<>(laenderFuerDemo);
    sortiertNachBevoelkerungAbsteigend.sort(Country.BY_POPULATION_ASC.reversed());
    ```
    *   Diese Variante ist häufig die eleganteste, um einen spezifischen `Comparator` umzudrehen. Ein Beispiel hierfür ist `Country.BY_AREA_DESC`, welcher als `Comparator.comparingDouble(Country::getAreaInSquareKm).reversed()` in der `Country`-Klasse definiert ist.

### 3. Java's Sortieralgorithmus - TimSort
//Geschrieben von Chatgpt
#### Welchen Algorithmus verwendet Java?

Für das Sortieren von Objektlisten (`List<T>`) mit `Collections.sort()` oder `list.sort()` verwendet Java (seit Version 7) typischerweise **TimSort**. Auch `Arrays.sort()` für Objekt-Arrays greift auf TimSort zurück.

#### TimSort Eigenschaften:

1.  **Hybrid-Algorithmus**: Eine adaptive Kombination aus Merge Sort und Insertion Sort.
2.  **Stabil**: Gleiche Elemente behalten ihre relative Reihenfolge bei. (Wenn A und B bezüglich des Sortierkriteriums gleich sind und A vor B in der unsortierten Liste stand, steht A auch nach der Sortierung vor B).
3.  **Adaptiv**: Nutzt bereits sortierte Teilbereiche (sogenannte "Runs") in den Daten optimal aus und erreicht bei teilweise vorsortierten Daten eine Performance nahe O(n).
4.  **Zeitkomplexität**:
    *   Best Case: O(n) (bei bereits sortierten oder fast sortierten Daten)
    *   Average Case: O(n log n)
    *   Worst Case: O(n log n)
5.  **Platzkomplexität**: O(n) im Worst-Case für temporäre Arrays beim Mergen, kann aber für typische Datenverteilungen auch O(log n) sein.

### 4. Comparator Interface - Flexible Sortierung

Falls eine Sortierreihenfolge abweichend von der natürlichen benötigt wird, oder wenn die zu sortierende Klasse das `Comparable`-Interface nicht implementiert (oder dessen Implementierung nicht modifiziert werden soll), tritt das `java.util.Comparator`-Interface auf den Plan.

```java
package ch.bbw.m323.funktionalessortieren;

// ...
import java.util.Comparator;

// Beispiel: Statischer Comparator in der Country-Klasse
public class Country /* ... */ {
    // ...
    public static final Comparator<Country> BY_POPULATION_ASC =
            Comparator.comparingLong(Country::getPopulation);

    public static final Comparator<Country> BY_AREA_DESC =
            Comparator.comparingDouble(Country::getAreaInSquareKm).reversed();
    // ...
}
```

#### Umsetzungsstrategien für Comparatoren:

1.  **Ausgelagerte Klasse**:
    Ein `Comparator` kann als separate, eigenständige Klasse definiert werden. Dieser Ansatz empfiehlt sich für komplexe oder häufig wiederverwendete Sortierlogiken.
    Das Projekt illustriert dies mit `SortByContinentThenPopulationDesc.java`:
    ```java
    package ch.bbw.m323.funktionalessortieren;

    import java.util.Comparator;

    public class SortByContinentThenPopulationDesc implements Comparator<Country> {
        @Override
        public int compare(Country c1, Country c2) {
            // Fehlerbehandlung für null-Länder oder null-Kontinente
            if (c1 == null && c2 == null) return 0;
            if (c1 == null) return 1;
            if (c2 == null) return -1;

            Continent cont1 = c1.getContinent();
            Continent cont2 = c2.getContinent();

            int continentCompare;
            if (cont1 == null && cont2 == null) continentCompare = 0;
            else if (cont1 == null) continentCompare = 1; // null Kontinente ans Ende
            else if (cont2 == null) continentCompare = -1;
            else continentCompare = cont1.getDisplayName().compareToIgnoreCase(cont2.getDisplayName());

            if (continentCompare != 0) return continentCompare;

            return Long.compare(c2.getPopulation(), c1.getPopulation()); // absteigend
        }
    }
    ```
    Die Anwendung in `MainApp.java` erfolgt dann mittels Instanziierung dieser Klasse:
    ```java
    laenderFuerDemo.sort(new SortByContinentThenPopulationDesc());
    ```

2.  **Anonyme innere Klasse**:
    Für einmalige, kontextspezifische Sortierkriterien kann ein `Comparator` direkt bei seiner Verwendung als anonyme innere Klasse deklariert werden.
    Ein Beispiel aus `MainApp.java` für die aufsteigende Sortierung nach Fläche:
    ```java
    laenderFuerDemo.sort(new Comparator<Country>() {
        @Override
        public int compare(Country c1, Country c2) {
            return Double.compare(c1.getAreaInSquareKm(), c2.getAreaInSquareKm());
        }
    });
    ```

3.  **Lambda Expression**:
    Seit Java 8 ermöglichen Lambda-Ausdrücke eine äusserst prägnante Notation für funktionale Interfaces wie `Comparator`.
    Die Sortierung nach UN-Beitrittsdatum (älteste zuerst, `null`-Werte am Ende) in `MainApp.java` zeigt dies:
    ```java
    laenderFuerDemo.sort((c1, c2) -> {
        if (c1.getAccessionToUN() == null && c2.getAccessionToUN() == null) return 0;
        if (c1.getAccessionToUN() == null) return 1; // nulls last
        if (c2.getAccessionToUN() == null) return -1; // nulls last
        return c1.getAccessionToUN().compareTo(c2.getAccessionToUN());
    });
    ```

4.  **Method Reference**:
    Falls ein Lambda-Ausdruck lediglich eine bereits existierende Methode aufruft, lässt sich dies oft noch bündiger mittels einer Methodenreferenz darstellen. Die Hilfsmethoden des `Comparator`-Interfaces wie `comparing()`, `comparingInt()`, `comparingLong()` oder `comparingDouble()` werden häufig in Verbindung mit Methodenreferenzen genutzt.
    Beispiele für statische Comparatoren aus der `Country`-Klasse:
    ```java
    // Sortierung nach Bevölkerung
    Comparator.comparingLong(Country::getPopulation);

    // Sortierung nach Fläche
    Comparator.comparingDouble(Country::getAreaInSquareKm);

    // Sortierung nach UN-Beitrittsdatum (LocalDate), nullsLast behandelt null-Werte
    Comparator.comparing(Country::getAccessionToUN, Comparator.nullsLast(LocalDate::compareTo));

    // Sortierung nach Anzahl Sprachen, Lambda für Logik
    Comparator.comparingInt(country -> country.getNationalLanguages() != null ? country.getNationalLanguages().size() : 0);
    ```

5.  **Comparator Chaining**:
    Durch die Methode `thenComparing()` lassen sich mehrere Sortierkriterien kaskadierend anwenden. Stuft der primäre `Comparator` zwei Elemente als gleichwertig ein, kommt das nächste Kriterium in der Kette zum Zuge.
    In `MainApp.java` wird dies für die Sortierung nach Anzahl der Sprachen (aufsteigend) und sekundär nach Name (aufsteigend, ohne Berücksichtigung der Gross-/Kleinschreibung, `null`-Werte zuerst) demonstriert:
    ```java
    Comparator<Country> sprachenComparator = Comparator.comparingInt(c -> c.getNationalLanguages() != null ? c.getNationalLanguages().size() : 0);
    Comparator<Country> nameComparator = Comparator.comparing(
            Country::getName,
            Comparator.nullsFirst(String.CASE_INSENSITIVE_ORDER)
    );
    laenderFuerDemo.sort(sprachenComparator.thenComparing(nameComparator));
    ```
### 5. Interne Optimierungen von Java und Algorithmus-Überblick
//Geschrieben von Chatgpt
#### Wie Java Sortierung optimiert und welche Algorithmen zum Einsatz kommen:

1.  **Primitive vs. Objekte**:
    *   **Objekt-Arrays (`Object[]`) und Listen (`List<T>`)**: Hauptsächlich **TimSort** (stabil, O(n log n)).
    *   **Primitive Arrays** (z.B. `int[]`, `double[]`, `char[]`): Für die meisten primitiven Typen wird seit Java 7 eine Variante von **Dual-Pivot Quicksort** verwendet. Dieser ist im Allgemeinen sehr schnell (O(n log n) im Average Case), aber nicht stabil. Der Worst-Case (O(n²)) ist selten und wird durch Techniken wie den Wechsel zu Heapsort bei zu tiefer Rekursion mitigiert. Für `byte[]` wird Counting Sort verwendet, für `char[]` und `short[]` ebenfalls Dual-Pivot Quicksort.

2.  **Speicher-Optimierungen**:
    *   TimSort benötigt zusätzlichen Speicher (O(n) im schlimmsten Fall), versucht aber, diesen gering zu halten.
    *   Dual-Pivot Quicksort ist eher "in-place" (O(log n) für den Rekursionsstack).

3.  **JVM-Optimierungen**:
    *   Der HotSpot JIT-Compiler kann häufig verwendete Sortierroutinen und Comparator-Methoden optimieren (z.B. durch Inlining).
    *   Branch Prediction und andere Hardware-nahe Optimierungen können ebenfalls die Performance beeinflussen.

#### Performance-Vergleich (Vereinfacht):

| Methode/Datentyp            | Hauptalgorithmus (Java 7+) | Stabil? | Zeit (Avg/Worst)    | Platz (Worst) |
|-----------------------------|----------------------------|---------|---------------------|---------------|
| `Collections.sort(List)`    | TimSort                    | Ja      | O(n log n)/O(n log n) | O(n)          |
| `List.sort(Comparator)`     | TimSort                    | Ja      | O(n log n)/O(n log n) | O(n)          |
| `Arrays.sort(Object[])`     | TimSort                    | Ja      | O(n log n)/O(n log n) | O(n)          |
| `Arrays.sort(primitive[])`  | Dual-Pivot Quicksort*      | Nein    | O(n log n)/O(n²)**  | O(log n)      |
| `Stream.sorted()` (Objekte) | TimSort (oft)              | Ja      | O(n log n)/O(n log n) | O(n)          |

_* Für `byte[]` Counting Sort; für andere Primitive meist Dual-Pivot Quicksort._
_** Worst-Case von Quicksort ist selten und wird oft durch Fallbacks (z.B. auf Heapsort) in der Java-Implementierung vermieden bzw. die Auswirkungen minimiert._

### 6. Praktische Beispiele aus dem Code (`MainApp.java`)

Die Klasse `MainApp.java` demonstriert die Anwendung der verschiedenen Sortiertechniken:

#### Datenbeschaffung:
```java
// In MainApp.java:
// Daten werden mit DataGenerator.java und Instancio generiert
List<Country> alleLaender = DataGenerator.generateCountries(100);
List<Country> laenderFuerDemo = new ArrayList<>(alleLaender.subList(0, Math.min(alleLaender.size(), 25)));
```

#### Natural Order Anwendung (nach Name, case-insensitive, nulls first):
```java
// Country implementiert Comparable<Country>
// In MainApp.java:
List<Country> sortiertNachName = new ArrayList<>(laenderFuerDemo);
Collections.sort(sortiertNachName); // Verwendet Country.compareTo()
System.out.println("--- Sortiert nach Name (Natürliche Ordnung) ---");
sortiertNachName.forEach(System.out::println);
```

#### Reverse Order Anwendung:
```java
// In MainApp.java:
// 1. Umkehrung der natürlichen Ordnung (Name absteigend)
List<Country> sortiertNachNameAbsteigend = new ArrayList<>(laenderFuerDemo);
Collections.sort(sortiertNachNameAbsteigend, Collections.reverseOrder());

// 2. Umkehrung eines spezifischen Comparators (Bevölkerung absteigend)
List<Country> sortiertNachBevoelkerungAbsteigend = new ArrayList<>(laenderFuerDemo);
sortiertNachBevoelkerungAbsteigend.sort(Country.BY_POPULATION_ASC.reversed());
```

#### Comparator: Separate Klasse (`SortByContinentThenPopulationDesc`):
```java
// In MainApp.java:
List<Country> sortiertNachKontinentDannBevoelkerung = new ArrayList<>(laenderFuerDemo);
sortiertNachKontinentDannBevoelkerung.sort(new SortByContinentThenPopulationDesc());
// Sortiert nach Kontinent (aufst.), dann Bevölkerung (abst.)
```

#### Comparator: Anonyme Klasse (nach Fläche aufsteigend):
```java
// In MainApp.java:
List<Country> sortiertNachFlaecheAnon = new ArrayList<>(laenderFuerDemo);
sortiertNachFlaecheAnon.sort(new Comparator<Country>() {
    @Override
    public int compare(Country c1, Country c2) {
        return Double.compare(c1.getAreaInSquareKm(), c2.getAreaInSquareKm());
    }
});
```

#### Comparator: Lambda Expression (nach UN-Beitrittsdatum, älteste zuerst, nulls last):
```java
// In MainApp.java:
List<Country> sortiertNachUNBeitrittLambda = new ArrayList<>(laenderFuerDemo);
sortiertNachUNBeitrittLambda.sort((c1, c2) -> {
    if (c1.getAccessionToUN() == null && c2.getAccessionToUN() == null) return 0;
    if (c1.getAccessionToUN() == null) return 1; // nulls last
    if (c2.getAccessionToUN() == null) return -1; // nulls last
    return c1.getAccessionToUN().compareTo(c2.getAccessionToUN());
});
```

#### Comparator: Statische Comparatoren aus `Country.java`:
```java
// In MainApp.java (Beispiel):
List<Country> sortiertNachBevoelkerungStatisch = new ArrayList<>(laenderFuerDemo);
sortiertNachBevoelkerungStatisch.sort(Country.BY_POPULATION_ASC);

List<Country> sortiertNachFlaecheStatischDesc = new ArrayList<>(laenderFuerDemo);
sortiertNachFlaecheStatischDesc.sort(Country.BY_AREA_DESC);
```

#### Comparator: Chaining (nach Anzahl Sprachen, dann Name):
```java
// In MainApp.java:
List<Country> sortiertNachSprachenzahlDannName = new ArrayList<>(laenderFuerDemo);
Comparator<Country> sprachenComparator = Comparator.comparingInt(c -> c.getNationalLanguages() != null ? c.getNationalLanguages().size() : 0);
Comparator<Country> nameComparator = Comparator.comparing(
        Country::getName,
        Comparator.nullsFirst(String.CASE_INSENSITIVE_ORDER)
);
sortiertNachSprachenzahlDannName.sort(sprachenComparator.thenComparing(nameComparator));
```

#### Sortieren mit Java Streams:
```java
// In MainApp.java:
// Stream-Sortierung mit natürlicher Ordnung (nach Name)
List<Country> streamSortiertNachName = laenderFuerDemo.stream()
        .sorted()
        .collect(java.util.stream.Collectors.toList());

// Stream-Sortierung mit einem Comparator (nach Bevölkerung absteigend)
List<Country> streamSortiertNachBevoelkerungDesc = laenderFuerDemo.stream()
        .sorted(Country.BY_POPULATION_ASC.reversed())
        .collect(java.util.stream.Collectors.toList());

// Stream-Sortierung mit Comparator-Kette (Kontinent aufst., dann Fläche abst.)
List<Country> streamSortiertMehrstufig = laenderFuerDemo.stream()
        .sorted(Comparator.comparing((Country c) -> c.getContinent() != null ? c.getContinent().getDisplayName() : "ZZZ", Comparator.nullsLast(String::compareToIgnoreCase))
                        .thenComparing(Country::getAreaInSquareKm, Comparator.reverseOrder()))
        .collect(java.util.stream.Collectors.toList());
```
## Fazit

Java stellt ein durchdachtes und vielseitiges Framework für Sortieroperationen bereit. Die Symbiose aus `Comparable` für eine natürliche Grundordnung und `Comparator` für anwendungsspezifische Sortierkriterien erlaubt es Entwicklern, nahezu jede erdenkliche Sortieranforderung zu realisieren. Die zugrundeliegenden, hochentwickelten Algorithmen wie TimSort (für Objekte) und Dual-Pivot Quicksort (für primitive Typen) garantieren eine robuste Leistung für ein breites Spektrum von Anwendungsfällen. Mit der Einführung von Lambda-Ausdrücken und den funktionalen Erweiterungen des `Comparator`-Interfaces in Java 8 wurde die Definition von Sortierlogiken zusätzlich vereinfacht und die Eleganz des Codes gesteigert.

> ⚠️ AI Verzeichnis
> - ChatGpt als unterstüzung während der Implementierung genutzt.
> - Dokumentation grund gerüst + Code teile selber rausgeholt und geschrieben und dann mit Cursor eine vollständige Doku daraus gemacht mit allen punkten zum Thema Sortierung.
