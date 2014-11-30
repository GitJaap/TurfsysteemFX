/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.services;

import database.DataInitializer;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;

/**
 * Service which sends a date update to the last_client_update column of the database
 * @author Jaap
 */
public class ClientUpdateService extends ScheduledService<Boolean>{

    private DataInitializer init;
    public ClientUpdateService(DataInitializer init){
        this.init = init;
    }
    
    @Override
    protected Task<Boolean> createTask() {
        return new Task<Boolean>(){
            protected Boolean call() {
                    Boolean ret = init.getDB().runUpdate(String.format("UPDATE clients SET last_client_update = NOW() WHERE client_id = %d", init.getCurClient().getID()));
                    ret &= init.getDB().commit();
                    return ret;               
            }
        };
    };
}


