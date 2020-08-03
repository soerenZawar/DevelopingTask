#Westernacher Solution Test Task

##
uploaded is the document on github

## System environment
In this project Spring Data and Spring Boot is used.So its important to generate Spring initializr 
(https://start.spring.io/) with the Spring Web Dependency. 
Also Maven and IntelliJ with the Java version 8 is used for creating this project.

##application.yml
This file is needed for using the embedded database management system H2 Database. This is used to control the database 
changes
The JDBC URL is noted in the field url.
The driver Class is named in 'driverClassName'
h2 -> console -> enabled true is used to enable the console on which its possible to follow the database steps with sql
queries

##pom.xml
contains the dependencies which are used for the project.

## important classes
The WesternacherApplication class starts the Spring Boot application. I tested the RESTApi additional manually with 
Postman