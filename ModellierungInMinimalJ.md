-> In Überarbeitung. In einigen Punkte stimmt dieser Text nicht mehr.

# Einführung #

Minimal-J verwendet zur Modellierung in Java im Gegensatz zu vielen anderen Frameworks keine Java Beans. Zusätzlich besitzen die Modellklassen einige implizite Eigenschaften und Anforderungen.

Die Gründe dennoch keine Java Beans zu verwenden sind:
  * Java Beans mit allen Gettern und Settern, wenn möglich noch mit vorgeschriebenem JavaDoc, tendieren lange und unübersichtliche Klassen zu werden. Beans mit einem Dutzend Attributen sind kaum auf einer Bildschirmseite darstellbar. Kommt ein weiteres Attribut hinzu, wird es entweder am Schluss der bisherigen hinzugefügt, oder einfach irgendwo. Damit geht die Aussagekraft der Beans im Laufe eines Projekts zu schnell verloren.
  * Java Beans besitzen eine sehr hohe Redundanz bezüglich der Namen der Attribute. Gut zehn Mal werden die Namen jeweils wiederholt. Entwicklerwerkzeuge und Code Generatoren ersparen dem Entwickler unterdessen die Wiederholung selber erzeugen zu müssen. Es verbleibt jedoch die Schwachstelle, dass bei Änderungen sämtliche Wiederholungen mitgezogen werden müssen. Die zwingende Redundanz ist eigentlich vorgeschriebener Code Smell.
  * Aktive Beans unterstützen Listeners. D.h. neben Gettern/Settern sind auch noch addListener/removeListener Methoden vorhanden. Die Listener werden bei jedem Setzen eines Wertes aufgerufen. Listener werden von Minimal-J nicht unterstützt und den damit verlorenen "Lost of Control" zu vermeiden. Modellklassen in Minimal-J sind vollkommen passive Datencontainer.

# Der Aufbau #

Am einfachsten lässt sich der Aufbau anhand eines Beispiels erklären:
```
public class Book {
	public static final Book BOOK = Keys.of(Book.class);

	@Required @Size(ExampleFormats.NAME) 
	public String title;
	public Media media;
	@Size(ExampleFormats.NAME)
	public String author;
	public Boolean available;
	public LocalDate date;
	@Size(4)
	public Integer pages;
	@Size(6) @Decimal(2)
	public BigDecimal price;
}
```
Diese Klasse findet sich im Beispielprojekt von Minimal-J.

Als erstes fällt auf:
  * Die Klasse muss weder von einer bestimmten Superklasse ableiten, noch muss sie ein Interface implementieren.
  * Sämtliche Attribute werden public deklariert.

Dass sind die auffallendsten und auch wichtigsten Punkte. Bei genauerem Hinsehen fällt weiter auf:
  * Die Keys - Instanz gleich zu Beginn der Klasse. Keys ist eine Klasse des Frameworks, die mit einigen Tricks dafür sorgt, dass keine eigenen String Konstanten für die Namen der Properties nötig sind. Die Klasse Book ist gleichzeitig Datencontainer und Lieferant für die später im UI verwendeten (Keys) Konstanten.
  * Attribute können auf die Java übliche Art initialisiert werden.
  * Der erlaubte Inhalt von String Properties wird über Annotations deklariert. Dabei gibt es zwei Möglichkeiten: Entweder man annotiert die genaue Spezifikation gleich zum Attribut ( @Size(6) ), oder man referenziert auf den Namen eines Formats ( @Size(ExampleFormats.NAME) ).

# Unterstütze Property Klassen #

## Basis Klassen ##

Die unterstützten Basis Klassen von Properties sind in der ModelTest Klasse in der Methode isAllowedPrimitive gut sichtbar. Konkret sind dies zur Zeit:
  * String
  * Integer
  * Boolean
  * BigDecimal
  * Von Joda: LocalDate, LocalTime, LocalDateTime, ReadablePartial

Eventuell kommt zu einem spätern Zeitpunkt noch Long dazu. Generell wird sich die Liste in Zukunft jedoch kaum vergrössern.

## Enum ##

Es können normal enum Klassen verwendet werden wie:

```
package ch.openech.mj.example.model;

public enum Media {
	hardcover, paperback, ebook,
}
```

Zu jeder enum Klasse gehört ein .property File, welche die umgangssprachlichen Texte zu den Werten der Enumeration enthält.

## Codes ##

Codes, deren Werte für die Applikation keinen direkten Einfluss haben, können als Liste in einem .properties - File aufgelistet werden. hier der Media Code aus der Beispielapplikation:
```
addressCategory.object=Kategorie
addressCategory.default=1
addressCategory.key.0=1
addressCategory.text.0=Private Postadresse
addressCategory.key.1=2
addressCategory.text.1=Geschäftliche Postadresse
```

Die Codes müssen beim init der Applikation bei der Codes - Klasse angemeldet werden: (subject to change)

```
@Override
public void init() {
	super.init();
	Codes.addCodes(ResourceBundle.getBundle("ch.openech.dm.person.types.ech_person")); 
}
```

Code Properties sind dann von der Klasse String mit einer speziellen @Code Annotation.

## Inline Klassen ##

Inline Klassen erzeugen kleine Hilfsobjekte, die dann in den eigentlichen Businessklassen verwendet werden. Ein häufige verwendetes Beispiel ist eine Money - Klasse, die aus einem Betrag und einer Währung besteht. Oder eine Daterange - Klasse, die aus einem Von- und einem Bisdatum bestehen.

Trotz ihrer Winzigkeit sind diese Klassen in den meisten Arten der Modellierung ein grösseres Problem. Denn es gibt da ein Dilemma:
  * Während sie als normal "fette" Klassen modelliert werden, soll z.B. bei der Money - Klasse die eine eigene Tablle für jede Verwendung der Money - Klasse entstehen. So werden die beiden Attribute (Geld, Währung) auf zwei Attributenamen in der Verwender - Klasse gemappt. Dies tönt jetzt nicht nur kompliziert, die Annotierung bei Hibernate sieht auch entsprechend kompliziert aus.
  * Dennoch ist man fast gezwungen, eine "fette" Klasse zu modellieren. Im UI Bereich möchte man auf eine Money - Klasse referenzieren können. Und auch Validierungen finden ein ideales zu Hause eher in solchen Klassen, als wenn "Money" auf eine andere Art modelliert wird.

Die Lösung in Minimal-J für die Verwendung eines Inline - Attributs sieht nun so aus:

```
        public final Address address = new Address();
	public final Money price = new Money();
```

Die wichtigen Punkte sind:
  * Das Attribut muss final deklariert werden
  * Das Attribut muss gleich initialisiert werden.
  * Es gibt einen **wichtigen** Unterschied, ob das Attribut gleich heisst wie die Inline-Klasse oder nicht!

## Inline Attribute in der DB ##

In der Datenbank werden Inline Attribute nicht als eigene Tabelle angelegt. Darum auch der Name Inline. Der Inhalt eines Inline Attributes wird "ausgepackt" und gleichwertig zu anderen, einfachen Attributen als mehrere Spaltennamen angelegt. Enthält als eine Money Klasse z.B.

```
public class Address {
	public String amount;
	public String currency;
}
```

und ein Book Objekt enthält ein solches Geldobjekt als Inline Attribut:
```
public class Book {
	@Required @Varchar(30)
	public String title;
        public final Money money = new Money();
}
```

so wird das von der Persistenz Schicht gleich in die Datenbank geschrieben, wie wenn das Buch Objekt die beiden Attribute des Money Objektes direkt enthalten würde:
```
public class Book {
	@Required @Varchar(30)
	public String title;
	public String amount;
	public String currency;
}
```

Der Vorteil einer eigenen Money Klasse ist neben einer besseren Gruppierung der Attribute, dass die Money Klasse eigene Methode enthalten kann, z.b. für die Validierung. Gerade bei einer mehrfachen Verwendung von Money kann so die Wiederholung der gleichen Validierung vermieden werden.

## Mehrfache Inline Attribute ##

Der obige Trick funktioniert natürlich nur, solange jede Klasse von Inline Attributen in der beinhaltenden Klasse nur einmal vorkommt. Das ist zwar der häufigste Fall, aber auch manchmal möchte man eine Inline Klasse auch zweimal verwenden können. So könnte das Buch einen Einkaufs- und einen Verkaufspreis haben:
```
public class Book {
	@Required @Varchar(30)
	public String title;
        public final Money buy = new Money();
        public final Money sell = new Money();
}
```
In diesem Fall können die beiden Attribute natürlich nicht mehr beide "money" heissen, sondern definieren durch ihren Namen genau den Unterschied zwischen sich.

In der Datenbank wird in diesem Fall immer noch nicht eine eigene Tabelle angelegt. Ein Money-Objekt wird durch die doppelte Verwendung nicht wesentlich selbständiger und würde für sich alleine keinen Sinn machen. Aber aber die Spaltennamen nicht doppelt geführt werden dürfen, wird der Namen des Inline Attributes vor die Namen der Attribute der verwendeten Inline Klasse gesetzt, als entstehen also die Spaltennamen "buyAmount", "buyCurrency", "sellAmount", "sellCurrency". Natürlich dürfen somit diese Namen als direkt Attribute in der Buch Klasse nicht mehr verwendet werden!

## Referenzen ##

Referenzen entstehen, wenn die verwendete Klasse für Attribute mehr Eigenständigkeit besitzt. Sprich in der Datenbank ihre eigene Tabelle. Modelliert sieht dies so aus wie man es erwarten würde:
```
public class Book {
	@Required @Varchar(30)
	public String title;
        public Person autor;
}

public class Person {
	@Required @Varchar(30)
	public String name;
}
```


# Listen #

Listen werden in Minimal-J erheblich weniger gebraucht als bei anderen Ideen der Modellierung. Es ist auch keine Verschachtelung von Listen möglich. Das heisst, wenn eine Klasse Elemente einer Liste liefert, so darf sie selber keine Listen Attribute mehr enthalten.

Das rührt auch daher, dass die Modellierung sich in Minimal-J viel stärker an den Bedürfnissen des Clients orientiert als an einer Normalisierungstrategie. Im UI Bereich führen verschachtelte Listen meist zu Unübersichtlichkeit oder zur Notwendigkeit Daten erst bei Bedarf anzuzeigen und zu Beginn zu verstecken, z.B. in einem Tree, der erst aufgeklappt werden muss. In Minimal-J sollen alle Daten eines Objektes auf einmal sichtbar sein.

Bei stärker verschachtelten Modellen muss auf ein Element einer List gewechselt werden. Dies führt auch zu einer "entnetzung" des Datenbank Modells. Man muss bei der Persistenzschicht nicht mehr befürchten, dass beim Laden eines Objektes über dessen Abhängigkeiten gleich ein grosser Teil der Datenbank mitgeladen wird.

Deklariert wird ein List Attribut einfach über
```
	public final List<Address> addresses = new ArrayList<Address>();
```

Die Persistenzschicht übernimmt auch hier die korrekte Abbildung und Historisierung der Daten.