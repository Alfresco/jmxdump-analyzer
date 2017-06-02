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
import java.util.Iterator;
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
public class JmxAppliedToServerController implements Initializable {

   @FXML public TextArea txtAppliedVersions;

   String jmxFilePath = JmxMainController.filePath;
   public static Set<String> setAppliedVersions = new HashSet<String>();
   
        
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
		String line; // applied version info
	
		BufferedReader br = new BufferedReader(new FileReader(jmxFilePath)); 

		populateVersionInfo("APPLIED VERSIONS (upgrade path)");
		populateVersionInfo("");
	
		if (!JmxMainController.setAppliedVersions.isEmpty() || JmxMainController.setAppliedVersions != null) {
		Iterator iter = JmxMainController.setAppliedVersions.iterator();
		while (iter.hasNext()) {
			populateVersionInfo(iter.next().toString());
		}
	} else {
		populateVersionInfo("No 'appliedToServer' information found");
	}
}
    

public void populateVersionInfo(String msg){

	txtAppliedVersions.appendText(msg + "\r\n");
	}
    
}

