package findanagrams;

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
import org.neo4j.graphmatching.CommonValueMatchers;
import org.neo4j.graphmatching.PatternMatch;
import org.neo4j.graphmatching.PatternMatcher;
import org.neo4j.graphmatching.PatternNode;
import org.neo4j.graphmatching.PatternRelationship;
import org.neo4j.graphmatching.ValueMatcher;
import org.neo4j.kernel.EmbeddedGraphDatabase;

import com.google.common.collect.Multiset;

public class FindOneAnagram {

	private  final String DB_PATH = "testAnagrams";
	private  GraphDatabaseService graphDb;
	private  Index<Node> numbersIndex;
	private  int totalRel =0;
	private String word;
	
	private Node lengthNode;

	private Collection<Node> allNodes;
	private final NodeMatchesLetterReturnEvaluator evaluator;
	
	
	private Multiset<Character> multiset;

	public FindOneAnagram(GraphDatabaseService graphDb, String word, final NodeMatchesLetterReturnEvaluator evaluator){
		
		this.graphDb = graphDb;
		this.numbersIndex = graphDb.index().forNodes( "numbers" );
		this.word = word;
		this.evaluator = evaluator;
		this.multiset = PopulateWordsHelpers.convertStringToMultimap(word);

		//map = PopulateWordsHelpers.convertStringToCharacterMap(word);
		//System.out.println(map );
		//evaluators = createEvaluators();



		//setUpGraphDb();
		lengthNode = findLengthNode();
		
		
		Traverser t = lengthNode.traverse(
				Order.DEPTH_FIRST,
				StopEvaluator.DEPTH_ONE,
				evaluator,
				RelTypes.HAS_LENGTH,
				Direction.OUTGOING
				);
		
		allNodes = t.getAllNodes();
		
		for (Node n : allNodes){
			System.out.println("Answer..n." + n.getProperty("word") );
		}


	}
	
	/**
	 * 10. Start at the node representing the length of the word
	 * 20. Create a relationship from theLengthNode to a wordNode
	 * 30. Iterate the characters in the multiset
	 * 32. Create a pattern node representing current character and
	 *     add a property constraint that it must match the current character
	 * 34. Add a relationship between the character pattern node and word node
	 * 40. Get the matcher
	 * 50. Get the iterator and iterate
	 */
	public void findAnagramUsingPatternMatching(){
		
		// 10. length node should represent 4 and can be only one node
		//	   so we associate it with that node
		final PatternNode theLengthNode = new PatternNode();
		theLengthNode.setAssociation(lengthNode);
		
		// 20. wordNode could be any node with the correct properties
		final PatternNode wordNode = new PatternNode();
		theLengthNode.createRelationshipTo(wordNode, RelTypes.HAS_LENGTH);

		
		// 30. Iterate the characters in the multiset
		for (final Character c : multiset){
			
			// 32. Create a pattern node representing current character and
			//     add a property constraint that it must match the current character
			PatternNode characterPatternNode = new PatternNode();
			characterPatternNode.addPropertyConstraint( "letter",
					CommonValueMatchers.exact( c ) );
			
			// 34. Add a relationship between the character pattern node and word node
			PatternRelationship localPatternRelationship = wordNode.createRelationshipTo(characterPatternNode, RelTypes.HAS_LETTER);
			localPatternRelationship.addPropertyConstraint("occurrences", new ValueMatcher(){
				public boolean matches(Object value){
					boolean result = (int)value == multiset.count(c);
					return result;
				}
			});
		}


		// 40. Get the matcher
		PatternMatcher pm = PatternMatcher.getMatcher();
		
		// 50. Get the iterator and iterate
		final Iterable<PatternMatch> matches = pm.match(theLengthNode, lengthNode);
		Iterator<PatternMatch> iter = matches.iterator();
		while(iter.hasNext()){
			PatternMatch match = iter.next();
			Node matchNode = match.getNodeFor(wordNode);
			System.out.println("matches from PM: " + matchNode.getProperty("word") );
		}
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

	private void setUpGraphDb() {
		// START SNIPPET: startDb
		graphDb = new EmbeddedGraphDatabase( DB_PATH );
		
		System.out.println("---li----" + numbersIndex);
		//registerShutdownHook();
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
		//shutdown();
	}
	
	private void shutdown()
	{
		
		graphDb.shutdown();
	}

	private void registerShutdownHook()
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
		GraphDatabaseService graphDb = new EmbeddedGraphDatabase( "testAnagrams" );
		
		
		
		System.out.println("---li----" );
		FindOneAnagram fom = new FindOneAnagram(graphDb, "node", new NodeMatchesLetterReturnEvaluator("node"));
		fom.findAnagramUsingPatternMatching();
	}
}
