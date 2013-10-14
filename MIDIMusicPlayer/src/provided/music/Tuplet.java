/**
 * 
 */
package provided.music;

/**
 * Class that represents a tuplet, a series of notes played in a fraction of their normal total time.
 * The exact fraction depends on the meter and the number of notes in the tuplet.
 * A triplet is a specific type of tuplet.
 * @author swong
 *
 */
public class Tuplet extends NoteCollection {
	
	/**
	 * Constructor for a tuplet
	 * @param notes vararg list of notes that form the tuplet, in order of play.
	 */
	public Tuplet(Note... notes) {
		super(notes);
	}

	/**
	 * Calls the "Tuplet" case of the visitor.
	 * @param algo the visitor being used
	 * @param params vararg list of parameters for the visitor
	 * @return the result of the Tuplet case of the visitor.
	 */
	@Override
	public Object execute(IPhraseVisitor algo, Object... params) {
	    return algo.caseAt("Tuplet", this, params);
	}
	
	/**
	 * String representation of the tuplet:
	 * @return "Tuplet(n1, n2, n3, etc)"
	 */
	public String toString()  {
		String result = "Tuplet(" + notes[0];
		for(int i =1; i< notes.length; i++){
			result += ", "+ notes[i];
		}
		return result + ")";
	}

}
