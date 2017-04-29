package sdn.cli.odl;
/**
 * This class provide interface for sending flow rules and getting information from controller
 * using REST API 
 * 
 *  * Created by Shahani Waheed Ali waheed.shahani@gmail.com
 * 
 * */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONException;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;





public class RESTClass {
	//LinkedHashMap maintains order in which elements are put. Useful when we create flow rules by iteraitng through Map
	public static Map<String, String> ruleParas= new LinkedHashMap<String,String>();
	public static String nodeID=null;
	private static String serverAddress=null;
	private static String serverPort=null;
	public static String flowName=null;
	private static String odlUserName=null;
	private static String odlPassword=null;
	public RESTClass(){
		setFlowParameters(null,null,null,null,null,null,null,null,null,null,null,null);

	}
	public static void setFlowParameters(String name,String id,String ingressPort,String etherType,
			String protocol,String nwDst,String nwSrc,String dlSrc,String dlDst,String priority,String vlanId,String actions){
		flowName=name;
		nodeID=id;
		ruleParas.put("name", name);
		ruleParas.put("id", id);
		ruleParas.put("ingressPort",ingressPort);
		ruleParas.put("etherType", etherType);
		ruleParas.put("protocol", protocol);
		ruleParas.put("nwDst", nwDst);
		ruleParas.put("nwSrc", nwSrc);
		ruleParas.put("dlDst", dlDst);
		ruleParas.put("dlSrc", dlSrc);	
		ruleParas.put("priority", priority);
		ruleParas.put("vlanId", vlanId);
		ruleParas.put("actions", actions);
	}
	
	public static String showFlows() {
		String baseURL = "http://"+serverAddress+":"+serverPort+"/controller/nb/v2/flowprogrammer/default";
		return getInputStreamAndResult(baseURL);
		}
	public static void showSwitches() throws JSONException {
		String baseURL = "http://"+serverAddress+":"+serverPort+"/controller/nb/v2/switchmanager/default/nodes";
		String result = getInputStreamAndResult(baseURL);

		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(result));

			Document doc = db.parse(is);
			NodeList nodes = doc.getElementsByTagName("nodeProperties");

			
			for (int i = 0; i < nodes.getLength(); i++) {
				Element element = (Element) nodes.item(i);
				NodeList name = element.getElementsByTagName("node");
				name.item(0);
				NodeList id = element.getElementsByTagName("id");
				Element idval = (Element) id.item(0);
				System.out.println(getCharacterDataFromElement(idval));
				
			}
		}
		catch (Exception e) {
			//If something went wrong or if record does not exist then return NULL so that calling method take other decision.
		
			e.printStackTrace();

		}

	



	}

	public static String showInterfaces(){
		//Need to be implemented. Out of scope of our project
	return "Need to be implemented\n";
	
	}
	public static String getInputStreamAndResult(String urlpara){
		
		String baseURL = urlpara;
		//   String containerName = "/default";

		try {

			// Create URL = base URL + container
			URL url = new URL(baseURL);

			// Create authentication string and encode it to Base64
			String authStr = odlUserName + ":" + odlPassword;
			String encodedAuthStr = Base64.encodeBase64String(authStr.getBytes());

			// Create Http connection
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			// Set connection properties
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Authorization", "Basic " + encodedAuthStr);
			connection.setRequestProperty("Accept", "application/xml");

			// Get the response from connection's inputStream
			InputStream content = (InputStream) connection.getInputStream();

			BufferedReader in = new BufferedReader(new InputStreamReader(content));
			String line = "";
			String result="";
			try {
				while ((line = in.readLine()) != null) {
					result+=line;
				}
			} catch (IOException e) {
				
				e.printStackTrace();
			}


			return result;

		} 

		catch (Exception e) {
			e.printStackTrace();
		}
		return null;



	}

	public static void generateJSONStringAndForward(){
		String jsonStr="{\"installInHw\":\"true\"";
		Iterator<Entry<String, String>> it = ruleParas.entrySet().iterator();
		while (it.hasNext()) {
			
			Entry<String, String> pairs = it.next();
			//System.out.println(pairs.getKey() + " = " + pairs.getValue());



			if (pairs.getKey().equals("id") && pairs.getValue()!=null){
				jsonStr=jsonStr+", \"node\": {\"id\":\""+pairs.getValue()+"\", \"type\":\"OF\"}";
			}
			else if(pairs.getKey().equals("actions") && pairs.getValue()!=null){
				jsonStr=jsonStr+",\""+pairs.getKey()+"\":"+"[\""+pairs.getValue()+"\"]}";
			}

			else if( pairs.getValue()!=null)
				jsonStr=jsonStr+",\""+pairs.getKey()+"\":\""+pairs.getValue()+"\"";

			it.remove(); // avoids a ConcurrentModificationException
		}

		//System.out.println(jsonStr);
		sendFlow(jsonStr);
	}


	public static void sendFlow(String jsonString){

		try {

			String urltemp="http://"+serverAddress+":"+serverPort+"/controller/nb/v2/flowprogrammer/default/node/OF/"+nodeID+"/staticFlow/"+flowName;

			URL url = new URL(urltemp);

			String encoding = Base64.encodeBase64String((odlUserName+":"+odlPassword).getBytes());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("PUT");
			conn.setRequestProperty("Content-Type", "application/json");

			conn.setRequestProperty("Authorization", "Basic " + encoding);


			OutputStream os = conn.getOutputStream();
			os.write(jsonString.getBytes());
			os.flush();

			if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED && conn.getResponseCode() != HttpURLConnection.HTTP_OK ) {
				throw new RuntimeException("Failed : HTTP error code : "+ conn.getResponseCode());
			
			}
			else {
			
			
			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

			//System.out.println("Message:");
			while ((br.readLine()) != null) {
				//System.out.println(output+"\n");

			}
			}
			conn.disconnect();

		} catch (MalformedURLException e) {
			
			e.printStackTrace();

		} catch (IOException e) {
			
			e.printStackTrace();

		}


	}


	//Get host mac address and IP Address
	public static String[] getHostMacAndIP(String nodeid,String portno){


		String result="";
		String [] MacAndIP = {null,null};
		//   String containerName = "/default";
		result=getInputStreamAndResult("http://"+serverAddress+":"+serverPort+"/controller/nb/v2/hosttracker/default/hosts/active");
		//Now result contains XML Response, now we'll get MAC and IP of the host

		try {
			DocumentBuilderFactory dbf =
					DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(result));

			Document doc = db.parse(is);
			NodeList nodes = doc.getElementsByTagName("hostConfig");

			
			for (int i = 0; i < nodes.getLength(); i++) {
				Element element = (Element) nodes.item(i);





				NodeList name = element.getElementsByTagName("nodeId");
				Element nameval = (Element) name.item(0);
				NodeList nodeconnector = element.getElementsByTagName("nodeConnectorId");
				Element connectorval = (Element) nodeconnector.item(0);




				if(getCharacterDataFromElement(nameval).equals(nodeid) && getCharacterDataFromElement(connectorval).equals(portno))
				{
					NodeList dataLayerAddressnode = element.getElementsByTagName("dataLayerAddress");
					Element dataLayerAddressval = (Element) dataLayerAddressnode.item(0);

					MacAndIP[0]=getCharacterDataFromElement(dataLayerAddressval);

					NodeList networkAddressnode = element.getElementsByTagName("networkAddress");

					Element networkAddressval = (Element) networkAddressnode.item(0);
					MacAndIP[1]=	getCharacterDataFromElement(networkAddressval);
					//		           
				}




			}
		}
		catch (Exception e) {
			//If something went wrong or if record does not exist then return NULL so that calling method take other decision.
			MacAndIP[0]=null;
			MacAndIP[1]=null;
			e.printStackTrace();

		}

		return MacAndIP;

	}

	public static String getCharacterDataFromElement(Element e) {
		Node child = e.getFirstChild();
		if (child instanceof CharacterData) {
			CharacterData cd = (CharacterData) child;
			return cd.getData();
		}
		return "?";
	}

	public static void setCredentials(String userName,String password){
		odlUserName=userName;
		odlPassword=password;
	}
	public static void setServerAddressAndPort(String serverAdd,String port){
		serverAddress=serverAdd;
		serverPort=port;
	}
	public static int getFlowNamesConfiguredAndDeleteFlows(String nodeID,String portNumber,String portType){
		
		
		
		//   String containerName = "/default";
		int returnCode=0;
		String result=getInputStreamAndResult("http://"+serverAddress+":"+serverPort+"/controller/nb/v2/flowprogrammer/default/node/OF/"+nodeID);
		//Now result contains XML Response, now we'll get MAC and IP of the host

		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(result));
			Document doc = db.parse(is);
			NodeList flows = doc.getElementsByTagName("flowConfig");

			
			for (int i = 0; i < flows.getLength(); i++) {
				Element element = (Element) flows.item(i);

				NodeList name = element.getElementsByTagName("name");
				Element flowNameVal = (Element) name.item(0);
				String flowName=getCharacterDataFromElement(flowNameVal);
				if(portType.equals("ACCESS")){
				if(flowName.equals("P"+portNumber+"noVlan") || flowName.startsWith("P"+portNumber+"UntagVlan") 
						|| flowName.startsWith("P"+portNumber+"ToTrunksPushVlan") || (flowName.contains("Drop") && flowName.contains("P"+portNumber))){
					returnCode=deleteFlowRules(nodeID,flowName);
					}
				}
				//This is when user uses no switchport mode trunk command. This finds flow rules in which action is set to forward traffic to trunk port
				else if(portType.equals("TRUNK") && flowName.contains("ToTrunksPushVlan") ){
					returnCode=deleteFlowRules(nodeID,flowName);
				}
				
				else if(portType.equals("TRUNK") && flowName.equals("TrkP"+portNumber+"ToTrunksPass")){
					returnCode=deleteFlowRules(nodeID,flowName);
				}
				
//				if(getCharacterDataFromElement(nameval).equals(nodeid) && getCharacterDataFromElement(connectorval).equals(portno))
//				{
//					NodeList dataLayerAddressnode = element.getElementsByTagName("dataLayerAddress");
//					Element dataLayerAddressval = (Element) dataLayerAddressnode.item(0);
//
//					MacAndIP[0]=getCharacterDataFromElement(dataLayerAddressval);
//
//					NodeList networkAddressnode = element.getElementsByTagName("networkAddress");
//
//					Element networkAddressval = (Element) networkAddressnode.item(0);
//					MacAndIP[1]=	getCharacterDataFromElement(networkAddressval);
//					//		           
//				}




			}
		}
		catch (Exception e) {
			//If something went wrong or if record does not exist then return NULL so that calling method take other decision.
			e.printStackTrace();

		}

return returnCode;	
		
		
		
	}
	
	public static void installFlow(Flow flow){
		
		setFlowParameters(null,null,null,null,null,null,null,null,null,null,null,null);
		setFlowParameters(flow.getName(), flow.getId(), flow.getIngressPort(), flow.getEtherType(), flow.getProtocol(), flow.getNwDst(), flow.getNwSrc(), flow.getDlSrc(), flow.getDlDst(), flow.getPriority(), flow.getVlanId(), flow.getActions());
		generateJSONStringAndForward();
	
	}
	
	public static int deleteFlowRules(String nodeID,String flowName) {
		String authStr = odlUserName + ":" + odlPassword;
		String encodedAuthStr = Base64.encodeBase64String(authStr.getBytes());
		URL url;
		try {
			url = new URL("http://"+serverAddress+":"+serverPort+"/controller/nb/v2/flowprogrammer/default/node/OF/"+nodeID+"/staticFlow/"+flowName);
			
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("DELETE");
			connection.setDoOutput(true);
			connection.setRequestProperty("Authorization", "Basic " + encodedAuthStr);
			return connection.getResponseCode();
			
			
		} catch (IOException e) {
		
			e.printStackTrace();
			return 0;
		}
		
		
		
		
		

			
	}

}




