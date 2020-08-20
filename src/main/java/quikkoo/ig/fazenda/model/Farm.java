package quikkoo.ig.fazenda.model;

import java.util.Optional;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
public class Farm {

	@Id
	@SequenceGenerator(name = "farm_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "farm_seq")
	private Long id;

	private String name;

	public Farm(String name) {
		this.name = name;
	}

	public Farm copy(Farm from) {
		Farm farm = new Farm();
		farm.id = Optional.ofNullable(from.getId()).orElse(id);
		farm.name = Optional.ofNullable(from.getName()).orElse(name);

		return farm;
	}
}
