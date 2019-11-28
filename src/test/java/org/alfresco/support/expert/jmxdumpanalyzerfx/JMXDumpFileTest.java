package org.alfresco.support.expert.jmxdumpanalyzerfx;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class JMXDumpFileTest 
    extends TestCase
{

    String testFilePath = "src/test/resources/unzippedJMXDumpfile55.txt";
    JMXDumpFile testFile;

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public JMXDumpFileTest( String testName )
    {
        super( testName );
        try {
            testFile = new JMXDumpFile(new File(testFilePath));
        }
        catch (IOException e) {
            // fail the test run
            assertFalse(e.toString(), true);
        }
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( JMXDumpFileTest.class );
    }

    /*************************************
     *  Variable initialisation tests    *
     *************************************/
    public void testAllLinesCount()
    {
        long lineCount;
        //long lineCountGlobalNames = -1;

        try (Stream<String> stream = Files.lines(Paths.get(testFilePath))){
            lineCount = stream.count();
            //lineCountGlobalNames = stream.filter(line -> line.contains("** Global Name")).count();
        } catch (Exception e) {
            // force test failure
            lineCount = -1; 
            //lineCountGlobalNames = -1;
        }

        assertEquals("Testing how many file lines have been read: ", lineCount, testFile.getAllLinesSize());
        //assertEquals("Testing how many Global Name lines have been read", lineCountGlobalNames, testFile.getAllObjectNamesSize());
    }
    public void testObjectNamesCount()
    {
        long lineCountObjectNames;

        try (Stream<String> stream = Files.lines(Paths.get(testFilePath))){
            lineCountObjectNames = stream.filter(line -> line.contains("** Object Name")).count();
        } catch (Exception e) {
            // force test failure
            lineCountObjectNames = -1;
        }

        assertEquals("Testing how many Global Name lines have been read", lineCountObjectNames, testFile.getAllObjectNamesSize());
    }
}