package io.avalia.gamification.api.endpoints;

import io.avalia.gamification.api.UsersApi;
import io.avalia.gamification.api.model.User;
import io.avalia.gamification.entities.ApplicationEntity;
import io.avalia.gamification.entities.UserEntity;
import io.avalia.gamification.repositories.ApplicationRepository;
import io.avalia.gamification.repositories.UserRepository;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

public class UsersApiController implements UsersApi {
    @Autowired
    UserRepository userRepository;

    @Autowired
    ApplicationRepository applicationRepository;

    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> createUser(@RequestHeader(value = "X-API-KEY") String xApiKey, @ApiParam(value = ""  ) @Valid @RequestBody(required=true) User user) {
        ApplicationEntity app = applicationRepository.findByApiKey(xApiKey);
        if (app != null) {
            UserEntity newUserEntity = toUserEntity(user);
            newUserEntity.setApp(app);
            userRepository.save(newUserEntity);

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

    private UserEntity toUserEntity(User user) {
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setName(user.getName());
        entity.setPoints(user.getPoints());
        entity.setReputation(user.getReputation());
        entity.setBirthdate(user.getBirthdate());
        return entity;
    }

    private User toUser(UserEntity entity) {
        User user = new User();
        user.setId(entity.getId());
        user.setName(entity.getName());
        user.setPoints(entity.getPoints());
        user.setReputation(entity.getReputation());
        user.setBirthdate(entity.getBirthdate());
        return user;
    }
}
