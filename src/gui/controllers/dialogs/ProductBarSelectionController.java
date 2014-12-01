/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers.dialogs;

import database.DataInitializer;
import database.data.Account;
import database.observable.ObservableProduct;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import static javafx.collections.FXCollections.copy;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.controlsfx.control.ListSelectionView;
import org.controlsfx.control.PopOver;

/**
 * FXML Controller class
 *
 * @author Jaap
 */
public class ProductBarSelectionController implements Initializable {

    @FXML ListView sourceView;
    @FXML ListView targetView;
    @FXML TextField inputField;
    
    private DataInitializer init;
    private PopOver parentPop;
    private ObservableList<ObservableProduct> productData =  FXCollections.observableArrayList();;
    private int barID;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    public void delayedInitialize(DataInitializer initIn, PopOver pop, ObservableList<ObservableProduct> productData, int barID){
        init = initIn;
        parentPop = pop;
        copy(productData,this.productData);
 
        this.barID = barID;
        //add listener to input text
        handleInputTextChange();
        inputField.textProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // now run the text change handler
                handleInputTextChange();
      }
        });
        //load the previous products into the target list
        targetView.setItems(productData);
       
        
        
    }
    
    
    
    public void handleInputTextChange(){
        
        String input = inputField.getText();
        sourceView.getItems().clear();
        //now start searching for products
        try{
            PreparedStatement query = init.getDB().getCon().prepareStatement("Select p.product_name, p.product_type_id, p.product_version_id FROM product_types as p INNER JOIN (SELECT MAX(product_version_id) as product_version_id,product_type_id " +
					"FROM product_types GROUP BY product_type_id) as ss " + 
					"Using(product_version_id,product_type_id) WHERE p.is_removed = false AND p.product_name LIKE ?"); 
            query.setString(1, '%' + input + '%');
            init.getDB().executePreparedStatement(query);
            init.getDB().commit();
        }
        catch(SQLException ex){ // SQL ERROR occured in statement
            ex.printStackTrace();
        }
        while(init.getDB().next()){
            sourceView.getItems().add(new ObservableProduct(init.getDB().getInt(2), init.getDB().getStr(1), init.getDB().getInt(3)));
        }
        
    }
    /**
     * handles items going from left to right in the lsits source to target
     * @param e 
     */
    public void handleSourceToTarget(ActionEvent e){
        if(!sourceView.getSelectionModel().isEmpty()){
            boolean alreadyPresent = false;
            ObservableProduct selectedProduct = (ObservableProduct)sourceView.getSelectionModel().getSelectedItem();
            //check if account is already in the target list
            for(int i = 0; i < targetView.getItems().size(); i++){
                if(((ObservableProduct)targetView.getItems().get(i)).getId() == selectedProduct.getId())
                    alreadyPresent = true;                
            }
            if(!alreadyPresent)
                targetView.getItems().add(selectedProduct);
        }
    }
     /**
     * Deletes an item in the right list(targetList)
     * @param e 
     */
    public void handleTargetToSource(ActionEvent e){
        if(!targetView.getSelectionModel().isEmpty()){
            targetView.getItems().remove(targetView.getSelectionModel().getSelectedItem());
        }
    }
    
    /**
     * deletes all items in the right list(targetList)
     * @param e 
     */
    public void handleAllTargetToSource(ActionEvent e){
        targetView.getItems().clear();
    }
    
    public void handleAllSourceToTarget(ActionEvent e){
        for(int j = 0; j < sourceView.getItems().size(); j++){
            boolean alreadyPresent = false;
            ObservableProduct selectedProduct = (ObservableProduct)sourceView.getItems().get(j);
            //check if account is already in the target list
            for(int i = 0; i < targetView.getItems().size(); i++){
                if(((ObservableProduct)targetView.getItems().get(i)).getId() == selectedProduct.getId())
                    alreadyPresent = true;                
            }
            if(!alreadyPresent)
                targetView.getItems().add(selectedProduct);
        }
    }
    /**
     * handles the pressing of the save button
     * Saves all current visibile products in the database
     * @param e 
     */
    public void handleSaveButton(ActionEvent e){
        boolean result = true;
        //delete old bar visibility entries
        result &= init.getDB().runUpdate(String.format("DELETE FROM product_bar_visibility WHERE bar_id = %d", barID));
            //run updates to the database
            for(int i =0; i < targetView.getItems().size(); i++){
                 result &= init.getDB().runUpdate(String.format("INSERT INTO product_bar_visibility(product_type_id, product_version_id, bar_id, product_bar_visibility) Values(%d, %d, %d, true)", 
                         ((ObservableProduct)targetView.getItems().get(i)).getId(), ((ObservableProduct)targetView.getItems().get(i)).getVersionId(), barID));
            }
        if(result){
            //send a log of the update
            init.getDB().runQuery("SELECT current_product_price_class_id FROM admin_changes ORDER BY admin_change_id DESC LIMIT 1");
            init.getDB().runUpdate(String.format("INSERT INTO admin_changes(current_product_price_class_id, admin_id, admin_change_date, admin_change_description) VALUES (%d, %d, NOW(), 'Bar visibility changed')", init.getDB().getNextInt(1), barID));
            init.getDB().commit();
        }
        else{
            init.getDB().rollback();
        }
        parentPop.hide();
    }
    
    public void handleCancelButton(ActionEvent e){
        parentPop.hide();
    }
    
}
