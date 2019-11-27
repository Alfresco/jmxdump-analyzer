package org.alfresco.support.expert.jmxdumpanalyzerfx;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Class used to represent data contained in a JMXDumpFile
 */
public class JMXDumpFile {
    private List<String> jmxFileAllLines = new ArrayList<>();
    private List<String> allGlobalProps = new ArrayList<>();
    private List<String> allObjectNames = new ArrayList<>();

    /**
     * Backwards compatible constructor that takes a File.
     * 
     * java.io.{@link File} can be passed directly from javafx.FileChooser
     * 
     * @param jmxFile This File object is opened and read
     * @throws IOException if an I/O error occurs reading from the file or a malformed or unmappable byte sequence is read
     */
    public JMXDumpFile(File jmxFile) throws IOException {

            jmxFileAllLines = getAllLines(jmxFile);
            allGlobalProps = getAllGlobalProps();
            allObjectNames = parseObjectNames();
    }

    private List<String> parseObjectNames() {
        List<String> result = new ArrayList<>();
        for (String line : jmxFileAllLines) {
            if (line.contains("** Object Name")) {
                result.add(line);
            }
        }
        return result;
    }

    /**
     * Helper for building list of lines from JMXDump file
     * 
     * @param jmxFile java.io.file which represents the JMXDump file
     * @return List of strings that are the contents of the file
     * @throws IOException if an I/O error occurs reading from the file or a malformed or unmappable byte sequence is read
     */
    private List<String> getAllLines(File jmxFile) throws IOException{
        return Files.readAllLines(jmxFile.toPath());
    }

    public String toString(){

        StringBuilder result = new StringBuilder();

        result.append("Number of lines added to all lines: " + jmxFileAllLines.size() + "\n");
        result.append("Number of lines added to all Global Props: " + allGlobalProps.size() + "\n");
        result.append("Number of lines added to all Object Names: " + allObjectNames.size() + "\n");
        return result.toString();

    }

    /**
     * Helper method when given a List<String> representing lines in a JMX Dump file,
     * Pulls out the properties found between the strings:
     * 
     * "Alfresco:Name=GlobalProperties"
     * ""
     * 
     * @return A list of string representing Global Properties. Empty list if none are found.
     * 
     */
    private List<String> getAllGlobalProps(){
        List<String> results = new ArrayList<>();
        Boolean startFound = false;

        for (String line : jmxFileAllLines){
            if (!startFound && line.contains("Alfresco:Name=GlobalProperties")) {
                startFound = true;
                results.add(line);
                continue;
            }
            if (startFound) {
                results.add(line);
            }
            if (startFound && line.equals("")) break;
        }
        return results;
    }
}