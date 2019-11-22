/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alfresco.support.expert.jmxdumpanalyzerfx;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.HashSet;

import java.util.ResourceBundle;
import java.util.Set;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

/**
 * FXML Controller class
 *
 * @author astrachan
 */
public class JmxRecController implements Initializable {

   @FXML public TextArea txtRec;
   
   private static Long totalRAM;
   private static Long totalHeap;
   private static Float percentage;
   private static String maxMemory = "";
   
;
 
 
  private static Set<String> sysProps;
  
   String jmxFilePath = JmxMainController.filePath;
   Set<String> osProps = JmxMainController.setOSProps;
   Set<String> globalProps = JmxMainController.setGlobalProps;
   private static Set<String> setJVMProps = JmxMainController.setJVMProps;
   
   
        
  private void start() throws IOException {

      doWork();
  }      
        


    public void initialize(URL url, ResourceBundle rb) {
       try {
           start();
       } catch (IOException ex) {
           
       }
    }

	private void doWork() throws IOException{
                sysProps = new HashSet<String>();
                maxMemory = JmxMainController.maxMem;
                sysProps = JmxMainController.setSysProps;
                
		populateCalcInfo("JVM ARGUMENTS");
		memRecs();
		populateCalcInfo("");
		dbRecs();
		systemRecs();
}
	
 private void memRecs(){

	 	 boolean disableExplicitGCPresent = false;
	 	 boolean allowUnsafeRenegotiation = false;
	 	 boolean preferIPv4Stack = false;

	 	 if (!setJVMProps.isEmpty()){
		for (String item : setJVMProps) {
			if (item.contains("-XX:+DisableExplicitGC")){
				disableExplicitGCPresent = true;
			}
			if (item.contains("-Dsun.security.ssl.allowUnsafeRenegotiation=true")){
				allowUnsafeRenegotiation = true;
			}
			if (item.contains("-Djava.net.preferIPv4Stack=true")){
				preferIPv4Stack = true;
			}
		}
		if (!allowUnsafeRenegotiation){
			populateCalcInfo("Setting -Dsun.security.ssl.allowUnsafeRenegotiation=true is advised (see http://docs.alfresco.com/4.2/concepts/solr-troubleshooting.html)");
		}
		if (!disableExplicitGCPresent){
			populateCalcInfo("-XX:+DisableExplicitGC should really be set (see MNT-6296)");
		}
		if (!preferIPv4Stack){
			populateCalcInfo("Setting -Djava.net.preferIPv4Stack=true is advised  if there are problems with communications in cluster nodes or between SOLR & Alfresco");
		}
		
		// isolate and report on total RAM against maximum heap
		if (!osProps.isEmpty() || osProps != null){
			for (String osElement : osProps){
				if (osElement.contains("Object")){
					// do nothing
				}
				else {
					String[] tokens = osElement.split("\\s{2,}");
							if (tokens.length != 0){
								if (tokens[0].startsWith("TotalPhysicalMemorySize")){
									totalRAM = Long.parseLong(tokens[1]);
								}
							}
				}
			}
		}
	
		if (!maxMemory.equals("")){
			String[] memTokens = maxMemory.split("\\s{2,}");
			totalHeap = Long.parseLong(memTokens[1]);
			percentage = (float) ((totalHeap*100)/totalRAM);
			populateCalcInfo("");
			populateCalcInfo("HEAP/TOTAL MEMORY");
			populateCalcInfo("Maximum heap of " + totalHeap + " bytes is " + percentage +"% of the total system memory of " + totalRAM + " bytes");
		}
	 }
	 else { populateCalcInfo("No JVM or memory settings to report on");
    }

 }
 
 private void dbRecs() throws IOException{
	 
	 String port = "";
	 Boolean tomcat = false;
	 
	String line10; // app server
	BufferedReader br10 = new BufferedReader(new FileReader(jmxFilePath));
	
	String line11; // maxThreads
	BufferedReader br11 = new BufferedReader(new FileReader(jmxFilePath));
	
	String line12; // database stuff
	BufferedReader br12 = new BufferedReader(new FileReader(jmxFilePath));
	
	 // parse the global properties
	populateCalcInfo("DATABASE/THREADS");
	for (String element : globalProps){
		if (element.startsWith("db.pool.max")){
			String[] tokens = element.split("\\s{2,}");
			if (tokens.length != 0){
				populateCalcInfo("Maximum database pool size (db.pool.max):  " + tokens[1]);
			}
		}
	}
	
	// figure out the app server (currently only tomcat is supported)
	while (( line10 = br10.readLine()) != null) {
	    if (line10.startsWith("** Object Name  Catalina:type=Server")){
	    	while (!line10.isEmpty()){
	    		if (line10.startsWith("serverInfo")){
	    			if (line10.substring(15).toLowerCase().contains("tomcat")){
	    				tomcat = true;
	    				populateCalcInfo("Application server: " +line10.substring(15));
	    			}
	    		}
	    		line10 = br10.readLine();
	    	}
	     }
	   }
	// populate a String set with the OS properties for the repo
	
	if (tomcat){
	while (( line11 = br11.readLine()) != null) {
    if (line11.startsWith("** Object Name") && line11.contains("Catalina:type=ProtocolHandler,port=")){
    	port =  line11.substring((line11.lastIndexOf("=") + 1));
    	if (!!port.isEmpty() || port != null) {
    		populateCalcInfo("Port " + port + ":");
    	}
    	while (!line11.contains("]") && !line11.isEmpty()){
    		if (line11.startsWith("maxThreads") || line11.startsWith("connectionCount")){
    			populateCalcInfo(line11);
    		}
    		line11 = br11.readLine();
    	   }
        }
      }
	}
	else { populateCalcInfo("Application server: unknown (possibly JBoss or Websphere) - check the maximum threads for the application server\nand check against the db.pool.max setting"); }
	if (tomcat){
		populateCalcInfo("Check the maxThreads  against the db.pool.max setting");
	}
	
	populateCalcInfo("");
	while (( line12 = br12.readLine()) != null) {
	    		if (line12.startsWith("DatabaseProductName")){
	    				populateCalcInfo(line12);
	    			}
	    		if (line12.startsWith("DatabaseProductVersion")){
    				populateCalcInfo(line12);
    			}
	    		if (line12.startsWith("DriverName")){
    				populateCalcInfo(line12);
    			}
	    		if (line12.startsWith("DriverVersion")){
    				populateCalcInfo(line12);
    			}
	    		if (line12.startsWith("URL ")){
    				populateCalcInfo(line12);
    			}
	    	}
	    	line12 = br12.readLine();
	    	
			populateCalcInfo("Ensure that the above database server/driver versions are supported.\nSee http://www.alfresco.com/services/subscription/supported-platforms");
	
	br10.close();
	br11.close();
	br12.close();
}

 private void systemRecs() throws IOException{
	 String osName = "";
	 String osVersion = "";
	 String vmName = "";
	 String vmVersion = "";
	 String vmVendor = "";
	 
	 populateCalcInfo("");
	 populateCalcInfo("SYSTEM/JDK");
		for (String sysProp : sysProps){
			if (sysProp.startsWith("os.name")){
				osName = sysProp.substring(sysProp.lastIndexOf("  "));
			}
			if (sysProp.startsWith("os.version")){
				osVersion = sysProp.substring(sysProp.lastIndexOf("  "));
			}
			if (sysProp.startsWith("java.version")){
				vmVersion = sysProp.substring(sysProp.lastIndexOf("  "));
			}
			if (sysProp.startsWith("java.vm.name")){
				vmName = sysProp.substring(sysProp.lastIndexOf("  "));
			}
			if (sysProp.startsWith("java.vm.vendor")){
				vmVendor = sysProp.substring(sysProp.lastIndexOf("  "));
			}
		}
		populateCalcInfo("Operating System: " + osName + " (" + osVersion + ")");
		populateCalcInfo("JDK version: " + vmVersion + " (" + vmName + " | " + vmVendor + ")");
		populateCalcInfo("Ensure that the above JDK and operating system versions are supported.\nSee http://www.alfresco.com/services/subscription/supported-platforms");
 }
    
    


public void populateCalcInfo(String msg){

    txtRec.appendText(msg + "\r\n");
	}
    
public static double round(double value, int places) {
    if (places < 0) throw new IllegalArgumentException();

    BigDecimal bd = new BigDecimal(value);
    bd = bd.setScale(places, RoundingMode.HALF_UP);
    return bd.doubleValue();
    }
}

