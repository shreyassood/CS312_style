package edu.utexas.cs.cs312;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileWriter;
import java.util.Collections;
import java.util.Scanner;

import static edu.utexas.cs.cs312.CheckStyleWrapper.runCheckStyle;

@Controller
@SpringBootApplication
public class StyleCheckerApplication {

	public static void main(String[] args) {
		SpringApplication.run(StyleCheckerApplication.class, args);
	}

	@RequestMapping("/")
	String home() {
		return "/index.html";
	}

	@RequestMapping("/hello_world")
	@ResponseBody
	String helloWorld() {

		try {

			// TODO Plug in actual file
			File inputFile = File.createTempFile("Mock", ".java");
			FileWriter writer = new FileWriter(inputFile);
			writer.write(
							"public class RandomClassName {\n}\n"
			);
			writer.close();


			File outputFile = File.createTempFile("output-test", ".xml");

			int resultCode = runCheckStyle(
					outputFile.toPath(),
					Collections.singletonList(inputFile)
			);

			// TODO Parse XML Result
			StringBuilder resultBuilder = new StringBuilder("Result code: " + resultCode + "<br/>");
			Scanner scanner = new Scanner(outputFile);
			while(scanner.hasNextLine()) {
				resultBuilder.append(scanner.nextLine()).append("<br/>");
			}
			return resultBuilder.toString();

		} catch (Exception e) {

			// TODO Handle errors
			e.printStackTrace();
			return e.getMessage();
		}
	}

}
