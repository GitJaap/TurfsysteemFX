/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *Class which handles login verification
 * @author Jaap
 */
public abstract class Login {
    

    public static boolean doRegularLogin(DataInitializer init, String userName, String passWord, int barID){
        /**
         * Handles regular user login event using userName and passWord as id's for getting the admin id from the database
         * sets the admin of the dataInitializer to currently logged in admin of the bar specified in arguments and to default if no admin has logged in.
         * @returns boolean if succes, does not change admin_logs!
         */
        Connection con = init.getDB().getCon();
        try{
            PreparedStatement query = con.prepareStatement("SELECT admin_id FROM admins WHERE login = ? and pass = SHA1(?) LIMIT 1");
            query.setString(1, userName);
            query.setString(2, passWord);
            init.getDB().executePreparedStatement(query);
            init.getDB().commit();
            int adminID = init.getDB().getNextInt(1);
            if(adminID == -9999)
                return false;     
            else{
                //now set the admin ID corresponding to the bar
                init.getDB().runQuery(String.format("SELECT admin_id, admin_log_type FROM admin_logs WHERE bar_id = %d ORDER BY admin_log_id DESC LIMIT 1", barID));
                init.getDB().commit();
                
                adminID = init.getDB().getNextInt(1);
                boolean logType = init.getDB().getBool(2);
                if(logType == false)
                    //when no admin has logged in to the bar yet set admin ID to default
                    init.setAdminID(1);
                else
                    init.setAdminID(adminID);
                return true;
            }
        }
        catch(SQLException e)
        {
            return false;
        }
    }
    
    public static boolean doAdminLogin(DataInitializer init, String userName, String passWord, int barID){
         /**
         * Handles admin login events using userName and passWord as id's for getting the admin id from the database
         * Also updates the admin_logs table so this admin is logged in to the corresponding bar
         * @returns true in case of succes
         *          false in case of failure/error
         */
        Connection con = init.getDB().getCon();
        try{
            PreparedStatement query = con.prepareStatement("SELECT admin_id FROM admins WHERE login = ? and pass = SHA1(?) LIMIT 1");
            query.setString(1, userName);
            query.setString(2, passWord);
            init.getDB().executePreparedStatement(query);
            init.getDB().commit();
            int adminID = init.getDB().getNextInt(1);
            if(adminID == -9999)
                return false;     
            else{ // admin ID has been recieved
                //file an admin login request
                init.getDB().runUpdate(String.format("INSERT INTO admin_logs(admin_log_date, admin_log_type, bar_id, admin_id, flow_meter_readout) VALUES(NOW(), true, %d, %d , 0)", barID, adminID ));
                init.getDB().commit();
                //set the current admin
                init.setAdminID(adminID);
                return true;
            }
        }
        catch(SQLException e)
        {
            return false;
        }
    }
}
