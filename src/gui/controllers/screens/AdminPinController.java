/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers.screens;

import database.DataInitializer;
import database.Transaction;
import database.data.Account;
import database.data.Card;
import gui.controllers.dialogs.AddCardController;
import gui.controllers.dialogs.BookAccountSelectionController;
import gui.controllers.dialogs.MemberChoiceController;
import gui.handlers.AdminTabListener;
import gui.handlers.CardButtonHandler;
import gui.services.BarUpdateService;
import gui.services.ReadCardService;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import org.controlsfx.control.ListSelectionView;
import org.controlsfx.control.PopOver;
import org.controlsfx.property.editor.Editors;

/**
 * Controller class for the pasbeheer tab in the admin screen
 * @author Jaap
 */
public class AdminPinController extends AdminController{
    
    private Account curAccount;
    private ChangeListener<Boolean> memberChoiceListener;
    private int pinAmount;
    private int corDec;
    private int corAmount;
    private TextField pinText = new TextField();
    private TextField corText1 = new TextField();
    private TextField corText2 = new TextField();
    private ReadCardService cardReader;
    private Button[] cardButtons;
    
    
    @FXML Label nameLabel;
    @FXML Label ageLabel;
    @FXML Label balanceLabel;
    @FXML Label creditLabel;
    @FXML VBox cardBox;
    @FXML AnchorPane mainPane;
    @FXML TextFlow outputFlow;
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
        tabID = 2;
        curAccount = new Account();
        
        //add a listener which stops the cardreader thread when a tab is changed
        tabs.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>(){

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if(cardReader != null)
                cardReader.cancel();
            }
            
            
        });
    }
    
    @Override
    public void delayedInitialize(){
        super.delayedInitialize(); // call initialize from superclass
         reloadAccountInfo(); //reset the labels
        //create listener for memberChoice event
        memberChoiceListener = new ChangeListener<Boolean>(){
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if(newValue == false){
                        //set labels to values and add cards to the cardBox
                        reloadAccountInfo();
                        cardReader.restart();
                    }
                }
            };
        //check for card detection changes
        cardReader = new ReadCardService(ReadCardService.READ_AND_WAIT);
        cardReader.periodProperty().set(Duration.millis(500));
        cardReader.start();
        cardReader.setOnSucceeded((WorkerStateEvent event) -> {
            String newValue = cardReader.getValue();
            if(!newValue.equals("")){
                //now search for the account belonging to this card in the database
                init.getDB().runQuery(String.format("Select account_id, account_name, balance, credit_limit FROM accounts INNER JOIN cards USING (account_id) WHERE card_uid = '%s' LIMIT 1",newValue));
                init.getDB().commit();
                if(init.getDB().getNextStr() != null){
                    curAccount.setID(init.getDB().getInt(1));
                    curAccount.setName(init.getDB().getStr(2));
                    curAccount.setBalance(init.getDB().getInt(3));
                    curAccount.setCredit(init.getDB().getInt(4));
                    reloadAccountInfo();
                    
                }
                else{//unknown card has been found
                    curAccount.setName(null); //tells the other functions there is no curAccount
                    reloadAccountInfo();
                    
                }
            }
        });
        
    }
     
        
       
    
    /**
     * handles the pressing of the pin button
     * @param e ActionEvent
     */
    public void handlePIN(ActionEvent e){
        mainPane.disableProperty().removeListener(memberChoiceListener);
        cardReader.cancel();
        
        if(curAccount.getName() != null){
            //create a popup for pinAmount asking accepting only integers and maximum of 4 chars
            TextField text =  new TextField();
            PopOver pop = new PopOver(pinText);
            pop.show((Node)e.getSource());
            pop.setDetached(true);
            pop.detachedTitleProperty().set("Voer bedrag in");
            pop.setX(mainPane.getWidth() / 2 - pop.getWidth() / 2);
            pop.setY(mainPane.getHeight() / 2 - pop.getHeight() / 2);
            pop.hideOnEscapeProperty().set(true);
            mainPane.disableProperty().bind(pop.showingProperty());
            
            //add listeners to the pintext
            pinText.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                if (newValue.matches("\\d*") && newValue.length() < 5) {
                    if(pinText.getText().equals(""))
                        pinAmount = 0;
                    else
                        pinAmount = Integer.parseInt(newValue);
                } else {
                    pinText.setText(oldValue);
                }
            });
            //set enter action pressed-> create pin transaction
            pinText.setOnAction((ActionEvent event) -> {
                if(Transaction.updateDatabaseWithDebitTransaction(init, curAccount.getID(), Transaction.PIN, pinAmount * 100)){
                Text output = new Text(curAccount.getName() + " opgewaardeerd met \u20ac" + Integer.toString(pinAmount) +"\n");
                output.setFill(Color.GREEN);
                outputFlow.getChildren().add(0, output);
                //add amount to current account
                curAccount.setBalance(curAccount.getBalance() + pinAmount * 100);
                reloadAccountInfo();
                }
                else{
                    Text error = new Text("PIN fout: transactie niet gelukt. Database error \n");
                    error.setFill(Color.RED);
                    outputFlow.getChildren().add(0, error);
                }
                pop.hide();
                cardReader.restart();
            });
            pinText.setOnKeyPressed((KeyEvent event) -> {
                if(event.getCode() == KeyCode.ESCAPE){
                    pop.hide();
                    cardReader.restart();
                }

            });

        }
        else{
            Text error = new Text("PIN fout: geen account geselecteerd \n");
            error.setFill(Color.RED);
            outputFlow.getChildren().add(0, error);
            cardReader.restart();
        }
    }
    /**
     * handles the pressing of the correction button
     * @param e ActionEvent
     */
    public void handleCorrection(ActionEvent e){ //TODO FIX THIS CODE
        //remove previous listeners
         mainPane.disableProperty().removeListener(memberChoiceListener);
         
        if(curAccount.getName() != null){
            //create a popup for pinAmount asking
            HBox inputFields = new HBox();
            inputFields.getChildren().addAll(corText1, new Label(" , "), corText2);
            PopOver pop = new PopOver(inputFields);
            pop.setDetachedTitle("Voer bedrag in:");
            pop.detachedProperty().set(true);
            pop.show((Node)e.getSource());
            pop.setX(mainPane.getWidth() / 2 - pop.getWidth() / 2);
            pop.setY(mainPane.getHeight() / 2 - pop.getHeight() / 2);
            pop.hideOnEscapeProperty().set(true);
            mainPane.disableProperty().bind(pop.showingProperty());
            

                //initialize the corText1 listeners
            corText1.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                if (newValue.matches("\\d*") && newValue.length() < 5) {
                    if(corText1.getText().equals(""))
                        corAmount = 0;
                    else
                        corAmount = Integer.parseInt(newValue);
                } else {
                    corText1.setText(oldValue);
                }
            });
            corText1.setOnAction((ActionEvent event) -> {
                corText2.requestFocus();
            });
            corText1.setOnKeyPressed((KeyEvent event) -> {
                if(event.getCode() == KeyCode.ESCAPE)
                    pop.hide();
            });

            //initialize the cor2Text listeneres used for decimals
            corText2.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                if (newValue.matches("\\d*") && newValue.length() < 3) {
                    if(corText2.getText().equals(""))
                        corDec = 0;
                    else
                        corDec = Integer.parseInt(newValue);
                } else {
                    corText2.setText(oldValue);
                }
            });
            corText2.setOnAction((ActionEvent event) -> {
                outputFlow.getChildren().add(0, new Text(Integer.toString(corAmount) + "," + Integer.toString(corDec) + "\n"));
            });
            corText2.setOnKeyPressed((KeyEvent event) -> {
                if(event.getCode() == KeyCode.ESCAPE)
                    pop.hide();
            });
            
        }
        else{
            Text error = new Text("Correctie fout: geen account geselecteerd \n");
            error.setFill(Color.RED);
            outputFlow.getChildren().add(0, error);
            }
    }
    
    /**
     * handles the pressing of the choose member button
     * @param e ActionEvent
     */
    public void handleMemberChoiceButton(ActionEvent e){
        try{
            //remove previous listeners
            mainPane.disableProperty().removeListener(memberChoiceListener);
            
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/gui/resources/dialogs/MemberChoiceContent.fxml"));
            Parent loadScreen = (Parent) myLoader.load();
            MemberChoiceController myScreenController = myLoader.getController();       
            // now add the controller adress to the node so it is usable later in the program
            loadScreen.setUserData(myScreenController);
            //now create a popup
            PopOver pop = new PopOver(loadScreen);
            myScreenController.delayedInitialize(init, pop, curAccount);
            pop.detachedProperty().set(true);
            pop.setDetachedTitle("Kies Lid");
            pop.show((Node)e.getSource());
            pop.setX(mainPane.getWidth() / 2 - pop.getWidth() / 2);
            pop.setY(mainPane.getHeight() / 2 - pop.getHeight() / 2);
            pop.hideOnEscapeProperty().set(true);
            mainPane.disableProperty().bind(pop.showingProperty());
            mainPane.disableProperty().addListener(memberChoiceListener);
        
        }
        catch(Exception excep){ // FXMLLOADER has failed
            excep.printStackTrace();
        }
    }
    /**
     * adds a card to the current account by retrieving input from a popup window
     * checks for duplicate cards and returns output in the outputFlow
     * @param e 
     */
    public void handleAddCard(ActionEvent e){
        if(curAccount.getName() != null){
            cardReader.cancel();
            //remove previous listeners
            mainPane.disableProperty().removeListener(memberChoiceListener);
            
            SimpleStringProperty addedCardUID = new SimpleStringProperty("");
            SimpleStringProperty addedCardName = new SimpleStringProperty("");
            //add a listener to the addedCardUID
            addedCardUID.addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                Text output;
                String UID = addedCardUID.get();
                String name = addedCardName.get();
                if(name.equals(""))
                    name = UID;
                //check for duplicates
                init.getDB().runQuery(String.format("SELECT card_id from cards WHERE card_uid = '%s' LIMIT 1", UID));
                init.getDB().commit();
                int duplicateID = init.getDB().getNextInt(1);
                init.getDB().commit();
                if(duplicateID == -9999){
                    //add the card to the database
                    try{
                        PreparedStatement query = init.getDB().getCon().prepareStatement("INSERT INTO cards(account_id, card_name, card_uid) VALUES (?, ?, ?)");
                        query.setInt(1, curAccount.getID());
                        query.setString(2, name);
                        query.setString(3, UID);
                        query.executeUpdate();
                        init.getDB().commit();
                    }
                    catch(SQLException ex){
                        ex.printStackTrace();
                        output = new Text("Fatale SQL FOUT, kaart niet toegevoegd\n");
                        output.setFill(Color.RED);
                    }
                    output = new Text("Kaart: " + UID + " toegevoegd aan " + curAccount.getName() + "\n");
                    output.setFill(Color.GREEN);
                    reloadAccountInfo();
                }
                else{//card already present
                    output = new Text("Kaart: " + UID + " komt al voor in database\n");
                    output.setFill(Color.RED);
                }
                outputFlow.getChildren().add(0, output);
            });
            
            
            FXMLLoader myLoader =null;
            Parent loadScreen=null;
            AddCardController myScreenController=null;
            try{            
                myLoader = new FXMLLoader(getClass().getResource("/gui/resources/dialogs/AddCardContent.fxml"));
                 loadScreen = (Parent) myLoader.load();
                myScreenController = myLoader.getController();       
                // now add the controller adress to the node so it is usable later in the program
                loadScreen.setUserData(myScreenController);
            }
            catch(IOException ex){};
            //now create a popup for checking the card
            PopOver pop = new PopOver(loadScreen);
            myScreenController.delayedInitialize(addedCardUID, addedCardName, pop, cardReader);
            pop.detachedProperty().set(true);
            pop.setDetachedTitle("Verifieer Pas");
            pop.show((Node)e.getSource());
            pop.setX(mainPane.getWidth() / 2 - pop.getWidth() / 2);
            pop.setY(mainPane.getHeight() / 2 - pop.getHeight() / 2);
            pop.hideOnEscapeProperty().set(true);
            mainPane.disableProperty().bind(pop.showingProperty()); 
        
        }
        else{
            Text error = new Text("Kan geen pas toevoegen: geen account geselecteerd \n");
            error.setFill(Color.RED);
            outputFlow.getChildren().add(0, error);
        }
    }
    /**
     * handles the pressing of the selectBookAccounts button. Shows a popup with a double list view, so accounts can be transferred from available to selected.
     * Uses the controlsFX listSelectionview
     * @param e 
     */
    public void handleSelectBookAccounts(ActionEvent e){
        FXMLLoader myLoader =null;
        Parent loadScreen=null;
        BookAccountSelectionController myScreenController=null;
            try{            
                myLoader = new FXMLLoader(getClass().getResource("/gui/resources/dialogs/BookAccountSelectionContent.fxml"));
                 loadScreen = (Parent) myLoader.load();
                myScreenController = myLoader.getController();       
                // now add the controller adress to the node so it is usable later in the program
                loadScreen.setUserData(myScreenController);
            }
            catch(IOException ex){ex.printStackTrace();};
        
        
        PopOver pop = new PopOver(loadScreen);
        myScreenController.delayedInitialize(init, pop);
        pop.show((Button)e.getSource());
        pop.setX(mainPane.getWidth() / 2 - pop.getWidth() / 2);
        pop.setY(mainPane.getHeight() / 2 - pop.getHeight() / 2);
        pop.setDetached(true);
        pop.detachedTitleProperty().set("  Selecteer accounts voor wegboeken");
        pop.hideOnEscapeProperty().set(true);
        mainPane.disableProperty().bind(pop.showingProperty());
        
    }
    /**
     * Handles adding an accountto the database with a specified (unique) name
     * @param e 
     */
    public void handleAddAccount(ActionEvent e){
        TextField nameField = new TextField();
        nameField.setPrefWidth(300);
        PopOver pop = new PopOver(new VBox(nameField));
        pop.show((Button)e.getSource());
        pop.setDetached(true);
        pop.detachedTitleProperty().set("  Voer unieke accountnaam in");
        pop.setX(mainPane.getWidth() / 2 - pop.getWidth() / 2);
        pop.setY(mainPane.getHeight() / 2 - pop.getHeight() / 2);
        pop.hideOnEscapeProperty().set(true);
        mainPane.disableProperty().bind(pop.showingProperty());
        // add listener to the text field for when text is entered
        nameField.setOnAction(new EventHandler<ActionEvent>(){
            public void handle(ActionEvent e){
                //handles the pressing of enter key
                Text output;
                String input = nameField.getText();
                if(input.equals("") || input.substring(0,1).equals(" ") || input.contains("/") || input.length() < 4
                        || input.contains(";") || input.contains(".") || input.contains(",") || input.contains("{") ||
                        input.contains("}")){
                    
                    //unvalid name input
                    output = new Text("Account niet toegevoegd. Ongeldige accountnaam.\n");
                    output.setFill(Color.RED);
                    pop.hide();
                }
                else{ //name input is valid
                    try{
                            //first check for duplicate names
                            PreparedStatement query = init.getDB().getCon().prepareStatement("SELECT account_id FROM accounts WHERE account_name = ?");
                            query.setString(1, nameField.getText());
                            init.getDB().executePreparedStatement(query);
                            init.getDB().commit();
                            if(init.getDB().getNextInt(1) == -9999){ //  no duplicate have been found
                                 //create a prepared statement for entering an account name
                                query = init.getDB().getCon().prepareStatement("INSERT INTO accounts(account_name, balance, credit_limit, account_creation_date) VALUES (?, 0, 0, NOW())");
                                query.setString(1, nameField.getText());
                                query.executeUpdate();
                                init.getDB().commit();
                                output = new Text(String.format("Account met naam %s toegevoegd \n", nameField.getText()));
                                output.setFill(Color.GREEN);
                                pop.hide();
                            }
                            else{
                                output = new Text("Account niet toegevoegd. Naam bestaat al\n");
                                output.setFill(Color.RED);
                                pop.hide();
                            }
                        }
                        catch(SQLException ex){
                            ex.printStackTrace();
                            output = new Text("Fatale SQL FOUT, kaart niet toegevoegd\n");
                            output.setFill(Color.RED);
                            pop.hide();
                    }
                } 
                outputFlow.getChildren().add(output);
            }
        });
        
        
    }
    /**
     * reloads the cards belonging to account and sets the blance, credit and name on the labels and adds buttons for the cards added to this account
     * stops if curAccount has null name
     */
    public void reloadAccountInfo(){
        //remove previous card buttons
            cardBox.getChildren().clear();
            if(curAccount.getCards() != null)
                curAccount.getCards().clear();
        if(curAccount.getName() != null){            
            //first search for the cards belonging to this account
            init.getDB().runQuery(String.format("SELECT card_id, card_uid, card_name FROM cards WHERE account_id = %d", curAccount.getID()));
            init.getDB().commit();
            int[] ids = init.getDB().getColumnInt(1);
            String[] uids = init.getDB().getColumnStr(2);
            String[] names = init.getDB().getColumnStr(3);
            //add these to curAccount
            if(ids != null){
                for(int i = 0; i < ids.length;i++){
                    curAccount.getCards().add(new Card(ids[i], names[i], uids[i]));
                }
            }
            //set labels to values
            nameLabel.setText(curAccount.getName());
            balanceLabel.setText(init.getdf().format(((double)curAccount.getBalance()) / 100));
            creditLabel.setText(init.getdf().format(((double)curAccount.getCredit()) / 100));
            
            //now add the card buttons
            cardButtons = new Button[curAccount.getCards().size()];
            for(int i = 0; i < curAccount.getCards().size();i++){
                cardButtons[i] = new Button(curAccount.getCards().get(i).getName());
                cardButtons[i].setOnAction(new CardButtonHandler(init, curAccount, i, this));
                cardButtons[i].prefWidthProperty().set(Double.MAX_VALUE);
                cardBox.getChildren().add(cardButtons[i]);
            }
        }
        else{//An unknown account is specified
            nameLabel.setText("Onbekend");
            balanceLabel.setText("---");
            creditLabel.setText("---");
        }
    }
    @Override
    /**
     * cleans up cardreader thread and set the current account back to normal
     */
    public void cleanAfterSwitch(){
        cardReader.cancel();
        curAccount = new Account();
    }
    
     //getters
    public TextFlow getOutputFlow(){
        return outputFlow;
    }
    
}