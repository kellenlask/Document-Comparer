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
			
			ArrayList<String> wordList = new ArrayList<>();
			boolean termExistsInDB;
			String tmp = null;
			Scanner sect;
			
			while(fin.hasNext()) {
				sect = new Scanner(fin.next());
				
				while(sect.hasNext()) {
					wordList.add(sect.next());
				}	
				
				termExistsInDB = false;
				
				for(String s : wordList) {
					
					tmp = db.findTable(s);
					if(tmp != null) {
						db.addRecords(tmp, wordList);
						break;
					} 
				}
			
				if(tmp == null && wordList.size() > 0) {
					db.addTable(wordList.get(0));
					db.addRecords(wordList.get(0), wordList);
				} 
				
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
