/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.data;

/**
 * Read only object Card similar to client and product. Also imitates the database structure
 * @author Jaap
 */
public class Card {
    private int id;
    private String name;
    private String uid;
    private String atr;
    
    public Card(int idIn, String nameIn, String uidIn, String atrIn){
        id = idIn;
        name = nameIn;
        uid = uidIn;
        atr = atrIn;
    }
    
    public Card(int idIn, String nameIn, String uidIn){
        id = idIn;
        name = nameIn;
        uid = uidIn;
        atr = null;
    }
    
    //getters
    public int getID(){
        return id;
    }
    
    public String getName(){
        return name;
    }
    
    public String getUid(){
        return uid;
    }
    
    public String getAtr(){
        return atr;
    }
    
}
