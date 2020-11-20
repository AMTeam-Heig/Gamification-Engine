package io.avalia.gamification.repositories;

import io.avalia.gamification.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
    Iterable<UserEntity> findAllByAppApiKey(String apiKey);
    UserEntity findByNameAndAppApiKey(String name, String ApiKey);
}
