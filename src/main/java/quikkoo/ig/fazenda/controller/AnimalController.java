package quikkoo.ig.fazenda.controller;

import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import quikkoo.ig.fazenda.controller.http.AnimalArrayRequest;
import quikkoo.ig.fazenda.controller.http.AnimalRequest;
import quikkoo.ig.fazenda.model.Animal;
import quikkoo.ig.fazenda.repository.AnimalRepository;
import quikkoo.ig.fazenda.repository.FarmRepository;

@RestController
public class AnimalController {

	private static final Logger log = LoggerFactory.getLogger(AnimalController.class);

	@Autowired
	private AnimalRepository animals;
	@Autowired
	private FarmRepository farms;

	@DeleteMapping("/animals/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		log.info("Deleting Animal with id {}", id);

		try {
			animals.deleteById(id);

			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		catch (Exception e) {
			log.error(e.getLocalizedMessage());

			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/animals")
	public ResponseEntity<?> create(@RequestBody AnimalRequest body) {
		log.info("Creating Animal with values {}", body);

		try {
			Animal animal = animals.save(body.toAnimal(farms));

			return new ResponseEntity<>(animal, HttpStatus.CREATED);
		}
		catch (Exception e) {
			log.error(e.getLocalizedMessage());

			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/animals/multi")
	public ResponseEntity<?> createm(@RequestBody AnimalArrayRequest body) {
		log.info("Creating Animal with values {}", body);

		try {
			Iterable<Animal> list = animals.saveAll(body.toAnimals(farms));

			return new ResponseEntity<>(list, HttpStatus.CREATED);
		}
		catch (Exception e) {
			log.error(e.getLocalizedMessage());

			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping("/animals/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody AnimalRequest body) {
		log.info("Updating Animal with id {} to {}", id, body);

		try {
			Animal oldAnimal = animals.findById(id).orElseThrow();
			Animal newAnimal = oldAnimal.copy(body.toAnimal(farms));
			animals.save(newAnimal);

			return new ResponseEntity<>(newAnimal, HttpStatus.OK);
		}
		catch (NoSuchElementException e) {
			log.error(e.getLocalizedMessage());

			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		catch (Exception e) {
			log.error(e.getLocalizedMessage());

			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/animals/{id}")
	public ResponseEntity<?> fetch(@PathVariable Long id) {
		log.info("Fetching Animal with id {}", id);

		try {
			Animal animal = animals.findById(id).orElseThrow();

			return new ResponseEntity<>(animal, HttpStatus.OK);
		}
		catch (Exception e) {
			log.error(e.getLocalizedMessage());

			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
