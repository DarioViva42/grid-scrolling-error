package org.vaadin.example;

import java.io.Serializable;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record Model(String name, String pokemon, String animal) implements Serializable {

	@Override
	public String toString() {
		return Stream.<String>builder()
				.add(name)
				.add(pokemon)
				.add(animal)
				.build()
				.collect(Collectors.joining(", "));
	}
}
