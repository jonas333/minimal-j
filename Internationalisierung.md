# Internationalisierung #

Die Internationalisierung wird im Java üblichen Rahmen realisiert.
Die Inhalte der Beschriftungen von Feldern in Formularen werden in .properites
Dateien abgelegt.

Die .properties Dateien entsprechen der üblichen Definition von solchen
Dateien. Siehe https://de.wikipedia.org/wiki/Java-Properties-Datei .
Vereinfacht bestehen solche Dateien aus Key/Value Paaren.

## Resources ##

Die Klasse [Resources](https://code.google.com/p/minimal-j/source/browse/Minimal-J/src/main/java/org/minimalj/util/resources/Resources.java) hält statisch das aktuelle ResourceBundle, welches für Texte
verwendet wird. Das verwendete ResourceBundle sollte während der Laufzeit nicht
mehr verändert werden.

### Konfiguration ###

In der Hauptklasse der Applikation (welche von [MjApplication](https://code.google.com/p/minimal-j/source/browse/Minimal-J/src/main/java/org/minimalj/application/MjApplication.java) ableiten muss) kann
die Method getResourceBundle überschrieben werden.

Wird die Method nicht überschrieben wird eine .properties Datei in dem gleichen
Package wie die Applikation erwartet.

Sollen verschiedene ResourceBundles angezogen werden bietet sich die
[MultiResourceBundle](https://code.google.com/p/minimal-j/source/browse/Minimal-J/src/main/java/org/minimalj/util/MultiResourceBundle.java) - Hilfsklasse an. Sie ermöglicht das Zusammenfassen von
verschiedenen ResourceBundle Instanzen.

## Felder in Formularen ##

Es wird auch hier versucht, möglichst wenig Redundanz in den Dateien zu halten
und die Keys der Texte so logisch wie möglich zu halten. Es gibt dabei
eine Rangordnung, in welcher passende Keys für ein Formular zum Zuge kommen.
Je spezifischer ein Key ist desto höher ist seine Priorität.

### Voll qualifizierter Key ###

Dieser Key hat Vorrang vor allen anderen passenden Keys. Als Beispiel soll
ein Feld für die Nationalität einer Person beschriftet werden:

```
ch.openech.model.Person.nationality = Nationalität
```

Man sieht, die Klasse Person ist vollständig qualifiziert, das heisst mit dem
ganzen package Pfad versehen.

### Klasse plus Feld ###

Im Normalfall sollte ein Klassenname in einer Applikation schon einzigartig
sein. In diesem Fall kann auf die Qualifikation über das Package verzichtet
werden:

```
Person.nationality = Nationalität
```

### Typ des Feldes ###

Bei nicht primitiven Feldern besteht meist schon ein anderer Key mit dem
gleichen Inhalt. In der nächsten Prioriät wird daher geschaut, ob ein Key
mit dem Typ des Feldes exisitert:

```
Nationality = Nationalität
```

Die Beschriftung für eine Feld des Typs Nationality wird nicht mehr
nur für die Person sondern für alle Verwendungen der Klasse Nationality definiert.

### Unqualifizierter Key ###

Haben mehrere Klassen Felder gleichen Namens und Typs kann auch hier auf eine
Wiederholung verzichtet werden. Einfach indem auf die Angabe der Klasse
komplett verzichtet wird:

```
nationality = Nationalität
```

In diesem Beispiel wäre die Verwendung des Typs jedoch angebrachter. Ein besseres
Beispiel für diese allgemeine Keys wäre ersten bei der Verwendung von primitiven
Typen (hier kann kein Key auf den Typ definiert werden):

```
age = Alter
```

Oder als zweites Beispiel, wenn mehrer Felder des gleichen Typs exisiteren:

```
comesFrom = Herkunftsort
goesTo = Wegzugsort
```

