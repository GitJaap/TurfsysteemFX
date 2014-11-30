/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers.screens;

import database.DataInitializer;
import database.Transaction;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import database.data.*;
import gui.handlers.ProductButtonHandler;
import gui.services.BarUpdateService;
import gui.services.ReadCardService;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import org.controlsfx.control.PopOver;

/**
 * FXML Controller class
 *
 * @author Jaap
 */
public class BarSheetController implements Initializable, ControlledScreen {
    
    public final int NProductCols = 5;
    public final int MAXProducts = 40;    
    
    private int curPaymentMethod = 0;
    private ScreensController myController;
    private DataInitializer init;
    private ProductPriceClass ppc;
    private ProductButtonHandler productButtonHandler;
    private int multiplication;
    private String historyText;
    private Account curAccount;
    private ReadCardService cardReader;
    private String curUID;
    //get components from FXML
    @FXML private GridPane productPane;
    @FXML private TextField multiplicationField;
    @FXML private VBox orderBox;
    @FXML private TextArea historyField;
    @FXML private Button pinButton;
    @FXML private Button cashButton;
    @FXML private Button orderButton;
    @FXML private Button schrapButton;
    @FXML private Label nameLabel;
    @FXML private Label ageLabel;
    @FXML private Label balanceLabel;
    @FXML private Label cardCheckLabel;
    @FXML private Label clockLabel;
    @FXML private Label totalLabel; //used for showing total order price
    @FXML private Label sessionLabel; // used for showing session info like clientid adminid, price class
    @FXML private Button bookButton;
    @FXML private AnchorPane mainPane;
    @FXML private Label topLabel;
    //declare components to fit in productPane
    //since they vary they cannot be defined in FXML
    private Button[][] productButtons;
    private Button[][] orderedButtons;
    private Button[][] orderDecrementers;
    private Button[][] orderIncrementers;
    private HBox[][] orderBoxHor;
    private int[][] orders;
    BarUpdateService updater;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        multiplication = 1;
        historyText = "";
        
        //create the clock
        bindToTime(clockLabel);
        
        curAccount = new Account();
    }    
   
    /**
     * Delayed initialize method used to initialize the updater thread using the DataInitializer object 
     */
    @Override public void delayedInitialize(){
        //create the update service
        updater = new BarUpdateService(init);
        updater.periodProperty().set(Duration.millis(500));
        updater.start();
        updater.lastValueProperty().addListener((observable, oldVal, newVal) -> {
            if(newVal == BarUpdateService.NEEDS_PRODUCT_UPDATE)
                loadComponents();
            else if(newVal == BarUpdateService.NEEDS_ADMIN_UPDATE)
                setSessionLabel();
        });
        
        //create the readcardservice
        cardReader = new ReadCardService(ReadCardService.READ_CONTINUOUS);
        cardReader.setPeriod(Duration.millis(300));
        //add listener to cardreader which changes the curAccount and labels when a card is read
        cardReader.lastValueProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            curUID = newValue;
            reloadAccountInfo();
        });
        
        cardReader.start();
        
        topLabel.setText("Proteus " + init.getCurBar().getName());
    }
    /**
     * Calls the initializeProducts function and loads those products as buttons on the screen
     */
    public void loadComponents()
    {
        //remove all children from product pane
        productPane.getChildren().clear();
        //load the products from the database
        ppc = init.getPPC();
        init.getVN().validateProducts(ppc);
        init.reInitializeProducts();
        setSessionLabel();
        productButtonHandler = new ProductButtonHandler(this,init);
        totalLabel.setText("Totaal \u20ac0,00  ");
        //initialize the productButtons
		productButtons =  new Button[ppc.getProductClassesSize()][];
        orderedButtons = new Button[ppc.getProductClassesSize()][];
        orderDecrementers = new Button[ppc.getProductClassesSize()][];
        orderIncrementers = new Button[ppc.getProductClassesSize()][];
        orderBoxHor = new HBox[ppc.getProductClassesSize()][];
		orders = new int[ppc.getProductClassesSize()][];

		//loop through all available product classes and create buttons accordingly
		int gridRowIndex = 0; //grid row index
        int gridColumnIndex = 0;
        for(int i = 0; i < ppc.getProductClassesSize();i++){
			int nProducts = ppc.getProductsSize(i);
			//initiate the orderbuttons and orderarrays
			orders[i] = new int[nProducts];
			orderedButtons[i] = new Button[nProducts];
            orderDecrementers[i] = new Button[nProducts];
            orderIncrementers[i] = new Button[nProducts];
            orderBoxHor[i] = new HBox[nProducts];
			//create productbuttons
			productButtons[i] = new Button[nProducts];
			for(int j = 0; j < nProducts;j++){
                if((gridRowIndex*NProductCols + gridColumnIndex < MAXProducts))
                {
                    productButtons[i][j] = new Button();
                    productButtons[i][j].setText(ppc.getProductName(i, j) +"\n"+ init.getdf().format((double)ppc.getProductPrice(i, j)/100));
                    productButtons[i][j].setStyle(String.format("-fx-background-color: #B3B3B3, #%s; -fx-background-insets: 0, 2;",ppc.getProductClass(i).getColor()));
                    productPane.add(productButtons[i][j],gridColumnIndex,gridRowIndex);
                    productButtons[i][j].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                    gridColumnIndex ++;
                    if(gridColumnIndex == NProductCols){
                        gridRowIndex++;
                        gridColumnIndex = 0;
                    }
                    
                    //Create the eventhandlers when a button is pressed
                    productButtons[i][j].setOnAction(productButtonHandler);
                }
            }
        }
    }
    
    public void setSessionLabel(){
        sessionLabel.setText(String.format("Huidige prijsklasse: %s    Huidige verantwoordelijke beheerder ID: %d(%s)    Huidige client ID: %d(%s)", ppc.getName(), init.getAdminID(), init.getAdminName(), init.getCurClient().getID(), init.getCurClient().getName()));
    }
      
    @Override //sets parent controllor class used for switching between screens
    public void setScreenParent(ScreensController screenPage) {
        myController = screenPage;
    }

    @Override
    public void setDataInitializer(DataInitializer initIn) {
        init = initIn; //To change body of generated methods, choose Tools | Templates.
    }
    
    //handles the multiplications buttons pressed
    public void multiplicationButtonHandler(ActionEvent e)
    {
        String number = ((Button)e.getSource()).getText();
        multiplicationField.appendText(number);
        multiplication = Integer.parseInt(multiplicationField.getText());
    }
    
    public void multiplicationCancelHandler(ActionEvent e){
        multiplicationField.setText("");
        multiplication = 1;
    }
    
    public void pinButtonHandler(ActionEvent e){
        //set the current paymentmethod and enable the orderbutton
        curPaymentMethod = Transaction.PIN;
        pinButton.disableProperty().set(true);
        orderButton.disableProperty().set(false);
        cashButton.setDisable(false);
        schrapButton.setDisable(false);
    }
    
    public void schrapButtonHandler(ActionEvent e){
        //set the current paymentmethod and enable the orderbutton
        curPaymentMethod = Transaction.SCHRAP;
        schrapButton.disableProperty().set(true);
        orderButton.disableProperty().set(false);
        pinButton.setDisable(false);
        cashButton.setDisable(false);
    }
    
    public void cashButtonHandler(ActionEvent e){
        //set the current paymentmethod and enable the orderbutton
        curPaymentMethod = Transaction.CASH;
        cashButton.disableProperty().set(true);
        orderButton.disableProperty().set(false);
        pinButton.setDisable(false);
        schrapButton.setDisable(false);
    }
    
    public void orderButtonHandler(ActionEvent e){
        //check if there is more than one order
        boolean ordersAreValid = false;
        for(int i =0; i < ppc.getProductClassesSize();i++){
            for(int j =0; j < ppc.getProductsSize(i);j++)
				{
                    if(orders[i][j] > 0){
                        ordersAreValid = true;
                    }
                }
        }
        if(ordersAreValid){
            //execute the transaction
            String tempText = Transaction.doProductTransaction(init, orders, curPaymentMethod, curAccount.getID())+"\n";
            //show the transaction in the historyField
            int totalPrice = 0;
            for(int i =0; i < ppc.getProductClassesSize();i++){
                for(int j =0; j < ppc.getProductsSize(i);j++)
                    {
                        if(orders[i][j] > 0){
                            totalPrice += orders[i][j] * ppc.getProductPrice(i, j);
                            orderBox.getChildren().remove(orderBoxHor[i][j]);
                            tempText += orders[i][j] + " x " + ppc.getProductName(i, j) +" "+ init.getdf().format((double)ppc.getProductPrice(i, j)*orders[i][j]/100) + "\n";
                            orders[i][j] = 0;
                        }
                    }
                }
                tempText+="Voor een totaal van: " + init.getdf().format((double)totalPrice/100) + "!\n\n";
                historyText = tempText + historyText;
                historyField.setText(historyText);

                //disable the orderbutton and re-enable the paymentmethod buttons
                if(curPaymentMethod == Transaction.CARD){
                    disableAllPaymentButtons();
                    reloadAccountInfo();
                }
                else{
                    enableAllPaymentButtons();
                }
                totalLabel.setText("Totaal: " + init.getdf().format(0) + " ");
        }
    }
    
    /**
     * shows a popup with the available account to book to when bookbutton is pressed
     * 
     * 
     */ 
    public void bookButtonHandler(ActionEvent e){
        //get the wegBoek_accounts values from the database
        List<Account> accountList = new ArrayList<>();
         //load the previous wegboek_accounts into the target list
        init.getDB().runQuery("SELECT account_id, account_name, balance, credit_limit FROM accounts INNER JOIN wegboek_accounts USING (account_id)");
        init.getDB().commit();
         //get the results
        int [] ids = init.getDB().getColumnInt(1);
        String[] names = init.getDB().getColumnStr(2);
        int[] balances = init.getDB().getColumnInt(3);
        int[] credits = init.getDB().getColumnInt(4);
        //add them to the list and create gui objects to show them
        VBox container = new VBox();
        PopOver pop = new PopOver(container);
        HBox[] rows;
        Button[] choiceButtons;
        accountList.clear();
        if(ids != null){
            rows = new HBox[ids.length / 4 + 1];
            choiceButtons = new Button[ids.length];
            //add tghe hboxes to the container
            for(int i = 0; i < rows.length;i++){
                rows[i] = new HBox();
                container.getChildren().add(rows[i]);
            }
            for(int i = 0; i < ids.length;i++){
                if(ids[i] > 3) {// check for not using the standard PIN SCHRAP or CASH accounts
                    accountList.add(new Account(ids[i], names[i], balances[i], credits[i]));
                    choiceButtons[i] = new Button(names[i]);
                    choiceButtons[i].setPrefHeight(100);
                    choiceButtons[i].setPrefWidth(150);
                    rows[i/4].getChildren().add(choiceButtons[i]);
                    choiceButtons[i].setOnAction((ActionEvent event) -> {
                        for(int j =0; j < choiceButtons.length;j++)
                            if(event.getSource() == choiceButtons[j]){
                                curAccount = accountList.get(j);
                                curPaymentMethod = Transaction.CARD;
                                orderButtonHandler(new ActionEvent());
                                pop.hide();
                            }
                    });
                    
                }
            }
        }
        //show the popup
        pop.show((Button)e.getSource());
        pop.setArrowLocation(PopOver.ArrowLocation.RIGHT_CENTER);
        pop.detachedTitleProperty().set("     Selecteer accounts voor wegboeken");
        pop.hideOnEscapeProperty().set(true);
        mainPane.disableProperty().bind(pop.showingProperty());
        pop.setX(mainPane.getWidth() / 2 - pop.getWidth() / 2);
        pop.setY(mainPane.getHeight() / 2 - pop.getHeight() / 2);
        pop.setDetached(true);
        pop.setHeight(400);
        pop.setWidth(600);
    }
    
    public void enableAllPaymentButtons(){
        pinButton.disableProperty().set(false);
        schrapButton.disableProperty().set(false);
        cashButton.disableProperty().set(false);
        bookButton.disableProperty().set(false);
        
        orderButton.disableProperty().set(true);
    }
    
    public void disableAllPaymentButtons(){
        pinButton.disableProperty().set(true);
        schrapButton.disableProperty().set(true);
        cashButton.disableProperty().set(true);
        bookButton.disableProperty().set(true);

        orderButton.disableProperty().set(false);
    }
    /**
     * reloads the account info belonging to the current UID
     * shows the values in the labels
     */
    public void reloadAccountInfo(){
        //now search for the account belonging to this card in the database
        if(!curUID.equals("")){
                init.getDB().runQuery(String.format("Select account_id, account_name, balance, credit_limit FROM accounts INNER JOIN cards USING (account_id) WHERE card_uid = '%s' LIMIT 1",curUID));
                init.getDB().commit();
                if(init.getDB().getNextStr() != null){
                    curAccount.setID(init.getDB().getInt(1));
                    curAccount.setName(init.getDB().getStr(2));
                    curAccount.setBalance(init.getDB().getInt(3));
                    curAccount.setCredit(init.getDB().getInt(4));
                    nameLabel.setText(curAccount.getName());
                    balanceLabel.setText(init.getdf().format(((double)curAccount.getBalance()) / 100));
                    cardCheckLabel.setText("Pas aanwezig");
                    disableAllPaymentButtons();
                    curPaymentMethod = Transaction.CARD;
                }
                else{//unknown card has been found
                    curAccount = new Account();
                    cardCheckLabel.setText("Onbekende Pas");
                    nameLabel.setText("");
                    balanceLabel.setText("");
                    enableAllPaymentButtons();
                }
        }
        else{//no card is being read
            curAccount = new Account();
            cardCheckLabel.setText("Geen pas");
            nameLabel.setText("");
            balanceLabel.setText("");
            enableAllPaymentButtons();
            
        }
    }
    
   /**
    * binds a label to the current system time
    * @param label to be bound
    */
  private void bindToTime(Label label) {
    Timeline timeline = new Timeline(
      new KeyFrame(Duration.seconds(0),
        new EventHandler<ActionEvent>() {
          @Override public void handle(ActionEvent actionEvent) {
            Calendar time = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
            label.setText(simpleDateFormat.format(time.getTime()));
          }
        }
      ),
      new KeyFrame(Duration.seconds(1))
    );
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();
   }
  
  
    
    //getters and setters used in the external file ProductButtonHandler
    public Button[][] getProductButtons(){return productButtons;}
    public int[][] getOrders(){return orders;}
    public int getMultiplication(){return multiplication;}
    public void setMultiplication(int m){multiplication = m;}
    public TextField getMultiplicationField(){return multiplicationField;}
    public VBox getOrderBox(){return orderBox;};
    public Button[][] getOrderedButtons(){return orderedButtons;}
    public Button[][] getOrderDecrementers(){return orderDecrementers;}
    public Button[][] getOrderIncrementers(){return orderIncrementers;}
    public HBox[][] getOrderBoxHor(){return orderBoxHor;}
    public Label getTotalLabel(){return totalLabel;}
}
