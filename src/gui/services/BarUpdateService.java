/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.services;

import database.DBConnection;
import database.DataInitializer;
import database.Validation;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;

/**
 *
 * @author Jaap
 */
public class BarUpdateService extends ScheduledService<Integer>{
    
    public static final int UP_TO_DATE = 0;
    public static final int NEEDS_PRODUCT_UPDATE = 1;
    public static final int NEEDS_ADMIN_UPDATE = 2;
    
    private DataInitializer init;
    private DBConnection dBUpdate;
    private Validation vn;
    
    public BarUpdateService(DataInitializer initIn){
       init = initIn;
       dBUpdate = new DBConnection();
	   vn = new Validation(dBUpdate);
}
    
    @Override
    protected Task<Integer> createTask() {
        return new Task<Integer>(){
            protected Integer call(){
                int ret;
                long t = System.currentTimeMillis();
                //Set the last update date to now
				dBUpdate.runUpdate(String.format("UPDATE clients SET last_client_update = NOW() WHERE client_id = %d",init.getCurClient().getID()));
				dBUpdate.commit();
				//check if products are still up to date and put the new ppc id in ppc otherwise
				if(!vn.validateProducts(init.getPPC()))
				{
                    return NEEDS_PRODUCT_UPDATE;
				}
                //check if the current admin at the bar is still responsible
                else if(!vn.validateLastAdminLog()){
                    //update the current admin using the currently know bar:
                    changeAdmin();
                    System.out.println("Admin needs change");
                    return NEEDS_ADMIN_UPDATE;
                }
                else
                    return UP_TO_DATE;
            }   
        };
    }
    
    public void changeAdmin(){
        //now set the admin ID corresponding to the bar
                init.getDB().runQuery(String.format("SELECT admin_id, admin_log_type FROM admin_logs WHERE bar_id = %d ORDER BY admin_log_id DESC LIMIT 1", init.getCurBar().getID()));
                init.getDB().commit();
                
                int adminID = init.getDB().getNextInt(1);
                boolean logType = init.getDB().getBool(2);
                if(logType == false){
                    //when no admin has logged in to the bar yet set admin ID to default
                    init.setAdminID(1);
                    init.setAdminName("default");
                }
                else{
                    init.setAdminID(adminID);
                    //get the admin name
                    init.getDB().runQuery(String.format("SELECT login FROM admins WHERE admin_id = %d", adminID));
                    init.getDB().commit();
                    init.setAdminName(init.getDB().getNextStr());
                }
    }
}
