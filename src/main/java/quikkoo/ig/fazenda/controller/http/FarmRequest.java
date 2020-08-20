package quikkoo.ig.fazenda.controller.http;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import quikkoo.ig.fazenda.model.Farm;

@NoArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class FarmRequest {

	private String name;

	public FarmRequest(Farm farm) {
		this.name = farm.getName();
	}

	public Farm toFarm() {
		return new Farm(name);
	}
}
