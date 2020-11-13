package io.avalia.gamification.repositories;

import io.avalia.gamification.entities.EventEntity;
import org.springframework.data.repository.CrudRepository;

public interface EventRepository extends CrudRepository<EventEntity, Long> {

}
