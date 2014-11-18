package database;

import database.data.ProductPriceClass;

/** 
 * Handles the product transactions
 * @author Jaap
 *
 */
public abstract class Transaction {
	public static final int CASH = 3;
	public static final int PIN = 2;
	public static final int CARD = 1;
    public static final int SCHRAP = 4;
    public static final int PINACCOUNTID = 1;
    public static final int CASHACCOUNTID = 2;
    public static final int SCHRAPACCOUNTID = 3;

	/**
	 * Executes a product transaction by using the main data storage object init and the array of orders
	 * @param init
	 * @param orders
	 * @param transactionType
     * @return A string with information about the succes of the transaction
	 */
	public static String doProductTransaction(DataInitializer init, int[][] orders, int transactionType){
        //first calculate the total price of the transaction
		int totalPrice = 0;
        int accountID;
		for(int i =0; i < init.getPPC().getProductClassesSize();i++)
		{
			for(int j =0; j < init.getPPC().getProductsSize(i);j++)
			{
				if(orders[i][j] > 0){
					totalPrice += orders[i][j] * init.getPPC().getProductPrice(i, j);
				}
			}
		}
		switch(transactionType){
		case CASH:
            //set the account to the account of the transaction type and update the database
            accountID = CASHACCOUNTID;
            //now try and execute the transactions
            if(updateDatabaseWithDebitTransaction(init, orders, accountID, CASH, totalPrice)){
                if(updateDatabaseWithCreditProductTransaction(init, orders, accountID, CASH, totalPrice)){
                    return "Contante Transactie geslaagd!";
                }
            }
            return "Error communicatie database";
		case PIN:
            //set the account to the account of the transaction type and update the database
            accountID = PINACCOUNTID;
            //now try and execute the transactions
            if(updateDatabaseWithDebitTransaction(init, orders, accountID, PIN, totalPrice)){
                if(updateDatabaseWithCreditProductTransaction(init, orders, accountID, PIN, totalPrice)){
                    return "PIN-Transactie geslaagd!";
                }
            }
            return "Error communicatie database";
        case SCHRAP:
            //set the account to the account of the transaction type and update the database
            accountID = SCHRAPACCOUNTID;
            //now try and execute the transactions
            if(updateDatabaseWithDebitTransaction(init, orders, accountID, SCHRAP, totalPrice)){
                if(updateDatabaseWithCreditProductTransaction(init, orders, accountID, SCHRAP, totalPrice)){
                    return "Schrapkaart Transactie geslaagd!";
                }
            }
            return "Error communicatie database";
        case CARD:
			accountID = CardReader.readCard();
			//now select the account and check its balance also locking the row so no one else can now read from it
			long t = System.currentTimeMillis();
			init.getDB().runQuery(String.format("SELECT balance FROM accounts WHERE account_id = %d FOR UPDATE",accountID ));
			int balance = init.getDB().getNextInt(1);
			//now check if balance>totalprice and quit otherwise
			if(balance >= totalPrice){
                if(updateDatabaseWithCreditProductTransaction(init,orders,accountID, CARD, totalPrice)){
                    System.out.println("Tijd nodig voor CARD transactie was: " + t);
                    return "Kaart transactie Geslaagd!";
                }
                else
                    return "Error communicatie Database";
            }
			else{
				init.getDB().rollback();
				System.out.println("Tijd nodig voor de transactie was: "+ Long.toString(System.currentTimeMillis()-t)+ " ms");
				return "Niet genoeg geld op de pas";
			
			}
		default:
			return "fout";
		}
	}
    /**
     * Updates the credit transaction table and the product_transaction table of the database with an order specified in the orders array. Also updates the account balance.
     * uses the main DataInitializer object to get all the products and the current open database connection, the orders array should always be directly coupled to the current PPC object in the initializer object.
     * @param init
     * @param orders
     * @param accountID
     * @param transactionType
     * @param totalPrice
     * @return 
     */
    private static boolean updateDatabaseWithCreditProductTransaction(DataInitializer init, int[][] orders, int accountID,int transactionType, int totalPrice){
        int adminID = init.getAdminID();
        int clientID = init.getCurClient().getID();
        //create a boolean value to store the succes of the transaction
        boolean succes = true;
        //Check the last transaction_id and read for update so no one else can do a transaction in the meantime
		succes &= init.getDB().runQuery("SELECT MAX(transaction_credit_id) from transactions_credit FOR UPDATE");
		int transactionID = init.getDB().getNextInt(1)+1; //set the current id to one transaction higher
		//create the transaction one id higher 
		if(transactionID != -9998){//error check
			succes &= init.getDB().runUpdate(String.format("INSERT INTO transactions_credit VALUES (%d, %d, %d, %d, %d, NOW(), %d)",transactionID, accountID,adminID,clientID,transactionType, totalPrice));
			//again run through all bought products
			for(int i =0; i < init.getPPC().getProductClassesSize();i++){
				for(int j =0; j < init.getPPC().getProductsSize(i);j++){
					if(orders[i][j] > 0){
                        //update the product_transaction table with the ordered products
						succes &= init.getDB().runUpdate(String.format("INSERT INTO product_transactions"
                    				+ "(product_version_id,product_type_id,transaction_credit_id,product_amount)"
									+ " VALUES( %d,%d,%d,%d)", init.getPPC().getProductVersion(i, j),init.getPPC().getProductID(i, j),transactionID,orders[i][j] ));
					}
				}
			}
            //update the account balance
			succes &= init.getDB().runUpdate(String.format("UPDATE accounts SET balance=balance-%d WHERE account_id = %d",totalPrice,accountID));
        	//finally commit the transaction to free the locked rows
			succes &= init.getDB().commit();
            return succes;
        }
        else{// an error has occured with retrieving the index
            init.getDB().rollback();
            return false;
        }
    }
    /**
     * Updates the database transactions_debit table with a transaction, also updates the corresponding account balancce
     * uses the main DataInitializer object to get the current database connection.
     * @param init
     * @param orders
     * @param accountID
     * @param transactionType
     * @param totalPrice
     * @return 
     */
    private static boolean updateDatabaseWithDebitTransaction(DataInitializer init, int[][] orders, int accountID, int transactionType, int totalPrice){
        int adminID = init.getAdminID();
        int clientID = init.getCurClient().getID();
        //create a boolean value to store the succes of the transaction
        boolean succes = true;
        //Check the last transaction_id and read for update so no one else can do a transaction in the meantime
		succes &= init.getDB().runQuery("SELECT MAX(transaction_debit_id) FROM transactions_debit FOR UPDATE");
		int transactionID = init.getDB().getNextInt(1)+1; //set the current id to one transaction higher
        if(transactionID != -9998){//error check
            //create the new transaction
            succes &= init.getDB().runUpdate(String.format("INSERT INTO transactions_debit VALUES (%d, %d, %d, %d, %d, NOW(), %d)",transactionID, accountID,adminID, clientID, transactionType, totalPrice));
            //update the account balance
			succes &= init.getDB().runUpdate(String.format("UPDATE accounts SET balance=balance-%d WHERE account_id = %d",totalPrice,accountID));
            //finally commit the transaction to free the locked rows
			succes &= init.getDB().commit();
            return succes;
        }
        return false;
    }
}
