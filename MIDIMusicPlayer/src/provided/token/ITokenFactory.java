package provided.token;

/**
 * Abstract factory to produce tokens
 */
public interface ITokenFactory {
  /**
   * Creates an instance of an Token from the given name and lexeme values
   */
  public Token makeToken(String name, String lexeme);                
}