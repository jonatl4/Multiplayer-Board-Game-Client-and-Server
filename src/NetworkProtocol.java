import java.io.Serializable;

public class NetworkProtocol implements Serializable{
	private ProtocolType dataType;
	private Object data;
	
	public enum ProtocolType{
		TESTCLIENT, TESTSERVER
	}
	
	public NetworkProtocol(ProtocolType sendDataType){
		this.dataType = sendDataType;
		//this.data = sendData;
	}
	
	public ProtocolType getDataType(){
		return dataType;
	}
}
