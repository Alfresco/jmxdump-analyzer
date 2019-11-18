/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alfresco.support.expert.jmxdumpanalyzerfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;

/**
 *
 * @author astrachan
 */
public class JMXFX extends Application {
    
    private FXMLLoader loader;

    private Parent root;

    private JmxMainController controller;

    private Scene scene;
    
    public void init() throws Exception {
        loader = new FXMLLoader(getClass().getResource("JmxMain.fxml"));
        root = loader.load();
        controller = loader.getController();
        controller.setHostServices(getHostServices());
        
        setUserAgentStylesheet(STYLESHEET_CASPIAN);

        scene = new Scene(root);
        scene.getStylesheets().add(this.getClass() .getResource("/stylesheets/textAreas.css").toExternalForm());
    }

    @Override
    public void start(final Stage mainStage) throws Exception {
        mainStage.setTitle("JMX Dump Analyzer (jmxdump-analyzer-fx) - " + JmxMainController.version);
        mainStage.setMinWidth(800.00);
        mainStage.setMinHeight(400.00);
        mainStage.getIcons().add(new Image(getClass().getResourceAsStream("Alfresco_Flower_Smaller.jpg")));
        mainStage.setScene(scene);
        mainStage.show();
       }
    
    /**
     * Clean up on exit
     */
    @Override
    public void stop() throws Exception{
        controller.cleanup();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
            }
    
    
}
