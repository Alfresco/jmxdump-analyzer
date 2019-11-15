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
public class JmxSOLRController implements Initializable {

   @FXML public TextArea txtSOLR;
    
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
		BufferedReader br = new BufferedReader(new FileReader(jmxFilePath)); 

		populateCalcInfo("SOLR/SOLR4 caches");
		populateCalcInfo("");
	
		
		// starting with the main Core 'alfresco'
		while (( line = br.readLine()) != null) {
			
		    if (line.contains("solr/alfresco:type=Searcher@")){
		    	populateCalcInfo("Alfresco core | Searcher");
		    	while (!line.isEmpty()){
		    		if (line.contains("maxDoc") || line.contains("numDocs") || line.contains("searcherName") || line.contains("warmupTime")){
		    			populateCalcInfo(line);
		    		}
		    		line = br.readLine();
		    	}
		    	populateCalcInfo("");
		    }
		    
		    if (line.contains("solr/alfresco:type=filterCache")){
		    	populateCalcInfo("   Alfresco core | filterCache");
		    	while (!line.contains("]") && !line.isEmpty()){
		    		if (line.contains("description") || line.contains("warmupTime")){
		    			populateCalcInfo("   " + line);
		    		}
		    		line = br.readLine();
		    	}
		    	populateCalcInfo("");
		    }
		    if (line.contains("solr/alfresco:type=alfrescoAuthorityCache")){
		    	populateCalcInfo("   Alfresco core | alfrescoAuthorityCache");
		    	while (!line.contains("]") && !line.isEmpty()){
		    		if (line.contains("description") || line.contains("warmupTime")){
		    			populateCalcInfo("   " + line);
		    		}
		    		line = br.readLine();
		    	}
		    	populateCalcInfo("");
		    }
		    if (line.contains("solr/alfresco:type=alfrescoCache")){
		    	populateCalcInfo("   Alfresco core | alfrescoCache");
		    	while (!line.contains("]") && !line.isEmpty()){
		    		if (line.contains("description") || line.contains("warmupTime")){
		    			populateCalcInfo("   " + line);
		    		}
		    		line = br.readLine();
		    	}
		    	populateCalcInfo("");
		    }
		    if (line.contains("solr/alfresco:type=alfrescoPathCache")){
		    	populateCalcInfo("   Alfresco core | alfrescoPathCache");
		    	while (!line.contains("]") && !line.isEmpty()){
		    		if (line.contains("description") || line.contains("warmupTime")){
		    			populateCalcInfo("   " + line);
		    		}
		    		line = br.readLine();
		    	}
		    	populateCalcInfo("");
		    }
		    if (line.contains("solr/alfresco:type=alfrescoReaderToAclIdsCache")){
		    	populateCalcInfo("   Alfresco core | alfrescoReaderToAclIdsCache");
		    	while (!line.contains("]") && !line.isEmpty()){
		    		if (line.contains("description") || line.contains("warmupTime")){
		    			populateCalcInfo("   " + line);
		    		}
		    		line = br.readLine();
		    	}
		    	populateCalcInfo("");
		    }
		    if (line.contains("solr/alfresco:type=alfrescoReaderCache")){
		    	populateCalcInfo("   Alfresco core | alfrescoReaderCache");
		    	while (!line.contains("]") && !line.isEmpty()){
		    		if (line.contains("description") || line.contains("warmupTime")){
		    			populateCalcInfo("   " + line);
		    		}
		    		line = br.readLine();
		    	}
		    	populateCalcInfo("");
		    }	    
		    if (line.contains("solr/alfresco:type=alfrescoDeniedCache")){
		    	populateCalcInfo("   Alfresco core | alfrescoDeniedCache");
		    	while (!line.contains("]") && !line.isEmpty()){
		    		if (line.contains("description") || line.contains("warmupTime")){
		    			populateCalcInfo("   " + line);
		    		}
		    		line = br.readLine();
		    	}
		    	populateCalcInfo("\r\n");
		    }			    
		    if (line.contains("solr/archive:type=Searcher@")){
		    	populateCalcInfo("Archive core | Searcher");
		    	while (!line.isEmpty()){
		    		if (line.contains("maxDoc") || line.contains("numDocs") || line.contains("searcherName") || line.contains("warmupTime")){
		    			populateCalcInfo(line);
		    		}
		    		line = br.readLine();
		    	}
		    	populateCalcInfo("");
		    }
		    
		    if (line.contains("solr/archive:type=filterCache")){
		    	populateCalcInfo("   Archive core | filterCache");
		    	while (!line.contains("]") && !line.isEmpty()){
		    		if (line.contains("description") || line.contains("warmupTime")){
		    			populateCalcInfo("   " + line);
		    		}
		    		line = br.readLine();
		    	}
		    	populateCalcInfo("");
		    }
		    if (line.contains("solr/archive:type=alfrescoAuthorityCache")){
		    	populateCalcInfo("   Archive core | alfrescoAuthorityCache");
		    	while (!line.contains("]") && !line.isEmpty()){
		    		if (line.contains("description") || line.contains("warmupTime")){
		    			populateCalcInfo("   " + line);
		    		}
		    		line = br.readLine();
		    	}
		    	populateCalcInfo("");
		    }
		    if (line.contains("solr/archive:type=alfrescoCache")){
		    	populateCalcInfo("   Archive core | alfrescoCache");
		    	while (!line.contains("]") && !line.isEmpty()){
		    		if (line.contains("description") || line.contains("warmupTime")){
		    			populateCalcInfo("   " + line);
		    		}
		    		line = br.readLine();
		    	}
		    	populateCalcInfo("");
		    }
		    
		    
		    if (line.contains("solr/archive:type=alfrescoPathCache")){
		    	populateCalcInfo("   Archive core | alfrescoPathCache");
		    	while (!line.contains("]") && !line.isEmpty()){
		    		if (line.contains("description") || line.contains("warmupTime")){
		    			populateCalcInfo("   " + line);
		    		}
		    		line = br.readLine();
		    	}
		    	populateCalcInfo("");
		    }
		    if (line.contains("solr/archive:type=alfrescoReaderToAclIdsCache")){
		    	populateCalcInfo("   Archive core | alfrescoReaderToAclIdsCache");
		    	while (!line.contains("]") && !line.isEmpty()){
		    		if (line.contains("description") || line.contains("warmupTime")){
		    			populateCalcInfo("   " + line);
		    		}
		    		line = br.readLine();
		    	}
		    	populateCalcInfo("");
		    }
		    if (line.contains("solr/archive:type=alfrescoReaderCache")){
		    	populateCalcInfo("   Archive core | alfrescoReaderCache");
		    	while (!line.contains("]") && !line.isEmpty()){
		    		if (line.contains("description") || line.contains("warmupTime")){
		    			populateCalcInfo("   " + line);
		    		}
		    		line = br.readLine();
		    	}
		    	populateCalcInfo("");
		    }	    
		    if (line.contains("solr/archive:type=alfrescoDeniedCache")){
		    	populateCalcInfo("   Archive core | alfrescoDeniedCache");
		    	while (!line.contains("]") && !line.isEmpty()){
		    		if (line.contains("description") || line.contains("warmupTime")){
		    			populateCalcInfo("   " + line);
		    		}
		    		line = br.readLine();
		    	}
		    	populateCalcInfo("");
		    }			    
 
		   }
}
    

public void populateCalcInfo(String msg){

    txtSOLR.appendText(msg + "\r\n");
	}
    
}

