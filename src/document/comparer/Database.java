package document.comparer;

/**
 *
 * @author Kellen
 */

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

//-------------------------------------------------------------
// Class to abstract away interfacing with a database for the purposes of
// creating a semantic database and then using said database to semantically
// analyse two documents for vector space comparison. 
//-------------------------------------------------------------

public class Database {

//Fields
	protected Connection connect;
	protected ArrayList<String> tables;

//Constructor
	public Database() throws Exception {
		String dbPath = "./smdb.db";
		
		Class.forName("org.sqlite.JDBC");
		String url = "jdbc:sqlite:" + dbPath;
		
		connect = DriverManager.getConnection(url);
		
		setTables();
		
	} //End Constructor

//Accessors
	public ArrayList<String> getTables() {
		return tables;
	}

	//Get a result set containing the Database's tables from the Meta-Data
	public ResultSet getTablesResSet() throws SQLException {
		DatabaseMetaData meta = connect.getMetaData();
		
		String[] tableTypes = {
        "TABLE",
        };
		
		//Statement stmt = connect.createStatement();
		ResultSet resSet = meta.getTables(null, null, "%", tableTypes);
		//stmt.executeQuery("SELECT * FROM sys.Tables;");
		
		/*if(stmt != null) {
			stmt.close();
		}*/
		
		return resSet;	
	}

	//Checks to see whether or not a particular record exists across all tables.
	public String findTable(String record) throws SQLException {
		
		boolean match = false;
		
		for(String table : tables) {
			if(containsRecord(table, record)) {
				return table;
			}
		}
		
		return null;	
	} //End findTable(String)
	
	//Checks to see whether or not a particular record exists in a particular table.
	public boolean containsRecord(String table, String record) throws SQLException {
		Statement stmt = connect.createStatement();
		
		boolean hasRecord; 
		
		ResultSet resSet = stmt.executeQuery("SELECT * FROM " + table + " WHERE Word='" + record + "';");
		hasRecord = resSet.next();
            
		
		if(stmt != null) {
			stmt.close();
		}            
		
		return hasRecord;
	
	} //End containsRecord(String, String)
	
	//Checks to see whether or not the database contains a specific table.
	public boolean containsTable(String table) throws SQLException {
		DatabaseMetaData meta = connect.getMetaData(); 
		
		return tables.contains(table);
	} //End containsTable(String)		
	
//Mutators	
	//Populates the tables ArrayList<String>
	public void setTables() throws SQLException {
		ResultSet resSet = getTablesResSet();
		tables = new ArrayList<>();
		String tmpTable = null;
		
		while(resSet.next()) {
			tmpTable = resSet.getString(3);
			
			if(!tmpTable.contains("~")) {
				tables.add(tmpTable);				
			}			
		}
	}	
	
	//Creates a table in the database
	public void addTable(String table) throws SQLException {
		Statement stmt = connect.createStatement();
		
		try {
			stmt.executeUpdate("CREATE TABLE " + table + " (Word varchar(50));");
			
			if(stmt != null) {
				stmt.close();
			}
		} catch(SQLException except) {
			System.out.println(table);
			except.printStackTrace();
		}
		
		if(!tables.contains(table)) {
			tables.add(table);
		}
	} //End addTable(String)	

	//Adds any and all records in a given ArrayList<String> to a given table.
	public void addRecords(String table, ArrayList<String> records) throws SQLException {
		try {
			Statement stmt = connect.createStatement();
			
			for(String s : records) {
				
				if(!containsRecord(table, s)) {	
					stmt.executeUpdate("INSERT INTO " + table + " (Word) VALUES ('" + s + "');");	
				}				
			}
			
			if(stmt != null) {
				stmt.close();
			}
		} catch (SQLException except) {
			System.out.println("Table: " + table);
			except.printStackTrace();
			
		}
	} //End addRecords(String, String[])	
	
	//Kills the database connection to avoid memory leaks.
	public void killDB() throws SQLException {
		if(connect != null) {
			connect.close();	
		}

	} //End killDB()	

} //End Class Database
