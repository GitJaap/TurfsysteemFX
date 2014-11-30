/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.handlers;

import database.DataInitializer;
import database.data.Account;
import gui.controllers.screens.AdminPinController;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 *
 * @author Jaap
 */
public class CardButtonHandler implements EventHandler<ActionEvent>{
    
    private DataInitializer init;
    private Account curAccount;
    private int cardIndex;
    private AdminPinController adminController;
    
    public CardButtonHandler(DataInitializer init, Account curAccount, int cardIndex, AdminPinController adminController){
        this.init = init;
        this.curAccount = curAccount;
        this.adminController = adminController;
        this.cardIndex = cardIndex;
    }
    @Override
    public void handle(ActionEvent e) {
        //when button is clicked give alert to remove
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle(String.format("Verwijder pas %s", ((Button)e.getSource()).getText()));
                    alert.setContentText("Wilt u de pas echt verwijderen?");
                    Optional<ButtonType> result =alert.showAndWait();
                    if(result.get() == ButtonType.OK){
                        //remove card from database
                        init.getDB().runUpdate(String.format("DELETE from cards WHERE card_id = %d", curAccount.getCards().get(cardIndex).getID()));
                        adminController.getOutputFlow().getChildren().add(0, new Text(curAccount.getCards().get(cardIndex).getName()+" verwijderd\n"));
                        curAccount.getCards().remove(cardIndex);
                        adminController.reloadAccountInfo();
                    }
                    
    }
    
    
}
