package ch.heig.amt.gamification.repositories;

import ch.heig.amt.gamification.entities.ApplicationEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ApplicationRepository extends CrudRepository<ApplicationEntity, Long> {
    ApplicationEntity findByName(String name);
    ApplicationEntity findByApiKey(String apiKey);
    List<ApplicationEntity> findAll();
}
