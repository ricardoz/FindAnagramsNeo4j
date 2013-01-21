package findanagrams;

import org.neo4j.graphdb.RelationshipType;

// START SNIPPET: createRelTypes
public enum RelTypes implements RelationshipType
{
    HAS_LETTER,
    HAS_LENGTH
}
// END SNIPPET: createRelTypes