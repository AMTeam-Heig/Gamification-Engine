package ch.heig.amt.gamification.api.endpoints;

import ch.heig.amt.gamification.api.UsersApi;
import ch.heig.amt.gamification.api.model.Badge;
import ch.heig.amt.gamification.api.model.User;
import ch.heig.amt.gamification.entities.ApplicationEntity;
import ch.heig.amt.gamification.entities.BadgeEntity;
import ch.heig.amt.gamification.entities.UserEntity;
import ch.heig.amt.gamification.entities.UserEvolutionEntity;
import ch.heig.amt.gamification.repositories.ApplicationRepository;
import ch.heig.amt.gamification.repositories.UserEvolutionRepository;
import ch.heig.amt.gamification.repositories.UserRepository;
import io.swagger.annotations.ApiParam;
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
    UserEvolutionRepository userEvolutionRepository;

    @Autowired
    ApplicationRepository applicationRepository;

    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> createUser(@RequestHeader(value = "X-API-KEY") String xApiKey, @ApiParam(value = ""  ) @Valid @RequestBody(required=true) User user) {
        ApplicationEntity applicationEntity = applicationRepository.findByApiKey(xApiKey);
        if (applicationEntity != null) {
            if(userRepository.findByUsernameAndApplicationEntity_ApiKey(user.getUsername(),xApiKey) == null) {
                UserEntity userEntity = new UserEntity();
                userEntity.setApplicationEntity(applicationEntity);
                userEntity.setUsername(user.getUsername());
                userEntity.setRole(user.getRole());
                userEntity.setPoints(user.getPoints());
                userEntity.setBirthdate(user.getBirthdate());
                userEntity.setBadges(badgesToBadgeEntityList(user, xApiKey));

                UserEvolutionEntity userEvolutionEntity = new UserEvolutionEntity();
                userEvolutionEntity.setUser(userEntity);
                userRepository.save(userEntity);
                userEvolutionRepository.save(userEvolutionEntity);

                URI location = ServletUriComponentsBuilder
                        .fromCurrentRequest().path("/{username}")
                        .buildAndExpand(userEntity.getUsername()).toUri();

                return ResponseEntity.created(location).build();
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    public ResponseEntity<List<User>> getUsers(@RequestHeader(value = "X-API-KEY") String xApiKey) {
        List<User> users = new ArrayList<>();
        for (UserEntity userEntity : userRepository.findAllByApplicationEntity_ApiKey(xApiKey)) {
            User user = new User();
            user.setUsername(userEntity.getUsername());
            user.setRole(userEntity.getRole());
            user.setPoints(userEntity.getPoints());
            user.setBirthdate(userEntity.getBirthdate());
            user.setBadges(badgeEntityToBadgeList(userEntity));
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
            user.setRole(userEntity.getRole());
            user.setPoints(userEntity.getPoints());
            user.setBirthdate(userEntity.getBirthdate());
            user.setBadges(badgeEntityToBadgeList(userEntity));

            return ResponseEntity.ok(user);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @Override
    public ResponseEntity<Void> deleteUser(@RequestHeader(value = "X-API-KEY") String xApiKey, @ApiParam(value = "",required=true) @PathVariable("username") String username) {

        UserEntity userEntity = userRepository.findByUsernameAndApplicationEntity_ApiKey(username, xApiKey);

        if(userRepository.findByUsernameAndApplicationEntity_ApiKey(username, xApiKey) != null) {
            userEntity.setUsername(userRepository.findByUsernameAndApplicationEntity_ApiKey(username, xApiKey).getUsername());
            userEntity.setRole(userRepository.findByUsernameAndApplicationEntity_ApiKey(username,xApiKey).getRole());
            userEntity.setReputation(userRepository.findByUsernameAndApplicationEntity_ApiKey(username, xApiKey).getReputation());
            userEntity.setBirthdate(userRepository.findByUsernameAndApplicationEntity_ApiKey(username, xApiKey).getBirthdate());
            userEntity.setPoints(userRepository.findByUsernameAndApplicationEntity_ApiKey(username, xApiKey).getPoints());
            userEntity.setId(userRepository.findByUsernameAndApplicationEntity_ApiKey(username, xApiKey).getId());
            userEntity.setApplicationEntity(userRepository.findByUsernameAndApplicationEntity_ApiKey(username, xApiKey).getApplicationEntity());
            userEntity.setBadges(userRepository.findByUsernameAndApplicationEntity_ApiKey(username, xApiKey).getBadges());
            userRepository.deleteById(userEntity.getId());

            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }


    private List<BadgeEntity> badgesToBadgeEntityList(User user, String xApiKey){
        ApplicationEntity applicationEntity = applicationRepository.findByApiKey(xApiKey);

        List<BadgeEntity> badgeEntities = new LinkedList<>();
        for(Badge b : user.getBadges()){
            BadgeEntity badgeEntity = new BadgeEntity();
            badgeEntity.setApplicationEntity(applicationEntity);
            badgeEntity.setDescription(b.getDescription());
            badgeEntity.setName(b.getName());
            badgeEntities.add(badgeEntity);
        }
        return badgeEntities;
    }

    private List<Badge> badgeEntityToBadgeList(UserEntity userEntity){
        List<Badge> badges = new LinkedList<>();
        for(BadgeEntity b : userEntity.getBadges()){
            Badge badge = new Badge();
            badge.setDescription(b.getDescription());
            badge.setName(b.getName());
            badges.add(badge);
        }
        return badges;
    }

    private List<UserEntity> userToUserEntityList(List<User> userList, String xApiKey){
        ApplicationEntity applicationEntity = applicationRepository.findByApiKey(xApiKey);

        List<UserEntity> users = new LinkedList<>();
        for(User u : userList){
            UserEntity userEntity = new UserEntity();
            userEntity.setApplicationEntity(applicationEntity);
            userEntity.setBadges(badgeToBadgeEntityList(u, xApiKey));
            userEntity.setUsername(u.getUsername());
            userEntity.setBirthdate(u.getBirthdate());
            userEntity.setPoints(u.getPoints());
            userEntity.setRole(u.getRole());
            users.add(userEntity);
        }
        return users;
    }

    private List<BadgeEntity> badgeToBadgeEntityList(User user, String xApiKey){
        ApplicationEntity applicationEntity = applicationRepository.findByApiKey(xApiKey);
        List<BadgeEntity> badgeEntities = new LinkedList<>();
        for(Badge b : user.getBadges()){
            BadgeEntity badgeEntity = new BadgeEntity();
            badgeEntity.setApplicationEntity(applicationEntity);
            badgeEntity.setDescription(b.getDescription());
            badgeEntity.setName(b.getName());
            badgeEntities.add(badgeEntity);
        }
        return badgeEntities;
    }

}
