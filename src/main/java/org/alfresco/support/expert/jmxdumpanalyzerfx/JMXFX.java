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
    
    
    @Override
    public void start(final Stage mainStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("JmxMain.fxml"));

        Parent root = loader.load();

        JmxMainController controller = loader.getController();

        Scene scene = new Scene(root);

        controller.setHostServices(getHostServices());

        setUserAgentStylesheet(STYLESHEET_CASPIAN);
        scene.getStylesheets().add(this.getClass() .getResource("/stylesheets/textAreas.css").toExternalForm());
        mainStage.setTitle("JMX Dump Analyzer (jmxdump-analyzer-fx) - " + JmxMainController.version);
        mainStage.setMinWidth(800.00);
        mainStage.setMinHeight(400.00);
        mainStage.getIcons().add(new Image(getClass().getResourceAsStream("Alfresco_Flower_Smaller.jpg")));
        mainStage.setScene(scene);
        mainStage.show();
       }

    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
            }
    
    
}
