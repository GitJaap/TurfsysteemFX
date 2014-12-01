/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers.dialogs;

import database.DataInitializer;
import database.data.Bar;
import database.data.ProductClass;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.controlsfx.control.PopOver;

/**
 *
 * @author Jaap
 */
public class AddProductController {
    
    private DataInitializer init;
    
    @FXML TextField nameField;
    @FXML ChoiceBox classSelector;
    @FXML ListView barView;
    @FXML VBox priceBox;
    String[] priceClassNames;
    
    private int[] priceClassIDs;
    private HBox[] rows;
    private TextField[] inputFields;
    private Label[] nameLabels;
    private PopOver pop;
    private List selectedBars;
    private TextFlow outputFlow;
    
    
    public void delayedInitialize(DataInitializer init, PopOver pop, TextFlow outputFlow){
        this.init = init;
        this.pop = pop;
        this.outputFlow = outputFlow;
        //load the different classes
        loadClasses();
        //load the different priceClasses
        //get the available classes from the database
        init.getDB().runQuery("SELECT product_price_class_id, class_name FROM product_price_class WHERE is_removed = false");
        init.getDB().commit();
        priceClassIDs = init.getDB().getColumnInt(1);
        priceClassNames = init.getDB().getColumnStr(2);
        if(priceClassIDs != null){
            rows = new HBox[priceClassIDs.length];
            inputFields = new TextField[priceClassIDs.length];
            nameLabels = new Label[priceClassIDs.length];
            for(int i = 0; i < priceClassIDs.length;i++){
                nameLabels[i] = new Label(priceClassNames[i]);
                nameLabels[i].setPrefWidth(150);
                inputFields[i] = new TextField("0");
                inputFields[i].setPrefWidth(80);
                rows[i] = new HBox();
                rows[i].getChildren().addAll(nameLabels[i], inputFields[i]);
                priceBox.getChildren().add(rows[i]);
                priceBox.setSpacing(5);
                
                //add listener to textfields so only numbers can be entered
                final int index = i;
                inputFields[i].textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                if (newValue.matches("\\d*") && newValue.length() < 6) {
                    //no action required
                } else {
                    inputFields[index].setText(oldValue);
                }
            });
            }
        }
        
        //load the avalaible bars
        init.reInitializeClients();
        for(int i =0; i < init.getBars().size(); i++){
            barView.getItems().add(init.getBars().get(i));
        }
        //allow for multiple selection
        barView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        selectedBars = barView.getSelectionModel().getSelectedItems();
          
    }
    
    /**
     * loads all product classes and puts them in selectionbox
     */
    public void loadClasses(){
        //take classes from previously initialized ppc object
        for(int i = 0; i < init.getPPC().getProductClassesSize(); i++){
            classSelector.getItems().add(init.getPPC().getProductClass(i));
        }
        classSelector.getSelectionModel().selectFirst();
    }
    
    public void handleSave(ActionEvent e){
        String productName = nameField.getText();
        if(productName.length() >= 3)
        {
            //check if name is unique
            try{
                PreparedStatement query = init.getDB().getCon().prepareStatement("SELECT product_type_id FROM product_types WHERE product_name = ? AND is_removed=false");
                query.setString(1, productName);
                init.getDB().executePreparedStatement(query);
                init.getDB().commit();
                if(init.getDB().getNextInt(1) == -9999){ //name doesnt exist yet
                    //get the new product id
                    init.getDB().runQuery("SELECT product_type_id From product_types ORDER BY product_type_id DESC LIMIT 1 FOR UPDATE");
                    int newID = 1+ init.getDB().getNextInt(1);
                    if(newID == -9998){//first product is being added
                        newID = 1;
                    }
                    //save the product
                    query = init.getDB().getCon().prepareStatement("INSERT INTO product_types(product_type_id, product_version_id, product_class_id, product_price_class_id, product_price, product_name, creation_date, is_removed, supply_product_id, supply_product_percentage ) " + 
                            "VALUES (?, 1, ?, 1, 0, ?, NOW(), false, 1 , 100)");
                    query.setInt(1, newID);
                    query.setInt(2, ((ProductClass)classSelector.getSelectionModel().getSelectedItem()).getID());
                    query.setString(3, productName);
                    query.executeUpdate();
                    //add visibility to bars
                    for(int i =0; i < selectedBars.size(); i++){
                        init.getDB().runUpdate(String.format("INSERT INTO product_bar_visibility(product_type_id, product_version_id, bar_id, product_bar_visibility) VALUES (%d, 1, %d, true)",
                                newID, ((Bar)selectedBars.get(i)).getID()));
                    }
                    //add prices to priceClass
                    for(int i =0; i < priceClassIDs.length; i++){
                        init.getDB().runUpdate(String.format("INSERT INTO price_per_product_price_class(product_version_id, product_type_id, product_price_class_id, product_price) VALUES (1, %d, %d, %d)",
                                newID, priceClassIDs[i], Integer.parseInt(inputFields[i].getText())));
                    }
                    init.getDB().commit();
                    //send a log of the update
                    init.getDB().runQuery("SELECT current_product_price_class_id FROM admin_changes ORDER BY admin_change_id DESC LIMIT 1");
                    init.getDB().runUpdate(String.format("INSERT INTO admin_changes(current_product_price_class_id, admin_id, admin_change_date, admin_change_description) VALUES (%d, %d, NOW(), 'Bar visibility changed')", init.getDB().getNextInt(1), init.getAdminID()));
                    init.getDB().commit();
                    outputFlow.getChildren().add(new Text("product toegevoegd!"));
                
                                
                }
                else
                    outputFlow.getChildren().add(new Text("productnaam bestaat al"));
                
            }
            catch(SQLException ex){ex.printStackTrace();}
            
        }
        else
            outputFlow.getChildren().add(new Text("productnaam is ongeldig"));
        pop.hide();
    }
    
    public void handleCancel(ActionEvent e){
        pop.hide();
    }
    
    public void handleCopyDown(ActionEvent e){
        if(inputFields != null){
            String value = inputFields[0].getText();
            for(int i =0; i< inputFields.length; i++){
                inputFields[i].setText(value);
            }
        }
    }
    
    
}
