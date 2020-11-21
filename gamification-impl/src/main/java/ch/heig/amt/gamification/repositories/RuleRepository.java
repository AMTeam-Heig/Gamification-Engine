package ch.heig.amt.gamification.repositories;

import ch.heig.amt.gamification.entities.RuleEntity;
import org.springframework.data.repository.CrudRepository;

public interface RuleRepository extends CrudRepository<RuleEntity, Long> {
    Iterable<RuleEntity> findAllByAppApiKey(String apiKey);
    RuleEntity findByNameAndAppApiKey(String name, String ApiKey);
}
