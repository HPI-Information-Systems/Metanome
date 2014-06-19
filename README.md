# Metanome

[![Build Status](https://travis-ci.org/HPI-Information-Systems/Metanome.png?branch=master)](https://travis-ci.org/HPI-Information-Systems/Metanome)
[![Coverage Status](https://coveralls.io/repos/HPI-Information-Systems/Metanome/badge.png)](https://coveralls.io/r/HPI-Information-Systems/Metanome)

The [Metanome project](http://www.hpi.uni-potsdam.de/naumann/projekte/metanome_data_profiling.html) is a joint project between the Hasso-Plattner-Institut ([HPI](http://www.hpi.uni-potsdam.de/willkommen.html?L=1)) and the Qatar Computing Reserach Institute ([QCRI](http://www.qcri.org)). Metanome provides a fresh view on data profiling by developing and integrating efficient algorithms into a common tool, expanding on the functionality of data profiling, and addressing performance and scalabilities issues for Big Data. A vision of the project appears in SIGMOD Record: "[Data Profiling Revisited](http://www.hpi.uni-potsdam.de/naumann/publications/publications_by_type/year/2013/2276/Nau13.html)".

#### Building Metanome
Metanome is a maven project, which can be build by executing:
```mvn verify```.
The verify phase should be executed as GWTTests are executed in this phase of the build.

Metanome can be packaged together with a jetty webserver and profiling algorithms. 
To speedup builds this package is not created in the default maven profile. 
The deployment package can be created by executing the build with the deployment profile: 
```
mvn verify -P deployment
```
or by executing package on the deployment project directly (if metanome has not been installed dependencies will be retrieved online): 
```
mvn -f deployment/pom.xml package
```

#### Downloads
Metanome releases can be found on the download page at:

https://www.hpi.uni-potsdam.de/naumann/sites/metanome/files/

#### Development
The Metanome modules are continously deployed to sonatype and can be used by adding the repository:
```xml
<repositories>
    <repository>
        <id>snapshots-repo</id>
        <url>https://oss.sonatype.org/content/repositories/snapshots</url>
    </repository>
</repositories>
```

