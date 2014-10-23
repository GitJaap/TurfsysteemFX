package database;
import java.sql.*;
import java.util.*;

public class DBConnection {
	// connection strings
	private static final String DRIVER = "com.mysql.jdbc.Driver";
	private String URL = "jdbc:mysql://localhost/turf_db";
	private String USERNAME = "test";
	private String PASSWORD = "test";

	private Connection con;
	private Statement st;
	private ResultSet result;
	private ResultSetMetaData rsmd;
	private boolean interrupted = false;

	//default constructor  
	private void startConnection(){
		try {
			//load the mysql driver
			Class.forName(DRIVER);
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		//start the connection with autocommit false and isolation at serializable
		try{
			con = DriverManager.getConnection(URL,USERNAME, PASSWORD);
			con.setAutoCommit(false);
			con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	public DBConnection()
	{
		startConnection();
	}
	// constructor with room for custom database url, username and password
	public DBConnection(String URLin, String USERNAMEin, String PASSWORDin )
	{
		URL=URLin;
		USERNAME=USERNAMEin;
		PASSWORD=PASSWORDin;
		//start the connection
		startConnection();
	}

	//runs but does not commit a query on the database
	public void runQuery(String queryIn)
	{
		try{
			if(!con.isValid(1000)){//check if connection is still available
				interrupted = true;
				startConnection();
			}
			//create an SQL statement
			st = con.createStatement();
			//Run the query
			result = st.executeQuery(queryIn);
			//get metadata of the resultset
			rsmd = result.getMetaData();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}

	//runs a single update on the database
	public void runUpdate(String queryIn)
	{
		try{
			if(!con.isValid(1000)){//check if connection is still available
				interrupted = true;
				startConnection();
			}
			//create an SQL statement
			st = con.createStatement();
			//Run the update
			st.executeUpdate(queryIn);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally{//close the statement
			try {
				st.close();
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}
	}


	//return the ResultSet adress
	public ResultSet getResult()
	{
		return result;
	}

	//returns the next row in a string format spaced with tabs
	public String getNextStr()
	{
		String resultString = "";
		try {
			if(result.next()){
				for (int i = 1; i <= rsmd.getColumnCount();i++)
					resultString += result.getString(i) + "\t";
				return resultString;
			}
			else
				return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	//return the next column in a row in string format
	public String getNextStr(int colNum){
		String resultString;
		try{
			if(result.next()){
				//check if colnum is not greater than the returned columns
				if(0 < colNum && colNum <= rsmd.getColumnCount()){
					resultString = result.getString(colNum);
					return resultString;
				}
			}
			return null;
		}
		catch(SQLException e){
			e.printStackTrace();
			return null;
		}
	}
	//return the next column in a row as an integer
	public int getNextInt(int colNum){
		int resultInt;
		try{
			if(result.next()){
				//check if colnum is not greater than the returned columns
				if(0 < colNum && colNum <= rsmd.getColumnCount()){
					resultInt = result.getInt(colNum);
					return resultInt;
				}
			}
			return -9999; 
		}
		catch(SQLException e){
			e.printStackTrace();
			return -9999;
		}
	}

	//returns all the strings in a column in StringArray form
	public String[] getColumnStr(int colNum)
	{
		int nRows = 0;
		int i = 0;
		String[] ret;
		if(colNum > 0){
			try {
				//get amount of rows
				while(result.next()){
					nRows ++;
				}
				//Check if there are more then 0 rows
				if(nRows == 0)
					return null;
				ret = new String[nRows]; //create the Stringarray with the now known size
				result.first();
				ret[i] = result.getString(colNum);
				i ++;
				while(result.next()){
					ret[i] = result.getString(colNum);
					i++;
				}

			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
			return ret;
		}
		else
		{
			return null;
		}

	}
	//Returns the specified column as an array of integers(works only if columns are integers)
	public int[] getColumnInt(int colNum)
	{
		int nRows = 0;
		int i = 0;
		int[] ret;
		if(colNum > 0){
			try {
				//get amount of rows
				while(result.next()){
					nRows ++;
				}
				//Check if there is more then 0 rows
				if(nRows == 0)
					return null;
				ret = new int[nRows]; //create the Intarray with the now known size
				result.first();
				ret[i] = result.getInt(colNum);
				i ++;
				while(result.next()){
					ret[i] = result.getInt(colNum);
					i++;
				}

			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
			return ret;
		}
		else
		{
			return null;
		}

	}

	//sets the transaction isolation level
	public void setIsolationLevel(int iL)
	{
		try{
			if(iL == Connection.TRANSACTION_NONE 
					|| iL == Connection.TRANSACTION_READ_COMMITTED 
					|| iL == Connection.TRANSACTION_READ_UNCOMMITTED 
					|| iL == Connection.TRANSACTION_REPEATABLE_READ
					|| iL == Connection.TRANSACTION_SERIALIZABLE)
				con.setTransactionIsolation(iL);
		}
		catch(SQLException e)
		{e.printStackTrace();}
	}

	//commits the current transaction
	public void commit()
	{
		try{
			con.commit();
		}
		catch(SQLException e)
		{e.printStackTrace();}
	}
	//rollsback the current transaction
	public void rollback()
	{
		try{
			con.rollback();
		}
		catch(SQLException e)
		{e.printStackTrace();}
	}

	public boolean interrupted(){
		return interrupted;
	}
	public void resetInterrupted(){
		interrupted = false;
	}

	//moves the cursor of the resultset to the next row
	public boolean next(){
		try{
			return result.next();
		}
		catch(SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	//returns a string in the current row at the column specified
	public String getStr(int columnIn)
	{
		try {
			return result.getString(columnIn);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	//returns an int in the cuurent row at the column specified
	public int getInt(int columnIn)
	{
		try {
			return result.getInt(columnIn);
		} catch (SQLException e) {
			e.printStackTrace();
			return -9999;
		}
	}
	

	//gets the number of rows in a resultset
	public int getNumRows()
	{
		int nRows = 0;
		try{
			while(result.next())
				nRows++;
			return nRows;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	//moves the cursor to the first row
	public void gofirst(){
		try {
			result.first();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public boolean getBool(int colNum)
	{
		try{
			return result.getBoolean(colNum);
		}
		catch(SQLException e){
			e.printStackTrace();
			return false;
		}
	}
}