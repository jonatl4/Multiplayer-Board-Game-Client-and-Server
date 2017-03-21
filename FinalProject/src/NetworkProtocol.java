import java.io.Serializable;
/*
 * Inf 122 Final Project
 * Team Members:
 * Gen Fillipow
 * Jana Abumeri
 * Eva Ruiz
 * Adil rafaa
 * Jonathan  lee
 * David Diep
 * Seth Kruse
 * Brandon Truong*/
public class NetworkProtocol implements Serializable{
	private ProtocolType dataType;
	private Object data;
	
	private Object userData;
	
	
	public enum ProtocolType{
		TESTCLIENT, GAMEOVER, TESTSERVER, ACCOUNTINVALID, ACCOUNTVALID, ACCOUNT, NEWACCOUNT, PRINTLOBBY, STARTGAME, MAKEMOVE, WAIT, CLIENTMOVE, SAVERECORD
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
