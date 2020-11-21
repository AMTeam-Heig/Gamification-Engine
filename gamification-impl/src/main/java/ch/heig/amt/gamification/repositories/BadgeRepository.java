package ch.heig.amt.gamification.repositories;

import ch.heig.amt.gamification.entities.BadgeEntity;
import org.springframework.data.repository.CrudRepository;

public interface BadgeRepository extends CrudRepository<BadgeEntity, Long> {
    Iterable<BadgeEntity> findAllByAppApiKey(String apiKey);
    BadgeEntity findByNameAndAppApiKey(String name, String ApiKey);
}
