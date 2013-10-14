package provided.music;

/**
 * An empty sequence list.
 *
 */
public class MTSeqList implements ISeqList {


	/**
	 * Singleton instance of the class
	 */
	public static final MTSeqList Singleton = new MTSeqList();
	
	/**
	 * private constructor
	 */
	private MTSeqList() {
	}


	/**
	 * Calls the "MTSeqList" case of the visitor.
	 */
	@Override
	public Object execute(IPhraseVisitor algo, Object... params) {
	    return algo.caseAt("MTSeqList", this, params);
	}
	
	/**
	 * Returns "{}"
	 * return "{}" always.
	 */
	public String toString() {
		return "{}";
	}
}
