/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alfresco.support.expert.jmxdumpanalyzerfx;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

/**
 * FXML Controller class
 *
 * @author astrachan
 */
public class JmxCacheStatsController implements Initializable {

   @FXML public TextArea txtCacheStats;
   private static int count = 0;
    
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
		String line; // cache info
		count = 0;
				
		BufferedReader br = new BufferedReader(new FileReader(jmxFilePath)); 

		populateCalcInfo("CACHE STATISTICS (CacheStatisticsMBeanImpl)");
		populateCalcInfo("");
	
		
		while (( line = br.readLine()) != null) {
		    if (line.contains("Object Name Alfresco:Name=CacheStatistics")){
		    	count ++;
		    	populateCalcInfo("Cache:		" + line.substring(line.lastIndexOf("CacheName=") + 10));
		    	while (!line.contains("]") && !line.isEmpty()){
		    		if (line.contains("Object Type org.alfresco.enterprise.repo.management.CacheStatisticsMBeanImpl") || line.contains("Object Name Alfresco:Name=CacheStatistics,CacheName=")){
		    			// do nothing
		    		}
		    		else{
		    			populateCalcInfo(line);
		    		}
		    		line = br.readLine();
		    	}
		    	populateCalcInfo("");
		    }
		   }
		populateCalcInfo("Total cache count: " + count);
}
    

public void populateCalcInfo(String msg){

    txtCacheStats.appendText(msg + "\r\n");
	}
    
}

