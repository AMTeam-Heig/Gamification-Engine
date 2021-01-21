package ch.heig.amt.gamification.api.endpoints;

import ch.heig.amt.gamification.api.UsersEvolutionApi;
import ch.heig.amt.gamification.api.model.PointTime;
import ch.heig.amt.gamification.api.model.User;
import ch.heig.amt.gamification.api.model.UserEvolution;
import ch.heig.amt.gamification.entities.PointTimeEntity;
import ch.heig.amt.gamification.entities.UserEntity;
import ch.heig.amt.gamification.entities.UserEvolutionEntity;
import ch.heig.amt.gamification.repositories.UserEvolutionRepository;
import ch.heig.amt.gamification.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.LinkedList;
import java.util.List;

@Controller
public class UsersEvolutionApiController implements UsersEvolutionApi {
    @Autowired
    UserEvolutionRepository userEvolutionRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public ResponseEntity<UserEvolution> getUserEvolution(String X_API_KEY, String username) {
        UserEntity user = userRepository.findByUsernameAndApplicationEntity_ApiKey(username, X_API_KEY);
        return ResponseEntity.ok(toUserEvolution(userEvolutionRepository.findByUser(user)));
    }

    private UserEvolution toUserEvolution(UserEvolutionEntity userEvolutionEntity) {
        UserEvolution userEvolution = new UserEvolution();
        userEvolution.setUser(toUser(userEvolutionEntity.getUser()));
        userEvolution.setPoints(toPointTimeEntityList(userEvolutionEntity.getPointTime()));
        return userEvolution;
    }

    private List<PointTime> toPointTimeEntityList(List<PointTimeEntity> pointTime) {
        List<PointTime> pointTimes = new LinkedList<>();
        pointTime.forEach(p -> {
            pointTimes.add(toPointTimeEntity(p));
        });
        return pointTimes;
    }

    private PointTime toPointTimeEntity(PointTimeEntity p) {
        PointTime pointTime = new PointTime();
        pointTime.setDate(p.getObtainedOnDate());
        pointTime.setIsAdded(p.isAdded());
        pointTime.setPoints(p.getPoints());
        return pointTime;
    }

    private User toUser(UserEntity entity) {
        User user = new User();
        user.setUsername(entity.getUsername());
        user.setPoints(entity.getPoints());
        user.setBirthdate(entity.getBirthdate());
        return user;
    }
}
