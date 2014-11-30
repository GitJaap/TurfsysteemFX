/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers.dialogs;

import database.DataInitializer;
import database.data.Account;
import database.data.Card;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import org.controlsfx.control.PopOver;

/**
 * Controller class for the choosing of members(accounts) from the accounts table in the database
 * returns an account object when ok is pressed
 * @author Jaap
 */
public class MemberChoiceController {

    private DataInitializer init;
    private PopOver parentPop;
    private ObservableList<Account> accountList;
    private Account retAccount;
    
    @FXML private ListView memberListView;
    @FXML private TextField inputField;
    @FXML private Button okButton;
    
    public void delayedInitialize(DataInitializer initIn, PopOver pop, Account retAcc){
        init = initIn;
        parentPop = pop;
        retAccount = retAcc;
        handleInputTextChange();
        //add listener to input text
        inputField.textProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // now run the text change handler
                handleInputTextChange();
      }
        });
        okButton.disableProperty().bind(memberListView.getSelectionModel().selectedItemProperty().isNull());
        
    }
    
    /**
     * handles the input text change, searches for accounts which contain the text letters
     * @param e 
     */
    public void handleTextEnter(ActionEvent e){
        
    }
    
    /**
     * handles the searching for accounts based on input text changes. Gets fired when the changelistener to input texts calls it
     */
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
        memberListView.getItems().clear();
        if(ids != null){
            for(int i = 0; i < ids.length;i++){
                if(ids[i] > 3) // check for not using the standard PIN SCHRAP or CASH accounts
                memberListView.getItems().add(new Account(ids[i], names[i], balances[i], credits[i]));
            }
        }
        
    }
    
    public void handleOKPressed(ActionEvent e){
        
        //get the selected account
        Account selectedAccount = (Account)memberListView.getSelectionModel().getSelectedItem();
        //first set all the properties of retaccount
        retAccount.setBalance(selectedAccount.getBalance());
        retAccount.setID(selectedAccount.getID());
        retAccount.setCredit(selectedAccount.getCredit());
        retAccount.setName(selectedAccount.getName());
        parentPop.hide();
    }
    
    public void handleCancelPressed(ActionEvent e){
         parentPop.hide();
    }
    
    public void handleSelectionChange(){
    }


}