# jmxdump-analyzer
The JMXDump Analyser Utility is a small, internal-only, self-contained JavaFX application which allows you to open a JMX dump file that a customer has sent through which will split the contents in manageable tabs. Highlighting the important stuff first and then breaking down the settings into the relevant topics, the utility makes JMX dumps easier to read

author @astrachan

* Full documentation is [here](https://nexus.alfresco.com/nexus/content/repositories/alfresco-internal-docs/jmxdump-analyzer-fx/latest/index.html)
* Full change log report is [here](https://nexus.alfresco.com/nexus/content/repositories/alfresco-internal-docs/jmxdump-analyzer-fx/latest/changes-report.html)
* Latest SNAPSHOT versions are [here](https://artifacts.alfresco.com/nexus/content/repositories/alfresco-support-snapshots/org/alfresco/support/jmxdump-analyzer-fx/)

## Features
* Allows you to load in a JMXDump file from any version of Alfresco (including version 5.2 +)
* NEW Allows you to drag and drop a JMXDump text file directly into the application
* NEW Allows you to load in an Alfresco-generated JMXDump zip file
* Displays at-a-glance important information (Basic tab)
* Breaks down the settings into relevant sections (tabs) in fixed-width font for easier reading and copying/pasting
* Separates Global Properties from JMX-persisted settings (Global Properties are prepended with ** in the tabs)
* Analyses memory usage and availability
* Makes recommendations based on the data within the JMX Dump
* Gives a break-down of contentstore information (mount points and space)
* Gives a break-down of transactional cache statistics breakdown
* Shows SOLR/SOLR4 cache information if applicable
* Shows the upgrade path
* Search through both the original jmxdump text as well as through the displayed tab information
* Ability to change font in the UI

## Downloading, building and running the application
The name of the application is **jmxdump-analyzer-fx** and can be run on any platform (Windows, MacOS or Linux).
First you can clone this repository.

**Pre-requisites**
* Maven 3.3.9 or above
* Java 8

**Building**
`mvn clean install`

**Running the application**
`java -jar target\jmxdump-analyzer-fx-x.x.x-jar-with-dependencies`


