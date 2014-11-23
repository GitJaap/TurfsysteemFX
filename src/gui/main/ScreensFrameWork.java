/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
*/

package gui.main;

import database.DataInitializer;
import gui.controllers.ScreensController;
import gui.handlers.CloseOperation;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ScreensFrameWork extends Application {
    
    //dataInitializer wrapper object used for global data acces
    DataInitializer init = new DataInitializer();
    
    //declare the names and ids of fxml files 
    public static String BARFile = "/gui/resources/BarSheet2.fxml";
    public static String BARID = "BAR";
    public static String LOGINFile = "/gui/resources/LoginSheet.fxml";
    public static String LOGINID = "LOGIN";
    public static String ADMINID = "ADMIN";
    public static String ADMINFile = "/gui/resources/AdminSheet.fxml";

    
    @Override
    public void start(Stage primaryStage) {
        //create the parent screen manager
        ScreensController mainContainer = new ScreensController(init);
        //load the screens
        mainContainer.loadScreen(LOGINID, LOGINFile);
        mainContainer.loadScreen(BARID, BARFile);
        mainContainer.loadScreen(ADMINID, ADMINFile);
        mainContainer.setScreen(LOGINID);
        Scene scene = new Scene(mainContainer);
        scene.getStylesheets().add("/gui/resources/loginsheet.css");
        primaryStage.setMaximized(true);
        primaryStage.setScene(scene);
        primaryStage.show();
        
       //set the close operation to a custom version allpowing for logouts before close
        primaryStage.setOnCloseRequest(new CloseOperation(init));
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
