/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.data;

import java.util.ArrayList;

/**
 * Account datastructure represented the same as it is stored in the database
 * @author Jaap
 */
public class Account {
    private int id;
    private String name;
    private int balance;
    private int credit;
    private ArrayList<Card> cards;

    public Account()
    {
        cards = new ArrayList<>();
    }
    public Account(int id, String name,int balance, int credit, ArrayList<Card> cards){
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.credit = credit;
        this.cards = cards;
    }
    
    public Account(int id, String name, int balance, int credit){
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.credit = credit;
        cards = new ArrayList<>();
    }
    
    //getters
    public int getID(){
        return id;
    }
    
    public String getName(){
        return name;
    }
    
    public int getBalance(){
        return balance;
    }
    
    public int getCredit(){
        return credit;
    }
    
    public ArrayList<Card> getCards(){
        return cards;
    }
    
    @Override
    public String toString(){
        return name+ "\t" + Integer.toString(balance / 100) + "," + Integer.toString(Math.abs(balance % 100));
    }
    
    public void setCards(ArrayList<Card> cards){
        this.cards = cards;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public void setBalance(int balance){
        this.balance = balance;
    }
    
    public void setCredit(int credit){
        this.credit = credit;
    }
   
    public void setID(int id){
        this.id = id;
    }
}
