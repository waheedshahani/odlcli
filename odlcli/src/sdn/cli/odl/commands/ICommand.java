package sdn.cli.odl.commands;
/**
 * This interface is designed for every command to be implemented by the user. All the command classes
 * in our code implement this interface.
 * execute method of class implementing this Interface has to implement the actual implementation of the command
 * getNeededMode method gives mode which user should be atleast IN so that this command works
 * GetMyMode returns mode of this command. E.g Enable command's mode is Priviledged mode.
 * Mode is used as enum variable throughout our program
 *  * 
 * 
 *  * Created by Shahani Waheed Ali waheed.shahani@gmail.com
 * 
 * **/
import sdn.cli.odl.MODE;
import sdn.cli.odl.MyConsole;
import sdn.cli.odl.NetDB;
import sdn.cli.odl.NodeIDAndPort;
import sdn.cli.odl.RESTClass;


public interface ICommand {
	public String getCommandString();
	public String getPriviledgeLevel();
	public MODE getNeededMode();
	public MODE getMyMode();
	public MODE execute(RESTClass rest, String command, MyConsole console, NetDB db, NodeIDAndPort nodeIDAndPort);

}
