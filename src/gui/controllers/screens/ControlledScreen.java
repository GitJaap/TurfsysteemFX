/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers.screens;
import database.DataInitializer;
/**
 *
 * @author Jaap
 */
public interface ControlledScreen { 

     //This method will allow the injection of the Parent ScreenPane
     public void setScreenParent(ScreensController screenPage);
     //this allow the database info holding object to be passed to the screens
     public void setDataInitializer(DataInitializer initIn);
     //Used for initialization after dataInitializer has been set
     public void delayedInitialize();
  } 
