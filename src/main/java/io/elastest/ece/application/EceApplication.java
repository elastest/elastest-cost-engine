package io.elastest.ece.application;

import io.elastest.ece.load.Loader;
import io.elastest.ece.load.model.HibernateCredentials;
import io.elastest.ece.persistance.HibernateClient;
import io.elastest.ece.persistance.HibernateConfiguration;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EceApplication {
	private static final Logger logger = LoggerFactory.getLogger(EceApplication.class.getName());

	public static void main(String[] args) {
		outputProgressBar("Loading ElasTest Cost Engine");

		// check params and potentially stop execution
		checkParameters(args);

		// check help parameter and potentially output it
		String param = args[0];
		checkHelp(param);

		// check configuration file and make sure it's correct
		checkConfigurationFile(param);

		// check and configure hibernate
		checkAndConfigureHibernate();

		SpringApplication.run(EceApplication.class, args);
	}

	/**
	 * Check number of parameters
	 * @param args as string array
	 */
	public static void checkParameters(String[] args) {
		if (args.length == 0) {
			String log = "A configuration file path has to be provided (as argument), otherwise the Elastest Cost Engine cannot be properly loaded";
			logger.error(log);
			System.err.println(log);
			System.exit(0);
		}

		outputProgressBar();
	}

	/**
	 * Check whether parameter was help
	 * @param param to be examined
	 */
	public static void checkHelp(String param) {
		if (param.equals("-h") || param.equals("--help")) {
			System.exit(0);
		}

		outputProgressBar();
	}

	/**
	 * Make sure configuration file is valid
	 * @param param path
	 */
	public static void checkConfigurationFile(String param) {
		try {
			// create and parse configuration settings
			Loader.createInstance(param);
		} catch (Exception e) {
			String log = "The configuration file is corrupted, make sure it's according to documentation and all fields are specified";
			logger.error(log);
			System.err.println(log);
			System.exit(0);
		}

		outputProgressBar();
	}

	/**
	 * Check and configure Hibernate
	 */
	public static void checkAndConfigureHibernate() {
		try {
			// get credentials
			HibernateCredentials credentials = Loader.getSettings().getHibernateCredentials();

			// create configuration
			Configuration configuration = HibernateConfiguration.createConfiguration(credentials);

			// create Hibernate
			HibernateClient.createInstance(configuration);

		} catch (Exception e) {
			String log = String.format("Couldn't connect to Hibernate: %s", e.getMessage());
			logger.error(log);
			System.err.println(log);
			System.exit(0);
		}

		outputProgressBar();
	}

	public static void outputProgressBar() {
		outputProgressBar("...");
	}

	public static void outputProgressBar(String text, Boolean ... emptyLine) {
		if (emptyLine.length > 0 && emptyLine[0]) {
			System.out.println();
		}

		System.out.print(text);

		if (emptyLine.length > 1 && emptyLine[1]) {
			System.out.println();
		}
	}
}
