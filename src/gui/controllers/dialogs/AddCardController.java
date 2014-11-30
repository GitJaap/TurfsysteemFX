/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers.dialogs;

import gui.services.ReadCardService;
import gui.services.ReadCardAndWaitTask;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.PopOver;

/**
 * FXML Controller class
 *
 * @author Jaap
 */
public class AddCardController implements Initializable {

    
    @FXML private ImageView check1Image;
    @FXML private ImageView check2Image;
    @FXML private Button restartButton;
    @FXML private AnchorPane anchorPane;
    @FXML private TextField cardNameField;
    
    private SimpleStringProperty outputName;
    private boolean check1Bool = false;
    private String check1String = "";
    private PopOver pop;
    private SimpleStringProperty outputUID;
    
    private ReadCardService cardReader;
    private ReadCardService parentReader;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cardReader = new ReadCardService(ReadCardService.READ_AND_WAIT);
        restartButton.setVisible(false);
               
    }
    
    public void delayedInitialize(SimpleStringProperty outputUID, SimpleStringProperty outputName, PopOver pop, ReadCardService parentReader){
        this.outputUID = outputUID;
        this.pop = pop;
        this.parentReader = parentReader;
        cardReader.setOnSucceeded(new EventHandler<WorkerStateEvent>(){
            @Override
            public void handle(WorkerStateEvent event) {
                String newValue = cardReader.getValue();
                if(newValue == null || newValue.equals("") ){
                    //check for first check completion
                    if(check1Bool){
                      check2Image.setImage(new Image("/gui/resources/pictures/ErrorIcon.png"));
                      check1Bool = false;
                      pop.setDetachedTitle("Leesfout: scan opnieuw");
                      restartButton.setVisible(true);
                      cardReader.cancel();
                    }
                }
                else {//current check is succes
                   if(check1Bool){ //this is the second check
                       if(check1String.equals(newValue)){
                           //both checks are a succes and strings are equal
                           check2Image.setImage(new Image("/gui/resources/pictures/SuccessIcon.png"));
                           outputName.set(cardNameField.getText());
                           outputUID.set(newValue);
                           parentReader.restart(); // restart the scanner in original tab
                           pop.hide();
                       }
                       else{
                           //strings are not equal
                           check2Image.setImage(new Image("/gui/resources/pictures/ErrorIcon.png"));
                           check1Bool = false;
                           cardReader.cancel();
                           pop.setDetachedTitle("Ongeschikte pas");
                           restartButton.setVisible(true);
                           restartButton.requestFocus();
                        }
                   }
                    else{ //this is the first succesful chceck
                        check1Bool = true;
                        check1String = newValue;
                        check1Image.setImage(new Image("/gui/resources/pictures/SuccessIcon.png"));
                    }
                }
                
            }
        
        });
        //start the reading
        cardReader.start();
        
    }
    
    public void handleRestart(ActionEvent e){
        cardReader.restart();
        check1Image.setImage(new Image("/gui/resources/pictures/HelpIcon.png"));
        check2Image.setImage(new Image("/gui/resources/pictures/HelpIcon.png"));
        pop.setDetachedTitle("Verifieer Pas");
        restartButton.setVisible(false);
    }
    
    public void handleRestartButtonKey(KeyEvent e){
        if(e.getCode() == KeyCode.ENTER )
            handleRestart(new ActionEvent());
        if(e.getCode() == KeyCode.ESCAPE){
            parentReader.restart();
            pop.hide();
        }
    }
    
}
