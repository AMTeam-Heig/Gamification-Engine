package ch.heig.amt.gamification.api.endpoints;

import ch.heig.amt.gamification.api.TopNUsersApi;
import ch.heig.amt.gamification.api.model.User;
import ch.heig.amt.gamification.entities.UserEntity;
import ch.heig.amt.gamification.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
public class TopNUsersApiController implements TopNUsersApi {
    @Autowired
    UserRepository userRepository;

    @Override
    public ResponseEntity<List<User>> getTopUsers(String X_API_KEY, Integer nbr) {
        List<User> users = new ArrayList<>();
        for (UserEntity userEntity : userRepository.findAllByApplicationEntity_ApiKey(X_API_KEY)) {
            users.add(toUser(userEntity));
        }
        users.sort(Comparator.comparing(User::getPoints));
        return ResponseEntity.ok(users.subList(0, nbr));
    }

    private User toUser(UserEntity entity) {
        User user = new User();
        user.setUsername(entity.getUsername());
        user.setPoints(entity.getPoints());
        user.setReputation(entity.getReputation());
        user.setBirthdate(entity.getBirthdate());
        return user;
    }

}
