package sdn.cli.odl.commands;
/**
 * This command is partially implemented in our project. This command just dumps all the flows in XML format.
 * *
 * 
 *  * Created by Shahani Waheed Ali waheed.shahani@gmail.com
 * 
 * */
import sdn.cli.odl.MODE;
import sdn.cli.odl.MyConsole;
import sdn.cli.odl.NetDB;
import sdn.cli.odl.NodeIDAndPort;
import sdn.cli.odl.RESTClass;


public class ShowFlowsCommand implements ICommand {

	private String commandString = "show flows";
	
	private MODE myMode=MODE.PRIVILEDGE;
	private MODE modeNeeded=MODE.PRIVILEDGE;
	@Override
	public String getCommandString() {

		return commandString;
	}

	@Override
	public MODE execute(RESTClass rest, String command,MyConsole console,NetDB db,NodeIDAndPort nodeIDAndPort) {
		
		console.display(RESTClass.showFlows());
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
