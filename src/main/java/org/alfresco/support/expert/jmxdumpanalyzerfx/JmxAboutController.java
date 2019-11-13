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
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.Border;
import javafx.geometry.Insets;


/**
 *
 * @author astrachan
 */
public class JmxAboutController implements Initializable {
    
    @FXML private Label lblVersion;
    @FXML private TextArea txtAbout;
    @FXML private Hyperlink txtGithubURL;

    public void initialize(URL url, ResourceBundle rb) {

       lblVersion.setText("Version: " + JmxMainController.version);
       txtAbout.setStyle("-fx-border-color: black; -fx-border-width: 1; "
               + "-fx-border-radius: 16; -fx-focus-color: transparent");

        txtGithubURL.setText(JmxMainController.githubURL);
        txtGithubURL.setBorder(Border.EMPTY);
        txtGithubURL.setPadding(new Insets(4, 0, 4, 0));
    }
       
}
