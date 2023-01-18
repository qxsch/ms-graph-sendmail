# Run the java demo

## Setup
Make sure, that:
 * you did follow the [setup instructions](../README.md)
 * you updated the [application.properties](src/main/resources/application.properties) file

# Run the Demo
Run the following command in this folder:
```bash
mvn exec:java -f ".\pom.xml" "-Dexec.mainClass=ch.marcoweber.DemoApplication" "-Dexec.cleanupDaemonThreads=false"
```