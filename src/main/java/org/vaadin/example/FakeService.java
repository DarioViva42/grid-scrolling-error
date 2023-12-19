package org.vaadin.example;

import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import static org.vaadin.example.DataCreator.FILE_PREFIX;

@Service
public class FakeService {

	int count() {
		try (Stream<Path> files = Files.list(Paths.get(FILE_PREFIX))) {
			long fileCount = files.count();
			return 50 * Math.toIntExact(fileCount);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
	List<Model> fetch(int offset, int limit) {
		List<Model> list;
		try (FileReader reader = new FileReader("%s/page-%s.txt".formatted(FILE_PREFIX, offset))) {
			StringBuilder content = new StringBuilder();
			int nextChar;
			while ((nextChar = reader.read()) != -1) {
				content.append((char) nextChar);
			}
			String string = String.valueOf(content);
			list = string.lines()
					.map(line -> line.split(", ", 3))
					.map(split -> new Model(split[0], split[1], split[2]))
					.limit(limit)
					.toList();
			System.out.printf("Successfully read from the file. offset: %s%n", offset);
		} catch (IOException e) {
			list = List.of();
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new IllegalStateException(e);
		}
		return list;
	}
}
