package findanagrams;

import java.util.Iterator;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ReturnableEvaluator;
import org.neo4j.graphdb.TraversalPosition;

import com.google.common.collect.Multiset;

class NodeMatchesLetterReturnEvaluator implements ReturnableEvaluator {


	private Multiset<Character> multiset;


	NodeMatchesLetterReturnEvaluator(String word){
		this.multiset = PopulateWordsHelpers.convertStringToMultimap(word);
	}

	/**
	 * 10. Check we are not at the start node
	 * 20. Check the multiset is not empty
	 * 30. Get the current nodes relationships of thype HAS_LETTER and iterator
	 * 40. Check the iterator is not empty
	 * 50. For each relationship
	 * 52. Get character from the node at the other end of the relationship
	 * 54. Check how many times the character is in the multiset
	 * 56. Does the value on the relationship match the value in the multiset?
	 */
	@Override
	public boolean isReturnableNode(TraversalPosition pos) {

		// 10. Check we are not at the start node
		if (pos.isStartNode())
			return false;
		
		// 20. Check the multiset is not empty
		if (multiset.isEmpty())
			return false;

		// 30. Get the current nodes relationships of thype HAS_LETTER and iterator
		Iterable<Relationship> rel = pos.currentNode().getRelationships(RelTypes.HAS_LETTER);
		Iterator<Relationship> iter = rel.iterator();

		// 40. Check the iterator is not empty
		if (!iter.hasNext()){
			return false;
		}

		// 50. For each relationship
		while (iter.hasNext()){
			
			// 52. Get character from the node at the other end of the relationship
			Relationship relationship = iter.next();
			Node otherNode = relationship.getEndNode();
			Character c = (Character)otherNode.getProperty("letter");

			// 54. Check how many times the character is in the multiset
			Integer characterCount = new Integer(multiset.count(c));

			// 56. Does the value on the relationship match the value in the multiset?
			if (characterCount > 0) {
				if (!relationship.hasProperty("occurrences")){
					return false;
				} else if (!characterCount.equals((Integer)relationship.getProperty("occurrences"))){
					return false;
				}
			} else { 
				return false;
			}


		}


		return true;
	}







}
