/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers.screens;

import database.DataInitializer;
import database.data.ProductPriceClass;
import database.observable.ObservableProduct;
import gui.controllers.dialogs.AddProductController;
import gui.controllers.dialogs.BookAccountSelectionController;
import gui.controllers.dialogs.ProductBarSelectionController;
import gui.handlers.AdminTabListener;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.TextFlow;
import javafx.util.Callback;
import org.controlsfx.control.PopOver;

/**
 *
 * @author Jaap
 */
public class AdminProductController extends AdminController{
    
    @FXML private HBox barBox;
    @FXML private HBox priceClassBox;
    @FXML private TableView<ObservableProduct> productTable;
    @FXML private TableColumn<ObservableProduct, String> productColumn;
    @FXML private TableColumn<ObservableProduct, String> classColumn;
    @FXML private TableColumn<ObservableProduct, Number> priceColumn;
    @FXML private TextFlow outputFlow;
    @FXML private AnchorPane mainPane;
    

    private Button[] priceClassButtons;
    private Button[] barButtons;
    private int selectedBarID;
    private ObservableList<ObservableProduct> productData = FXCollections.observableArrayList();
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
        tabID = 1;
    }
    
    @Override
    public void delayedInitialize(){
        super.delayedInitialize(); // call initialize from superclass
        
        selectedBarID = init.getCurBar().getID();
        //load the different ppc's and create buttons
        init.getVN().validateProducts(init.getPPC());
        init.reInitializeProducts();
        createPriceClassButtons();
        //load the barbuttons
        init.reInitializeClients();
        createBarButtons();
        initProductData();
        productColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        classColumn.setCellValueFactory(cellData -> cellData.getValue().classNameProperty());
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty());
        
        productTable.setItems(productData);
        
    }
    /**
     * creates buttons belonging to all priceclasses read from the database and reloads the ppc when one is pressed
     */
    public void createPriceClassButtons(){
        priceClassBox.getChildren().clear();
        //get the available classes from the database
        init.getDB().runQuery("SELECT product_price_class_id, class_name FROM product_price_class WHERE is_removed = false");
        init.getDB().commit();
        int[] ids = init.getDB().getColumnInt(1);
        String[] names = init.getDB().getColumnStr(2);
        
        //create the priceclass buttons
        if(ids != null){
            priceClassButtons = new Button[ids.length];
            for(int i =0; i < ids.length; i++){
                priceClassButtons[i] = new Button(names[i]);
                priceClassButtons[i].setPrefWidth(150);
                priceClassButtons[i].setPrefHeight(Double.MAX_VALUE);
                priceClassButtons[i].setAlignment(Pos.CENTER);
                priceClassButtons[i].setWrapText(true);
                priceClassButtons[i].setOnAction((ActionEvent event) -> {
                    for(int j = 0; j < priceClassButtons.length; j++){
                        priceClassButtons[j].setDisable(false);
                        if(event.getSource() == priceClassButtons[j]){
                            //the jth priceclass button has been pressed
                            init.getPPC().setID(ids[j]);
                            init.getPPC().setName(names[j]);
                            init.reInitializeProducts(selectedBarID);
                            priceClassButtons[j].setDisable(true);
                            initProductData();
                        }
                    }
                });
                //add the buttons to the vBOX
                priceClassBox.getChildren().add(priceClassButtons[i]);
               
                //now disable the current priceclass button
                if(ids[i] == init.getPPC().getID()){
                    priceClassButtons[i].setDisable(true);
                }
                
            }
        }
    
        
    }
    /**
     * creates buttons belonging to all bars reda from the database and reloads the ppc when one is pressed
     */
    public void createBarButtons(){
        barBox.getChildren().clear();
        //get the available classes from the database
        
        //create the bar buttons
        barButtons = new Button[init.getBars().size()];
        for(int i =0; i < init.getBars().size(); i++){
            barButtons[i] = new Button(init.getBars().get(i).getName());
            barButtons[i].setPrefWidth(150);
            barButtons[i].setPrefHeight(Double.MAX_VALUE);
            barButtons[i].setWrapText(true);
            barButtons[i].setAlignment(Pos.CENTER);
            barButtons[i].setOnAction((ActionEvent event) -> {
                for(int j = 0; j < barButtons.length; j++){
                    barButtons[j].setDisable(false);
                    if(event.getSource() == barButtons[j]){
                        //the jth barButton button has been pressed
                        selectedBarID = init.getBars().get(j).getID();
                        init.reInitializeProducts(selectedBarID);
                        barButtons[j].setDisable(true);
                        initProductData();
                    }
                }
            });
            //add the buttons to the vBOX
            barBox.getChildren().add(barButtons[i]);

            //now disable the current priceclass button
            if(init.getBars().get(i).getID() == selectedBarID){
                barButtons[i].setDisable(true);
            }

        }
    }
    /**
     *  adds the products of the current ppc and bar to the observablelist personData required for the table
     */
    public void initProductData(){
        ProductPriceClass ppc = init.getPPC();
        productData.clear();
        for(int i =0; i < ppc.getProductClassesSize(); i++){
            for(int j = 0; j < ppc.getProductsSize(i); j++){
                productData.add(new ObservableProduct(ppc.getProductID(i, j),
                                                      ppc.getProductVersion(i, j),
                                                      ppc.getProductName(i, j),
                                                      ppc.getProductClass(i).getName(),
                                                      ppc.getProductClassID(i),
                                                      ppc.getProductPrice(i, j),
                                                      ppc.getProductClass(i).getColor()));
            }
        }
    }
    /**
     * returns the data as an observable list of products fit for use in the table
     * @return the observable list of persons
     */
  
    public void handleNewProduct(ActionEvent e){
        FXMLLoader myLoader =null;
        Parent loadScreen=null;
        AddProductController myScreenController=null;
            try{            
                myLoader = new FXMLLoader(getClass().getResource("/gui/resources/dialogs/AddProductContent.fxml"));
                 loadScreen = (Parent) myLoader.load();
                myScreenController = myLoader.getController();       
                // now add the controller adress to the node so it is usable later in the program
                loadScreen.setUserData(myScreenController);
            }
            catch(IOException ex){ex.printStackTrace();};
        
        
        PopOver pop = new PopOver(loadScreen);
        myScreenController.delayedInitialize(init, pop, outputFlow);
        pop.show((Button)e.getSource());
        pop.setX(mainPane.getWidth() / 2 - pop.getWidth() / 2);
        pop.setY(mainPane.getHeight() / 2 - pop.getHeight() / 2);
        pop.setDetached(true);
        pop.detachedTitleProperty().set("  Vul gegevens in voor nieuw product");
        pop.hideOnEscapeProperty().set(true);
        mainPane.disableProperty().bind(pop.showingProperty());
        pop.showingProperty().addListener(new ChangeListener<Boolean>(){
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue == false){
                    init.reInitializeProducts(selectedBarID);
                    initProductData();
                }
            }
            
        });
    }
    
    public void handleEditProduct(ActionEvent e){
        
    }
    
    public void handleAddClass(ActionEvent e){
        
    }
    
    public void handleAddPriceClass(ActionEvent e){
        
    }
    /**
     * sends a popup window for user to select products per bar
     * @param e 
     */
    public void handleSelectProducts(ActionEvent e){
        FXMLLoader myLoader =null;
        Parent loadScreen=null;
        ProductBarSelectionController myScreenController=null;
            try{            
                myLoader = new FXMLLoader(getClass().getResource("/gui/resources/dialogs/ProductBarSelectionContent.fxml"));
                 loadScreen = (Parent) myLoader.load();
                myScreenController = myLoader.getController();       
                // now add the controller adress to the node so it is usable later in the program
                loadScreen.setUserData(myScreenController);
            }
            catch(IOException ex){ex.printStackTrace();};
        
        
        PopOver pop = new PopOver(loadScreen);
        myScreenController.delayedInitialize(init, pop, productData, selectedBarID);
        pop.show((Button)e.getSource());
        pop.setX(mainPane.getWidth() / 2 - pop.getWidth() / 2);
        pop.setY(mainPane.getHeight() / 2 - pop.getHeight() / 2);
        pop.setDetached(true);
        pop.detachedTitleProperty().set("  Selecteer producten voor deze bar");
        pop.hideOnEscapeProperty().set(true);
        mainPane.disableProperty().bind(pop.showingProperty());
        pop.showingProperty().addListener(new ChangeListener<Boolean>(){
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue == false){
                    init.reInitializeProducts(selectedBarID);
                    initProductData();
                }
            }
            
        });
    }
    
    
}
