package ch.heig.amt.gamification.api.endpoints;

import ch.heig.amt.gamification.api.model.Badge;
import ch.heig.amt.gamification.entities.ApplicationEntity;
import ch.heig.amt.gamification.entities.BadgeEntity;
import ch.heig.amt.gamification.entities.UserEntity;
import ch.heig.amt.gamification.repositories.ApplicationRepository;
import ch.heig.amt.gamification.repositories.UserRepository;
import ch.heig.amt.gamification.api.UsersApi;
import ch.heig.amt.gamification.api.model.User;
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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Controller
public class UsersApiController implements UsersApi {
    @Autowired
    UserRepository userRepository;

    @Autowired
    ApplicationRepository applicationRepository;

    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> createUser(@RequestHeader(value = "X-API-KEY") String xApiKey, @ApiParam(value = ""  ) @Valid @RequestBody(required=true) User user) {
        ApplicationEntity applicationEntity = applicationRepository.findByApiKey(xApiKey);
        if (applicationEntity != null) {
            UserEntity userEntity = new UserEntity();
            userEntity.setApplicationEntity(applicationEntity);
            userEntity.setUsername(user.getUsername());
            userEntity.setPoints(user.getPoints());
            userEntity.setBirthdate(user.getBirthdate());
            userEntity.setReputation(user.getReputation());

            /*List<BadgeEntity> badgeEntities = new LinkedList<>();
            for(Badge b : user.getBadges()){
                BadgeEntity badgeEntity = new BadgeEntity();
                badgeEntity.setApplicationEntity(applicationEntity);
                badgeEntity.setDescription(b.getDescription());
                badgeEntity.setName(b.getName());
                badgeEntity.setObtainedOnDate(b.getObtainedOnDate());
                badgeEntities.add(badgeEntity);
            }

            userEntity.setBadgeEntity(badgeEntities);*/

            userRepository.save(userEntity);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{username}")
                    .buildAndExpand(userEntity.getUsername()).toUri();

            return ResponseEntity.created(location).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }


    public ResponseEntity<List<User>> getUsers(@RequestHeader(value = "X-API-KEY") String xApiKey) {
        List<User> users = new ArrayList<>();
        for (UserEntity userEntity : userRepository.findAllByApplicationEntity_ApiKey(xApiKey)) {
            User user = new User();
            user.setUsername(userEntity.getUsername());
            user.setPoints(userEntity.getPoints());
            user.setReputation(userEntity.getReputation());
            user.setBirthdate(userEntity.getBirthdate());
            users.add(user);
        }
        return ResponseEntity.ok(users);
    }

    public ResponseEntity<User> getUser(@RequestHeader(value = "X-API-KEY") String xApiKey, @ApiParam(value = "",required=true) @PathVariable("username") String username) {
        ApplicationEntity applicationEntity = applicationRepository.findByApiKey(xApiKey);
        if (applicationEntity != null) {

            UserEntity userEntity = userRepository
                    .findByUsernameAndApplicationEntity_ApiKey(username, xApiKey);
            if(userEntity == null){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
            userEntity.setApplicationEntity(applicationEntity);

            User user = new User();
            user.setUsername(userEntity.getUsername());
            user.setPoints(userEntity.getPoints());
            user.setReputation(userEntity.getReputation());
            user.setBirthdate(userEntity.getBirthdate());

            return ResponseEntity.ok(user);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
