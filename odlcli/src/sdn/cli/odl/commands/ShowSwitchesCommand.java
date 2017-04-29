package sdn.cli.odl.commands;
/***
 * This command shows IDs of all the switches in our openflow network
 *  * Created by Shahani Waheed Ali waheed.shahani@gmail.com
 * 
 * **/
import org.json.JSONException;

import sdn.cli.odl.MODE;
import sdn.cli.odl.MyConsole;
import sdn.cli.odl.NetDB;
import sdn.cli.odl.NodeIDAndPort;
import sdn.cli.odl.RESTClass;

public class ShowSwitchesCommand implements ICommand{


	private String commandString = "show switches";
	private MODE myMode=MODE.PRIVILEDGE;
	private MODE modeNeeded=MODE.PRIVILEDGE;
	@Override
	public String getCommandString() {

		return commandString;
	}

	@Override
	public MODE execute(RESTClass rest, String command,MyConsole console,NetDB db,NodeIDAndPort nodeIDAndPort) {

		try {
			RESTClass.showSwitches();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			console.display("Found some problem in showing switchews informaiton\n");
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
