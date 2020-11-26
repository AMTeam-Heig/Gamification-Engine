package ch.heig.amt.gamification.repositories;

import ch.heig.amt.gamification.entities.EventEntity;
import org.springframework.data.repository.CrudRepository;

public interface EventRepository  extends CrudRepository<EventEntity, Long> {
    Iterable<EventEntity> findAllByApplicationEntity_ApiKey(String apiKey);
    EventEntity findByNameAndApplicationEntity_ApiKey(String name, String apiKey);
}
