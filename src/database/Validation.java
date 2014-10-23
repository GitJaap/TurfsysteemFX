package database;
//This class takes care of all local data-validation by checking if there has not been an update to certain data since last check
import database.*;
import database.data.ProductPriceClass;

import java.util.*;
public class Validation {
	private final int CLIENT_UPDATE_INTERVAL_SEC = 2;
	
	private DBConnection dB;
	private int lastClientLogID;
	private int lastAdminChangeID;
	
	public Validation(DBConnection dBIn)
	{
		dB = dBIn;
		lastClientLogID = 0;
		lastAdminChangeID = 0;
	}
	
	//check for clients that have not send an update for more then CLIENT_UPDATE_INTERVAL seconds
	public boolean validateClients()
	{
		boolean ret = true;
		String clientIDString = "";
		ArrayList<Integer> clientIDs = new ArrayList<Integer>();
		dB.runQuery("SELECT client_id,TIME_TO_SEC(TIMEDIFF(NOW(),last_client_update)) FROM clients WHERE client_is_active = true");
		while(dB.next())
		{
			if(dB.getInt(2) > CLIENT_UPDATE_INTERVAL_SEC)
			{
				ret = false;
				clientIDs.add(dB.getInt(1));
				clientIDString += "OR client_id = " +Integer.toString(clientIDs.get(clientIDs.size()-1)) + " ";
			}
		}
		//now change the inactive client_is_active states to false
		dB.runUpdate("UPDATE clients SET client_is_active = false WHERE 1=2 "+ clientIDString);
		for(int i =0; i < clientIDs.size();i++)
		dB.runUpdate(String.format("INSERT INTO client_logs(client_id,client_log_type,log_date) VALUES (%d , false, '0000-00-00 00:00:00')",clientIDs.get(i)));
		dB.commit();
		return ret;
		
	}
	
	//check for more recent logs than currently known and report if false.
	public boolean validateLastClientLog()
	{
		dB.runQuery("SELECT client_log_id from client_logs ORDER BY client_log_id DESC LIMIT 1");
		dB.commit();
		int newID = dB.getNextInt(1);
		if(newID == lastClientLogID)
			return true;
		else if(newID == -9999)
			return true;
		else
		{
			lastClientLogID = newID;
			return false;
		}
	}
	//check for recent changes in admin settings(for example product modification) and if so change price class
	public boolean validateProducts(ProductPriceClass ppcIn)
	{
		dB.runQuery("SELECT admin_change_id,current_product_price_class_id FROM admin_changes ORDER BY admin_change_id DESC LIMIT 1");
		dB.commit();
		int newAdminID = dB.getNextInt(1);
		int newPPCID = dB.getInt(2);
		if(lastAdminChangeID == newAdminID){
			return true;
		}
		else if(newAdminID == -9999)//check for the possibility of 0 rows returned
		{
			ppcIn.setID(1); // if no admin change has been admitted set ppc.id to the first class
			dB.runQuery(String.format("SELECT class_name FROM product_price_class WHERE product_price_class_id = %d",1));
			dB.commit();
			ppcIn.setName(dB.getNextStr(1));
			return true;
		}
		else{//we know for sure newPPCID has a valid value
			//create a new ppc
			ppcIn.reset();
			ppcIn.setID(newPPCID);
			lastAdminChangeID = newAdminID;
			dB.runQuery(String.format("SELECT class_name FROM product_price_class WHERE product_price_class_id = %d",newPPCID));
			dB.commit();
			ppcIn.setName(dB.getNextStr(1));
			
			return false;
		}
	}
}
