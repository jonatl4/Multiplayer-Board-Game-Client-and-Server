import java.io.Serializable;
import java.util.*;

public class User implements Serializable {
	private String userName; 
	private String password;
	private HashMap<String, Integer> records;
	private int userToken; // won't be set until a game is chosen, i.e. "X" --> 0
	
	public User(){
		this.userName = "";
		this.password = "";
		this.userToken = 0;
		this.records.put("Wins", 0);
		this.records.put("Losses", 0);
		this.records.put("Ties", 0);
		this.records.put("TotalGamesPlayed", 0);
	}
	
	public User(String userName, String password, int token){
		this.userName = userName;
		this.password = password;
		this.token = token;
		this.records.put("Wins", records.get("Wins"));
		this.records.put("Losses", records.get("Losses"));
		this.records.put("Ties", records.get("Ties"));
		this.records.put("TotalGamesPlayed", records.get("Ties"));
	}
	
	public User(User user){
		this.userName = user.userName;
		this.password = user.password;
		this.token = user.token;
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
}
