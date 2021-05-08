Broker:

This is a client that receives key-value form data and redirects them to the number of servers it is connected to.

Build on:
- Java version 11.0.4
- Apache Maven 3.6.2

Installation:
    mvn clean
    mvn package
Run:  java -jar target/kvBroker-1.0-SNAPSHOT.jar -i {dataToIndexFile} -s {serverFileName} -k {int}

example:    java -jar target/kvBroker-1.0-SNAPSHOT.jar -i dataToIndex.txt -s serverFile.txt -k 2