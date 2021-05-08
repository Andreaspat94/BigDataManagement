Key value Server:
This is a server running localhost, that receives data of key-value pair form, and stores them based on a 'trie' structure.

Build on:
- Java version 11.0.4
- Apache Maven 3.6.2

External libraries: 
-jackson annotations 2.12.2
-jackson core 2.12.2
-jackson databind 2.12.2

Installation:
     mvn clean 
     mvn package 
Run:
    java -jar target/kvServer-1.0-SNAPSHOT.jar -p {port}

example: java -jar target/kvServer-1.0-SNAPSHOT.jar -p 5000

