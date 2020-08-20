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

import quikkoo.ig.fazenda.controller.http.FarmRequest;
import quikkoo.ig.fazenda.model.Farm;
import quikkoo.ig.fazenda.repository.FarmRepository;

@TestInstance(Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
public class FarmSpec {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private FarmRepository farms;

	private ObjectMapper mapper;

	@BeforeAll
	public void prepare() {
		mapper = new ObjectMapper();

		farms.saveAll(Arrays.asList(
			new Farm("Farm To Be Deleted"),
			new Farm("Farm To Be Updated"),
			new Farm("Farm To Be Fetched")));
	}

	@AfterAll
	public void clean() {
		farms.deleteAll();
	}

	@Test
	public void shouldDeleteFarm() throws Exception {
		Farm farm = farms.findByName("Farm To Be Deleted").get();

		String path = String.format("/farms/%s", farm.getId());
		MockHttpServletRequestBuilder request = delete(path)
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON);

		mvc.perform(request)
			.andDo(print())
			.andExpect(status().isNoContent());
	}

	@Test
	public void shouldCreateFarm() throws Exception {
		Farm farm = new Farm("Farm Created In Test");
		FarmRequest body = new FarmRequest(farm);

		MockHttpServletRequestBuilder request = post("/farms")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(mapper.writeValueAsBytes(body));

		mvc.perform(request)
			.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.name", is(equalTo("Farm Created In Test"))));
	}

	@Test
	public void shouldUpdateFarm() throws Exception {
		Farm oldFarm = farms.findByName("Farm To Be Updated").get();
		Farm newFarm = oldFarm.copy(new Farm("Farm To Be Updated In Test"));

		FarmRequest body = new FarmRequest(newFarm);
		String path = String.format("/farms/%s", oldFarm.getId());
		MockHttpServletRequestBuilder request = put(path)
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(mapper.writeValueAsBytes(body));

		mvc.perform(request)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(equalTo(oldFarm.getId())), Long.class))
			.andExpect(jsonPath("$.id", is(equalTo(newFarm.getId())), Long.class))
			.andExpect(jsonPath("$.name", is(equalTo("Farm To Be Updated In Test"))));
	}

	@Test
	public void shouldFetchFarmById() throws Exception {
		Farm farm = farms.findByName("Farm To Be Fetched").get();

		String path = String.format("/farms/%s", farm.getId());
		MockHttpServletRequestBuilder request = get(path)
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON);

		mvc.perform(request).andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(equalTo(farm.getId())), Long.class))
			.andExpect(jsonPath("$.name", is(equalTo("Farm To Be Fetched"))));
	}
}
