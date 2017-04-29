package sdn.cli.odl;
/**
 * This class posses console of the program and display prompt according to the current mode
 * 
 * 
 * 
 *  * Created by Shahani Waheed Ali waheed.shahani@gmail.com
 * */
import java.io.Console;

public class MyConsole {
	public static Console console;
	private static String basePrompt="ODLCLI";
	public MyConsole(){
		console = System.console();
	}
	
	public String readPassword(){
		
		return new String(console.readPassword());
		
	}
	public String readInput(){
		return console.readLine();
		
	}
	public void display(String display){
		System.out.print(display);
	}
	public String getBasePrompt(){
		return basePrompt;
	}
	public void showPrompt(MODE mode){
		switch (mode){
		case EXEC:
			this.display(getBasePrompt()+">");	
			break;
		case PRIVILEDGE:
			this.display(getBasePrompt()+"#");
			break;
		case CONFIG:
			this.display(getBasePrompt()+"(config)#");
			break;
		case INTERFACE:
			this.display(getBasePrompt()+"(config-if)#");
			break;
		default:
			this.display("No valid mode specified....May be some bug occured.Exiting!!!!");
			System.exit(0);

	}
	}
	
	

}
