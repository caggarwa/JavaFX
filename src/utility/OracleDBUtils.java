package utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executor;

public class OracleDBUtils {

	Connection conn;
	ResultSet resultSet;
	Statement stmt;

	public Connection ConnectOracle(String serverName, String portNumber, String sid, String username,
			String password) {
		try {

			String url = "jdbc:oracle:thin:@//" + serverName + ":" + portNumber + "/" + sid; // url="jdbc:oracle:thin:@//dbsrt0720.uhc.com:1521/brs2ts00svc.uhc.com";
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(url, username, password);
			
			if ((!conn.isClosed()) || (conn != null)) {
				System.out.println("ConnectOracle : Oracle connection is established.");
				
			} else {
				System.out.println("ConnectOracle : Oracle connection is Closed or NUll.");
				
				
			}
		} catch (Exception e) {
			System.out.println((e.toString()));
		}
		return conn;
	}

	/**
	 * This method will return a result of query execution
	 * 
	 * @param sQuery
	 * @return
	 */
	public ResultSet getRecords(String sQuery) {

		try {
			if (conn == null) {
				System.out.println("getRecords: conn is null");
				Log.error("DB Connection is null");
			}
			if (conn != null && !conn.isClosed()) {
				stmt = conn.createStatement();
				resultSet = stmt.executeQuery(sQuery);
			} else {
				System.out.println("getRecords : Connection is closed");
				Log.error("Connection is already closed");
			}

		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			Log.error("SQLException: " + ex.getMessage());
			Log.error("SQLState: " + ex.getSQLState());
			Log.error("VendorError: " + ex.getErrorCode());
		}

		return resultSet;

	}
	
	public static ResultSet getRecords(Connection conn, String sQuery) {
		ResultSet resultSet=null;
		try {
			if(conn ==null)
			{
				System.out.println("is null");
//				Log.error("DB Connection is null");
			}
			if (conn != null && !conn.isClosed() ) {
//				System.out.println("DB Connection: " + conn.isClosed());
				Statement stmt = conn.createStatement(
					    ResultSet.TYPE_SCROLL_INSENSITIVE,
					    ResultSet.CONCUR_READ_ONLY);
				stmt.setQueryTimeout(1000);
				resultSet = stmt.executeQuery(sQuery);
				resultSet.beforeFirst();
				
			} else {
//				System.out.println("Connection is closed");
				Log.error("Connection is already closed");
			}

		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			Log.error("SQLException: " + ex.getMessage());
			Log.error("SQLState: " + ex.getSQLState());
			Log.error("VendorError: " + ex.getErrorCode());
		}

		return resultSet;

	}
	
	public ResultSet getrecordsfromDB(Connection conn, String sQuery) {
		ResultSet resultSet=null;
		try {
			if(conn ==null)
			{
				System.out.println("is null");
			}
			if (conn != null && !conn.isClosed() && conn.isValid(100)) {

				Statement stmt = conn.createStatement(
				ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
				stmt.setQueryTimeout(1000);
				resultSet = stmt.executeQuery(sQuery);
				resultSet.beforeFirst();
				
			} else {

				Log.error("Connection is already closed");
			}

		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			Log.error("SQLException: " + ex.getMessage());
			Log.error("SQLState: " + ex.getSQLState());
			Log.error("VendorError: " + ex.getErrorCode());
			resultSet=null;
		}

		return resultSet;

	}
	
	public static  int updateQuery(Connection conn, String sQuery) {
		int rowsUpdated=0;
		try {
			if(conn ==null)
			{
				System.out.println("is null");
//				Log.error("DB Connection is null");
			}
			if (conn != null && !conn.isClosed() ) {
//				System.out.println("DB Connection: " + conn.isClosed());
//				Statement stmt = conn.createStatement(
//					    ResultSet.TYPE_SCROLL_INSENSITIVE,
//					    ResultSet.CONCUR_READ_ONLY);
				Statement stmt = conn.createStatement();
				rowsUpdated = stmt.executeUpdate(sQuery);
			} else {
				System.out.println("Connection is closed");
				Log.error("Connection is already closed");
			}

		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			Log.error("SQLException: " + ex.getMessage());
			Log.error("SQLState: " + ex.getSQLState());
			Log.error("VendorError: " + ex.getErrorCode());
		}

		return rowsUpdated;

	}
	
public static  String getDBrec(Connection con,String sQuery) throws SQLException{
		
		Statement stmt = con.createStatement();
		stmt.setMaxRows(1);
		ResultSet rs = stmt.executeQuery(sQuery);
		
		String Data="";
		
		while(rs.next()){
			for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
				//Data.add(rs.getMetaData().getColumnName(i));
				Data=rs.getString(i);
				
			}
		}
				
		return Data;
		
	}

public String getDBrec(String sQuery) throws SQLException{
	
	Statement stmt = conn.createStatement();
	stmt.setMaxRows(1);
	ResultSet rs = stmt.executeQuery(sQuery);
	
	String Data="";
	
	while(rs.next()){
		for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
			//Data.add(rs.getMetaData().getColumnName(i));
			Data=rs.getString(i);
			
		}
	}
			
	return Data;
	
}

	public List<HashMap<String, String>> getDBRecords(Connection con, String sQuery) {

		List<HashMap<String, String>> dbDataLists = new ArrayList<HashMap<String, String>>();
		ResultSet rs = null;
		try {
			HashMap<String, String> valMap;

			Statement stmt = con.createStatement();
			rs = stmt.executeQuery(sQuery);
			while (rs.next()) {
				valMap = new HashMap<String, String>();
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
					String colName = rs.getMetaData().getColumnName(i);
					String value = rs.getString(i);
					valMap.put(colName, value);
				}
				// Hash Map data is added in List for each row
				dbDataLists.add(valMap);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			try {
				rs.close();
				rs=null;
			} catch (SQLException e) {
				
			}
			
		}
		
		return dbDataLists;
	}

	public static  List resultSetToArrayList(ResultSet rs) throws SQLException {
		ResultSetMetaData md = rs.getMetaData();
		int columns = md.getColumnCount();
		ArrayList list = new ArrayList<String>(50);
		while (rs.next()) {
			HashMap row = new HashMap(columns);
			for (int i = 1; i <= columns; ++i) {
				row.put(md.getColumnName(i), rs.getObject(i));
			}
			list.add(row);
		}

		return list;
	}
	
	public  List resultSetToArraylist(ResultSet rs) throws SQLException {
		ResultSetMetaData md = rs.getMetaData();
		int columns = md.getColumnCount();
		ArrayList list = new ArrayList<String>(50);
		while (rs.next()) {
			HashMap row = new HashMap(columns);
			for (int i = 1; i <= columns; ++i) {
				row.put(md.getColumnName(i), rs.getObject(i));
			}
			list.add(row);
		}

		return list;
	}
	
	

	public static int getRecordCount(ResultSet resultset){
		
		int RecordCount=0;
		try{
			if (resultset!=null){
				resultset.beforeFirst();
				resultset.last();
				RecordCount=resultset.getRow();
				resultset.beforeFirst();
				
				Log.info("Total records in the Resultset: " + RecordCount);
			}
		}
		catch(Exception ex){
			Log.error("Exception in getting the records count of resultset. :" + ex.getMessage() );
		}
			
		return RecordCount;
	}
	
public int getRecordcount(ResultSet resultset){
		
		int RecordCount=0;
		try{
			if (resultset!=null){
				resultset.beforeFirst();
				resultset.last();
				RecordCount=resultset.getRow();
				resultset.beforeFirst();
				
				Log.info("Total records in the Resultset: " + RecordCount);
			}
		}
		catch(Exception ex){
			Log.error("Exception in getting the records count of resultset. :" + ex.getMessage() );
		}
			
		return RecordCount;
	}
	
	/**
	 * This method will close all opened result set, connections, statements
	 */
	public void closeConnection() {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (Exception e) {
				Log.error(e.toString());
			}

		}

		if (stmt != null) {
			try {
				stmt.close();
			} catch (Exception e) {
				Log.error(e.toString());
			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception e) {
				Log.error(e.toString());
			}
		}

	}
	public int updateQuery(String sQuery) {
		int rowsUpdated=0;
		try {
			if(conn ==null)
			{
				System.out.println("is null");
//				Log.error("DB Connection is null");
			}
			if (conn != null && !conn.isClosed() ) {
//				System.out.println("DB Connection: " + conn.isClosed());
//				Statement stmt = conn.createStatement(
//					    ResultSet.TYPE_SCROLL_INSENSITIVE,
//					    ResultSet.CONCUR_READ_ONLY);
				Statement stmt = conn.createStatement();
				rowsUpdated = stmt.executeUpdate(sQuery);
			} else {
				System.out.println("Connection is closed");
				Log.error("Connection is already closed");
			}

		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			Log.error("SQLException: " + ex.getMessage());
			Log.error("SQLState: " + ex.getSQLState());
			Log.error("VendorError: " + ex.getErrorCode());
		}

		return rowsUpdated;

	}
	
	/**
	 * Added by Shekar Dubbaka	
	 * @param con : COnn obj
	 * @param sQuery : query to be executed
	 * @return  : Hash Map
	 */
		public static HashMap<String, String> get_DBRecords(Connection con, String sQuery) {

			HashMap<String, String> valMap= new HashMap<String, String>();
			try {
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(sQuery);
				while (rs.next()) {
					
					for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
						String colName = rs.getMetaData().getColumnName(i);
						String value = rs.getString(i);
						valMap.put(colName, value);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return valMap;
		}
	
	
}
