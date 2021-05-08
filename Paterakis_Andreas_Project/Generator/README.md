Generator:

This is a data generator that creates a .txt file, which contains the data in the appropriate format

Build with:
- Java version 11.0.4
- Apache Maven 3.6.2

Installation:
mvn clean
mvn package

Run:  java -jar target/Generator-1.0-SNAPSHOT.jar -k keyFile.txt -n {numberOfLines} -d {levelOfNesting} -l {maximumStringLength} -m {maximumNumberOfKeys}

example:    java -jar target/Generator-1.0-SNAPSHOT.jar -k keyFile.txt -n 50 -d 3 -l 4 -m 5

All parameters should be included in the command with their values