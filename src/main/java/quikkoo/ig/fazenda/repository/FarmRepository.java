package quikkoo.ig.fazenda.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import quikkoo.ig.fazenda.model.Farm;

public interface FarmRepository extends CrudRepository<Farm, Long> {

	Optional<Farm> findByName(String name);
}
