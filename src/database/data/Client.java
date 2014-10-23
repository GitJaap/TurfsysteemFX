package database.data;

public class Client {
	private int id;
	private String name;

	public Client(int idIn, String nameIn){
		id=idIn;
		name=nameIn;
	}
	public Client()
	{
	}
	
	//getters
	public int getID(){return id;}
	public String getName(){return name;}
	
	//setters
	public void setID(int idIn){id = idIn;}
	public void setName(String nameIn){name=nameIn;}
	
}
