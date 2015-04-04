# Minimal-J #

Minimal-J hat den Anspruch bei einer Applikation den Geschäftsablauf, die Bedienung und die Implementierung so einfach und übersichtlich wie möglich zu machen.

So ist Minimal-J weit mehr als ein paar Programmier-Konzepte. Schlussendlich ist Minimal-J eine Denkweise für Analyse, Design, Implementierung und Testing.

## Download ##

Die aktuelle Version findet sich jeweils im maven repository oder etwas früher auf:

https://oss.sonatype.org/#nexus-search;quick~minimalj

Einbindung in pom.xml:

```
<dependency>
	<groupId>org.minimalj</groupId>
	<artifactId>minimalj</artifactId>
	<version>0.3.0.1</version>
</dependency>
```

## Getting started ##

Eine Setup - Anleitung für eigene Projekte findet sich unter https://code.google.com/p/minimal-j/wiki/SetupMinimalJ

## Beispiel ##

Als Beispiel hier die Modellierung in Minimal-J:

```
public class Customer {	
	public static final Customer $ = Keys.of(Customer.class);
	
	public Object id;
	
	@Size(32) @Searched
	public String customerNr;

	@Size(50) @Searched
	public String firstname, surname;
	
	@Size(50)
	public String company;

	public Title title;
	public Salutation salutation;
	
	@Size(50)
	public String email;

	@Size(50)
	public String address, zip, city;

	public LocalDate customersince;
}
```


GUIs folgen einem strikten Layout:

![https://minimal-j.googlecode.com/git/doc/code.google/mj_demo_add_customer.png](https://minimal-j.googlecode.com/git/doc/code.google/mj_demo_add_customer.png)

oder als Web (Vaadin):

![https://minimal-j.googlecode.com/git/doc/code.google/mj_web_demo_add_customer.png](https://minimal-j.googlecode.com/git/doc/code.google/mj_web_demo_add_customer.png)

Die Grundlage ist für beide Frontends die gleiche:

```
public class CustomerForm extends Form<Customer> {

	public CustomerForm(boolean editable) {
		super(editable, 2);
		
		line($.customerNr);
		line($.salutation, $.title);
		line($.firstname);
		line($.surname);
		line($.email);
		line($.company);
		line($.address);
		line($.zip, $.city);
		line($.customersince);
	}
}
```
