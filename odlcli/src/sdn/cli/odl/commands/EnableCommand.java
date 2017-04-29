package sdn.cli.odl.commands;
/**
 * This is Cisco like command which is  entered into EXEC mode to get access to Priviledged mode
 * This implements ICommand interface
 *
 *
 * * Created by Shahani Waheed Ali waheed.shahani@gmail.com
 **/
import sdn.cli.odl.MODE;
import sdn.cli.odl.MyConsole;
import sdn.cli.odl.NetDB;
import sdn.cli.odl.NodeIDAndPort;
import sdn.cli.odl.RESTClass;


public class EnableCommand implements ICommand {
	
	private String commandString = "enable";
	private MODE myMode=MODE.PRIVILEDGE;
	private MODE modeNeeded=MODE.EXEC;
	@Override
	public String getCommandString() {

		return commandString;
	}

	@Override
	public MODE execute(RESTClass rest, String command,MyConsole console,NetDB db,NodeIDAndPort nodeIDAndPort) {

		//Someone may implement password authentication for enable here.
		console.display("You are now a priviledged user\n");
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
