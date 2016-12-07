package co.usernamelist.controllers.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import com.googlecode.zohhak.api.TestWith;
import com.googlecode.zohhak.api.runners.ZohhakRunner;

import co.usernamelist.controllers.UsernameListController;
import co.usernamelist.model.RestrictedWords;
import co.usernamelist.model.Usernames;

/**
 * Class to test some methods of UsernameListController class
 * 
 * @author jose
 * @version 1.0
 * @since 1.0
 */
@RunWith(ZohhakRunner.class)
public class UsernameListControllerTest {

	private static List<Usernames> testUsernamesList;
	private static List<RestrictedWords> testRestrictedWordsList;

	@BeforeClass
	public static void setUpDataTesting() {
		// Load data for usernames for test
		testUsernamesList = new ArrayList<Usernames>();
		testUsernamesList.add(new Usernames("peterson"));
		testUsernamesList.add(new Usernames("donald"));
		testUsernamesList.add(new Usernames("montagne"));
		testUsernamesList.add(new Usernames("albert"));
		testUsernamesList.add(new Usernames("stivenson"));
		testUsernamesList.add(new Usernames("splouie"));
		testUsernamesList.add(new Usernames("baracus"));
		testUsernamesList.add(new Usernames("ltsmash"));
		testUsernamesList.add(new Usernames("bsozzie"));
		testUsernamesList.add(new Usernames("mchammer"));
		testUsernamesList.add(new Usernames("macgyver"));
		testUsernamesList.add(new Usernames("swonder"));
		testUsernamesList.add(new Usernames("strouper"));
		testUsernamesList.add(new Usernames("mmrachel"));
		testUsernamesList.add(new Usernames("etwoods"));
		testUsernamesList.add(new Usernames("ldonovan"));
		testUsernamesList.add(new Usernames("brichard"));
		testUsernamesList.add(new Usernames("mmperez"));
		testUsernamesList.add(new Usernames("jftorres"));
		testUsernamesList.add(new Usernames("apruiz"));

		// Load data for restricted words
		testRestrictedWordsList = new ArrayList<RestrictedWords>();
		testRestrictedWordsList.add(new RestrictedWords("cannabis"));
		testRestrictedWordsList.add(new RestrictedWords("abuse"));
		testRestrictedWordsList.add(new RestrictedWords("crack"));
		testRestrictedWordsList.add(new RestrictedWords("damn"));
		testRestrictedWordsList.add(new RestrictedWords("drunk"));
		testRestrictedWordsList.add(new RestrictedWords("grass"));
	}

	/**
	 * Method to check the functionality with unit tests of method
	 * validateUsername
	 * 
	 * @param username
	 *            Username to create and validate
	 * @param result
	 *            String that indicates the expected result, true or false
	 * @author jose
	 * @since 1.0
	 * @return True if Username could be created, False in other case
	 * @exception Exception
	 *                in case of a general error in process
	 */
	@TestWith({ "pabuse, false", "alBert, false", "albert2, true", "pjlouie,true", "lou,false", "marclt,true",
			"apruiz,false", "macbridge,true", "etwoods,false", "gbjeff,true", "bill,false", "mjokovich,true",
			"bart,false", "spooner,true" })
	public final void testValidateUsername(String username, String result) {
		assertEquals(
				Boolean.toString(
						UsernameListController.validateUsername(testUsernamesList, testRestrictedWordsList, username)),
				result);
	}

}
