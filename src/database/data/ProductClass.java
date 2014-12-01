package database.data;

import java.util.ArrayList;

/**
 * Datastructure with getters and setters for the productclass variable read from the database
 * @author Jaap
 */
public class ProductClass {
	private int id;
	private String name;
	private ArrayList<Product> products;
	private String color;
	
	public ProductClass(int idIn, String nameIn, ArrayList<Product> prIn, String colorIn)
	{
		id = idIn;
		name= nameIn;
		products=prIn;
		//create the color from the string with hexadecimal numbers
		color = colorIn;
	}
	
	public ProductClass(int idIn, String nameIn, String colorIn)
	{
		id = idIn;
		name= nameIn;
		//create the color from the string 'red,gre,blu'
		color = colorIn;
		products = new ArrayList<Product>();
	}
	public ProductClass(){products = new ArrayList<Product>();}
	
	public int getID(){return id;}
	public String getName(){return name;}
	public int getProductID(int productIndex)
	{
		if(productIndex >= 0 && productIndex < products.size())
		return products.get(productIndex).getID();
		return -9999;
	}
	public int getProductPrice(int productIndex)
	{
		if(productIndex >= 0 && productIndex < products.size())
		return products.get(productIndex).getPrice();
		return -9999;
	}
	public String getProductName(int productIndex)
	{
		if(productIndex >=0 && productIndex < products.size())
			return products.get(productIndex).getName();
		return "ERROR INDEX OUT OF BOUNDS";
	}
	public int getProductVersion(int productIndex)
	{
		if(productIndex >= 0 && productIndex < products.size())
		return products.get(productIndex).getVersion();
		return -9999;
	}
	public Product getProduct(int productIndex)
	{
		if(productIndex >= 0 && productIndex < products.size())
		return products.get(productIndex);
		return null;
	}
	public int getProductsSize(){return products.size();}
	public String getColor(){return color;}
	
	//setters
	public void setID(int idIn){id= idIn;}
	public void setName(String nameIn){name=nameIn;}
	public void setColor(String colorIn){color=colorIn;}
	public void setProducts(ArrayList<Product> productsIn){products = productsIn;}
	public void addProduct(Product productIn){products.add(productIn);}
	public void setProduct(Product productIn,int productIndex)
	{
		if(productIndex >= 0 && productIndex < products.size())
		products.set(productIndex, productIn);
	}
	
    public String toString(){return name;}
}
