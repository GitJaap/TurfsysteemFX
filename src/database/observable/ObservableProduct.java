/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.observable;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


/**
 * observable class used in the showing of table data of different products
 * @author Jaap
 */
public class ObservableProduct{
    private final IntegerProperty id;
    private final IntegerProperty versionId;
    private final StringProperty name;
    private final StringProperty className;
    private final IntegerProperty classId;
    private final IntegerProperty price;
    private final StringProperty color;
    
    public ObservableProduct(int id, int versionId, String name,String className,int classID,int price,String color){
        this.id = new SimpleIntegerProperty(id);
        this.versionId = new SimpleIntegerProperty(versionId);
        this.name = new SimpleStringProperty(name);
        this.className = new SimpleStringProperty(className);
        this.classId = new SimpleIntegerProperty(classID);
        this.price = new SimpleIntegerProperty(price);
        this.color = new SimpleStringProperty(color);
    }
    
    public ObservableProduct(int id, String name, int versionId){
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.versionId = new SimpleIntegerProperty(versionId);
        this.className = new SimpleStringProperty(null);
        this.classId = new SimpleIntegerProperty(-9999);
        this.price = new SimpleIntegerProperty(-9999);
        this.color = new SimpleStringProperty(null);
    }
    
    public String toString(){
        return name.get();
    }
    
    //getters
    public int getId(){return id.get();}
    public int getVersionId(){return versionId.get();}
    public String getName(){return name.get();}
    public String getClassName(){return className.get();}
    public int getClassId(){return classId.get();}
    public int getPrice(){return price.get();}
    public String getColor(){return color.get();}
    
    public IntegerProperty versionIdProperty(){return versionId;}
    public IntegerProperty idProperty(){return id;}
    public StringProperty nameProperty(){return name;}
    public StringProperty classNameProperty(){return className;}
    public IntegerProperty classIdProperty(){return classId;}
    public IntegerProperty priceProperty(){return price;}
    public StringProperty colorProperty(){return color;}
    
    //setters
    public void setId(int id){this.id.set(id);}
    public void setName(String name){this.name.set(name);}
    public void setClassName(String className){this.className.set(className);}
    public void setClassId(int classId){this.classId.set(classId);}
    public void setPrice(int price){this.price.set(price);}
    public void setColor(String color){this.color.set(color);}
    
}
