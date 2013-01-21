package anagrams;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import org.neo4j.graphdb.RelationshipType;



// START SNIPPET: createRelTypes
 public enum OccurrenceRelTypes implements RelationshipType
{
	ONE(1),
	TWO(2),
	THREE(3),
	FOUR(4),
	FIVE(5),
	SIX(6),
	SEVEN(7);

	private int occurrences;
	
	 private static final Map<Integer, OccurrenceRelTypes> lookup 
     = new HashMap<Integer, OccurrenceRelTypes>();

static {
     for(OccurrenceRelTypes ort : EnumSet.allOf(OccurrenceRelTypes.class))
          lookup.put(ort.getOccurrences(), ort);
}

	 OccurrenceRelTypes(int occ){
		this.occurrences = occ;
	}

	public int getOccurrences(){
		return occurrences;
	}
	
	public static OccurrenceRelTypes get(int code) { 
          return lookup.get(code); 
     }
}
// END SNIPPET: createRelTypes