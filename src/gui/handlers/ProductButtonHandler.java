/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.handlers;

import database.DataInitializer;
import gui.controllers.BarSheetController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 *
 * @author Jaap
 */
public class ProductButtonHandler extends BarHandler {

    public ProductButtonHandler(BarSheetController bscIn, DataInitializer initIn){
        super(bscIn, initIn);
    }
    
    @Override
    public void handle(ActionEvent event) {
        Button[][] productButtons = bsc.getProductButtons();
        Button[][] orderedButtons = bsc.getOrderedButtons();
        Button[][] orderDecrementers = bsc.getOrderDecrementers();
        Button[][] orderIncrementers = bsc.getOrderIncrementers();
        HBox[][] orderBoxHor = bsc.getOrderBoxHor();
        int[][] orders = bsc.getOrders();
        int multiplication = bsc.getMultiplication();
        VBox orderBox = bsc.getOrderBox();
        Label totalLabel = bsc.getTotalLabel();
        int totalPrice = 0;
        
       for(int i = 0; i < ppc.getProductClassesSize();i++){
			//create eventhandlers for product buttons that are pressed
			for(int j=0; j < ppc.getProductsSize(i);j++)
			{
                if(event.getSource() == productButtons[i][j]){
                    //add the orders to the order array
                    if(orders[i][j] == 0)
					{
                        //initialize the new orderButton
						orderedButtons[i][j] = new Button();
                        orderedButtons[i][j].setMaxWidth(Double.MAX_VALUE);
						orderedButtons[i][j].setOnAction(this);
                        //initialize the incrementer and decremetner button
                        orderDecrementers[i][j] = new Button("-");
                        orderIncrementers[i][j] = new Button("+");
                        orderIncrementers[i][j].setOnAction(this);
                        orderDecrementers[i][j].setOnAction(this);
                        //add them to a horizontal box
                        orderBoxHor[i][j] = new HBox();
                        orderBoxHor[i][j].setMaxWidth(Double.MAX_VALUE);
                        orderBoxHor[i][j].setSpacing(5);
                        orderBoxHor[i][j].setHgrow(orderedButtons[i][j], Priority.ALWAYS);
                        orderBoxHor[i][j].getChildren().addAll(orderedButtons[i][j],orderDecrementers[i][j],orderIncrementers[i][j]);
                        //now add this box to the vertical box
                        orderBox.getChildren().add(orderBoxHor[i][j]);
					}							
					orders[i][j] += multiplication;
					orderedButtons[i][j].setText(orders[i][j] + " x " + ppc.getProductName(i, j));
					bsc.getMultiplicationField().setText("");
					bsc.setMultiplication(1);
                }
                //check if an order is canceled
                if(event.getSource() == orderedButtons[i][j]){
                    orders[i][j] = 0;
                    orderBox.getChildren().remove(orderBoxHor[i][j]);
                }
                //check for - sign pressed
                if(event.getSource() == orderDecrementers[i][j]){
                    //check if there are more then 2 orders remaining
                    if(orders[i][j] >= 2){
                        orders[i][j] --;
                        orderedButtons[i][j].setText(orders[i][j] + " x " + ppc.getProductName(i, j));
                    }
                    //if there is only 1 delete the order
                    else {
                        orders[i][j] = 0;
                        orderBox.getChildren().remove(orderBoxHor[i][j]);
                    }                     
                }
                //check for + sign pressed
                if(event.getSource() == orderIncrementers[i][j])
                {
                    orders[i][j]++;
                    orderedButtons[i][j].setText(orders[i][j] + " x " + ppc.getProductName(i, j));
                }
                totalPrice += orders[i][j] * ppc.getProductPrice(i, j);
            }
		}
       //update the totallabel text
       totalLabel.setText("Totaal " + init.getdf().format((double)totalPrice/100) + "  ");
    }
}
