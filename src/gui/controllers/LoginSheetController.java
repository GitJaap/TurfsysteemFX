/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import database.*;
import gui.main.ScreensFrameWork;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

/**
 *
 * @author Jaap
 */
public class LoginSheetController implements Initializable, ControlledScreen {
    
    private ScreensController myController;
    private DataInitializer init;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
 
    }
    
    @Override
    public void setScreenParent(ScreensController screenPage) {
        myController = screenPage;
    }
    
    @Override
    public void setDataInitializer(DataInitializer initIn){
        init = initIn;
    }
    
    public void handleLogin(ActionEvent e){
        //set the screen to the bar screen
        myController.setScreen(ScreensFrameWork.BARID);
        //get the controller of the barscreen
        ((BarSheetController)myController.getScreen(ScreensFrameWork.BARID).getUserData()).initBarProducts();
        ((Button)e.getSource()).setDisable(true);
    }
}
