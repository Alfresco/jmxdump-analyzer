/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alfresco.support.expert.jmxdumpanalyzerfx;


import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;


/**
 *
 * @author astrachan
 */
public class JmxAboutController implements Initializable {
    
    @FXML private Label lblVersion;
    @FXML private TextArea txtAbout;

    
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
       lblVersion.setText(JmxMainController.version);
       txtAbout.setStyle("-fx-border-color: black; -fx-border-width: 1; "
               + "-fx-border-radius: 16; -fx-focus-color: transparent");
    }
       
}
