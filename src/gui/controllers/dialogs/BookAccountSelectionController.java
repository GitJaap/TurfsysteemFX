/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers.dialogs;

import database.DataInitializer;
import database.data.Account;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.controlsfx.control.ListSelectionView;
import org.controlsfx.control.PopOver;

/**
 * FXML Controller class
 *
 * @author Jaap
 */
public class BookAccountSelectionController implements Initializable {

    @FXML ListView sourceView;
    @FXML ListView targetView;
    @FXML TextField inputField;
    
    private DataInitializer init;
    private PopOver parentPop;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    public void delayedInitialize(DataInitializer initIn, PopOver pop){
        init = initIn;
        parentPop = pop;
        //add listener to input text
        handleInputTextChange();
        inputField.textProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // now run the text change handler
                handleInputTextChange();
      }
        });
        //load the previous wegboek_accounts into the target list
        init.getDB().runQuery("SELECT account_id, account_name, balance, credit_limit FROM accounts INNER JOIN wegboek_accounts USING (account_id)");
        init.getDB().commit();
         //get the results
        int [] ids = init.getDB().getColumnInt(1);
        String[] names = init.getDB().getColumnStr(2);
        int[] balances = init.getDB().getColumnInt(3);
        int[] credits = init.getDB().getColumnInt(4);
        //show them in the listview
        targetView.getItems().clear();
        if(ids != null){
            for(int i = 0; i < ids.length;i++){
                if(ids[i] > 3) // check for not using the standard PIN SCHRAP or CASH accounts
                targetView.getItems().add(new Account(ids[i], names[i], balances[i], credits[i]));
            }
        }
        
    }
    
    
    
    public void handleInputTextChange(){
        
        String input = inputField.getText();
        int[] ids;
        String[] names;
        int[] balances;
        int[] credits;
        //now start searching for accounts
        try{
            PreparedStatement query = init.getDB().getCon().prepareStatement("SELECT account_id, account_name, balance, credit_limit FROM accounts WHERE account_name LIKE ? ");
            query.setString(1, '%' + input + '%');
            init.getDB().executePreparedStatement(query);
            init.getDB().commit();
        }
        catch(SQLException ex){ // SQL ERROR occured in statement
            ex.printStackTrace();
        }
        //get the results
        ids = init.getDB().getColumnInt(1);
        names = init.getDB().getColumnStr(2);
        balances = init.getDB().getColumnInt(3);
        credits = init.getDB().getColumnInt(4);
        //show them in the listview
        sourceView.getItems().clear();
        if(ids != null){
            for(int i = 0; i < ids.length;i++){
                if(ids[i] > 3) // check for not using the standard PIN SCHRAP or CASH accounts
                sourceView.getItems().add(new Account(ids[i], names[i], balances[i], credits[i]));
            }
        }
        
    }
    /**
     * handles items going from left to right in the lsits source to target
     * @param e 
     */
    public void handleSourceToTarget(ActionEvent e){
        if(!sourceView.getSelectionModel().isEmpty()){
            boolean alreadyPresent = false;
            Account selectedAcc = (Account)sourceView.getSelectionModel().getSelectedItem();
            //check if account is already in the target list
            for(int i = 0; i < targetView.getItems().size(); i++){
                if(((Account)targetView.getItems().get(i)).getID() == selectedAcc.getID())
                    alreadyPresent = true;                
            }
            if(!alreadyPresent)
                targetView.getItems().add(selectedAcc);
        }
    }
     /**
     * Deletes an item in the right list(targetList)
     * @param e 
     */
    public void handleTargetToSource(ActionEvent e){
        if(!targetView.getSelectionModel().isEmpty()){
            targetView.getItems().remove(targetView.getSelectionModel().getSelectedItem());
        }
    }
    
    /**
     * deletes all items in the right list(targetList)
     * @param e 
     */
    public void handleAllTargetToSource(ActionEvent e){
        targetView.getItems().clear();
    }
    /**
     * handles the pressing of the save button
     * Saves all current account id in the database table wegboek_accounts
     * @param e 
     */
    public void handleSaveButton(ActionEvent e){
        boolean result = true;
        //delete old wegboek_accounts entries
        result &= init.getDB().runUpdate("DELETE FROM wegboek_accounts WHERE 1=1");
            //run updates to the database
            for(int i =0; i < targetView.getItems().size(); i++){
                 result &= init.getDB().runUpdate(String.format("INSERT INTO wegboek_accounts(account_id) Values(%d)", ((Account)targetView.getItems().get(i)).getID()));
            }
        if(result){
            init.getDB().commit();
        }
        else{
            init.getDB().rollback();
        }
        parentPop.hide();
    }
    
    public void handleCancelButton(ActionEvent e){
        parentPop.hide();
    }
    
}
