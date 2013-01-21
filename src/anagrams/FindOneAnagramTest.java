package anagrams;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.Index;
import org.neo4j.kernel.EmbeddedGraphDatabase;

public class FindOneAnagramTest {

	private String DB_PATH;
	private GraphDatabaseService graphDb;
	private Index<Node> numbersIndex;
	private Node lengthNode;
	private NodeMatchesLetterReturnEvaluator returnableEvaluator;
	private RealFindAnagrams rfe;
	
	

	@Before
	public void setUp() throws Exception {
		DB_PATH = "testAnagrams";
		graphDb = new EmbeddedGraphDatabase( DB_PATH );
		registerShutdownHook();
		numbersIndex = graphDb.index().forNodes( "numbers" );
		String word = "node";
		lengthNode = numbersIndex.get("length", word.length()).getSingle();
		
		returnableEvaluator = new NodeMatchesLetterReturnEvaluator(word);
		
		rfe = new RealFindAnagrams(lengthNode, returnableEvaluator);
	}

	@Test
	public void test() {
		try {
			System.out.println(rfe.call());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// TODO
	}
	
	private  void shutdown()
	{

		graphDb.shutdown();
	}
	
	@After
	public void tearDown() throws Exception {
		shutdown();
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

}
