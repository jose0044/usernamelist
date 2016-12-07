package co.usernamelist.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Class to manage the entity restricted_words
 * 
 * @author jose
 * @version 1.0
 * @since 1.0
 *
 */
@Entity
@Table(name = "restricted_words")
public class RestrictedWords {

	@Id
	private String word;

	public RestrictedWords() {
	}

	public RestrictedWords(String word) {
		this.word = word;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

}