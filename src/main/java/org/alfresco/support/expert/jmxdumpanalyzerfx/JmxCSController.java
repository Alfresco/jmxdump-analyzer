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
public class JmxCSController implements Initializable {

   @FXML public TextArea txtCS;
    
   String jmxFilePath = JmxMainController.filePath;
   
        
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
		String line; // contentstore
		BufferedReader br = new BufferedReader(new FileReader(jmxFilePath)); 

		populateCalcInfo("CONTENTSTORE (size information)");
		populateCalcInfo("");
		
		// ContentStore info
		String[] tokens;
		String[] tokens1;
		
		while (( line = br.readLine()) != null) {
		    if (line.contains("Alfresco:Name=ContentStore,Type=")){
		    	populateCalcInfo("Path:		" + line.substring(line.lastIndexOf("Root=") + 5));
		    	while (!line.contains("]") && !line.isEmpty()){
		    		if (line.contains("SpaceFree")){
		    			tokens = line.split("\\s+");
		    			Double gbVal = (((Double.parseDouble(tokens[1])/1024)/1024)/1024);
						populateCalcInfo("SpaceFree:	" + tokens[1] + " bytes  / " + round(gbVal, 2)  + "GB");
		    		}		    		
		    		if (line.contains("SpaceTotal")){
		    			tokens1 = line.split("\\s+");
		    			Double gbVal = (((Double.parseDouble(tokens1[1])/1024)/1024)/1024);
						populateCalcInfo("SpaceTotal:	" + tokens1[1] + " bytes  / " + round(gbVal, 2)  + "GB");
		    		}
		    		line = br.readLine();
		    	}
		    	populateCalcInfo("");
		    }
		   }
	
} 
    
    


public void populateCalcInfo(String msg){

    txtCS.appendText(msg + "\r\n");
	}
    
public static double round(double value, int places) {
    if (places < 0) throw new IllegalArgumentException();

    BigDecimal bd = new BigDecimal(value);
    bd = bd.setScale(places, RoundingMode.HALF_UP);
    return bd.doubleValue();
    }
}

