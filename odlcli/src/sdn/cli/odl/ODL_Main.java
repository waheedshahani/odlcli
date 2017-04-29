package sdn.cli.odl;
/**
 *This class contains Main method of our program. Below in instance variables we have also set credentails
 *for controller as well. By default they are configured as admin and admin
 *Server IP and port of conrtoller is also defined here in this class
 * This class created objects of CommandHandler, MyConsole,NetDB and RESTClass specially.
 * 
 * Note:- As this program runs , we are removing any previous data saved into database which is handy for 
 * testing purposes. If this program is run in production , remove the command which deletes all records of DB
 * 
 *  * Created by Shahani Waheed Ali waheed.shahani@gmail.com
 * 
 * 
 ***/
import java.util.Scanner;

import sdn.cli.odl.commands.ConfigureTerminalCommand;
import sdn.cli.odl.commands.EnableCommand;
import sdn.cli.odl.commands.ExitCommand;
import sdn.cli.odl.commands.InterfaceSwitchIDPortIDCommand;
import sdn.cli.odl.commands.NoSwitchPortAccessVlanCommand;
import sdn.cli.odl.commands.ShowFlowsCommand;
import sdn.cli.odl.commands.ShowInterfacesCommand;
import sdn.cli.odl.commands.ShowSwitchesCommand;
import sdn.cli.odl.commands.SpanningTreeBPDUFilterDisableCommand;
import sdn.cli.odl.commands.SpanningTreeBPDUFilterEnableCommand;
import sdn.cli.odl.commands.SwitchportAccessVlanCommand;
import sdn.cli.odl.commands.SwitchportModeTrunkCommand;


public class ODL_Main {

	static Scanner reader;					//Used for reading user input
	static String basePrompt="ODL";		
	static String inputCommand;		//Any command entered by user will first store in this String variable
	static MODE mode =null;	//
	static RESTClass rest;
	static NetDB db;
	static MyConsole console;
	static Login login;
	//By default opendaylight username and password are admin. If you want to use another credentials ,just change them here and they'll be used to communicate between ODLCLI and opendaylight controller 
	static String controllerUserName="admin";
	static String controllerPassword="admin";
	static String controllerAddress="localhost";
	static String controllerPort="8080";
	static CommandHandler commander;
	
	static String nodeIDAndPort;	//Used when user go into interface switchID/portNumber mode e.g interface 00:00:00:00:00:00:00:01/1 , nodeIDAndPort stores 00:00:00:00:00:00:00:01/1
	//Enumeration of CLI modes. We have implemented 4 major modes found in Cisco CLI
	
	public static void main(String[] args) {

		 
		console=new MyConsole();
		//create login object and checking for credentials. 
		login=new Login(console);
		login.LoginMe();
		mode=MODE.EXEC;
		
		//create commandhandler object and adding all command objects to it.
		commander= CommandHandler.getInstance();;
		commander.addCommand((new ExitCommand()));
		commander.addCommand(new EnableCommand());
		commander.addCommand(new ConfigureTerminalCommand());
		commander.addCommand(new ShowFlowsCommand());
		commander.addCommand(new ShowSwitchesCommand());
		commander.addCommand(new ShowInterfacesCommand());
		commander.addCommand(new InterfaceSwitchIDPortIDCommand());
		commander.addCommand(new NoSwitchPortAccessVlanCommand());
		commander.addCommand(new SpanningTreeBPDUFilterEnableCommand());
		commander.addCommand(new SpanningTreeBPDUFilterDisableCommand());
		commander.addCommand(new SwitchportAccessVlanCommand());
		commander.addCommand(new SwitchportModeTrunkCommand());
		
		//adding all the objects into collection
		
		
		rest=new RESTClass();	//creating object for RESTClass which help us to install and retrieve flows from opendaylight controller
		//Setting Credentials for Opendaylight controller. One can further implement this as interactive to user but for sake of simplicity we haven't implemented such functionality
		RESTClass.setCredentials(controllerUserName, controllerPassword);
		//Server address and port number where Opendaylight controller is listening at. For sake of simplicity we are hardcoding these paramters.
		RESTClass.setServerAddressAndPort(controllerAddress,controllerPort);
		
		db= new NetDB(); //creating database class object

		//Removing all the records from our database if any(Just for Demo purpose). 
		//Note:-This line should be removed when using in production network
		db.deleteRecords("ALL","ALL");

		
		do{
			console.showPrompt(mode);
			inputCommand=console.readInput();
			/**
			 * checking if user enters ENTER key without entering any command 
			 * (Usually Network administrator press Enter key many times without any intention , may be just to get warm up or may be addiction
			 */
			if(!inputCommand.equals(""))	
				mode=commander.execute(inputCommand, console, mode,rest,db);
			if(mode==null){
				console.display("Nice to interact with you!!!Good bye");
				System.exit(0);
			}
		
		}
		while(true);

	}


}
