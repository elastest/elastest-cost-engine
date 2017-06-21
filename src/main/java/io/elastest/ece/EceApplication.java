package io.elastest.ece;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EceApplication {

	public static void main(String[] args) {
		outputProgressBar("Loading ElasTest Cost Engine");


		SpringApplication.run(EceApplication.class, args);
	}

	private static void outputProgressBar(String text, Boolean ... emptyLine) {
		if (emptyLine.length > 0 && emptyLine[0]) {
			System.out.println();
		}

		System.out.print(text);

		if (emptyLine.length > 1 && emptyLine[1]) {
			System.out.println();
		}
	}
}
