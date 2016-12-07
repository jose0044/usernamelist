package co.usernamelist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Class that uses Spring to enable the app like a runnable aplication
 * 
 * @author jose
 * @version 1.0
 * @since 1.0
 *
 */
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
