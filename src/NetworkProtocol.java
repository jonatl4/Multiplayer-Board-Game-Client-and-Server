import java.io.Serializable;

public class NetworkProtocol implements Serializable{
	private ProtocolType dataType;
	private Object data;
	
	private Object userData;
	
	
	public enum ProtocolType{
		TESTCLIENT, TESTSERVER, ACCOUNTINVALID, ACCOUNTVALID, ACCOUNT, NEWACCOUNT, PRINTLOBBY, STARTGAME, MAKEMOVE, WAIT, CLIENTMOVE
	}
	
	public NetworkProtocol(ProtocolType sendDataType){
		this.dataType = sendDataType;
	}
	
	public NetworkProtocol(ProtocolType inType, GameObject inMessage, GameObject user)
	{
		dataType = inType;
		data      = inMessage;
		userData = user;
	}
	
	public NetworkProtocol(ProtocolType inType, GameObject inMessage)
	{
		dataType = inType;
		data      = inMessage;
	}
	
	
	public ProtocolType getDataType(){
		return dataType;
	}
	
	public Object getData()
	{
		return data;
	}
	
	public Object getUser()
	{
		return userData;
	}
	
}
