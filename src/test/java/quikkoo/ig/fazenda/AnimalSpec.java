package quikkoo.ig.fazenda;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import quikkoo.ig.fazenda.controller.http.AnimalArrayRequest;
import quikkoo.ig.fazenda.controller.http.AnimalRequest;
import quikkoo.ig.fazenda.model.Animal;
import quikkoo.ig.fazenda.model.Farm;
import quikkoo.ig.fazenda.repository.AnimalRepository;
import quikkoo.ig.fazenda.repository.FarmRepository;

@TestInstance(Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
public class AnimalSpec {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private AnimalRepository animals;

	@Autowired
	private FarmRepository farms;

	private ObjectMapper mapper;

	@BeforeAll
	public void prepare() {
		mapper = new ObjectMapper();

		Farm farm1 = new Farm("Farm 1");
		Farm farm2 = new Farm("Farm 2");
		farms.saveAll(Arrays.asList(
			farm1, farm2));

		List<Animal> list = Arrays.asList(
			new Animal("Animal To Be Deleted", farm1),
			new Animal("Animal To Be Updated", farm1),
			new Animal("Animal To Be Fetched", farm2));
		animals.saveAll(list);
	}

	@AfterAll
	public void clean() {
		animals.deleteAll();
		farms.deleteAll();
	}

	@Test
	public void shouldDeleteAnimal() throws Exception {
		Animal animal = animals.findByTag("Animal To Be Deleted").get();

		String path = String.format("/animals/%s", animal.getId());
		MockHttpServletRequestBuilder request = delete(path)
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON);

		mvc.perform(request)
			.andDo(print())
			.andExpect(status().isNoContent());
	}

	@Test
	public void shouldCreateAnimal() throws Exception {
		Farm farm1 = farms.findByName("Farm 1").get();
		Animal animal = new Animal("Animal Created In Test", farm1);

		AnimalRequest body = new AnimalRequest(animal);
		MockHttpServletRequestBuilder request = post("/animals")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(mapper.writeValueAsBytes(body));

		mvc.perform(request)
			.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.tag", is(equalTo("Animal Created In Test"))))
			.andExpect(jsonPath("$.farm.name", is(equalTo("Farm 1"))));
	}

	@Test
	public void shouldCreateManyAnimals() throws Exception {
		Farm farm1 = farms.findByName("Farm 1").get();
		Farm farm2 = farms.findByName("Farm 2").get();
		List<Animal> list = Arrays.asList(
			new Animal("Animal 1 Created In Test", farm1),
			new Animal("Animal 2 Created In Test", farm1),
			new Animal("Animal 3 Created In Test", farm2),
			new Animal("Animal 4 Created In Test", farm2),
			new Animal("Animal 5 Created In Test", farm2));

		AnimalArrayRequest body = new AnimalArrayRequest(list);
		MockHttpServletRequestBuilder request = post("/animals/multi")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(mapper.writeValueAsBytes(body));

		mvc.perform(request)
			.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$[0].tag", is(equalTo("Animal 1 Created In Test"))))
			.andExpect(jsonPath("$[0].farm.name", is(equalTo("Farm 1"))))
			.andExpect(jsonPath("$[4].tag", is(equalTo("Animal 5 Created In Test"))))
			.andExpect(jsonPath("$[4].farm.name", is(equalTo("Farm 2"))));

	}

	@Test
	public void shouldUpdateAnimal() throws Exception {
		Farm farm2 = farms.findByName("Farm 2").get();
		Animal oldAnimal = animals.findByTag("Animal To Be Updated").get();
		Animal newAnimal = oldAnimal.copy(new Animal("Animal To Be Updated In Test", farm2));

		AnimalRequest body = new AnimalRequest(newAnimal);
		String path = String.format("/animals/%s", oldAnimal.getId());
		MockHttpServletRequestBuilder request = put(path)
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(mapper.writeValueAsBytes(body));

		mvc.perform(request)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(equalTo(oldAnimal.getId())), Long.class))
			.andExpect(jsonPath("$.id", is(equalTo(newAnimal.getId())), Long.class))
			.andExpect(jsonPath("$.tag", is(equalTo("Animal To Be Updated In Test"))))
			.andExpect(jsonPath("$.farm.name", is(equalTo("Farm 2"))));
	}

	@Test
	public void shouldFetchAnimalById() throws Exception {
		Animal animal = animals.findByTag("Animal To Be Fetched").get();

		String path = String.format("/animals/%s", animal.getId());
		MockHttpServletRequestBuilder request = get(path)
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON);

		mvc.perform(request).andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(equalTo(animal.getId())), Long.class))
			.andExpect(jsonPath("$.tag", is(equalTo("Animal To Be Fetched"))))
			.andExpect(jsonPath("$.farm.name", is(equalTo("Farm 2"))));
	}
}
