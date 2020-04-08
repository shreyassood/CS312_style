package edu.utexas.cs.cs312;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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

	@PostMapping("/upload")
	@ResponseBody
	public String singleFileUpload(@RequestParam("file") MultipartFile file) {

		if (file.isEmpty()) {
			return "no file";
		}

		try {
			Path tempDir = Files.createTempDirectory("temp_dir");
			Path outputFile = tempDir.resolve(file.getOriginalFilename());
			Files.write(outputFile, file.getBytes());
			return outputFile.toString();
		} catch (IOException e) {
			return "error uploading";
		}

	}

}
