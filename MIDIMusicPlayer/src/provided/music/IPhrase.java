package provided.music;

/**
 * Top level of a piece of abc music.
 */
public interface IPhrase {

	  /**
	   * Executes (accepts) the visitor, calling the case associated with this host's index value.
	   * @param algo The visitor to execute
	   * @param params The input parameters supplied to the algo when its appropriate case is called.
	   * @return The return value from executing the appropriate case on the visitor.
	   */
	  public Object execute(IPhraseVisitor algo,  Object... params);
}
