package document.comparer;

/**
 *
 * @author Kellen
 */

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

//-------------------------------------------------------------
// The static methods for constructing functional vectors from documents 
// for vector space analysis. 
//-------------------------------------------------------------
public class Vectors {

	//-------------------------------------------------------------
	// Word Count Vector
	//-------------------------------------------------------------
	public static HashMap<String, Integer> makeWordVector(Scanner book) {
            HashMap<String, Integer> map = new HashMap<>();			
			String word;
            
            while(book.hasNext()) {
			word = book.next().toLowerCase();
			
			if(map.containsKey(word)) {
				map.put(word, map.get(word) + 1);
			} else {
				map.put(word, 1);
			}
		}
		
		return map;
	}

	//-------------------------------------------------------------
	//Order-based vector
	//-------------------------------------------------------------

	public static HashMap<String, Integer> makeOrderVector(Scanner in) {
		HashMap<String, Integer> map = new HashMap<>();
		String tmp1;
		
	//First fence post	
		if(in.hasNext()) {
			tmp1 = in.next();
		} else {
			tmp1 = null;
		}
		String tmp2 = null;

	//While we have tokens, add them to the vector	
		while(in.hasNext()) {
			tmp2 = in.next();
			
			if(map.containsKey(tmp1 + " " + tmp2)) {
				map.put(tmp1 + " " + tmp2, map.get(tmp1 + " " + tmp2) + 1);
			} else {
				map.put(tmp1 + " " + tmp2, 1);
			}
			
			tmp1 = tmp2;
		} //End while(in.hasNext())

	//Conditions for special cases: Exactly 1 token	
		if(tmp2 == null && tmp1 != null){
			map.put(tmp1, 1);
		}

		return map;
	} //End makeOrderVector(Scanner)

	//-------------------------------------------------------------
	//Semantics-based vector
	//-------------------------------------------------------------	
	
	public static HashMap<String, Integer> makeSemanticVector(HashMap<String, Integer> wcVector) throws SQLException {		
		HashMap<String, Integer> map = new HashMap<>();
		
		try {
			Database db = new Database();		
		
			String table = null;
			Set<String> keySet = wcVector.keySet();

			//While the vector has keys, grab their magnitude and throw it through the DB
			for(String s : keySet) {
				table = db.findTable(s);

				if(table != null && !map.containsKey(table)) {
					map.put(table, wcVector.get(s));			

				} else if(table != null && map.containsKey(table)) {
					map.put(table, wcVector.get(s) + map.get(table));

				}

				table = null;
			}		

			db.killDB();
		
		} catch (Exception ex) {}
		return map;
	
	} //End makeSemanticVector(HashMap<String, Integer>)
	
	
	//-------------------------------------------------------------
	// Give both vectors the same dimensionality 
	//-------------------------------------------------------------

	public static void sameMapKeys(Map<String, Integer> mapOne, Map<String, Integer> mapTwo) {
		//Create sets from the maps' keys 
		Set<String> setOne = mapOne.keySet();
		Set<String> setTwo = mapTwo.keySet();

		//Iterate over each key set and make sure each map has the same words.
		for(String s : setOne) {
			if(!mapTwo.containsKey(s)) {
				mapTwo.put(s, 0);			
			} 	
		}
		
		for(String s : setTwo) {
			if(!mapOne.containsKey(s)) {
				mapOne.put(s, 0);			
			} 	
		}
	} //End sameMapKeys(Map<String, Integer>, Map<String, Integer>)

	//-------------------------------------------------------------
	// Compare Vectors
	//-------------------------------------------------------------

	public static double compareVectors(HashMap<String, Integer> mapOne, HashMap<String, Integer> mapTwo) {
		return calcNumerator(mapOne, mapTwo)/(calcLength(mapOne)*calcLength(mapTwo));

	} //End compareVectors(Map<String, Integer>, Map<String, Integer>)

	//Requires that sameMapKeys be run before this method
	public static double calcNumerator(HashMap<String, Integer> mapOne, HashMap<String, Integer> mapTwo) {
		Set<String> keys = mapOne.keySet();
		double sum = 0;	
		
		for(String s : keys) {
			sum += mapOne.get(s).intValue() * mapTwo.get(s).intValue();		
		}
		
		return sum;
		
	}//End calcNumerator(Map<String, Integer>, Map<String, Integer>)

	public static double calcLength(HashMap<String, Integer> map) {
		Set<String> keys = map.keySet();
		double sum = 0;
		
		//Find the sum of the squares
		for(String s : keys) {
			sum += Math.pow(map.get(s).intValue(), 2);	
		}
		
		return Math.sqrt(sum);
		
	}//End calcLength(Map<String, Integer>)
} //End Class Vectors
