package provided.music;

/**
 * A non-empty ("cons") sequence list.
 * 
 * IPhrase
 * ISeqList
 */
public class NESeqList implements ISeqList {

	/**
	 * The "first" of the list
	 */
	private IPhrase _first;
	
	/**
	 * The "rest" of the list
	 */
	private ISeqList _rest;

	/**
	 * Cons an IPhrase to a Sequence List.
	 */
	public NESeqList(IPhrase first, ISeqList rest) {
		this._rest = rest;
		this._first = first;
	}

	/**
	 * Accessor method for the first element
	 * @return the IPhrase that is the first element of the sequence list
	 */
	public IPhrase getFirst() {
		return _first;
	}

	/**
	 * Accessor method for the rest of the lsit
	 * @return the rest of the sequence list
	 */
	public ISeqList getRest() {
		return _rest;
	}

	/**
	 * Calls the "NESeqList" case of the visitor.
	 * @param algo  The visitor that is being used
	 * @param params vararg input parameters for the visitor.
	 * @return the result of running the visitor on this host.
	 */
	@Override
	public Object execute(IPhraseVisitor algo, Object... params) {
		return algo.caseAt("NESeqList", this, params);
	}

	/**
	 * toString algo for rest, the recursive helper function of the ToString algo. 
	 */
	private static IPhraseVisitor _toStringAlgo;

	/**
	 * Setter for the _toStringAlgo helper.
	 * @param stringAlgo The helper algo to use.
	 */
	public static void setToStringAlgo(IPhraseVisitor stringAlgo) {
		_toStringAlgo = stringAlgo;
	}

	/**
	 * Run a visitor over the list to convert it to a String.  
	 * Delegates to the rest of the list using the helper algo, with the accumulator 
	 * (params[0]) set to "{"+_first.toString().  
	 * @return "{a, b, c, d}"
	 */
	public String toString() {
		return (String) _rest.execute(_toStringAlgo, "{" + _first.toString());
	}
}
