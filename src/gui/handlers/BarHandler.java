/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.handlers;

import database.DataInitializer;
import database.data.ProductPriceClass;
import gui.controllers.BarSheetController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 *
 * @author Jaap
 */
//creates a handler for use in the barScreen
public abstract class BarHandler implements EventHandler<ActionEvent>{
    
    protected ProductPriceClass ppc;
    protected DataInitializer init;
    protected BarSheetController bsc;
    
    public BarHandler(BarSheetController bscIn, DataInitializer initIn){
        bsc = bscIn;
        init = initIn;
        ppc = init.getPPC();
    }
    @Override
    public abstract void handle(ActionEvent event);
    
    
}
