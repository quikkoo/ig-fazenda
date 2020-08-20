package quikkoo.ig.fazenda.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import quikkoo.ig.fazenda.model.Animal;

public interface AnimalRepository extends CrudRepository<Animal, Long> {

	Optional<Animal> findByTag(String tag);
}
