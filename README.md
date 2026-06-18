# MoMTSim-Library

MoMTSim-Library is an open-source Java software package for generating synthetic mobile money transaction datasets. The software transforms an
agent-based simulation model from a standalone simulation application into a reusable library and executable research tool.

## The Agent-Based Simulation Model

![](./model.png?raw=true)

## Requirements

A JDK environment is needed to start using/contributing to this project.

- Get and [install](https://adoptopenjdk.net) a recent JDK 11 instance for your platform
- Clone this repository
- Install a copy of [Apache Maven](https://maven.apache.org/download.cgi) for your operating system
- That's all you need

## Use as a Library

MoMTSim can be embedded in another Java application through the `org.momtsim.MoMTSim` facade. The regular JAR exposes the simulator API and keeps dependencies transitive through Maven. Install it locally with `mvn install`, or publish it to a Maven repository, then depend on it from another project:

```xml
<dependency>
  <groupId>org.momtsim</groupId>
  <artifactId>momtsim</artifactId>
  <version>2.7.1</version>
</dependency>
```

Run a simulation from a properties file and process transactions as a stream:

```java
import org.momtsim.MoMTSim;
import org.momtsim.base.Transaction;

import java.util.stream.Stream;

try (Stream<Transaction> transactions = MoMTSim.fromProperties("MoMTSim.properties").stream()) {
    transactions.forEach(transaction -> {
        // Persist, aggregate, or inspect each transaction here.
    });
}
```

Collecting transactions is also available for smaller simulations:

```java
List<Transaction> transactions = MoMTSim.fromDefaultProperties().runToList();
```

The default `MoMTSim.properties` and `paramFiles/` inputs are packaged as classpath resources, so `fromDefaultProperties()` works when MoMTSim is consumed from its JAR. The standalone application still works as before:

```bash
mvn package
java -jar target/momtsim-2.7.1-all.jar -file MoMTSim.properties 1
```
