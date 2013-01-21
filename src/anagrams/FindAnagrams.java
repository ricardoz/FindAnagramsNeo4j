package anagrams;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ReturnableEvaluator;
import org.neo4j.graphdb.StopEvaluator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.Traverser;
import org.neo4j.graphdb.Traverser.Order;
import org.neo4j.graphdb.index.Index;
import org.neo4j.kernel.EmbeddedGraphDatabase;

public class FindAnagrams {

	private  final String DB_PATH = "testAnagrams8";
	private  GraphDatabaseService graphDb;
	private  Index<Node> numbersIndex;
	private  int totalRel =0;
	private String word;

	private Node lengthNode;
	private Map<Character,Integer> map;

	private Collection<Node> allNodes;

	public FindAnagrams(String word){
		this.word = word;

		map = PopulateWordsHelpers.convertStringToCharacterMap(word);
		//System.out.println(map );
		//evaluators = createEvaluators();



		setUp();
		lengthNode = findLengthNode();
		
		Iterator<Relationship> rels = lengthNode.getRelationships().iterator();
		
		
		while (rels.hasNext()){
			//System.out.pr
		}
		Traverser t = lengthNode.traverse(
				Order.DEPTH_FIRST,
				StopEvaluator.DEPTH_ONE,
				new NodeMatchesLetterReturnEvaluator(word),
				RelTypes.HAS_LETTER,
				Direction.INCOMING
				);
		
		allNodes = t.getAllNodes();
		
		for (Node n : allNodes){
			System.out.println("Answer:" + n.getProperty("word"));
		}
				//allWordsOfCorrectLength = returnAllWordsOfCorrectLength();

				//System.out.println(allWordsOfCorrectLength.size());

				/**for (Node n : allWordsOfCorrectLength ){
			Iterable<Relationship> nodeRelationships = n.getRelationships();

			for (Relationship r : nodeRelationships ) {
				//for each rel
				try {
					Node other = r.getOtherNode(n);
					System.out.println("yes" + map.get((Character)other.getProperty("letter")) );
					if (map.get((Character)other.getProperty("letter")) != null){
						System.out.println("yessss");
					}
				} catch (Exception e) {

				}
			}
		}*/
		/**
				//ArrayList<Node> newList = new ArrayList<Node>();

				for (Node n : allWordsOfCorrectLength ){

					//System.out.println(n.getProperty("word") + ":--" + n.getRelationships());



					int x = 0;


					//System.out.println(n.getProperty("word") + ":--" + x);

					for (NodeMatchesLetterReturnEvaluator eval : evaluators) {
						if (rand = n.traverse(
								Order.DEPTH_FIRST,
								StopEvaluator.DEPTH_ONE,
								eval,
								//OccurrenceRelTypes.get(eval.getOccurrences()),
								Direction.OUTGOING
								//OccurrenceRelTypes.ONE
								).getAllNodes().size() == 0
								){
							//allWordsOfCorrectLength.remove(n);
							//System.out.println("Size of words now: " + allWordsOfCorrectLength.size());
						} else {
							System.out.println("contents" + rand);
						}


					}
				}*/


	}

	private Collection<NodeMatchesLetterReturnEvaluator> createEvaluators() {
		ArrayList<NodeMatchesLetterReturnEvaluator> list = new ArrayList<>();
		for (Map.Entry<Character, Integer> entry : map.entrySet()){
			//boolean add = list.add( new NodeMatchesLetterReturnEvaluator(null));
		}

		return list;
	}

	private Collection<Node> returnAllWordsOfCorrectLength() {
		Traverser traverser = lengthNode.traverse(
				Order.BREADTH_FIRST, 
				StopEvaluator.DEPTH_ONE, 
				ReturnableEvaluator.ALL_BUT_START_NODE,
				RelTypes.HAS_LENGTH,
				Direction.OUTGOING
				);
		return traverser.getAllNodes();
	}

	private void setUp() {
		// START SNIPPET: startDb
		graphDb = new EmbeddedGraphDatabase( DB_PATH );
		numbersIndex = graphDb.index().forNodes( "numbers" );
		System.out.println("---li----" + numbersIndex);
		registerShutdownHook();
		// END SNIPPET: startDb

	}

	private Node findLengthNode() {
		return numbersIndex.get("length", word.length()).getSingle();
	}

	public void foo( final String[] args )
	{


		char upper;
		Relationship letterRelationship;
		Node wordNode;


		System.out.println( "Shutting down database ..." + totalRel);
		shutdown();
	}

	private  void shutdown()
	{

		graphDb.shutdown();
	}



	private  void registerShutdownHook()
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

	public static void main(String[] args){
		new FindAnagrams("node");
	}
}
