package ch.heig.amt.gamification.api.endpoints;

import ch.heig.amt.gamification.entities.ApplicationEntity;
import ch.heig.amt.gamification.entities.BadgeEntity;
import ch.heig.amt.gamification.repositories.ApplicationRepository;
import ch.heig.amt.gamification.repositories.BadgeRepository;
import ch.heig.amt.gamification.api.BadgesApi;
import ch.heig.amt.gamification.api.model.Badge;
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
        ApplicationEntity applicationEntity = applicationRepository.findByApiKey(xApiKey);
        if (applicationEntity != null) {
            if(badgeRepository.findByNameAndApplicationEntity_ApiKey(badge.getName(),xApiKey) == null)
            {
                BadgeEntity entity = new BadgeEntity();
                entity.setName(badge.getName());
                entity.setDescription(badge.getDescription());
                entity.setObtainedOnDate(badge.getObtainedOnDate());
                entity.setApplicationEntity(applicationEntity);
                badgeRepository.save(entity);

                URI location = ServletUriComponentsBuilder
                        .fromCurrentRequest().path("/{name}")
                        .buildAndExpand(entity.getName()).toUri();

                return ResponseEntity.created(location).build();
            }

        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    public ResponseEntity<List<Badge>> getBadges(@RequestHeader(value = "X-API-KEY") String xApiKey) {
        List<Badge> badges = new ArrayList<>();
        for (BadgeEntity badgeEntity : badgeRepository.findAllByApplicationEntity_ApiKey(xApiKey)) {
            Badge badge = new Badge();
            badge.setName(badgeEntity.getName());
            badge.setDescription(badgeEntity.getDescription());
            badge.setObtainedOnDate(badgeEntity.getObtainedOnDate());
            badges.add(badge);
        }
        return ResponseEntity.ok(badges);
    }

    public ResponseEntity<Badge> getBadge(@RequestHeader(value = "X-API-KEY") String xApiKey, @ApiParam(value = "",required=true) @PathVariable("name") String name) {
        ApplicationEntity app = applicationRepository.findByApiKey(xApiKey);
        if (app != null) {

            BadgeEntity badgeEntity = badgeRepository
                    .findByNameAndApplicationEntity_ApiKey(name, xApiKey);
            if(badgeEntity == null){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }

            Badge badge = new Badge();
            badge.setName(badgeEntity.getName());
            badge.setDescription(badgeEntity.getDescription());
            badge.setObtainedOnDate(badgeEntity.getObtainedOnDate());

            return ResponseEntity.ok(badge);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
