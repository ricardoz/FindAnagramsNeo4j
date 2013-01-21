package anagrams;

import java.util.concurrent.Callable;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.StopEvaluator;
import org.neo4j.graphdb.Traverser;
import org.neo4j.graphdb.Traverser.Order;

public class RealFindAnagrams implements Callable {
	private Node node;
	private NodeMatchesLetterReturnEvaluator evaluator;

	public RealFindAnagrams(Node node, NodeMatchesLetterReturnEvaluator evaluator){
		this.node = node;
		this.evaluator = evaluator;
	}

	@Override
	public Object call() throws Exception {
		// TODO Auto-generated method stub
		return getFullAnagrams();
	}

	private Object getFullAnagrams() {
		Traverser t = node.traverse(
				Order.DEPTH_FIRST,
				StopEvaluator.DEPTH_ONE,
				evaluator,
				RelTypes.HAS_LENGTH,
				Direction.OUTGOING
				);
		
		return t.getAllNodes();
	}
	
	public static void main(String[] args){
		
		
		
		
		
	}
}
