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

import quikkoo.ig.fazenda.controller.http.FarmRequest;
import quikkoo.ig.fazenda.model.Farm;
import quikkoo.ig.fazenda.repository.FarmRepository;

@RestController
public class FarmController {

	private static final Logger log = LoggerFactory.getLogger(FarmController.class);

	@Autowired
	private FarmRepository farms;

	@DeleteMapping("/farms/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		log.info("Deleting Farm with id {}", id);

		try {
			farms.deleteById(id);

			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		catch (Exception e) {
			log.error(e.getLocalizedMessage());

			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/farms")
	public ResponseEntity<?> create(@RequestBody FarmRequest body) {
		log.info("Creating Farm with values {}", body);

		try {
			Farm farm = farms.save(body.toFarm());

			return new ResponseEntity<>(farm, HttpStatus.CREATED);
		}
		catch (Exception e) {
			log.error(e.getLocalizedMessage());

			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/farms/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody FarmRequest body) {
		log.info("Updating Farm with id {} to {}", id, body);

		try {
			Farm oldFarm = farms.findById(id).orElseThrow();
			Farm newFarm = oldFarm.copy(body.toFarm());
			farms.save(newFarm);

			return new ResponseEntity<>(newFarm, HttpStatus.OK);
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

	@GetMapping("/farms/{id}")
	public ResponseEntity<?> fetch(@PathVariable Long id) {
		log.info("Fetching Farm with id {}", id);

		try {
			Farm farm = farms.findById(id).orElseThrow();

			return new ResponseEntity<>(farm, HttpStatus.OK);
		}
		catch (Exception e) {
			log.error(e.getLocalizedMessage());

			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
