package ch.heig.amt.gamification.repositories;

import ch.heig.amt.gamification.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
    Iterable<UserEntity> findAllByAppApiKey(String apiKey);
    UserEntity findByNameAndAppApiKey(String name, String ApiKey);
}
