package sdn.cli.odl.commands;
/***
 * 
 * This command is entered into INTERFACE mode to configure a particular port to a particular VLAN.
 * This command create multiple rules depending on the current configuration of other ports of the switch 
 * 
 *  * Created by Shahani Waheed Ali waheed.shahani@gmail.com
 * **/
import java.util.ArrayList;
import java.util.Iterator;

import sdn.cli.odl.Flow;
import sdn.cli.odl.MODE;
import sdn.cli.odl.MyConsole;
import sdn.cli.odl.NetDB;
import sdn.cli.odl.NodeIDAndPort;
import sdn.cli.odl.RESTClass;

public class SwitchportAccessVlanCommand implements ICommand {

	private String commandString = "switchport access vlan";
	private Flow flow;
	private MODE myMode=MODE.INTERFACE;
	private MODE modeNeeded=MODE.INTERFACE;
	@Override
	public String getCommandString() {

		return commandString;
	}

	@Override
	public MODE execute(RESTClass rest, String command,MyConsole console,NetDB db,NodeIDAndPort nodeIDAndPort) {


		//System.out.println(command.substring(command.lastIndexOf("tag")));
		int vlanNumber=0;
		try{
			vlanNumber=Integer.parseInt((command.substring ("switchport access vlan".length()+1)));
		}
		catch(Exception e){
			System.out.println("Incorrect VLAN Specified - VLAN Number should be in digits");
		}
		if(vlanNumber>=0 && vlanNumber<=4095){

			try{


				//Generate a flow rule on switch to broadcast ARP traffic
				flow=new Flow();
				flow.setName("ARPTraffic");
				flow.setId(nodeIDAndPort.getNodeID());
				flow.setEtherType("0x0806");
				flow.setPriority("700");
				flow.setActions("CONTROLLER");
				RESTClass.installFlow(flow);
				String hostMacAndIP[]=RESTClass.getHostMacAndIP(nodeIDAndPort.getNodeID(),nodeIDAndPort.getPortID());

				if(hostMacAndIP[0]!=null && hostMacAndIP[1]!=null){
					//MAC=hostMacAndIP[0] & IP=hostMacAndIP[1]
					//creating one rule for sending traffic to this port when traffic is destined for MAC address connected to this port
					flow=new Flow();	//This will call constructor which will set all parametrs to NULL first
					flow.setName("P"+nodeIDAndPort.getPortID()+"noVlan");
					flow.setId(nodeIDAndPort.getNodeID());
					flow.setDlDst(hostMacAndIP[0]);
					flow.setPriority("550");
					flow.setActions("OUTPUT="+nodeIDAndPort.getPortID());
					RESTClass.installFlow(flow);
					
					//creating rule for sending traffic to this port when traffic with VLAN Number tagged is received on the switch and its destination MAC is MAC of host connected to this port 

					int vlanToUntag=vlanNumber;

					flow=new Flow();
					flow.setName("P"+nodeIDAndPort.getPortID()+"UntagVlan"+vlanNumber);
					flow.setId(nodeIDAndPort.getNodeID());
					flow.setDlDst(hostMacAndIP[0]);
					flow.setPriority("600");
					flow.setVlanId( Integer.toString(vlanToUntag));
					flow.setActions("POP_VLAN\","+"\"OUTPUT="+nodeIDAndPort.getPortID());
					RESTClass.installFlow(flow);
					//RESTClass.installFlow(flowName, nodeID, null, null, null, null, null, null, hostMacAndIP[0],"600", Integer.toString(vlanToUntag), action);

					//Push flow and then find the trunk ports on current switch and then create rule to pushvlan and forward on that trunk
					//condition in if populate database with new record for this port and returns true if record is added successfully				
					if(db.insertIntoDB(nodeIDAndPort.getNodeID(),nodeIDAndPort.getPortID(),"P"+nodeIDAndPort.getPortID()+"UntagVlan"+vlanNumber,"ACCESS",vlanNumber)){

						//find list of trunk ports on current switch and create rule to forward from this access port to that trunk port
						ArrayList<String> resultList=new ArrayList<String>();
						resultList=db.getListOfTrunks(nodeIDAndPort.getNodeID());
						Iterator<String> it = resultList.iterator();
						while(it.hasNext())	{
							flow=new Flow();
							flow.setName("P"+nodeIDAndPort.getPortID()+"ToTrunksPushVlan"+vlanNumber);
							flow.setId(nodeIDAndPort.getNodeID());
							flow.setIngressPort(nodeIDAndPort.getPortID());
							flow.setPriority("500");
							flow.setActions("SET_VLAN_ID="+vlanNumber+"\",\"OUTPUT="+(String) it.next());
							RESTClass.installFlow(flow);
							//RESTClass.installFlow(flowName, nodeID, portNumber, null, null, null, null, null, null, "500", null, action);

						}


						resultList=db.getPortAndVLANIDFromDB(nodeIDAndPort.getNodeID());	
						Iterator<String> accessPortIterator = resultList.iterator();


						while(accessPortIterator.hasNext())
						{
							String portno=(String) accessPortIterator.next();
							String vlanNum=(String) accessPortIterator.next();
							//check if both ports are in same vlan or not , if not then create two rules to deny traffic between them

							if(!vlanNum.equals(Integer.toString(vlanToUntag)) && !nodeIDAndPort.getPortID().equals(portno)){
								//get MAC and IP of destination host
								String hostMacAndIPOfDestination[]=RESTClass.getHostMacAndIP(nodeIDAndPort.getNodeID(),portno);
								//creating one flow rule to deny traffic from PortX to PortY
								flow=new Flow();
								flow.setName("DropP"+nodeIDAndPort.getPortID()+"ToP"+portno);
								flow.setId(nodeIDAndPort.getNodeID());
								flow.setIngressPort(nodeIDAndPort.getPortID());
								flow.setDlSrc(hostMacAndIP[0]);
								flow.setDlDst(hostMacAndIPOfDestination[0]);
								flow.setPriority("600");
								flow.setActions("DROP");
								RESTClass.installFlow(flow);
								//creating another flow rule to deny traffic from PortY to PortX
								flow=new Flow();
								flow.setName("DropP"+portno+"ToP"+nodeIDAndPort.getPortID());
								flow.setId(nodeIDAndPort.getNodeID());
								flow.setIngressPort(portno);
								flow.setDlSrc(hostMacAndIPOfDestination[0]);
								flow.setDlDst(hostMacAndIP[0]);
								flow.setPriority("600");
								flow.setActions("DROP");
								RESTClass.installFlow(flow);
								
							}

						}



					}
					else { System.out.println("Not successful data base insert"); 	}

				}

			}
			catch(Exception e ){
				System.out.println("Error writing rule to switch"+e);
			}

		}
		else
			System.out.println("VLAN number is not in range , Allowed Range is 0 - 4095 ");


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
