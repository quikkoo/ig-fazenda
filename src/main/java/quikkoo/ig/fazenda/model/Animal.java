package quikkoo.ig.fazenda.model;

import java.util.Optional;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
@Entity
public class Animal {

	@Id
	@SequenceGenerator(name = "animal_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "animal_seq")
	private Long id;

	private String tag;

	@ManyToOne(optional = false)
	private Farm farm;

	public Animal(String tag, Farm farm) {
		this.tag = tag;
		this.farm = farm;
	}

	public Animal copy(Animal from) {
		Animal animal = new Animal();
		animal.id = Optional.ofNullable(from.getId()).orElse(id);
		animal.tag = Optional.ofNullable(from.getTag()).orElse(tag);
		animal.farm = Optional.ofNullable(from.getFarm()).orElse(farm);

		return animal;
	}
}
