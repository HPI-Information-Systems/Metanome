# Metanome CLI

The main purpose of this project is to provide a command-line interface for [Metanome](https://github.com/HPI-Information-Systems/Metanome) to allow for easy automation of data profiling tasks, e.g., to conduct experiments or to profile datasets batchwise.

Furthermore, HDFS is supported as input source.

## Installation

The project can be built with Maven (from its parent directory):
```
.../Metanome$ mvn install -pl metanome-cli -am
```
This command creates a "fatjar" (`target/metanome-cli-1.2-SNAPSHOT.jar` or similar) that contains Metanome and the Metanome CLI along with all their dependencies (except for Metanome algorithms, though).

## Usage

Once you have obtained above described fatjar, you can simply put it on the Java classpath along with your algorithm jar files and run them as a normal Java application.
As an example, assume you have an algorithm jar file called `my-algorithm.jar` with the main algorithm class `com.example.MyAlgorithm`.
Then you can execute it via
```
$ java -cp metanome-cli-1.2-SNAPSHOT.jar:my-algorithm.jar de.metanome.cli.App --algorithm com.example.MyAlgorithm <parameters...>
```
In order to get an overview of the various parameters of the Metanome CLI, you may also execute it without any parameters (including `--algorithm`).

The amount and severity of logging output can be controlled via the system properties of [Tinylog](http://www.tinylog.org/configuration).
Most importantly if the algorithm does not behave as expected or you are trying to debug, enable all log messages by appending `-Dtinylog.level=trace` to the `java` invocation.
If log output is not releveant at all, turn Metanome CLI into quiet mode with `-Dtinylog.level=off`.


## Contributing

In case of problems, feel free to file an issue.
Preferably stick to the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html).
Formatter instructions for Eclipse and Intellij are available on [Github](https://github.com/google/styleguide).

## Original Version

This CLI was initially developed by [@sekruse](https://github.com/sekruse). You can find the original version [here](https://github.com/sekruse/metanome-cli).
