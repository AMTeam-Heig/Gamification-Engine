package ch.heig.amt.gamification.api.endpoints;

import ch.heig.amt.gamification.entities.ApplicationEntity;
import ch.heig.amt.gamification.entities.UserEntity;
import ch.heig.amt.gamification.entities.UserEvolutionEntity;
import ch.heig.amt.gamification.repositories.ApplicationRepository;
import ch.heig.amt.gamification.repositories.UserEvolutionRepository;
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
import java.util.List;

@Controller
public class UsersApiController implements UsersApi {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserEvolutionRepository userEvolutionRepository;

    @Autowired
    ApplicationRepository applicationRepository;

    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> createUser(@RequestHeader(value = "X-API-KEY") String xApiKey, @ApiParam(value = ""  ) @Valid @RequestBody(required=true) User user) {
        ApplicationEntity app = applicationRepository.findByApiKey(xApiKey);
        if (app != null) {
            UserEntity newUserEntity = toUserEntity(user, app);
            UserEvolutionEntity userEvolutionEntity = new UserEvolutionEntity();
            userEvolutionEntity.setUser(newUserEntity);

            newUserEntity.setApp(app);
            userRepository.save(newUserEntity);
            userEvolutionRepository.save(userEvolutionEntity);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{id}")
                    .buildAndExpand(newUserEntity.getId()).toUri();

            return ResponseEntity.created(location).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    public ResponseEntity<List<User>> getUsers(@RequestHeader(value = "X-API-KEY") String xApiKey) {
        List<User> users = new ArrayList<>();
        for (UserEntity userEntity : userRepository.findAllByAppApiKey(xApiKey)) {
            users.add(toUser(userEntity));
        }
        return ResponseEntity.ok(users);
    }

    public ResponseEntity<User> getUser(@RequestHeader(value = "X-API-KEY") String xApiKey, @ApiParam(value = "",required=true) @PathVariable("id") int id) {
        ApplicationEntity app = applicationRepository.findByApiKey(xApiKey);
        if (app != null) {
            String idStr = String.valueOf(id);

            UserEntity userEntity = userRepository
                    .findByNameAndAppApiKey(idStr, xApiKey);
            if(userEntity == null){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.ok(toUser(userEntity));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    private UserEntity toUserEntity(User user, ApplicationEntity app) {
        UserEntity userEntity = new UserEntity();

        userEntity.setId(user.getId().isPresent() ? user.getId().get() : 1); // TODO set new id if null
        userEntity.setApp(app);
        userEntity.setName(user.getName());
        userEntity.setPoints(user.getPoints());
        userEntity.setBirthdate(user.getBirthdate());
        userEntity.setReputation(user.getReputation().isPresent() ? user.getReputation().get() : "Basic reputation");

        return userEntity;
    }

    private User toUser(UserEntity entity) {
        User user = new User();
        user.setId(JsonNullable.of(entity.getId()));
        user.setName(entity.getName());
        user.setPoints(entity.getPoints());
        user.setReputation(JsonNullable.of(entity.getReputation()));
        user.setBirthdate(entity.getBirthdate());
        return user;
    }
}
