# Metanome

[![Build Status](https://travis-ci.org/HPI-Information-Systems/Metanome.png?branch=master)](https://travis-ci.org/HPI-Information-Systems/Metanome)
[![Coverage Status](https://coveralls.io/repos/HPI-Information-Systems/Metanome/badge.png)](https://coveralls.io/r/HPI-Information-Systems/Metanome)

The [Metanome project](http://hpi.de/naumann/projects/data-profiling-and-analytics/metanome-data-profiling.html) is a joint project between the Hasso-Plattner-Institut ([HPI](http://www.hpi.de)) and the Qatar Computing Reserach Institute ([QCRI](http://www.qcri.org)). Metanome provides a fresh view on data profiling by developing and integrating efficient algorithms into a common tool, expanding on the functionality of data profiling, and addressing performance and scalabilities issues for Big Data. A vision of the project appears in SIGMOD Record: "[Data Profiling Revisited](http://hpi.de/naumann/publications/publications-by-type/year/2013/102276/Nau13.html)".

The Metanome tool is supplied under Apache License. You can use and extend the tool to develop your own profiling algorithms. The profiling algorithms contained in our downloadable Metanome build have HPI copyright. You are free to use and distribute them for research purposes. 

#### Building Metanome
Metanome is a maven project, which can be build by executing:

```mvn clean install```

Metanome can be packaged together with a jetty webserver and profiling algorithms. 
To speedup builds this package is not created in the default maven profile. 
The deployment package can be created by executing the build with the deployment profile: 

```mvn verify -P deployment```

or by executing package on the deployment project directly (if metanome has not been installed dependencies will be retrieved online): 

```mvn -f deployment/pom.xml package```

To start the Metanome frontend you then have to execute the following steps in the deployment folder:

1. Unzip `target/deployment-0.0.2-SNAPSHOT-package_with_jetty.zip`
2. Go into the unzipped folder and start the run script, either `run.sh` or `run.bat`
4. Open a browser at [http://localhost:8888/metanome/](http://localhost:8888/metanome/)

#### Downloads
Metanome releases can be found on the download page at:

https://hpi.de/naumann/sites/metanome/files

#### Documentation
The Metanome tool, information for algorithm developers and contributors to the project can be found in the [github wiki](https://github.com/HPI-Information-Systems/Metanome/wiki).

Javadocs for the project can be found at https://hpi.de/naumann/sites/metanome/javadoc.

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
##### Coding style
The project follows the google-styleguide please make sure that all contributions adhere to the correct format. Formatting settings for common ides can be found at: http://code.google.com/p/google-styleguide/
All files should contain the apache copyright header. The header can be found in the ```COPYRIGHT_HEADER``` file.

