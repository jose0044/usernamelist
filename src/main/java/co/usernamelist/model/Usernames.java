package co.usernamelist.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Class to manage the entity usernames
 * 
 * @author jose
 * @version 1.0
 * @since 1.0
 *
 */
@Entity
@Table(name = "usernames")
public class Usernames {

	@Id
	private String text;

	public Usernames() {
	}

	public Usernames(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

    @Override
    public boolean equals(Object o) {
        if (o instanceof Usernames) {
        	Usernames myUSernames = (Usernames) o;
            return myUSernames.text.equalsIgnoreCase(this.text);
        }
        return false;
    }
}