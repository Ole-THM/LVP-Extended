#!/bin/bash
mvn clean package
java -jar ./src/main/java/lvp-0.5.4.jar --log --watch=./src/main/java/start.java