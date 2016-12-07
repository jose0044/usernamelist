package co.usernamelist;

/**
 * Bean for manage information and views about Usernames
 * 
 * @author jose
 * @version 1.0
 * @since 1.0
 *
 */
public class Username {
		
	private String text;

	public Username() {		
	}
	
	public Username(String text) {
		super();
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
