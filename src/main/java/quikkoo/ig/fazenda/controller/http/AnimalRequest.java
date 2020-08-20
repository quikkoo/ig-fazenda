package quikkoo.ig.fazenda.controller.http;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import quikkoo.ig.fazenda.model.Animal;
import quikkoo.ig.fazenda.model.Farm;
import quikkoo.ig.fazenda.repository.FarmRepository;

@NoArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class AnimalRequest {

	private String tag;
	private String farm;

	public AnimalRequest(Animal animal) {
		this.tag = animal.getTag();
		this.farm = animal.getFarm().getName();
	}

	public Animal toAnimal(FarmRepository farms) {
		Farm efarm = farms.findByName(farm).get();

		return new Animal(tag, efarm);
	}
}
