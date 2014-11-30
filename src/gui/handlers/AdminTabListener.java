/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.handlers;

import database.DataInitializer;
import gui.controllers.screens.AdminController;
import gui.controllers.screens.ControlledScreen;
import gui.controllers.screens.ScreensController;
import gui.main.ScreensFrameWork;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

/**
 * ChangeListener used to detect tab selection changes, launching a new screen at change
 * @author Jaap
 */
public class AdminTabListener implements ChangeListener<Number> {
    
    ScreensController myController;
    AdminController refScreen;
    TabPane tabs;
    public AdminTabListener(ScreensController controller, AdminController refScreen, TabPane tabs){
        myController = controller;
        this.refScreen = refScreen;
    }
    
    @Override
    public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
        myController.setAnimation(false); 
        refScreen.cleanAfterSwitch(); //clean up threads used in each tab
        switch(t1.intValue()){
                case 0:
                    myController.setScreen(ScreensFrameWork.ADMINHome);
                    break;
                case 1:
                    myController.setScreen(ScreensFrameWork.ADMINProduct);
                    break;
                case 2:
                    myController.setScreen(ScreensFrameWork.ADMINPIN);
                    break;
                case 3:
                    myController.setScreen(ScreensFrameWork.ADMINStat);
                    break;
                case 4:
                    myController.setScreen(ScreensFrameWork.ADMINSupply);
                    break;
                default:
                    System.out.println("all wrong");
            }
    }

    
}
