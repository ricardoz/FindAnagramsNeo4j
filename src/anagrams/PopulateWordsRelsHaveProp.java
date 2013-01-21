package anagrams;

import java.util.List;
import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;
import org.neo4j.kernel.EmbeddedGraphDatabase;



public class PopulateWordsRelsHaveProp {

	private static final String DB_PATH = "testAnagrams";
	
	private static GraphDatabaseService graphDb;
	private static Index<Node> lettersIndex;
	private static int totalRel =0;
	private static Index<Node> numbersIndex;

	public static void main( final String[] args )
	{
		// START SNIPPET: startDb
		graphDb = new EmbeddedGraphDatabase( DB_PATH );
		registerShutdownHook();
		// END SNIPPET: startDb
		
		// START SNIPPET: getIndexes
		lettersIndex = graphDb.index().forNodes( "letters" );
		numbersIndex = graphDb.index().forNodes( "numbers" );
		System.out.println("---li----" + lettersIndex);
		// END SNIPPET: getIndexes

		char letter;
		Relationship wordToLetterRelationship;
		Node wordNode;
		int occ = 0;
		Node letterNode = lettersIndex.get("letter", '-').getSingle();
		System.out.println("Check '-' is in index: " + letterNode.getProperty("letter"));

		for ( letter = 'a';  letter <= 'z';  letter++ ){
			// START SNIPPET: addWords

			//get the words from the csv files
			List<String> list = PopulateWordsHelpers.readCsv(letter);

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

					//For each letter in the word
					for (Map.Entry<Character, Integer> entry : map.entrySet()){
						//System.out.println(entry);
						occ = entry.getValue();
						letterNode = lettersIndex.get("letter", entry.getKey()).getSingle();
						//System.out.println("entry:" + entry.getKey());
						
						
						if (letterNode!=null) {
							//create relationship
							wordToLetterRelationship = wordNode.createRelationshipTo(
									letterNode, HasLetterRelTypes.HAS_LETTER);
							wordToLetterRelationship.setProperty("occurrences", occ);
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
