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
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author Jaap
 */
public class BarSheetController implements Initializable, ControlledScreen {
    
    public final int NProductCols = 5;
    public final int MAXProducts = 20;    
    
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
    @FXML private ImageView fotoView;
    //declare components to fit in productPane
    //since they vary they cannot be defined in FXML
    private Button[][] productButtons;
    private Button[][] orderedButtons;
    private Button[][] orderDecrementers;
    private Button[][] orderIncrementers;
    private HBox[][] orderBoxHor;
    private int[][] orders;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        multiplication = 1;
        historyText = "";
    }    
    
    public void initBarProducts()
    {
        //load the products from the database
        ppc = init.getPPC();
        init.getVN().validateProducts(ppc);
        init.reInitializeBar();
        productButtonHandler = new ProductButtonHandler(this,init);
        System.out.println(ppc.getID());
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
        String tempText = Transaction.doProductTransaction(init, orders, curPaymentMethod, 1, 1)+"\n";
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
            fotoView.disableProperty().set(true);
            ageLabel.setDisable(true);
            balanceLabel.setDisable(true);
            balanceLabel.setText("Saldo: ");
            pinButton.disableProperty().set(false);
            cashButton.disableProperty().set(false);
            schrapButton.disableProperty().set(false);
            orderButton.setDisable(true);
        }
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
}
