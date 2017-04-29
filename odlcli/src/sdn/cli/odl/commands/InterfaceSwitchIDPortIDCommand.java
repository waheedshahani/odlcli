package sdn.cli.odl.commands;
/**
 * This command is used to enter into interface configuration mode.
 * it gets SWID and portNumber as parameters. Example is shown below
 * interface 00:00:00:00:00:00:00:01/1
 * port number is 1 in above command
 * 
 *  * Created by Shahani Waheed Ali waheed.shahani@gmail.com
 * 
 * **/
import sdn.cli.odl.MODE;
import sdn.cli.odl.MyConsole;
import sdn.cli.odl.NetDB;
import sdn.cli.odl.NodeIDAndPort;
import sdn.cli.odl.RESTClass;


public class InterfaceSwitchIDPortIDCommand implements ICommand {

	private String commandString = "interface";
	
	private MODE myMode=MODE.INTERFACE;
	private MODE modeNeeded=MODE.CONFIG;

	@Override
	public String getCommandString() {

		return commandString;
	}

	@Override
	public MODE execute(RESTClass rest, String command,MyConsole console,NetDB db, NodeIDAndPort nodeIDAndPortNo) {

		
		if(nodeIDAndPortNo.setNodeIDAndPortNo(command)){
			return getMyMode();
		}
		else console.display("Wrong Interface Command Entered\n");
		
		return  getNeededMode();
		
		
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
