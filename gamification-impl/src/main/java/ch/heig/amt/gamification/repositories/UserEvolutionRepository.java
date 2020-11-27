package ch.heig.amt.gamification.repositories;

import ch.heig.amt.gamification.entities.UserEntity;
import ch.heig.amt.gamification.entities.UserEvolutionEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserEvolutionRepository extends CrudRepository<UserEvolutionEntity, UserEntity> {
    UserEvolutionEntity findByUser(UserEntity userEntity);
}
