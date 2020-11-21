package ch.heig.amt.gamification.api.endpoints;

import ch.heig.amt.gamification.entities.ApplicationEntity;
import ch.heig.amt.gamification.entities.RuleEntity;
import ch.heig.amt.gamification.repositories.ApplicationRepository;
import ch.heig.amt.gamification.repositories.RuleRepository;
import ch.heig.amt.gamification.api.RulesApi;
import ch.heig.amt.gamification.api.model.Rule;
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

public class RulesApiController implements RulesApi {
    @Autowired
    ApplicationRepository applicationRepository;

    @Autowired
    RuleRepository ruleRepository;

    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> createRule(@RequestHeader(value = "X-API-KEY") String xApiKey, @ApiParam(value = ""  ) @Valid @RequestBody(required=true) Rule rule) {
        ApplicationEntity app = applicationRepository.findByApiKey(xApiKey);
        if (app != null) {
            RuleEntity newRuleEntity = toRuleEntity(rule);
            newRuleEntity.setApp(app);
            ruleRepository.save(newRuleEntity);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{id}")
                    .buildAndExpand(newRuleEntity.getId()).toUri();

            return ResponseEntity.created(location).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    public ResponseEntity<Rule> getRule(@RequestHeader(value = "X-API-KEY") String xApiKey, @ApiParam(value = "",required=true) @PathVariable("id") int id) {
        ApplicationEntity app = applicationRepository.findByApiKey(xApiKey);
        if (app != null) {
            String idStr = String.valueOf(id);

            RuleEntity ruleEntity = ruleRepository
                    .findByNameAndAppApiKey(idStr, xApiKey);
            if(ruleEntity == null){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.ok(toRule(ruleEntity));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    private RuleEntity toRuleEntity(Rule rule) {
        RuleEntity entity = new RuleEntity();
        entity.setName(rule.getName());
        entity.setDefinition(rule.getDefinition());
        //entity.setPoints(rule.getPoints()); //TODO
        //entity.setReputation(rule.getReputation()); //TODO fix dis
        entity.setEventId(rule.getEventId());
        entity.setBadgeId(rule.getBadgeId());
        return entity;
    }

    private Rule toRule(RuleEntity entity) {
        Rule rule = new Rule();
        rule.setName(entity.getName());
        rule.setDefinition(entity.getDefinition());
        //rule.setPoints(entity.getPoints());
        //rule.setReputation(entity.getReputation());
        rule.setEventId(entity.getEventId());
        rule.setBadgeId(entity.getBadgeId());
        return rule;
    }
}
