package findanagrams;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;
import org.neo4j.kernel.EmbeddedGraphDatabase;

public class PoplulateLetters2 {
    private static final String DB_PATH = "testAnagrams125";
    //private static final String USERNAME_KEY = "username";
    private static GraphDatabaseService graphDb;
    private static Index<Node> lettersIndex;
	private static Index<Node> numbersIndex;

    public static void main( final String[] args )
    {
        // START SNIPPET: startDb
        graphDb = new EmbeddedGraphDatabase( DB_PATH );
        lettersIndex = graphDb.index().forNodes( "letters" );
        numbersIndex = graphDb.index().forNodes( "numbers" );
        registerShutdownHook();
        // END SNIPPET: startDb

        char upper;
        Relationship letterRelationship;
        Node letterNode = null;
        Node numberNode;
        
        for ( upper = 'a';  upper <= 'z';  upper++ ){
        // START SNIPPET: addUsers
        Transaction tx = graphDb.beginTx();
        try
        {
            // Create letters sub reference node (see design guidelines on
            // http://wiki.neo4j.org/ )
            letterNode = graphDb.createNode();
            letterNode.setProperty("letter", upper);
            
            lettersIndex.add(letterNode, "letter", upper);
            
            //letterRelationship = graphDb.getReferenceNode().createRelationshipTo(letterNode, RelTypes.IS_LETTER );
          
            // END SNIPPET: addUsers
            //System.out.println( "Letter created: " + upper + " -- " + letterNode.getProperty("letter") );

          
            System.out.println( "Letter index: " + upper + " --- " + lettersIndex.get("letter", upper).getSingle().getProperty("letter") );
            
            tx.success();
        }
        finally
        {
            tx.finish();
        }
        
        
        
        
        }
        
        for ( int length = 0;  length <= 40;  length++ ){
            // START SNIPPET: addUsers
            Transaction tx = graphDb.beginTx();
            try
            {
                // Create letters sub reference node (see design guidelines on
                // http://wiki.neo4j.org/ )
                numberNode = graphDb.createNode();
                numberNode.setProperty("length", length);
                
                numbersIndex.add(numberNode, "length", length);
                
                //letterRelationship = graphDb.getReferenceNode().createRelationshipTo(letterNode, RelTypes.IS_LETTER );
              
                // END SNIPPET: addUsers
                //System.out.println( "Letter created: " + upper + " -- " + letterNode.getProperty("letter") );

              
                //System.out.println( "Letter index: " + upper + " --- " + lettersIndex.get("letter", upper).getSingle().getProperty("letter") );
                
                tx.success();
            }
            finally
            {
                tx.finish();
            }
            
            
            
            
            }
        
        Transaction tx = graphDb.beginTx();
        try
        {
            // Create letters sub reference node (see design guidelines on
            // http://wiki.neo4j.org/ )
            letterNode = graphDb.createNode();
            letterNode.setProperty("letter", '-');
            
            lettersIndex.add(letterNode, "letter", '-');
            
            //letterRelationship = graphDb.getReferenceNode().createRelationshipTo(letterNode, RelTypes.IS_LETTER );
          
            // END SNIPPET: addUsers
            //System.out.println( "Letter created: " + upper + " -- " + letterNode.getProperty("letter") );

          
            System.out.println( "Letter index: -   --- " + lettersIndex.get("letter", '-').getSingle().getProperty("letter") );
            
            tx.success();
        }
        finally
        {
            tx.finish();
        }
        
        
        System.out.println( "Shutting down database ..." );
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
