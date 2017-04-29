package sdn.cli.odl.commands;
/**
 * This command is used to Exit the program when entered into EXEC mode otherwise this command go back 
 * one mode for one EXIT command. finally EXITs the program if entered multiple times
 * This implements ICommand interface
 * 
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


public class ExitCommand implements ICommand {

	private String commandString = "exit";
	
	private MODE myMode=MODE.EXEC;
	
	private MODE modeNeeded=MODE.EXEC;
	@Override
	public String getCommandString() {

		return commandString;
	}


	@Override
	public MODE execute(RESTClass rest, String command,MyConsole console,NetDB db,NodeIDAndPort nodeIDAndPort) {

		
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
