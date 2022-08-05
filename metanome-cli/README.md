# Metanome CLI

The main purpose of this project is to provide a command-line interface for [Metanome](https://github.com/HPI-Information-Systems/Metanome) to allow for easy automation of data profiling tasks, e.g., to conduct experiments or to profile datasets batchwise.
Besides that, this project integrates Metanome with
* [Metacrate](https://github.com/stratosphere/metadata-ms), a storage and analytics tool for data profiles, and
* [ProfileDB](https://github.com/sekruse/profiledb-java), a tiny tool to collect and store experimental data.

Furthermore, HDFS is supported as input source.

## Installation

Either obtain a Metanome CLI [release](https://github.com/sekruse/metanome-cli/releases) or build it yourself.
Conceretely, the project can be built with Maven:
```
.../metanome-cli$ mvn package -Pdistro
```
This command creates a "fatjar" (`target/metanome-cli-0.1-SNAPSHOT.jar` or similar) that contains Metanome and the Metanome CLI along with all their dependencies (except for Metanome algorithms, though).

Note that this project might depend on unstable snapshot versions of [Metanome](https://github.com/HPI-Information-Systems/Metanome), [Metacrate](https://github.com/stratosphere/metadata-ms), and [ProfileDB](https://github.com/sekruse/profiledb-java).
In case of build errors related to these projects, you might need to clone, build, and install (i.e., `mvn install`) them yourself.
Then, re-run the build with
```
.../metanome-cli$ mvn package -Pdistro --offline
```

## Usage

Once you have obtained above described fatjar, you can simply put it on the Java classpath along with your algorithm jar files and run them as a normal Java application.
As an example, assume you have an algorithm jar file called `my-algorithm.jar` with the main algorithm class `com.example.MyAlgorithm`.
Then you can execute it via
```
$ java -cp metanome-cli.jar:my-algorithm.jar de.metanome.cli.App --algorithm com.example.MyAlgorithm <parameters...>
```
In order to get an overview of the various parameters of the Metanome CLI, you may also execute it without any parameters (including `--algorithm`).

The amount and severity of logging output can be controlled via the system properties of [Tinylog](http://www.tinylog.org/configuration).
Most importantly if the algorithm does not behave as expected or you are trying to debug, enable all log messages by appending `-Dtinylog.level=trace` to the `java` invocation.
If log output is not releveant at all, turn Metanome CLI into quiet mode with `-Dtinylog.level=off`.


## Contributing

In case of problems, feel free to file an issue.
Preferably stick to the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html).
Formatter instructions for Eclipse and Intellij are available on [Github](https://github.com/google/styleguide).
