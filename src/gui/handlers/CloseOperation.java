/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.handlers;

import database.DataInitializer;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;

/**
 *Closing operation class used for sending logout requests to the database when applicable
 * @author Jaap
 */
public class CloseOperation implements EventHandler<WindowEvent>{
    
    private DataInitializer init;
    public CloseOperation(DataInitializer initIn){
        init = initIn;
    }
    @Override
    public void handle(WindowEvent event) {
        switch(init.getLogType()){
            case DataInitializer.NOT_LOGGED_IN:
                // user is not logged in so no action is required
                break;
            case DataInitializer.ADMIN_LOGGED_IN:
                //admin is logged in and needs to send a logout
                init.getDB().runUpdate(String.format("INSERT INTO admin_logs(admin_log_date, admin_log_type, bar_id, admin_id, flow_meter_readout) VALUES(NOW(), false, %d, %d, 0)", init.getCurBar().getID(), init.getAdminID() ));
                init.getDB().commit();
            default:
                //user is logged in to bar screen so the client needs to send a logout request
                init.getDB().runUpdate(String.format("INSERT INTO client_logs(client_id,client_log_type,log_date) VALUES (%d , false, NOW())",init.getCurClient().getID()));
                init.getDB().runUpdate(String.format("UPDATE clients SET client_is_active=false, last_client_update = NOW() WHERE client_id = %d", init.getCurClient().getID()));
                init.getDB().commit();
                break;
        }
    }
    
}
