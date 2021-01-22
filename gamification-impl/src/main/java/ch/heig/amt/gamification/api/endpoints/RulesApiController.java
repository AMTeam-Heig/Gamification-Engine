package ch.heig.amt.gamification.api.endpoints;

import ch.heig.amt.gamification.api.RulesApi;
import ch.heig.amt.gamification.api.model.Rule;
import ch.heig.amt.gamification.entities.ApplicationEntity;
import ch.heig.amt.gamification.entities.RuleEntity;
import ch.heig.amt.gamification.repositories.ApplicationRepository;
import ch.heig.amt.gamification.repositories.RuleRepository;
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
import java.util.LinkedList;
import java.util.List;

@Controller
public class RulesApiController implements RulesApi {
    @Autowired
    ApplicationRepository applicationRepository;

    @Autowired
    RuleRepository ruleRepository;

    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> createRule(@RequestHeader(value = "X-API-KEY") String xApiKey, @ApiParam(value = "") @Valid @RequestBody(required = true) Rule rule) {
        ApplicationEntity applicationEntity = applicationRepository.findByApiKey(xApiKey);
        if (applicationEntity != null) {
            RuleEntity ruleEntity = new RuleEntity();
            ruleEntity.setName(rule.getName());
            ruleEntity.setDefinition(rule.getDefinition());
            ruleEntity.setPoints(rule.getPoints());
            ruleEntity.setReputation(rule.getReputation());
            ruleEntity.setEventName(rule.getEventName());
            ruleEntity.setBadgeName(rule.getBadgeName());
            ruleEntity.setApplicationEntity(applicationEntity);

            ruleRepository.save(ruleEntity);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{name}")
                    .buildAndExpand(ruleEntity.getName()).toUri();

            return ResponseEntity.created(location).build();
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @Override
    public ResponseEntity<List<Rule>> getRules(@RequestHeader(value = "X-API-KEY") String xApiKey) {
        List<Rule> rules = new LinkedList<>();
        for (RuleEntity ruleEntity : ruleRepository.findAll()) {
            Rule rule = new Rule();
            rule.setBadgeName(ruleEntity.getBadgeName());
            rule.setDefinition(ruleEntity.getDefinition());
            rule.setEventName(ruleEntity.getEventName());
            rule.setName(ruleEntity.getName());
            rule.setPoints(ruleEntity.getPoints());
            rule.setReputation(ruleEntity.getReputation());

            rules.add(rule);
        }
        return ResponseEntity.ok(rules);
    }

    @Override
    public ResponseEntity<Rule> getRule(@RequestHeader(value = "X-API-KEY") String xApiKey, @ApiParam(value = "", required = true) @PathVariable("name") String name) {
        ApplicationEntity applicationEntity = applicationRepository.findByApiKey(xApiKey);
        if (applicationEntity != null) {

            RuleEntity ruleEntity = ruleRepository
                    .findByNameAndApplicationEntity_ApiKey(name, xApiKey);
            if (ruleEntity == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
            Rule rule = new Rule();
            rule.setName(ruleEntity.getName());
            rule.setDefinition(ruleEntity.getDefinition());
            rule.setPoints(ruleEntity.getPoints());
            rule.setReputation(ruleEntity.getReputation());
            rule.setEventName(ruleEntity.getEventName());
            rule.setBadgeName(ruleEntity.getBadgeName());
            return ResponseEntity.ok(rule);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
