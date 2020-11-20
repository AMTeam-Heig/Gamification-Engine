package io.avalia.gamification.repositories;

import io.avalia.gamification.entities.BadgeEntity;
import org.springframework.data.repository.CrudRepository;

public interface BadgeRepository extends CrudRepository<BadgeEntity, Long> {
    Iterable<BadgeEntity> findAllByAppApiKey(String apiKey);
    BadgeEntity findByNameAndAppApiKey(String name, String ApiKey);
}
