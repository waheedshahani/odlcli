package sdn.cli.odl.commands;
/***
 * This command is entered into INTERFACE mode and it create flowrules to filter all the 
 * Spanning tree traffic for that edge port.
 * 
 *  * Created by Shahani Waheed Ali waheed.shahani@gmail.com * 
 * ***/
import java.sql.SQLException;

import sdn.cli.odl.Flow;
import sdn.cli.odl.MODE;
import sdn.cli.odl.MyConsole;
import sdn.cli.odl.NetDB;
import sdn.cli.odl.NodeIDAndPort;
import sdn.cli.odl.RESTClass;

public class SpanningTreeBPDUFilterEnableCommand implements ICommand {

	private String commandString = "spanning-tree bpdu-filter enable";
	private Flow flow;
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
		String hostMacAndIP[]=RESTClass.getHostMacAndIP(nodeID,portNumber);

		if(hostMacAndIP[0]!=null && hostMacAndIP[1]!=null){
			//("MAC=hostMacAndIP[0] & IP=hostMacAndIP[1]

			//creating one rule for dropping bpdu when incoming on this port
			
			flow=new Flow();
			flow.setName("Drop"+portNumber+"BPDUIngress");
			flow.setId(nodeID);
			flow.setEtherType("0x010B");
			flow.setPriority("500");
			flow.setActions("Drop");
			RESTClass.installFlow(flow);

			//creating flow rule to drop traffic coming from other port but destined to the host connected to this port. 

			
			
			flow=new Flow();
			flow.setName("DROP"+portNumber+"BPDUEgress");
			flow.setId(nodeID);
			flow.setEtherType("0x010B");
			flow.setDlDst(hostMacAndIP[0]);
			flow.setPriority("500");
			flow.setActions("Drop");
			RESTClass.installFlow(flow);
			
//			RESTClass.setFlowParasToNull();
//			RESTClass.setFlowParameters(flowName, nodeID, null, "0x010B", null, null, null, null, hostMacAndIP[0],"500", null, action);
//			RESTClass.generateJSONStringAndForward();
			try {
				db.insertIntoDB(nodeID,portNumber,"DROP"+portNumber+"BPDUEgress","BPDU",0);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("Couldn't write STP BPDU Record into Databse");
				e.printStackTrace();
			}


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
