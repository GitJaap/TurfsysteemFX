/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.services;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;

/**
 * A service for reading cards UID's. Readtype can be specified by static arguments
 * @author Jaap
 */
public class ReadCardService extends ScheduledService<String>{
    
    public static final int READ_CONTINUOUS = 1;
    public static final int READ_AND_WAIT = 2;
    
    private boolean waitForAbsent = false;
    private int readType;
    
    public ReadCardService(int readType){
        this.readType = readType;
 
        this.lastValueProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            //check for succesfull read and then let the terminal wait for card to be removed, otherwise keep scanning
            if(newValue != null && !newValue.equals("")){
                waitForAbsent = true;
            }
            else{
                waitForAbsent = false;
            }
        });
    }
        
    @Override
    protected Task<String> createTask() {
        switch(readType){
            case READ_CONTINUOUS:
                ReadCardContinuousTask task = new ReadCardContinuousTask(waitForAbsent);
                return task;
            case READ_AND_WAIT:
                return new ReadCardAndWaitTask();
            default:
                return new ReadCardAndWaitTask();
        }
    }
   
}
    
    