# MoMTSim Project

MoMTSim is a multi-agent simulation platform tailored to mobile money financial transactions from Sub-Saharan Africa (SSA) for generating synthetic financial datasets in the domain.
Unlike related studies, we collected qualitative data from stakeholders such as Telco IT officers, Banks, Agents, Clients and Merchants to aid modelling. 
We also obtained a sample real mobile money transactions data from one of our stakeholders in the region in order to extract statistical information for the simulations.

## The MoMTSim Simulation Model

![](./model.png?raw=true)

## Design Considerations for the Simulation Platform
Our paper: **Scenario-based Synthetic Dataset Generation for Mobile Money Transactions** details the design considerations for the simulation platform and unique fraud scenarios in the Sub-saharan Context. Access here: 
[https://doi.org/10.1145/3531056.3542774](https://doi.org/10.1145/3531056.3542774)

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
