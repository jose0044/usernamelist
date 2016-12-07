package co.usernamelist;

/**
 * Bean for manage information and views about Restricted Words
 * @author jose
 * @version 1.0
 * @since 1.0
 *
 */
public class RestrictedWord {
		
	private String word;

	public RestrictedWord() {		
	}
	
	public RestrictedWord(String word) {
		super();
		this.word = word;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

}
