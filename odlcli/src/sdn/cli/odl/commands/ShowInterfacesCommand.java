package sdn.cli.odl.commands;
/**
 * This command need to be implemented further. Out of scope of our project
 * 
 * 
 *  * Created by Shahani Waheed Ali waheed.shahani@gmail.com
 * **/
import sdn.cli.odl.MODE;
import sdn.cli.odl.MyConsole;
import sdn.cli.odl.NetDB;
import sdn.cli.odl.NodeIDAndPort;
import sdn.cli.odl.RESTClass;


public class ShowInterfacesCommand implements ICommand {

	private String commandString = "show interfaces";
	private MODE myMode=MODE.PRIVILEDGE;
	private MODE modeNeeded=MODE.PRIVILEDGE;
	@Override
	public String getCommandString() {

		return commandString;
	}

	@Override
	public MODE execute(RESTClass rest, String command,MyConsole console,NetDB db,NodeIDAndPort nodeIDAndPort) {
		
			console.display(RESTClass.showInterfaces());
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
