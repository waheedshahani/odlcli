package sdn.cli.odl;
/**
 * 
 * As all the commands are implemented as separate class. Therefore when our program run all those
 * commands are put into Map implemented in this class. This map contians initial string of command
 * as key and command object as value.As user input some command , it is passed to this class and 
 * we match user input to one of the command stored into Map. If found then we call execute method
 * of that command object which has its own implementation * 
 * 
 * 
 * Created by Shahani Waheed Ali waheed.shahani@gmail.com
 * 
 * */
import java.util.HashMap;
import java.util.Map;

import sdn.cli.odl.commands.ICommand;

public class  CommandHandler {

	private static CommandHandler commander;
	private Map<String, ICommand> commands = new HashMap<String, ICommand>();
	private static NodeIDAndPort nodeIDAndPort;

	public void addCommand(ICommand command) {
		//	this.commands.put("test", new ExitCommand());	
		this.commands.put(command.getCommandString().trim().toLowerCase(), command);

	}


	public static synchronized CommandHandler getInstance() {
		if (commander == null)
			commander = new CommandHandler();
		return commander;
	}

	public synchronized MODE execute(String commandString,MyConsole console,MODE currentMode,RESTClass rest, NetDB db){

		if(!(commandString.equals("exit") || commandString.equals("quit") )){
			ICommand command=null;
			//verify if this command exists in our hashmap of commands
			for (Map.Entry<String, ICommand> entry : commands.entrySet()){
				//every entry in HashMap has string key unique for each command e.g for interface switchID/PortID ,we have key of interface.
				if(commandString.startsWith(entry.getKey())){
					command=entry.getValue();
						if(currentMode.compareTo(command.getNeededMode())>=0){
						//command.execute will execute the execute method of particular command, implemented as separate class and then it will return its MODE e.g PRIVILEDGE mode for enable command 
						return command.execute(rest, commandString, console, db,nodeIDAndPort);

					}
					else 
						console.display("No sufficient priviledges or Invalid Mode\n");


				}

			}

		}
		else 
			return	currentMode.getPrevious(currentMode);	//because user inputed exit or quit command , we want to go back to previous mode.
	//If command and mode has been matched in previous If clause then program will not reach here. If user input is something neither EXIT nor QUIT and nor any valid command then following should happen
		console.display("Invalid Command Entered\n");
		return currentMode;



	}

	public CommandHandler(){
		nodeIDAndPort=new NodeIDAndPort();
	
	}
}
