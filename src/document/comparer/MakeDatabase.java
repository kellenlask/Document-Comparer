package document.comparer;

/**
 *
 * @author Kellen
 */

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class MakeDatabase {

	public static void main(String[] args) throws SQLException {
		try {
			Database db = new Database();
			MakeDatabase mkdb = new MakeDatabase();			
			
			Scanner fin = new Scanner(mkdb.getFile());
			fin.useDelimiter("\\.");
			
			db.addTable("Words");
			
			for(int i = 0; i < 30; i++) {
				db.addColumn("Words", "" + i);
					
			} //End For Loop
			
			int max = 0;
			ArrayList<String> wordList = new ArrayList<>();
			boolean termExistsInDB;
			String tmp = null;
			Scanner sect;
			
			while(fin.hasNext()) {
				sect = new Scanner(fin.next());
				
				while(sect.hasNext()) {
					wordList.add(sect.next());
					
				}	
				
				db.addRecords("Words", wordList);
//				
				wordList.clear();
				tmp = null;	
			}		
			
			db.killDB();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	} //End main
	
	public File getFile() throws Exception {
		return new File(getClass().getClassLoader().getResource("resource/thesaurus.txt").toURI());
	}
	
} //End Class MakeSemanticDatabase
