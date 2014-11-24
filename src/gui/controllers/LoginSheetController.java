/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import database.*;
import database.data.Bar;
import database.Login;
import database.data.Client;
import gui.main.ScreensFrameWork;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 *
 * @author Jaap
 */
public class LoginSheetController implements Initializable, ControlledScreen {
    
    private ScreensController myController;
    private DataInitializer init;
    @FXML ChoiceBox barChooser;
    @FXML CheckBox adminCheckBox;
    @FXML TextField userField;
    @FXML TextField passField;
    @FXML Label feedBackLabel;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
 
    }
    
    @Override
    public void delayedInitialize(){
        /*
        loads the bars and clients in the loginscreen combobox
        */
        //set the last client update to now
        init.getVN().validateClients();
        init.reInitializeClients();
        barChooser.getItems().clear();
        init.getBars().stream().forEach((bar) -> {
            bar.getClients().stream().forEach((client) -> {                
                barChooser.getItems().add(client);
            });
        });
       barChooser.getSelectionModel().selectFirst();  
    }
            
    
    @Override
    public void setScreenParent(ScreensController screenPage) {
        myController = screenPage;
    }
    
    @Override
    public void setDataInitializer(DataInitializer initIn){
        init = initIn;
    }
    
    public void handleLogin(ActionEvent e){
        /*
        Handles the pressing of the login button.
        Creates a log in the database table client_logs and sets the clientisactive value to true in succes
        Shows an error message if the active clients have been altered in the meantime forcing the client to login again
        */
        if(init.getVN().validateLastClientLog()) //check for more recent than known logins
        {
            boolean admin = adminCheckBox.selectedProperty().getValue();
            if(admin){
                if(Login.doAdminLogin(init, userField.getText(), passField.getText(), ((Client)barChooser.getSelectionModel().getSelectedItem()).getBar().getID() )){
                    init.setCurBar(((Client)barChooser.getSelectionModel().getSelectedItem()).getBar());
                    init.setCurClient((Client)barChooser.getSelectionModel().getSelectedItem());
                    if(init.getDB().runUpdate(String.format("INSERT INTO client_logs(client_id,client_log_type,log_date) VALUES (%d , true, NOW())",init.getCurClient().getID())) &&
                            //set the current client_is_active state to true
                            init.getDB().runUpdate(String.format("UPDATE clients SET client_is_active=true, last_client_update = NOW() WHERE client_id = %d ",init.getCurClient().getID())) &&
                            init.getDB().commit()){
                        System.out.println("client logged in");
                         //set the screen to the admin screen
                        init.setLogType(DataInitializer.ADMIN_LOGGED_IN);
                        myController.setScreen(ScreensFrameWork.ADMINID);
                    }
                    else{//some communication error has occured
                            delayedInitialize();
                            Alert alert = new Alert(AlertType.INFORMATION);
                            alert.setTitle("Communicatie Fout");
                            alert.setContentText("Er is iets misgegaan met het communiceren met de database");
                            alert.showAndWait();
                        }
                }
                else{ //login info is incorrect
                    feedBackLabel.setText("Ongeldige Gebruiker / Wachtwoord");
                    userField.setText("");
                    passField.setText("");
                }
            }
            else{//admin has not been selected
                if(Login.doRegularLogin(init, userField.getText(), passField.getText(), ((Client)barChooser.getSelectionModel().getSelectedItem()).getBar().getID() )){
                    init.setCurBar(((Client)barChooser.getSelectionModel().getSelectedItem()).getBar());
                    init.setCurClient((Client)barChooser.getSelectionModel().getSelectedItem());
                    if(init.getDB().runUpdate(String.format("INSERT INTO client_logs(client_id,client_log_type,log_date) VALUES (%d , true, NOW())",init.getCurClient().getID())) &&
                            //set the current client_is_active state to true
                            init.getDB().runUpdate(String.format("UPDATE clients SET client_is_active=true, last_client_update = NOW() WHERE client_id = %d ",init.getCurClient().getID())) &&
                            init.getDB().commit()){
                        System.out.println("client logged in");
                         //set the screen to the admin screen
                        init.setLogType(DataInitializer.USER_LOGGED_IN);
                        myController.setScreen(ScreensFrameWork.BARID);
                    }
                    else{//some communication error has occured
                            delayedInitialize();
                            Alert alert = new Alert(AlertType.INFORMATION);
                            alert.setTitle("Communicatie Fout");
                            alert.setContentText("Er is iets misgegaan met het communiceren met de database");
                            alert.showAndWait();
                        }
                }
                else{ //login info is incorrect
                    feedBackLabel.setText("Ongeldige Gebruikernaam / Wachtwoord");
                    userField.setText("");
                    passField.setText("");
                }
                
            }
        }
        else{ //client_log_id is not validated
            //someone else has chosen a bar first
            delayedInitialize();
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Communicatie Conflict");
            alert.setContentText("Iemand anders heeft tegelijk een bar geselecteerd. Selecteer opnieuw.");
            alert.showAndWait();
        }
    }
}
