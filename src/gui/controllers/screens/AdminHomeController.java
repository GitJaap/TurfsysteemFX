/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers.screens;

import database.DataInitializer;
import gui.handlers.AdminTabListener;
import gui.services.ClientUpdateService;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 *
 * @author Jaap
 */
public class AdminHomeController extends AdminController{
    
    @FXML private VBox priceClassBox;
    
    private Button[] priceClassButtons;
    private ClientUpdateService clientUpdater;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
        tabID = 0;
    }
    
    @Override
    public void delayedInitialize(){
        super.delayedInitialize(); // call initialize from superclass
        if(clientUpdater == null){ //check for first run and then start the client update thread
            clientUpdater = new ClientUpdateService(init);
            clientUpdater.setPeriod(Duration.seconds(1));
            clientUpdater.start();
        }
        createPriceClassButtons();

    }
    
    /**
     * creates the price class buttons according to database entries and selects the current priceclass
     */
    public void createPriceClassButtons(){
        priceClassBox.getChildren().clear();
        //Check which is the current priceClass
        init.getDB().runQuery("SELECT current_product_price_class_id FROM admin_changes ORDER BY admin_change_id DESC LIMIT 1");
        init.getDB().commit();
        int priceClassID = init.getDB().getNextInt(1);
        //get the available classes from the database
        init.getDB().runQuery("SELECT product_price_class_id, class_name FROM product_price_class WHERE is_removed = false");
        init.getDB().commit();
        int[] ids = init.getDB().getColumnInt(1);
        String[] names = init.getDB().getColumnStr(2);
        
        //create the priceclass buttons
        if(ids != null){
            priceClassButtons = new Button[ids.length];
            for(int i =0; i < ids.length; i++){
                priceClassButtons[i] = new Button(names[i]);
                priceClassButtons[i].setPrefWidth(Double.MAX_VALUE);
                priceClassButtons[i].setOnAction((ActionEvent event) -> {
                    for(int j = 0; j < priceClassButtons.length; j++){
                        priceClassButtons[j].setDisable(false);
                        if(event.getSource() == priceClassButtons[j]){
                            //the jth priceclass button has been pressed
                            init.getDB().runUpdate(String.format("INSERT INTO admin_changes(current_product_price_class_id, admin_id, admin_change_date, admin_change_description)"
                                    + " VALUES(%d, %d, NOW(), '%s')", ids[j], init.getAdminID(), "Prijsklasse veranderd" ));
                            init.getDB().commit();
                            priceClassButtons[j].setDisable(true);
                        }
                    }
                });
                //add the buttons to the vBOX
                priceClassBox.getChildren().add(priceClassButtons[i]);
               
                //now disable the current priceclass button
                if(ids[i] == priceClassID){
                    priceClassButtons[i].setDisable(true);
                }
            }
        }
    
        
    }
}
    
   
