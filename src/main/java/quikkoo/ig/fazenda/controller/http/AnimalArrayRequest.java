package quikkoo.ig.fazenda.controller.http;

import java.util.List;
import java.util.stream.Collectors;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import quikkoo.ig.fazenda.model.Animal;
import quikkoo.ig.fazenda.repository.FarmRepository;

@NoArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class AnimalArrayRequest {

	private List<AnimalRequest> animals;

	public AnimalArrayRequest(List<Animal> animals) {
		this.animals = animals.stream()
			.map(a -> new AnimalRequest(a))
			.collect(Collectors.toList());
	}

	public List<Animal> toAnimals(FarmRepository farms) {
		return animals.stream()
			.map(a -> a.toAnimal(farms))
			.collect(Collectors.toList());
	}
}
