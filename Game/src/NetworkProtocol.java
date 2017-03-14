import java.io.Serializable;

public class NetworkProtocol implements Serializable{
	private ProtocolType dataType;
	private Object data;
	
	public enum ProtocolType{
		TESTCLIENT, TESTSERVER, ACCOUNTINVALID, ACCOUNTVALID, ACCOUNT, NEWACCOUNT, PRINTLOBBY
	}
	
	public NetworkProtocol(ProtocolType sendDataType){
		this.dataType = sendDataType;
		//this.data = sendData;
	}
	
	public NetworkProtocol(ProtocolType inType, Object inMessage)
	{
		/*if ((inType < ACCOUNTVALID) || (inType > NEWACCOUNT))
		{
			throw new IllegalArgumentException("Invalid type argument for a message containing packet");
		}*/
		dataType = inType;
		data = inMessage;
	}
	
	
	public ProtocolType getDataType(){
		return dataType;
	}
	
	/**
	 * Accessor function to return type.
	 * @return the message (data) of the packet.
	 */
	public Object getData()
	{
		return data;
	}
	
}
