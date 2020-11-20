package ch.heig.amt.gamification.api.endpoints;

import ch.heig.amt.gamification.entities.ApplicationEntity;
import ch.heig.amt.gamification.api.BadgesApi;
import ch.heig.amt.gamification.api.model.Badge;
import ch.heig.amt.gamification.entities.BadgeEntity;
import ch.heig.amt.gamification.repositories.ApplicationRepository;
import ch.heig.amt.gamification.repositories.BadgeRepository;
import io.swagger.annotations.ApiParam;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class BadgesApiController implements BadgesApi {
    @Autowired
    BadgeRepository badgeRepository;

    @Autowired
    ApplicationRepository applicationRepository;

    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> createBadge(@RequestHeader(value = "X-API-KEY") String xApiKey, @ApiParam(value = ""  ) @Valid @RequestBody(required=true) Badge badge) {
        ApplicationEntity app = applicationRepository.findByApiKey(xApiKey);
        if (app != null) {
            BadgeEntity newBadgeEntity = toBadgeEntity(badge);
            newBadgeEntity.setApp(app);
            badgeRepository.save(newBadgeEntity);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{id}")
                    .buildAndExpand(newBadgeEntity.getId()).toUri();

            return ResponseEntity.created(location).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    public ResponseEntity<List<Badge>> getBadges(@RequestHeader(value = "X-API-KEY") String xApiKey) {
        List<Badge> badges = new ArrayList<>();
        for (BadgeEntity badgeEntity : badgeRepository.findAllByAppApiKey(xApiKey)) {
            badges.add(toBadge(badgeEntity));
        }
        return ResponseEntity.ok(badges);
    }

    public ResponseEntity<Badge> getBadge(@RequestHeader(value = "X-API-KEY") String xApiKey, @ApiParam(value = "",required=true) @PathVariable("id") int id) {
        ApplicationEntity app = applicationRepository.findByApiKey(xApiKey);
        if (app != null) {
            String idStr = String.valueOf(id);

            BadgeEntity existingBadgeEntity = badgeRepository
                    .findByNameAndAppApiKey(idStr, xApiKey);
            if(existingBadgeEntity == null){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.ok(toBadge(existingBadgeEntity));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    private BadgeEntity toBadgeEntity(Badge badge) {
        BadgeEntity entity = new BadgeEntity();
        entity.setId(badge.getId().isPresent() ? badge.getId().get() : 2); // TODO create new id
        entity.setLevel(badge.getLevel());
        entity.setName(badge.getName());
        entity.setDescription(badge.getDescription());
        entity.setObtainedOnDate(badge.getObtainedOnDate().isPresent() ? badge.getObtainedOnDate().get() : LocalDate.now());
        return entity;
    }

    private Badge toBadge(BadgeEntity entity) {
        Badge badge = new Badge();
        badge.setId(JsonNullable.of(entity.getId()));
        badge.setLevel(entity.getLevel());
        badge.setName(entity.getName());
        badge.setDescription(entity.getDescription());
        badge.setObtainedOnDate(JsonNullable.of(entity.getObtainedOnDate()));
        return badge;
    }
}
