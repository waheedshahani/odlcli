package sdn.cli.odl.commands;
/***
 * This command is used to disable spanning-tree filteration for particular mode. Not implemented properly
 * Out of scope of our project
 * 
 *  * Created by Shahani Waheed Ali waheed.shahani@gmail.com
 * **/
import sdn.cli.odl.MODE;
import sdn.cli.odl.MyConsole;
import sdn.cli.odl.NetDB;
import sdn.cli.odl.NodeIDAndPort;
import sdn.cli.odl.RESTClass;


public class SpanningTreeBPDUFilterDisableCommand implements ICommand {


	private String commandString = "spanning-tree bpdu-filter disable";
	
	private MODE myMode=MODE.INTERFACE;
	private MODE modeNeeded=MODE.INTERFACE;
	@Override
	public String getCommandString() {

		return commandString;
	}

	@Override
	public MODE execute(RESTClass rest, String command,MyConsole console,NetDB db,NodeIDAndPort nodeIDAndPort) {
		//Out of scope of our project. Provision for extension
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
