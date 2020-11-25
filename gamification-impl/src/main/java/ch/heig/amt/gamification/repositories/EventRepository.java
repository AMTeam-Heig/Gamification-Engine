package ch.heig.amt.gamification.repositories;

import ch.heig.amt.gamification.entities.EventEntity;
import org.springframework.data.repository.CrudRepository;

public interface EventRepository  extends CrudRepository<EventEntity, Long> {
    Iterable<EventEntity> findAllByAppApiKey(String apiKey);
    EventEntity findByNameAndAppApiKey(String name, String ApiKey);
}
