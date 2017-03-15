import java.io.Serializable;

public class NetworkProtocol implements Serializable{
	private ProtocolType dataType;
	private Object data;
	
	public enum ProtocolType{
		TESTCLIENT, TESTSERVER, ACCOUNTINVALID, ACCOUNTVALID, ACCOUNT, NEWACCOUNT, PRINTLOBBY, STARTGAME, CLIENTMOVE, MAKEMOVE, WAIT
	}
	
	public NetworkProtocol(ProtocolType sendDataType){
		this.dataType = sendDataType;
	}
	
	public NetworkProtocol(ProtocolType inType, GameObject inMessage)
	{
		dataType = inType;
		data = inMessage;
	}
	
	public ProtocolType getDataType(){
		return dataType;
	}
	
	public Object getData()
	{
		return data;
	}
	
}
