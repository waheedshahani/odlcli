package sdn.cli.odl;
/**
 * This enumeration is used to verify current mode of command and traverse through different modes
 * Currently we have implemented 4 major modes ditto named as Cisco names them.
 * 
 * 
 *  * Created by Shahani Waheed Ali waheed.shahani@gmail.com
 * 
 **/
public enum MODE {
	EXEC, PRIVILEDGE, CONFIG, INTERFACE;

	/*This method of enum is called when someone enters exit command and mode is above EXEC
	 * This method return previous mode in the series.
	 */
	
	public MODE getPrevious(MODE m){
		switch(m){
		case INTERFACE:
			return CONFIG;
		case CONFIG:
			return PRIVILEDGE;
		case PRIVILEDGE:
			return EXEC;
		default:
			break;
		}
		return null;
	}

	
	
}
