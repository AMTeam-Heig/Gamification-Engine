package ch.heig.amt.gamification.api.endpoints;

import ch.heig.amt.gamification.api.TopNUsersApi;
import ch.heig.amt.gamification.api.model.Badge;
import ch.heig.amt.gamification.api.model.User;
import ch.heig.amt.gamification.entities.BadgeEntity;
import ch.heig.amt.gamification.entities.UserEntity;
import ch.heig.amt.gamification.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
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
        user.setBirthdate(entity.getBirthdate());
        user.setBadges(BadgeEntityToBadgeList(entity));
        return user;
    }

    private List<Badge> BadgeEntityToBadgeList(UserEntity userEntity){
        List<Badge> badges = new LinkedList<>();
        for(BadgeEntity b : userEntity.getBadges()){
            Badge badge = new Badge();
            badge.setDescription(b.getDescription());
            badge.setName(b.getName());
            badges.add(badge);
        }
        return badges;
    }

}
