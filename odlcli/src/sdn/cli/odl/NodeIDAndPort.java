package sdn.cli.odl;
/**
 * Objects of this class are used to store the information of host and port when a user is into INTERFACE mode.
 *  * Created by Shahani Waheed Ali waheed.shahani@gmail.com
 * */
public class NodeIDAndPort {
	private String nodeID=null;
	private String portNo=null;
	public boolean setNodeIDAndPortNo(String command){
		
		if(command.indexOf(" ")=="interface".length() && command.indexOf("/")=="interface 00:00:00:00:00:00:00:00".length()){
			String nodeIDAndPort=command.substring(10);
			nodeID=nodeIDAndPort.substring(0, nodeIDAndPort.indexOf("/"));
			portNo=nodeIDAndPort.substring(nodeIDAndPort.indexOf("/")+1);
			return true;
		}
		else {
			this.nodeID=null;
			this.portNo=null;
			return false;
		}
		
		
	}
	public String getNodeID(){
		return this.nodeID;
		
	}
	public String getPortID(){
		return this.portNo;
		
	}
	
	
	
}
