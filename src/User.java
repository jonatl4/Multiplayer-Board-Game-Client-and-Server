import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;

public class User extends GameObject{
	private String userName; 
	private String password;
	private HashMap<String, Integer> records;
	private int userToken; // won't be set until a game is chosen, i.e. "X" --> 0
	
	
	
	
	public User(){
		records = new HashMap<String, Integer>();
		this.userName = "";
		this.password = "";
		this.userToken = 0;
		this.records.put("Wins", 0);
		this.records.put("Losses", 0);
		this.records.put("Ties", 0);
		this.records.put("TotalGamesPlayed", 0);
	}
	
	
	public User(String userName, String password){
		records = new HashMap<String, Integer>();
		this.userName = userName;
		this.password = password;
		this.userToken = 0;
		this.records.put("Wins", 0);
		this.records.put("Losses", 0);
		this.records.put("Ties", 0);
		this.records.put("TotalGamesPlayed", 0);
	}
	
	public User(String userName, String password, int token){
		records = new HashMap<String, Integer>();
		this.userName = userName;
		this.password = password;
		this.userToken = token;
		this.records.put("Wins", records.get("Wins"));
		this.records.put("Losses", records.get("Losses"));
		this.records.put("Ties", records.get("Ties"));
		this.records.put("TotalGamesPlayed", records.get("Ties"));
	}
	
	public User(User user){
		
		this.userName = user.userName;
		this.password = user.password;
		this.userToken = user.userToken;
		this.records.putAll(records);
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public HashMap<String, Integer> getRecords() {
		return records;
	}

	public void setRecords(HashMap<String, Integer> records) {
		this.records = records;
	}
	
	public int getRecord(String type){
		return this.records.get(type);
	}
	
	public void increaseRecord(String type){
		this.records.replace(type, this.records.get(type)+1);
		this.records.replace("TotalGamesPlayed", this.records.get("TotalGamesPlayed")+1);
	}

	public int getUserToken() {
		return userToken;
	}

	public void setUserToken(int userToken) {
		this.userToken = userToken;
	}
	
	/* added */
	// Static methods (to manage accounts) start here
	private static Hashtable<String, User> database = new Hashtable<String, User>();
	/**
	 * Loads the account list from an existing file and applies it
	 * database
	 */
	public static void loadAccounts()
	{
		File extFile = new File("accounts.obj");
		if(extFile.exists()) // if the file exists, read it in
		{
			FileInputStream inFile;
			try
			{
				inFile = new FileInputStream(extFile);
				ObjectInputStream inStream = new ObjectInputStream(inFile);
				database = (Hashtable<String, User>)inStream.readObject();
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			catch (ClassNotFoundException e)
			{
				e.printStackTrace();
			}			
		}
	}
	
	/**
	 * Saves the updated account list.
	 * @param database - the account list to save
	 */
	public static void saveAccounts()
	{
		FileOutputStream outFile;  // the below is file saving
		try
		{
			outFile = new FileOutputStream("accounts.obj");
			ObjectOutputStream outStream = new ObjectOutputStream(outFile);
			outStream.writeObject(database);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Validates the account.
	 * @param database
	 * @param inAccount
	 * @return - whether the account is valid or not
	 */
	public static boolean validate(User inAccount)
	{
		User temp = database.get(inAccount.getUserName());
		if(temp!=null)
		{
			if(temp.password.equals(inAccount.password))
			return true;
		}
		return false;
	}
	
	/**
	 * Adds a new account to the database.
	 * @param database
	 * @param inAccount - the new account
	 * @return
	 */
	public static void addAccount(User inAccount)
	{
		database.put(inAccount.getUserName(), inAccount);
	}
	
	/**
	 * Checks if an account exists.
	 * @param database
	 * @param inAccount
	 * @return  - whether the account exists or not.
	 */
	public static boolean exists(User inAccount)
	{
		return (database.containsKey(inAccount.userName));
	}
	
	public static User retrieve(String key)
	{
		return database.get(key);
	}
	
	/**
	 * Clones the account.
	 */
	public User clone()
	{
		User clonedAccount = new User("temp", "temp");
		clonedAccount.userName = this.userName;
		clonedAccount.password = this.password;
		
		return clonedAccount;
	}
	
	
	
}
