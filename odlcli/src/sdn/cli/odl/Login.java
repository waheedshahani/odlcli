package sdn.cli.odl;
/**
 * This is for userlogin initially when program starts.
 * default username and password are set below. 
 * 
 * 
 * 
 *  * Created by Shahani Waheed Ali waheed.shahani@gmail.com
 * 
 **/
import sdn.cli.odl.MyConsole;
public class Login {
	
	String username;
	String password;
	String usernamedummy="shahani";
	String passworddummy="odlcli";
	
	boolean loginFlag=false;
	
	MyConsole console;
	public Login(MyConsole console){
		
		this.console=console;
	}
public void LoginMe(){
		
	//Looping forever untill user inputs correct credentials
	while(!(getUserName().equals(usernamedummy) && getPassword().equals(passworddummy))){
				
	}	
	console.display("Login Successful\n");
	
	}

	private String getPassword() {
		console.display("Password:");
		return console.readPassword();

	}

	private String getUserName() {
		console.display("Username:");
		return console.readInput();
		
	}
	
}
