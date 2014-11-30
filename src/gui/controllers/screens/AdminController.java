/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers.screens;

import database.DataInitializer;
import gui.handlers.AdminTabListener;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TabPane;

/**
 * Superclass with some functions used by all adminController classes like the adding of the admin tab listener
 * @author Jaap
 */
public abstract class AdminController implements ControlledScreen, Initializable {
    protected ScreensController myController;
    protected DataInitializer init;
    protected boolean listenerHasBeenAdded = false;
    protected int tabID;
    protected AdminTabListener tabListener;
    @FXML protected TabPane tabs;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
        tabID = 0; //set default tab to home
    }
   @Override //sets parent controllor class used for switching between screens
    public void setScreenParent(ScreensController screenPage) {
        myController = screenPage;
    }

    @Override
    public void setDataInitializer(DataInitializer initIn) {
        init = initIn; 
    }

    @Override
    /**
     * Initialization of the tab listener which is applicable for all adminstatcontrollers
     */
    public void delayedInitialize() {
        if(!listenerHasBeenAdded){
            tabs.getSelectionModel().select(tabID);
            listenerHasBeenAdded = true;
            tabListener = new AdminTabListener(myController, this,  tabs);
            tabs.getSelectionModel().selectedIndexProperty().addListener(tabListener);
        } 
        else{ // Strange index out of bounds error on tab.getSelectionModel().select(tabID) workaround @TODO fix this differently
            tabs.getSelectionModel().selectedIndexProperty().removeListener(tabListener);
            tabs.getSelectionModel().select(tabID);
            tabListener = new AdminTabListener(myController, this,  tabs);
            tabs.getSelectionModel().selectedIndexProperty().addListener(tabListener);
            
        }
        
    }
    
    /**
     * Cleans up threads used in each tab so they are properly closed after switch
     */
    public void cleanAfterSwitch(){
        
    }
    
}
