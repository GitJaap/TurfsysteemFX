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
import javafx.scene.control.TabPane;

/**
 *
 * @author Jaap
 */
public class AdminHomeController extends AdminController{
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
        tabID = 0;
    }
    
    @Override
    public void delayedInitialize(){
        super.delayedInitialize(); // call initialize from superclass
    }
}
    
   
