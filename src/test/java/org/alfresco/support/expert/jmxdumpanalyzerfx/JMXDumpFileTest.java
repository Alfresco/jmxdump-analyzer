package org.alfresco.support.expert.jmxdumpanalyzerfx;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class JMXDumpFileTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public JMXDumpFileTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( JMXDumpFileTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }
}