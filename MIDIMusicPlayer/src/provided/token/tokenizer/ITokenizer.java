package provided.token.tokenizer;

import provided.token.*;

/**
 * Extract and return an appropriate Token from some given source.
 */
public abstract interface ITokenizer {
    /**
     * Return the next token.
     */
    public abstract Token getNextToken();

    /**
     * Put the previously consumed token back into the token stream. 
     */
    public abstract void putBack(Token t);
}
