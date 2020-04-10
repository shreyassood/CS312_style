package edu.utexas.cs.cs312;

import org.apache.logging.log4j.util.Strings;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

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
		return "Hello, World!";
	}

	@PostMapping("/upload")
	@ResponseBody
	public String singleFileUpload(@RequestParam("file") MultipartFile file) {

		if (file.isEmpty()) {
			return "no file";
		}

		try {
			Path tempDir = Files.createTempDirectory("temp_dir");
			Path sourceFilePath = tempDir.resolve(file.getOriginalFilename());
			Files.write(sourceFilePath, file.getBytes());

			File sourceFile = sourceFilePath.toFile();
			ArrayList<String> result = runCheckStyle(sourceFile);

			return Strings.join(result, '\n');
		} catch (Exception e) {
			return String.format("Error uploading: %s", e.getMessage());
		}

	}

}
