package ch.heig.amt.gamification.api.endpoints;

import ch.heig.amt.gamification.entities.ApplicationEntity;
import ch.heig.amt.gamification.api.ApplicationsApi;
import ch.heig.amt.gamification.api.model.Application;
import ch.heig.amt.gamification.api.model.InlineObject;
import ch.heig.amt.gamification.repositories.ApplicationRepository;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;

@Controller
public class ApplicationsApiController implements ApplicationsApi {
    @Autowired
    ApplicationRepository applicationRepository;

    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Application> createApplication(@ApiParam(value = "", required = true) @Valid @RequestBody InlineObject inlineObject) {
        ApplicationEntity applicationEntity = new ApplicationEntity();
        applicationEntity.setApiKey(ApplicationEntity.generateApiKey());
        applicationEntity.setName(inlineObject.getName());
        applicationRepository.save(applicationEntity);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{name}")
                .buildAndExpand(applicationEntity.getName()).toUri();

        return ResponseEntity.created(location).build();
    }

    public ResponseEntity<List<Application>> getApplication(String xApiKey) {
        List<Application> applications = new LinkedList<>();
        Application application = new Application();
        application.setName(applicationRepository.findByApiKey(xApiKey).getName());
        application.setApiKey(xApiKey);
        applications.add(application);
        return ResponseEntity.ok(applications);
    }
}
