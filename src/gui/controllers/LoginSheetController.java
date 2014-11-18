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
import database.data.Client;
import gui.main.ScreensFrameWork;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

/**
 *
 * @author Jaap
 */
public class LoginSheetController implements Initializable, ControlledScreen {
    
    private ScreensController myController;
    private DataInitializer init;
    @FXML ChoiceBox barChooser;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
 
    }
    
    @Override
    public void loadComponents(){
        //load the bars and clients in the loginscreen combobox
        //check if clients are up to date
        init.getVN().validateClients();
        init.getBars().stream().forEach((bar) -> {
            bar.getClients().stream().forEach((client) -> {                
                barChooser.getItems().add(client);
            });
        });
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
        
        
        //read the chosen bar/client
        if(init.getVN().validateLastClientLog())
        {
            init.setCurBar(((Client)barChooser.getSelectionModel().getSelectedItem()).getBar());
            init.setCurClient((Client)barChooser.getSelectionModel().getSelectedItem());
        }
        else
        {
            //someone else has chosen a bar first
            loadComponents();
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Communicatie Conflict");
            alert.setContentText("Iemand anders heeft tegelijk een bar geselecteerd. Selecteer opnieuw.");
            alert.showAndWait();
        }
        //set the screen to the bar screen
        myController.setScreen(ScreensFrameWork.BARID);
        ((Button)e.getSource()).setDisable(true);
    }
}
