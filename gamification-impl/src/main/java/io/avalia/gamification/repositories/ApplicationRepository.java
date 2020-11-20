package io.avalia.gamification.repositories;

import io.avalia.gamification.entities.ApplicationEntity;
import org.springframework.data.repository.CrudRepository;

public interface ApplicationRepository extends CrudRepository<ApplicationEntity, Long> {
    ApplicationEntity findByApiKey(String apiKey);
}
