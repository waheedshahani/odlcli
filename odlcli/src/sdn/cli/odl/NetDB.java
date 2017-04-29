package sdn.cli.odl;
/**
 *This class is used to write and retrieve sql records from sqllite database present in sqlitedblib folder
 * * Created by Shahani Waheed Ali waheed.shahani@gmail.com 
 ****/
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class NetDB {



	public boolean insertIntoDB(String nodeID, String portNumber, String flowName, String type, int vlanNumber) throws SQLException{
		Connection c = null;
		Statement stmt=null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:sqlitedblib/networkDB.db");



			stmt = c.createStatement();


			
			String sql = "INSERT INTO SWITCHING (SWID,SWNAME,PORTNO,VLANNO,PORTTYPE,FLOWNAME) " +
					"VALUES ('"+nodeID+"',"+"'dummy',"+portNumber+",'"+vlanNumber+"','"+type+"','"+flowName+"');"; 
			stmt.executeUpdate(sql);


		}
		catch(Exception e){
			System.out.println("Catch clause in Writing records");
			System.out.println(e);


			return false;
		}
		stmt.close();
		c.close();
		return true;


	}


	//Called by trunk links only
	public ArrayList<String> getPortAndVLANIDFromDB(String SwID){
		Connection c = null;
		Statement stmt=null;
		ArrayList<String> resultList=new ArrayList<String>();
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:sqlitedblib/networkDB.db");

			//System.out.println("Opened database successfully for SQL Querry");

			stmt = c.createStatement();

			ResultSet rs = stmt.executeQuery( "SELECT * FROM SWITCHING where SWID = '"+SwID+"' AND VLANNO !=0 AND PORTTYPE='ACCESS';" );

			while ( rs.next() ) {

				resultList.add(Integer.toString(rs.getInt("PORTNO")));
				resultList.add(rs.getString("VLANNO"));
			}
			rs.close();
			stmt.close();
			c.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );

		}

		return resultList;
	}




	//called by Access links only to find list of trunk ports on current switch
	public ArrayList<String> getListOfTrunks(String SwID){
		Connection c = null;
		Statement stmt=null;
		ArrayList<String> resultList=new ArrayList<String>();
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:sqlitedblib/networkDB.db");
stmt = c.createStatement();

			ResultSet rs = stmt.executeQuery( "SELECT * FROM SWITCHING where SWID = '"+SwID+"' AND PORTTYPE='TRUNK' AND VLANNO =0;" );

			while ( rs.next() ) {

				resultList.add(Integer.toString(rs.getInt("PORTNO")));

				   
			}
			rs.close();
			stmt.close();
			c.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );

		}

		return resultList;
	}

	public void deleteRecords(String nodeID, String portNumber){
		Connection c = null;
		Statement stmt=null;

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:sqlitedblib/networkDB.db");
			stmt = c.createStatement();

		if(portNumber.equals("ALL") &&  nodeID.equals("ALL"))	
			stmt.executeUpdate( "DELETE FROM SWITCHING;" );
		else 
			stmt.executeUpdate( "DELETE FROM SWITCHING where PORTNO='"+portNumber+"' AND SWID='"+nodeID+"';");
		
		}
		catch(Exception e){

			System.out.println("Error in deleting all records from database"+e);
		}
	}
	public void deleteOneRecord(){
		
	}

}

