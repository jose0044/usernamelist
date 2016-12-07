package co.usernamelist.controllers;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import co.usernamelist.RestrictedWord;
import co.usernamelist.Username;
import co.usernamelist.model.RestrictedWords;
import co.usernamelist.model.RestrictedWordsDAO;
import co.usernamelist.model.Usernames;
import co.usernamelist.model.UsernamesDAO;

/**
 * Principal controller of app, manages the views and calls processing of Model
 * layer and contains other processing methods
 * 
 * @author jose
 * @version 1.0
 * @since 1.0
 */
@Controller
public class UsernameListController {

	@Autowired
	private UsernamesDAO usernamesDAO;

	@Autowired
	private RestrictedWordsDAO restrictedWordsDAO;

	@ModelAttribute("allRestrictedWords")
	public List<RestrictedWords> getAllRestrictedWords() {
		return (List<RestrictedWords>) restrictedWordsDAO.findAll();
	}

	@ModelAttribute("suggestedNewUsernames")
	public List<Usernames> getSuggestedUsernames() {
		return (List<Usernames>) listSuggestedUsernames;
	}

	private static String messageOperation = "";
	private static boolean resultValidation;
	private static int countTriesSuggestions = 1;
	private static List<Usernames> listSuggestedUsernames = new ArrayList<Usernames>();

	/**
	 * Method to render view of Usernames and manage messages sent to view
	 * 
	 * @param username
	 *            Param to control the data of Username from template
	 * @param model
	 *            Model to control the params passed to view
	 * @author jose
	 * @since 1.0
	 * @return String with the name of template to manage Usernames
	 */
	@RequestMapping("/welcome")
	public String welcome(final Username username, ModelMap model) {
		if (!messageOperation.equals("")) {
			model.addAttribute("resultProcess",
					"The result of validation is " + Boolean.toString(resultValidation) + ", " + messageOperation);
		}
		// Clean list of suggested usernames and reset count of
		// suggested usernames
		listSuggestedUsernames = new ArrayList<Usernames>();
		countTriesSuggestions = 1;
		return "welcome";
	}

	/**
	 * Method to render view of Usernames and manage the action of add a new
	 * Username
	 * 
	 * @param username
	 *            Param to control the data of Username from template
	 * @param model
	 *            Model to control the params passed to view
	 * @author jose
	 * @since 1.0
	 * @return String with the name of template to manage Usernames
	 */
	@RequestMapping(value = "/validateUsername", params = { "validateUsername" })
	public String validateUsername(final Username username, final ModelMap model) {

		Logger.getRootLogger().info("Username given:" + username.getText());
		// Validate alphanumerica characters
		if (isAlphaNumeric(username.getText())) {
			// Proceed to validate username
			resultValidation = validateUsername((List<Usernames>) usernamesDAO.findAll(),
					(List<RestrictedWords>) restrictedWordsDAO.findAll(), username.getText().toLowerCase());
			// If validation is true, save the Username in DB
			if (resultValidation) {
				usernamesDAO.save(new Usernames(username.getText().toLowerCase()));
				messageOperation = "Username created succesfull";
			}
		} else {
			resultValidation = Boolean.FALSE;
			messageOperation = "Characters not allowed, please provide a new Username";
		}
		model.clear();
		return "redirect:/welcome";
	}

	/**
	 * Method to render view of Restricted Words and manage messages sent to
	 * view
	 * 
	 * @param restrictedWord
	 *            Param to control the data of Restricted Word from template
	 * @param model
	 *            Model to control the params passed to view
	 * @author jose
	 * @since 1.0
	 * @return String with the name of template to manage Restricted Words
	 */
	@RequestMapping("/adminRestrictedWords")
	public String adminRestrictedWords(final RestrictedWord restrictedWord, ModelMap model) {
		if (messageOperation.equals("added")) {
			model.addAttribute("resultProcess", "The Restricted Word was created succesfully!");
		}
		if (messageOperation.equals("duplicated")) {
			model.addAttribute("resultProcess", "The Restricted Word was duplicated, please add another word.");
		}
		if (messageOperation.equals("invalidCharacter")) {
			model.addAttribute("resultProcess", "Characters not allowed, please provide a new Restricted Word.");
		}
		// Clear Message Operation
		messageOperation = "";
		// Clean list of suggested usernames and reset count of suggested
		// usernames
		listSuggestedUsernames = new ArrayList<Usernames>();
		countTriesSuggestions = 1;
		return "adminRestrictedWords";
	}

	/**
	 * Method to render view of Restricted Words and manage the action of add a
	 * new Restricted Word
	 * 
	 * @param restrictedWord
	 *            Param to control the data of Restricted Word from template
	 * @param model
	 *            Model to control the params passed to view
	 * @author jose
	 * @since 1.0
	 * @return String with the name of template to manage Restricted Words
	 */
	@RequestMapping(value = "/showAdminRestrictedWords", params = { "showAdminRestrictedWords" })
	public String showAdminRestrictedWords(final RestrictedWord restrictedWord, final ModelMap model) {

		// Validate alphanumerica characters
		if (isAlphaNumeric(restrictedWord.getWord())) {
			Logger.getRootLogger().info("Restricted Word to add " + restrictedWord.getWord());

			if (restrictedWordsDAO.exists(restrictedWord.getWord().toLowerCase())) {
				Logger.getRootLogger().info("Not created");
				messageOperation = "duplicated";
			} else {
				restrictedWordsDAO.save(new RestrictedWords(restrictedWord.getWord().toLowerCase()));
				messageOperation = "added";
				Logger.getRootLogger().info("Created");
			}
		} else {
			messageOperation = "invalidCharacter";
		}
		model.clear();
		return "redirect:/adminRestrictedWords";
	}

	/**
	 * Method to validate a given Username and if it's acording to rules,
	 * indicate with true or false if it's may be created or not in system
	 * 
	 * @param currentUsernamesCreated
	 *            List of usernames created
	 * @param allRestrictedWords
	 *            List of all restricted words created
	 * @param username
	 *            Username to create and validate
	 * @author jose
	 * @since 1.0
	 * @return True if Username could be created, False in other case
	 * @exception Exception
	 *                in case of a general error in process
	 */
	public static boolean validateUsername(List<Usernames> currentUsernamesCreated,
			List<RestrictedWords> allRestrictedWords, String username) {

		try {
			// Check length of username given
			if (username.length() < 6) {
				messageOperation = "Username with a length less than six characters, please provide a new username";
				return Boolean.FALSE;
			}
			// Check if Username was previously created
			if (currentUsernamesCreated.contains(new Usernames(username))) {
				messageOperation = "Username already exists in system, please provide a new username";
				// Loop to validate that the process of get new suggested
				// usernames
				// only was made three times
				do {
					// In the first iteration, takes the given username, in next
					// iterations take the last Username suggested
					String usernameToValidate = countTriesSuggestions == 1 ? username
							: listSuggestedUsernames.get(listSuggestedUsernames.size()).getText();
					// Call method to generate usernames suggested
					permuteUsernameString("", usernameToValidate);
					// Check if suggested list of new usernames exists or
					// contains
					// an
					// restricted word
					validateListOfSuggestedUsernames(currentUsernamesCreated, allRestrictedWords);
					// Increase count of process to generate suggested new
					// Usernames
					countTriesSuggestions++;
				} while (countTriesSuggestions <= 3);

				// Sort the list of suggestions
				listSuggestedUsernames.sort(null);
				return Boolean.FALSE;
			}
			// Username doesn't exists, validate Username vs Restricted Words
			else {
				// Username contains at least one Restricted Word
				if (usernameContainsRestrictedWords(allRestrictedWords, username)) {
					messageOperation = "Username contains a restricted word, please provide a new username";
					return Boolean.FALSE;
				}
				// Username doesn't contains a Restricted Word, proceed to
				// return true
				else {
					return Boolean.TRUE;
				}
			}
		} catch (Exception exception) {
			Logger.getRootLogger().error(exception.getStackTrace());
		}
		return Boolean.FALSE;
	}

	/**
	 * Method created to check if a given Username contains at least one
	 * Restricted Word
	 * 
	 * @param allRestrictedWords
	 *            List of all restricted words created
	 * @param username
	 *            The Username to validate
	 * @author jose
	 * @since 1.0
	 * @return True if Username contains a Restricted Word, False if not
	 * @exception Exception
	 *                in case of a general error in process
	 */
	private static boolean usernameContainsRestrictedWords(List<RestrictedWords> allRestrictedWords, String username) {
		try {
			for (RestrictedWords currentRestrictedWord : allRestrictedWords) {
				if (username.contains(currentRestrictedWord.getWord())) {
					return Boolean.TRUE;
				}
			}
		} catch (Exception exception) {
			Logger.getRootLogger().error(exception.getStackTrace());
		}

		return Boolean.FALSE;
	}

	/**
	 * Method to generate the suggested new Usernames, generates only fourteen
	 * Usernames
	 * 
	 * @param firstString
	 *            First string to permute, usually is empty
	 * @param lastString
	 *            Last string to permute, usually is the seed string, in this
	 *            case, an Username
	 * @author jose
	 * @since 1.0
	 * @exception Exception
	 *                in case of a general error in process
	 */
	private static void permuteUsernameString(String firstString, String lastString) {
		try {
			if (lastString.length() <= 1) {
				// Check if size of list it's less than 14, add a new sugested
				// Username
				if (listSuggestedUsernames.size() <= 14) {
					Usernames newUsername = new Usernames(firstString + lastString);
					listSuggestedUsernames.add(newUsername);
				} else {
					return;
				}
			} else {
				for (int i = 0; i < lastString.length(); i++) {
					try {
						String newString = lastString.substring(0, i) + lastString.substring(i + 1);

						permuteUsernameString(firstString + lastString.charAt(i), newString);
					} catch (Exception exception) {
						Logger.getRootLogger().error(exception.getStackTrace());
					}
				}
			}
		} catch (Exception exception) {
			Logger.getRootLogger().error(exception.getStackTrace());
		}

	}

	/**
	 * Method to validate the whole liste of fourteen suggested new Usernames,
	 * also delete usernames with conflicts (duplicated or that include a
	 * Restricted Word)
	 * 
	 * @author jose
	 * @param currentUsernamesCreated
	 *            List of usernames created
	 * @param allRestrictedWords
	 *            List of all restricted words created
	 * @since 1.0
	 * @exception Exception
	 *                in case of a general error in process
	 */

	private static void validateListOfSuggestedUsernames(List<Usernames> currentUsernamesCreated,
			List<RestrictedWords> allRestrictedWords) {
		try {
			for (Usernames currentUsernames : listSuggestedUsernames) {
				// Check if current suggested username exists in created
				// Usernames,
				// if so, remove it
				if (currentUsernamesCreated.contains(new Usernames(currentUsernames.getText()))) {
					listSuggestedUsernames.remove(currentUsernames);
				}
				// Check if current suggested username contains a restricted
				// word,
				// if so, remove it
				if (usernameContainsRestrictedWords(allRestrictedWords, currentUsernames.getText())) {
					listSuggestedUsernames.remove(currentUsernames);
				}
			}
		} catch (Exception exception) {
			Logger.getRootLogger().error(exception.getLocalizedMessage());
		}
	}

	/**
	 * Method to check if String contains only letters and numbers
	 * 
	 * @author jose
	 * @param stringToValidate
	 *            String to check if contains alphanumeric
	 * @since 1.o
	 * @return True if string is alphanumeric, false in other case
	 */
	private boolean isAlphaNumeric(String stringToValidate) {
		String pattern = "^[a-zA-Z0-9]*$";
		return stringToValidate.matches(pattern);
	}
}
