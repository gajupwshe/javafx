/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package e1262;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author snehal
 */
public class E1262 extends Application {
    
    @Override
    public void start(Stage stage) {
       Platform.runLater(() -> {
            try {
                stage.initStyle(StageStyle.TRANSPARENT);
                Parent root = FXMLLoader.load(E1262.this.getClass().getResource("LoginScreen.fxml"));
                Scene scene = new Scene(root);
                stage.getIcons().add(new Image(this.getClass().getResourceAsStream("images/app_Logo.png")));
                Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
                stage.setX(primaryScreenBounds.getMinX());
                stage.setY(primaryScreenBounds.getMinY());
                stage.setWidth(primaryScreenBounds.getWidth());
                stage.setHeight(primaryScreenBounds.getHeight());
                stage.setScene(scene);
                stage.setOnCloseRequest((event) ->{
                    System.exit(0);
                });
                stage.show();
            } catch (IOException e) {
                System.out.println("I/O Exception = " + e.getMessage());
            }
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws URISyntaxException, IOException {
     
        launch(args);
    }
    
}
