package sdn.cli.odl.commands;
/**
 * This is Cisco like command whihc is  entered into Priviledged mode to get access to global config mode
 * This implements ICommand interface
 *
 *
 * * Created by Shahani Waheed Ali waheed.shahani@gmail.com
 *
 **/
import sdn.cli.odl.MODE;
import sdn.cli.odl.MyConsole;
import sdn.cli.odl.NetDB;
import sdn.cli.odl.NodeIDAndPort;
import sdn.cli.odl.RESTClass;


public class ConfigureTerminalCommand implements ICommand {
	private String commandString = "configure terminal";
	
	private MODE myMode=MODE.CONFIG;
	private MODE modeNeeded=MODE.PRIVILEDGE;
	
	@Override
	public String getCommandString() {

		return commandString;
	}

	@Override
	public MODE execute(RESTClass rest, String command, MyConsole console,NetDB db,NodeIDAndPort nodeIDAndPort) {
		
			System.out.println("Welcome to Global configuration Mode");
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
