package sdn.cli.odl;
/**
 * This class contains parameters for setting flow. Although we are not using getters of this method 
 * but someone else can utilize them as per need. In our project we are creating flow objects in 
 * following commands. 
 * switchport access vlan vlan_number
 * switchport mode trunk
 * spanning-tree bpdu-filter enable
 * 
 * Object of this class is passed to RESTClass for installation of particular flow 
 *  * Created by Shahani Waheed Ali waheed.shahani@gmail.com
 * */
import java.util.LinkedHashMap;
import java.util.Map;

public class Flow {
	private String name;
	private String id;
	private String ingressPort;
	private String etherType;
	private String protocol;
	private String nwDst;
	private String nwSrc;
	private String dlDst;
	private String dlSrc;	
	private String priority;
	private String vlanId;
	private String actions;
	public static Map<String, String> ruleParas= new LinkedHashMap<String,String>();
	public Flow(){
		//when this object is created all values should be strictly set to NULL
		name=null;
		id=null;
		ingressPort=null;
		etherType=null;
		protocol=null;
		nwDst=null;
		nwSrc=null;
		dlDst=null;
		dlSrc=null;	
		priority=null;
		vlanId=null;
		actions=null;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIngressPort() {
		return ingressPort;
	}
	public void setIngressPort(String ingressPort) {
		this.ingressPort = ingressPort;
	}
	public String getEtherType() {
		return etherType;
	}
	public void setEtherType(String etherType) {
		this.etherType = etherType;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public String getNwDst() {
		return nwDst;
	}
	public void setNwDst(String nwDst) {
		this.nwDst = nwDst;
	}
	public String getNwSrc() {
		return nwSrc;
	}
	public void setNwSrc(String nwSrc) {
		this.nwSrc = nwSrc;
	}
	public String getDlDst() {
		return dlDst;
	}
	public void setDlDst(String dlDst) {
		this.dlDst = dlDst;
	}
	public String getDlSrc() {
		return dlSrc;
	}
	public void setDlSrc(String dlSrc) {
		this.dlSrc = dlSrc;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getVlanId() {
		return vlanId;
	}
	public void setVlanId(String vlanId) {
		this.vlanId = vlanId;
	}
	public String getActions() {
		return actions;
	}
	public void setActions(String actions) {
		this.actions = actions;
	}
	
	
	
	
	

}
