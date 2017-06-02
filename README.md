# jmxdump-analyzer
The JMXDump Analyser Utility is a small, internal-only, self-contained JavaFX application which allows you to open a JMX dump file that a customer has sent through which will split the contents in manageable tabs. Highlighting the important stuff first and then breaking down the settings into the relevant topics, the utility makes JMX dumps easier to read

author @astrachan

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



