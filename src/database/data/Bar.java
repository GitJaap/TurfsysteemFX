package database.data;

import java.util.ArrayList;



public class Bar {
	private int id;
	private String name;
	private ArrayList<Client> clients;
	private int cash;
	
	public Bar(int idIn, String nameIn,  int cashIn,ArrayList<Client> clientsIn){
		id=idIn;
		name=nameIn;
		cash=cashIn;
		clients = clientsIn;	
	}
	public Bar(int idIn, String nameIn,int cashIn){
		id=idIn;
		name=nameIn;
		cash=cashIn;
		clients = new ArrayList<Client>();
	
		}
	public Bar(){clients = new ArrayList<Client>();}
	
	//getters
	public int getID(){return id;}
	public int getCash(){return cash;}
	public String getName(){return name;}
	public ArrayList<Client> getClients(){return clients;}
	public Client getClient(int clientIndex){
		if(clientIndex>=0 && clientIndex < clients.size())
			return clients.get(clientIndex);
		return null;
	}
	public int getClientID(int clientIndex)
	{
		if(clientIndex>=0 && clientIndex < clients.size())
			return clients.get(clientIndex).getID();
		return -9999;
	}
	public String getClientName(int clientIndex)
	{
		if(clientIndex>=0 && clientIndex < clients.size())
			return clients.get(clientIndex).getName();
		return "ERROR INDEX OUT OF BOUNDS";
	}
	public int getClientsSize(){return clients.size();}
	
	
	//setters
	public void setID(int idIn){id = idIn;}
	public void setCash(int cashIn){cash = cashIn;}
	public void setName(String nameIn){name=nameIn;}
	public void addClient(Client clientIn){clients.add(clientIn);}
	public void setClient(Client clientIn,int clientIndex)
	{
		if(clientIndex>=0 && clientIndex < clients.size())
			clients.set(clientIndex, clientIn);
	}
}
	
	
	
