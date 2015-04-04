# Requirements #

  * Java 8
  * Maven
  * Eclipse Luna (4.4)

## Start a project ##

  * create a new directory in your workspace
  * let maven create the directory structure for a new java project
```
mvn archetype:create -DgroupId=org.minimalj.example -DartifactId=first-project
```
  * I like to work with eclipse. If you prefer an other IDE feel free to use it. For eclipse let maven create the needed project files
```
cd first-project

mvn eclipse:eclipse
```
  * Now eclipse can import the project in it's workspace (File / Import... / Existing Projects into Workspace)
  * Next please open the pom.xml with eclipses 'Maven POM Editor'.
  * Add a dependency to minimalj. groupId is org.minimalj , artifactId minimalJ , version at least 0.1.0.0
  * In the pom.xml tab (last in row you see now the dependency)
```
	<dependency>
		<groupId>org.minimalj</groupId>
		<artifactId>minimalj</artifactId>
		<version>0.1.0.0</version>
	</dependency>
```
  * run again
```
mvn eclipse:eclipse
```
  * and refresh the project in eclipse
  * Go to the class created by maven: App.java . Replace it's content with
```
package org.minimalj.example;

import org.minimalj.application.Application;

public class App extends Application {

}
```

## Start the application ##
  * Go to "Run / Run configurations..."
  * Unfortunately eclipse doesn't detect the application. You have to create the configuration manually. Select 'Java Application' and click the 'Add' button on the top of the tree.
  * Enter the main class 'org.minimalj.example.App'
  * Click 'Apply' and 'Run'
  * The empty application should start as Swing application

## Then you could try ##
  * More examples stored in the [git example directory](https://code.google.com/p/minimal-j/source/browse/#git%2Fexamples)
  * Or the [OpenEch](http://code.google.com/p/open-ech/) application (the application minimal-j originated from)