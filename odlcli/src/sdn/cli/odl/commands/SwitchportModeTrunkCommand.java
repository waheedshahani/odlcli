package sdn.cli.odl.commands;
/***
 * This command is used to assign a trunk role to a particular port.
 * It created flow rules to allow traffic from edge ports to this trunk port and from this trunk
 * port to other trunk ports. 
 *  * Created by Shahani Waheed Ali waheed.shahani@gmail.com
 * ***/
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import sdn.cli.odl.Flow;
import sdn.cli.odl.MODE;
import sdn.cli.odl.MyConsole;
import sdn.cli.odl.NetDB;
import sdn.cli.odl.NodeIDAndPort;
import sdn.cli.odl.RESTClass;

public class SwitchportModeTrunkCommand implements ICommand {

	private String commandString = "switchport mode trunk";
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
		String flowName="P"+portNumber+"Trunk";


		try {
			if(db.insertIntoDB(nodeID,portNumber,flowName,"TRUNK",0)){
				//Creating rule to forward tag traffic from access ports and forward to trunk				

				ArrayList<String> resultList=new ArrayList<String>();
				resultList=db.getPortAndVLANIDFromDB(nodeID);
				ArrayList<String> resultList1=new ArrayList<String>();
				resultList1=db.getListOfTrunks(nodeID);

				Iterator<String> it = resultList.iterator();


				while(it.hasNext())
				{
					Iterator<String> it1 = resultList1.iterator();
					String portno=(String) it.next();
					String vlanNum=(String) it.next();


					//Adding all the trunk list into output ports
					String dummy="";
					while(it1.hasNext()){
						String temp=(String) it1.next();
						//System.out.println("VLAN="+temp);
						dummy=dummy+"OUTPUT="+temp;
						if(it1.hasNext()){dummy=dummy+"\",\"";}

					}
					

					flow=new Flow();
					flow.setName("P"+portno+"ToTrunksPushVlan"+vlanNum);
					flow.setId(nodeID);
					flow.setIngressPort(portno);
					flow.setPriority("500");
					flow.setActions("SET_VLAN_ID="+vlanNum+"\",\""+dummy);
					RESTClass.installFlow(flow);
					

				
				}


				/**
				 * Creating rule to forward traffic from Trunk ports to other trunk ports
				 * First find which trunk ports exist on current switch
				 * then create forwarding rules beteen trunks
				 */

				Iterator<String> trkItr = resultList1.iterator();
				//check if there is only one trunk on this switch then dont create any trunk to trunk rules
				if(resultList1.size()>1)
					while(trkItr.hasNext())
					{
						//selecting one trunk port and then in inner loop we'll forward its traffic to all other trunks
						String portno=(String) trkItr.next();

						Iterator<String> trkItr1 = resultList1.iterator();

						String action="OUTPUT="+portno;
				
						String dummy="";
						ArrayList<Object> portListWithoutMyPort=new ArrayList<Object>();
						while(trkItr1.hasNext()){
							String temp=(String) trkItr1.next();

							//storing all the trunk ports except myself into arraylist
							if(!portno.equals(temp))
								portListWithoutMyPort.add(temp);

						}
						//Iterating through arraylist and creating action as output to multiple ports if any
						Iterator<Object> TrkportItr = portListWithoutMyPort.iterator();
						while(TrkportItr.hasNext()){
							dummy=dummy+"OUTPUT="+TrkportItr.next();
							if(TrkportItr.hasNext()){dummy=dummy+"\",\"";}
						}

						action=dummy;
						flow=new Flow();
						flow.setName("TrkP"+portno+"ToTrunks"+"Pass");
						flow.setId(nodeID);
						flow.setIngressPort(portno);
						flow.setPriority("500");
						flow.setActions(action);
						RESTClass.installFlow(flow);
						

					}

			}
			else { 	System.out.println("Not successful data base insert"); 	}
		} catch (SQLException e) {
			e.printStackTrace();
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
