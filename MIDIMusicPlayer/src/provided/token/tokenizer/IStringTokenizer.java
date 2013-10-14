package provided.token.tokenizer;



/**
 * Tokenizer that only returns string tokens.  This is a specialization of ITokenizer for strings.
 * 
 * @author swong
 * 
 */
public interface IStringTokenizer {

	/**
	 * Return the next token.
	 */
	public abstract String getNextStringToken();

	/**
	 * Put the previously consumed token back into the token stream. Can only
	 * put back one token.
	 */
	public abstract void putStringBack(String t);

}
