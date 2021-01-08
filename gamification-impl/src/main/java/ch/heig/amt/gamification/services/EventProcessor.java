package ch.heig.amt.gamification.services;

import ch.heig.amt.gamification.entities.BadgeEntity;
import ch.heig.amt.gamification.entities.EventEntity;
import ch.heig.amt.gamification.entities.RuleEntity;
import ch.heig.amt.gamification.entities.UserEntity;
import ch.heig.amt.gamification.repositories.BadgeRepository;
import ch.heig.amt.gamification.repositories.RuleRepository;
import ch.heig.amt.gamification.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventProcessor {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RuleRepository ruleRepository;

    @Autowired
    BadgeRepository badgeRepository;

    //In the applications, events grant points, rules distribute badges according to points
    public void process(String xApiKey, EventEntity event){
        Iterable<UserEntity> userEntities = userRepository.findAllByApplicationEntity_ApiKey(xApiKey);
        Iterable<RuleEntity> ruleEntities = ruleRepository.findAllByApplicationEntity_ApiKey(xApiKey);
        Iterable<BadgeEntity> badgeEntities = badgeRepository.findAllByApplicationEntity_ApiKey(xApiKey);

        UserEntity userEntity = findUser(userEntities, event.getUsername());

        if(userEntity != null){
            for(RuleEntity rule : ruleEntities){
                //We're looking for rules triggered by the event
                if(rule.getEventName().equals(event.getName())){
                    //What happens for the user
                    //Distributing event points
                    userEntity.setPoints(userEntity.getPoints() + event.getPoints());
                    //Distributing badges
                    //userEntity.setBadges()
                    //Distributing reputation

                }
            }
            userRepository.save(userEntity);
        }

    }

    private UserEntity findUser(Iterable<UserEntity> userEntities, String username){
        for(UserEntity usr : userEntities){
            if(usr.getUsername().equals(username)){
                return usr;
            }
        }
        return null;
    }

}
