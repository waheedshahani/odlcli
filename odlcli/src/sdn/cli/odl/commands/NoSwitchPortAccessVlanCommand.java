package sdn.cli.odl.commands;
/****
 * 
 * This command is used to remove the access VLAN for particular port. Vlan number need not to be specified
 * this can only be executed in INTERFACE mode.
 * This removes all the flow rules which has been installed for that particular VLAN
 * 
 *  * Created by Shahani Waheed Ali waheed.shahani@gmail.com
 * ***/
import javax.net.ssl.HttpsURLConnection;

import sdn.cli.odl.MODE;
import sdn.cli.odl.MyConsole;
import sdn.cli.odl.NetDB;
import sdn.cli.odl.NodeIDAndPort;
import sdn.cli.odl.RESTClass;

public class NoSwitchPortAccessVlanCommand implements ICommand {

	private String commandString = "no switchport access vlan";
	
	private MODE myMode=MODE.INTERFACE;

	private MODE modeNeeded=MODE.INTERFACE;
	@Override
	public String getCommandString() {
		
		return commandString;
	}


	@Override
	public MODE execute(RESTClass rest, String command,MyConsole console,NetDB db,NodeIDAndPort nodeIDAndPort) {

		String nodeID=nodeIDAndPort.getNodeID();
		String portNumber=nodeIDAndPort.getPortID();
		if(RESTClass.getFlowNamesConfiguredAndDeleteFlows(nodeID,portNumber,"ACCESS")==HttpsURLConnection.HTTP_NO_CONTENT){
			
			db.deleteRecords(nodeID,portNumber);

		}
	
		return  getMyMode();
		
		
	}

	@Override
	public String getPriviledgeLevel() {

		return null;
	}

	@Override
	public MODE getNeededMode() {

		return modeNeeded;
	}
	@Override
	public MODE getMyMode() {

		return myMode;
	}

}
