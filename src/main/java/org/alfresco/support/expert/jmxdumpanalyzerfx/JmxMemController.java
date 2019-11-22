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
public class JmxMemController implements Initializable {

   @FXML public TextArea txtMem;
    
   String jmxFilePath = JmxMainController.filePath;
   Set<String> osProps = JmxMainController.setOSProps;
   private static Set<String> heapUsage;
   private static Set<String> nonHeapUsage;
        
  private void start() throws IOException {
      doWork();
  }      
        


    public void initialize(URL url, ResourceBundle rb) {
       try {
           start();
       } catch (IOException ex) {
           
       }
    }

@SuppressWarnings("restriction")
private void doWork() throws IOException{
                 //txtMem.appendText("hello");
                 heapUsage = new HashSet<String>();
                 nonHeapUsage  = new HashSet<String>();
                 
		String line; // heap usage
		BufferedReader br = new BufferedReader(new FileReader(jmxFilePath)); // heap usage
		String line1; // non-heap usage
		BufferedReader br1 = new BufferedReader(new FileReader(jmxFilePath)); // non-heap usage
		String line2; // full GC
		BufferedReader br2 = new BufferedReader(new FileReader(jmxFilePath)); // full GC
		String line3; // young GC
		BufferedReader br3 = new BufferedReader(new FileReader(jmxFilePath)); // young GC

		populateCalcInfo("MEMORY (system and heap)");	
		populateCalcInfo("");
		/* SYSTEM MEMORY -- using existing setOSProps set */
		if (!osProps.isEmpty() || osProps != null){
		for (String osElement : osProps){
			if (osElement.contains("Object")){
				// do nothing
			}
			else {
				String[] tokens = osElement.split("\\s{2,}");
						if (tokens.length != 0){
							if (tokens[0].startsWith("TotalPhysicalMemorySize")){
								Double gbVal = (((Double.parseDouble(tokens[1])/1024)/1024)/1024);
								populateCalcInfo("Total system memory: 	" + tokens[1] + " bytes  / " + round(gbVal, 2)  + "GB");
							}
							if (tokens[0].startsWith("FreePhysicalMemorySize")){
								Double gbVal = (((Double.parseDouble(tokens[1])/1024)/1024)/1024);
								populateCalcInfo("Free system memory: 	" + tokens[1] + " bytes  / " + round(gbVal, 2)  + "GB");
							}
							if (tokens[0].startsWith("TotalSwapSpaceSize")){
								Double gbVal = (((Double.parseDouble(tokens[1])/1024)/1024)/1024);
								populateCalcInfo("Total swap space: 	" + tokens[1] + " bytes  / " + round(gbVal, 2)  + "GB");
							}
							if (tokens[0].startsWith("FreeSwapSpaceSize")){
								Double gbVal = (((Double.parseDouble(tokens[1])/1024)/1024)/1024);
								populateCalcInfo("Free swap space: 	" + tokens[1] + " bytes  / " + round(gbVal, 2)  + "GB");
							}
							else { /* do nothing here */ }
					}
			}
		 }
		}
		else {populateCalcInfo("-- nothing to report for system memory"); }
		populateCalcInfo("");
		
		populateCalcInfo("Heap Usage:");
		while (( line = br.readLine()) != null) {
		    if ((line.contains("HeapMemoryUsage") && (!line.contains("NonHeapMemoryUsage")))){
		    	while ((!line.contains("]") && !line.isEmpty()) || line.isEmpty() ){
		    		if (line.contains("----") || line.contains("[") || line.isEmpty() || line.equals("") || line.contains("HeapMemoryUsage") || line.contains("Attribute")){
		    			// do nothing
		    		}
		    		else {
		    		heapUsage.add(line.trim());
		    	}
		    		line = br.readLine();
		    	}
		     }
		   }
			for (String id : heapUsage){
				
					String[] tokens = id.split("\\s{2,}");
					Double gbVal = (((Double.parseDouble(tokens[1])/1024)/1024)/1024);
					
					if (tokens[0].contains("committed")) {
						populateCalcInfo("comm:			" + tokens[1] + " bytes  / " + round(gbVal, 2)  + "GB");
					}
					else {
						populateCalcInfo(tokens[0] + ":			" + tokens[1] + " bytes  / " + round(gbVal, 2)  + "GB");
					}
		}
		populateCalcInfo("");
		
		populateCalcInfo("Non-heap Usage:");	
		while (( line1 = br1.readLine()) != null) {
		    if ((line1.contains("NonHeapMemoryUsage"))){
		    	while ((!line1.contains("]") && !line1.isEmpty()) || line1.isEmpty() ){
		    		if (line1.contains("----") || line1.contains("[") || line1.isEmpty() || line1.equals("") || line1.contains("HeapMemoryUsage")  || line1.contains("Attribute")){
		    			// do nothing
		    		}
		    		else {
		    			nonHeapUsage.add(line1.trim());
		    	}
		    		line1 = br1.readLine();
		    	}
		     }
		   }
			for (String id : nonHeapUsage){
					String[] tokens = id.split("\\s{2,}");
					Double kbVal = (((Double.parseDouble(tokens[1])/1024)/1024));
					
					if (tokens[0].contains("committed")) {
						populateCalcInfo("comm:			" + tokens[1] + " bytes  / " + round(kbVal, 2)  + "KB");
					}
					else {
						populateCalcInfo(tokens[0] + ":			" + tokens[1] + " bytes  / " + round(kbVal, 2)  + "KB");
					}
					
		}
		populateCalcInfo("");
		/* ------ END HEAP USAGE ------- */

		/* GC INFO */
		populateCalcInfo("GARBAGE COLLECTION");
		populateCalcInfo("");
		while (( line2 = br2.readLine()) != null) {
		    if (line2.startsWith("** Object Name  java.lang:type=GarbageCollector,name=")){
		    	populateCalcInfo("GC Type: " + line2.substring(53));
		    	while (!line2.isEmpty()){
		    		if (line2.contains("CollectionCount")){
		    			populateCalcInfo(line2);
		    		}
		    		if (line2.contains("CollectionTime")){
		    			populateCalcInfo(line2 + " milliseconds");
		    			populateCalcInfo("");  // line break
		    		}
		    		line2 = br2.readLine();
		    	}
		    }
		   }
                Set<String> osProps = null;
	br.close();
	br1.close();
	br2.close();
	br3.close();
}


public void populateCalcInfo(String msg){
    //System.out.println(msg);
    
    txtMem.appendText(msg + "\r\n");
	}
    
public static double round(double value, int places) {
    if (places < 0) throw new IllegalArgumentException();

    BigDecimal bd = new BigDecimal(value);
    bd = bd.setScale(places, RoundingMode.HALF_UP);
    return bd.doubleValue();
    }
}

