package findanagrams;

import java.util.List;
import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;
import org.neo4j.kernel.EmbeddedGraphDatabase;



public class PopulateWords {

	private static final String DB_PATH = "testAnagrams8";
	
	private static GraphDatabaseService graphDb;
	private static Index<Node> lettersIndex;
	private static int totalRel =0;
	private static Index<Node> numbersIndex;

	public static void main( final String[] args )
	{
		// START SNIPPET: startDb
		graphDb = new EmbeddedGraphDatabase( DB_PATH );
		lettersIndex = graphDb.index().forNodes( "letters" );
		numbersIndex = graphDb.index().forNodes( "numbers" );
		System.out.println("---li----" + lettersIndex);
		registerShutdownHook();
		// END SNIPPET: startDb

		char upper;
		Relationship letterRelationship;
		Node wordNode;
		int occ = 0;
		Node letterNode = lettersIndex.get("letter", '-').getSingle();
		System.out.println("LI---" + letterNode.getProperty("letter"));

		for ( upper = 'a';  upper <= 'z';  upper++ ){
			// START SNIPPET: addUsers

			List<String> list = PopulateWordsHelpers.readCsv(upper);

			for (String s : list){
				
				
				Transaction tx = graphDb.beginTx();
				
				
				try
				{
					
					//create the Node for this word
					wordNode = graphDb.createNode();
					wordNode.setProperty("word", s);
					
					//create Relationship to length
					Node lengthNode = numbersIndex.get("length", s.length()).getSingle();
					
					lengthNode.createRelationshipTo(wordNode, RelTypes.HAS_LENGTH);
					
					// Get Map
					Map<Character,Integer> map = PopulateWordsHelpers.convertStringToCharacterMap(s);

					for (Map.Entry<Character, Integer> entry : map.entrySet()){
						//System.out.println(entry);
						occ = entry.getValue();
						letterNode = lettersIndex.get("letter", entry.getKey()).getSingle();
						//System.out.println("entry:" + entry.getKey());
						
						
						if (letterNode!=null) {
							//create relationship
							//letterRelationship = wordNode.createRelationshipTo(letterNode, OccurrenceRelTypes.get(occ));
							//letterRelationship.setProperty("occurrences", occ);
						}
						totalRel++;


					}
					

					//letterRelationship = graphDb.getReferenceNode().createRelationshipTo(
					//   letterNode, RelTypes.IS_LETTER );

					// END SNIPPET: addUsers
					//System.out.println( "Letter created: " + upper + " -- " + wordNode.getProperty("letter") );




					tx.success();
				}
				finally
				{
					tx.finish();
				}


			}
		}
		System.out.println( "Shutting down database ..." + totalRel);
		shutdown();
	}

	private static void shutdown()
	{
		
		graphDb.shutdown();
	}



	private static void registerShutdownHook()
	{
		// Registers a shutdown hook for the Neo4j and index service instances
		// so that it shuts down nicely when the VM exits (even if you
		// "Ctrl-C" the running example before it's completed)
		Runtime.getRuntime().addShutdownHook( new Thread()
		{
			@Override
			public void run()
			{
				shutdown();
			}
		} );
	}

}
