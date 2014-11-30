package database.data;

/**
 * Datastructure with getters and setters for the client variable read from the database
 * @author Jaap
 */
public class Client {
	private int id;
	private String name;
    private Bar bar;

	public Client(int idIn, String nameIn, Bar barIn){
		id=idIn;
		name=nameIn;
        bar = barIn;
	}
	public Client()
	{
	}
	
	//getters
	public int getID(){return id;}
	public String getName(){return name;}
	public Bar getBar(){return bar;}
	//setters
	public void setID(int idIn){id = idIn;}
	public void setName(String nameIn){name=nameIn;}
    
    //overide toString function used in choicebox
    public String toString(){
        return bar.getName() + " - " + name;
    }

	
}
