package database.data;

import java.util.ArrayList;

public class ProductPriceClass {

	private int id;
	private ArrayList<ProductClass> productClasses;
	private String name;
	
	public ProductPriceClass(int idIn,ArrayList<ProductClass> pcIn,String nameIn)
	{
		id=idIn;
		productClasses = pcIn;
		name=nameIn;
	}
	public ProductPriceClass(int idIn,String nameIn){
		id = idIn;
		name= String.valueOf(nameIn);
		productClasses = new ArrayList<ProductClass>();
	}
	public ProductPriceClass()
	{
		productClasses = new ArrayList<ProductClass>();
	}
	public int getID(){return id;}
	public String getName(){return name;}
	public int getProductID(int classIndex,int productIndex)
	{
		if(classIndex >= 0 && classIndex < productClasses.size())
		return productClasses.get(classIndex).getProductID(productIndex);
		return -9999;
	}
	public int getProductPrice(int classIndex,int productIndex)
	{
		if(classIndex >= 0 && classIndex < productClasses.size())
		return productClasses.get(classIndex).getProductPrice(productIndex);
		return -9999;
	}
	public int getProductVersion(int classIndex,int productIndex)
	{
		if(classIndex >= 0 && classIndex < productClasses.size())
		return productClasses.get(classIndex).getProductVersion(productIndex);
		return -9999;
	}
	public String getProductName(int classIndex,int productIndex)
	{
		if(classIndex >=0 && classIndex < productClasses.size())
			return productClasses.get(classIndex).getProductName(productIndex);
		return "ERROR INDEX OUT OF BOUNDS";
	}
	public ProductClass getProductClass(int classIndex)
	{
		if(classIndex >= 0 && classIndex < productClasses.size())
		return productClasses.get(classIndex);
		return null;
	}
	public Product getProduct(int classIndex,int productIndex)
	{
		if(classIndex >= 0 && classIndex < productClasses.size())
		return productClasses.get(classIndex).getProduct(productIndex);
		return null;
	}
	public int getProductClassesSize(){return productClasses.size();}
	public int getProductsSize(int classIndex){
		if(classIndex >= 0 && classIndex < productClasses.size())
			return productClasses.get(classIndex).getProductsSize();
		return -9999;
	}
	public int getProductClassID(int classIndex)
	{
		if(classIndex >= 0 && classIndex < productClasses.size())
			return productClasses.get(classIndex).getID();
			return -9999;
	}
	
	//setters
	
	public void setID(int idIn){id= idIn;}
	public void setName(String nameIn){name=nameIn;}
	public void setProducts(ArrayList<Product> productsIn, int classIndex)
	{
		productClasses.get(classIndex).setProducts(productsIn);
	}
	public void addProductClass(ProductClass productClassIn){productClasses.add(productClassIn);}
	public void setProductClass(ProductClass productClassIn,int classIndex)
	{
		productClasses.set(classIndex, productClassIn);
	}
	public void addProduct(Product productIn,int classIndex)
	{
		if(classIndex >= 0 && classIndex < productClasses.size())
		productClasses.get(classIndex).addProduct(productIn);
	}
	public void setProduct(Product productIn,int classIndex,int productIndex)
	{
		if(classIndex >= 0 && classIndex < productClasses.size())
			productClasses.get(classIndex).setProduct(productIn, productIndex);
	}
	/**
	 * resets the products and classes to a new empty adress
	 */
	public void reset()
	{
		productClasses = new ArrayList<ProductClass>();
	}
	
}
