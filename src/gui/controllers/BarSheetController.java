/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers;

import database.DataInitializer;
import database.Transaction;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import database.data.*;
import gui.handlers.ProductButtonHandler;
import gui.services.BarUpdateService;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
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
        //execute the transaction
        String tempText = Transaction.doProductTransaction(init, orders, curPaymentMethod)+"\n";
        //show the transaction in the historyField
        tempText +="U heeft de volgende producten besteld: \n";
		int totalPrice = 0;
		for(int i =0; i < ppc.getProductClassesSize();i++){
            for(int j =0; j < ppc.getProductsSize(i);j++)
				{
					if(orders[i][j] > 0){
						totalPrice += orders[i][j] * ppc.getProductPrice(i, j);
						orderBox.getChildren().remove(orderBoxHor[i][j]);
						tempText += orders[i][j] + " x " + ppc.getProductName(i, j) +" voor "+ init.getdf().format((double)ppc.getProductPrice(i, j)*orders[i][j]/100) + "\n";
						orders[i][j] = 0;
					}
				}
			}
			tempText+="Voor een totaal van: " + init.getdf().format((double)totalPrice/100) + "!\n\n";
            historyText = tempText + historyText;
            historyField.setText(historyText);
            
            //disable the orderbutton and re-enable the paymentmethod buttons
            orderButton.setDisable(true);
            cashButton.setDisable(false);
            pinButton.setDisable(false);
            schrapButton.setDisable(false);
    }
    
    //checks if a card is being read and show the person
    public void keyPressedHandler(KeyEvent event){
        if(event.getCode().equals(KeyCode.ENTER))
        {   
            curPaymentMethod = Transaction.CARD;
            nameLabel.setText("Jaap Wesdorp");
            nameLabel.setDisable(false);
            ageLabel.setDisable(false);
            balanceLabel.setDisable(false);
            //get the saldo from the database
            init.getDB().runQuery("SELECT balance FROM accounts WHERE account_name = 'Jaap'");
            int balance = init.getDB().getNextInt(1);
            init.getDB().commit();
            balanceLabel.setText("Saldo: " + init.getdf().format((double)balance/100));
            pinButton.disableProperty().set(true);
            cashButton.disableProperty().set(true);
            schrapButton.disableProperty().set(true);
            orderButton.setDisable(false);
        }
    }
    //checks if the card is removed and disables the user
    public void keyReleasedHandler(KeyEvent event){
        if(event.getCode().equals(KeyCode.ENTER))
        {
            nameLabel.setText("Naam Persoon");
            nameLabel.disableProperty().set(true);
            ageLabel.setDisable(true);
            balanceLabel.setDisable(true);
            balanceLabel.setText("Saldo: ");
            pinButton.disableProperty().set(false);
            cashButton.disableProperty().set(false);
            schrapButton.disableProperty().set(false);
            orderButton.setDisable(true);
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
