package org.vaadin.example;

import com.github.javafaker.Faker;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class DataCreator {

	public static final String FILE_PREFIX = "src/main/resources/data";

	@GetMapping("create")
	String generatePageFile(@RequestParam int count) {
		for (int i = 0; i < 50 * count; i+=50) {
			writeToFile(i);
		}
		return "list generated";
	}

	private void writeToFile(int offset) {
		boolean newlyCreated = createFile(offset);
		if (!newlyCreated) {
			return;
		}
		List<Model> list = Stream.generate(Faker::new)
				.limit(50)
				.map(faker -> new Model(
						faker.name().fullName(),
						faker.pokemon().name(),
						faker.animal().name()
				))
				.toList();
		try (FileWriter writer = new FileWriter("%s/page-%s.txt".formatted(FILE_PREFIX, offset))) {
			String string = list.stream()
					.map(Model::toString)
					.collect(Collectors.joining("\n"));
			writer.write(string);
			System.out.printf("Successfully wrote to the file. offset: %s%n", offset);
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	private boolean createFile(int offset) {
		try {
			File file = new File("%s/page-%s.txt".formatted(FILE_PREFIX, offset));
			if (file.createNewFile()) {
				System.out.println("File created: " + file.getName());
				return true;
			} else {
				System.out.println("File already exists.");
				return false;
			}
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		return false;
	}
}
