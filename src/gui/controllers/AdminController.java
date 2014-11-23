/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers;

import database.DataInitializer;

/**
 *
 * @author Jaap
 */
public class AdminController implements ControlledScreen {
    
    private ScreensController myController;
    private DataInitializer init;
    
    
   @Override //sets parent controllor class used for switching between screens
    public void setScreenParent(ScreensController screenPage) {
        myController = screenPage;
    }

    @Override
    public void setDataInitializer(DataInitializer initIn) {
        init = initIn; 
    }

    @Override
    public void loadComponents() {
        
    }
    
}
