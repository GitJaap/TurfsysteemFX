/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
*/

package gui.main;

import database.DataInitializer;
import gui.controllers.screens.ScreensController;
import gui.handlers.CloseOperation;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
/**
 * The main framework for all the screens to be loaded 
 * @author Jaap
 */
public class ScreensFrameWork extends Application {
    
    //dataInitializer wrapper object used for global data acces
    DataInitializer init = new DataInitializer();
    
    //declare the names and ids of fxml files 
    public static String BARFile = "/gui/resources/screens/BarSheet2_1.fxml";
    public static String BARID = "BAR";
    public static String LOGINFile = "/gui/resources/screens/LoginSheet.fxml";
    public static String LOGINID = "LOGIN";
    public static String ADMINPIN = "ADMIN_PIN";
    public static String ADMINPINFile = "/gui/resources/screens/AdminPIN.fxml";
    public static String ADMINHome = "ADMIN_HOME";
    public static String ADMINHomeFile = "/gui/resources/screens/AdminHome.fxml";
    public static String ADMINProduct = "ADMIN_PRODUCT";
    public static String ADMINProductFile = "/gui/resources/screens/AdminProduct.fxml";
    public static String ADMINSupply = "ADMIN_SUPPLY";
    public static String ADMINSupplyFile = "/gui/resources/screens/AdminSupply.fxml";
    public static String ADMINStat = "ADMIN_STAT";
    public static String ADMINStatFile = "/gui/resources/screens/AdminStat.fxml";

    
    @Override
    public void start(Stage primaryStage) {
        //create the parent screen manager
        ScreensController mainContainer = new ScreensController(init);
        //load the screens
        mainContainer.loadScreen(LOGINID, LOGINFile);
        mainContainer.loadScreen(BARID, BARFile);
        mainContainer.loadScreen(ADMINPIN, ADMINPINFile);
        mainContainer.loadScreen(ADMINHome, ADMINHomeFile);
        mainContainer.loadScreen(ADMINStat, ADMINStatFile);
        mainContainer.loadScreen(ADMINSupply, ADMINSupplyFile);
        mainContainer.loadScreen(ADMINProduct, ADMINProductFile);
        
        mainContainer.setScreen(LOGINID);
        Scene scene = new Scene(mainContainer);
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
