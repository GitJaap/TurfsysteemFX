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
public interface ControlledScreen { 

     //This method will allow the injection of the Parent ScreenPane
     public void setScreenParent(ScreensController screenPage);
     public void setDataInitializer(DataInitializer initIn);
  } 
