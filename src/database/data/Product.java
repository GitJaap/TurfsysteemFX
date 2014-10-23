package database.data;
import java.util.*;

public class Product {
	private int id;
	private int price;
	private int version;
	private String name;
	
	public Product(){}
	public Product(int verIn, int idIn, int priceIn, String nameIn)
	{
		id = idIn;
		price = priceIn;
		name = nameIn;
		version = verIn;
	}
	
	public int getID(){return id;}
	public String getName(){return name;}
	public int getPrice(){return price;}
	public int getVersion(){return version;}
	
	public void setID(int idIn){id= idIn;}
	public void setName(String nameIn){name=nameIn;}
	public void setPrice(int priceIn){price=priceIn;}
	public void setVersion(int versionIn){version=versionIn;}
}
