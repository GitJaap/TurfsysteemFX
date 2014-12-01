package database;
import java.util.*;
import java.text.*;

import database.*;
import database.data.Bar;
import database.data.Client;
import database.data.Product;
import database.data.ProductClass;
import database.data.ProductPriceClass;
/**
* This class reads the database to initialize the GUI variables and servers as the main namespace for all data/database/variable acces
* 
* 
**/
public class DataInitializer {
    
    //login variables used in creating logs in the close operation
    private int logType;
    public static final int NOT_LOGGED_IN = 0;
    public static final int USER_LOGGED_IN = 1;
    public static final int ADMIN_LOGGED_IN = 2;
	//variables
	private DBConnection dB = new DBConnection(); //initialize database connection	
	private Validation vn = new Validation(dB);
	//startup variables
	private ArrayList<Bar> bars;
	//bar variables
	private ProductPriceClass ppc;
	private Client curClient;
	private Bar curBar;
    private int adminID;
    private String adminName;
    //create a format for displaying cash amount
	private DecimalFormatSymbols otherSymbols;
	private DecimalFormat df;
    

	public DataInitializer()
	{
		// create the right format for displaying the cash amounts
        otherSymbols = new DecimalFormatSymbols(Locale.GERMAN);
		otherSymbols.setDecimalSeparator(',');
		otherSymbols.setGroupingSeparator('.'); 
		df = new DecimalFormat("\u20ac0.00", otherSymbols);
		//initialize other variables
        ppc = new ProductPriceClass();
        logType = NOT_LOGGED_IN;

	}
	public void reInitializeClients(){
		//check if all logged-in clients are actually logged in, otherwise log them out
		vn.validateClients();
		//retrieve only the visible bars from the database
		dB.runQuery("Select bar_id, bar_name, current_bar_cash FROM bars WHERE bar_visibility = true");
		dB.commit();
		bars = new ArrayList<>();
		while(dB.next())
			bars.add(new Bar(dB.getInt(1),dB.getStr(2),dB.getInt(3)));
		
			//retrieve visible client Pc's from the database and store them with their respective bars
		for(int j =0; j < bars.size(); j++)
		{
			dB.runQuery(String.format("Select client_id,client_name FROM clients WHERE bar_id = %d AND client_visibility = true AND client_is_active = false",bars.get(j).getID()));
			dB.commit();
			while(dB.next())
				bars.get(j).addClient(new Client(dB.getInt(1),dB.getStr(2), bars.get(j)));
		}
	}
    /**
     * Reads and saves the products from the database belonging to the current price class and current bar in this dataInitializer object as ppc
     * 
     */
	public void reInitializeProducts(){
        loadProducts(curBar.getID());
	}
      /**
     * Reads and saves the products from the database belonging to the current price class'and a specified bar in this dataInitializer object as ppc
     * 
     */
    public void reInitializeProducts(int barID){
        loadProducts(barID);
    }
    
    private void loadProducts(int barID){
        //now insert for this price_class the product_classes like eten drinken shift
        ppc.reset();
		dB.runQuery("Select product_class_id,class_name,class_color_hex from product_class");
		dB.commit();
		while(dB.next())
			ppc.addProductClass(new ProductClass(dB.getInt(1),dB.getStr(2),dB.getStr(3)));

		//insert the visible products for this bar for each class and the current price class and the latest version id into ppc
		for(int j=0; j < ppc.getProductClassesSize();j++)
		{
			dB.runQuery(String.format("SELECT p.product_version_id,p.product_type_id,ppc.product_price,p.product_name "
					+ "FROM product_types as p "
					+ "INNER JOIN( "
					+ "SELECT MAX(product_version_id) as product_version_id,product_type_id "
					+ "FROM product_types GROUP BY product_type_id) as ss "
					+ "Using(product_version_id,product_type_id) "
					+ "INNER JOIN product_bar_visibility as b "
					+ "USING (product_version_id,product_type_id) "
                    + "INNER JOIN price_per_product_price_class as ppc USING (product_version_id, product_type_id)"
					+ "WHERE p.product_class_id = %d "
					+ "AND ppc.product_price_class_id = %d "
					+ "AND b.product_bar_visibility = true "
					+ "AND b.bar_id = %d;"
					,ppc.getProductClassID(j),ppc.getID(), barID));
			dB.commit();
			while(dB.next())
			ppc.addProduct(new Product(dB.getInt(1),dB.getInt(2),dB.getInt(3),dB.getStr(4)),j);
		}
    }

	//returns an array with all visible bars
	public ArrayList<Bar> getBars(){
		return bars;
	}
	//return the current active productpriceClass
	public ProductPriceClass getPPC(){
		return ppc;
	}
	//returns a decimalformat used to format product prices
	public DecimalFormat getdf()
	{return df;}
	//returns the DBConnection. Used for updates.
	public DBConnection getDB()
	{
		return dB;
	}
	public Validation getVN()
	{
		return vn;
	}
	public void setCurClient(Client curClientIn)
	{
		curClient = curClientIn;
	}
	public void setCurBar(Bar curBarIn)
	{
		curBar = curBarIn;
	}
	public Client getCurClient()
	{
		return curClient;
	}
	public Bar getCurBar()
	{
		return curBar;
	}
    public int getAdminID(){
        return adminID;
    }
    public void setAdminID(int IDin){
        adminID = IDin;
    }
    public int getLogType(){
        return logType;
    }
    
    public void setLogType(int logTypeIn){
        logType = logTypeIn;
    }
    
    public void setAdminName(String name){adminName = name;}
    public String getAdminName(){return adminName;}
}
