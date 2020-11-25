package ch.heig.amt.gamification.repositories;

import ch.heig.amt.gamification.entities.BadgeEntity;
import org.springframework.data.repository.CrudRepository;

public interface BadgeRepository extends CrudRepository<BadgeEntity, Long> {
    Iterable<BadgeEntity> findAllByApplication_ApiKey(String apiKey);
    BadgeEntity findByIdAndApplication_ApiKey(String name, String apiKey);
}
