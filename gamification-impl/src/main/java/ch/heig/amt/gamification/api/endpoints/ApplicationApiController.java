package ch.heig.amt.gamification.api.endpoints;

import ch.heig.amt.gamification.entities.ApplicationEntity;
import ch.heig.amt.gamification.api.ApplicationApi;
import ch.heig.amt.gamification.api.model.Application;
import ch.heig.amt.gamification.api.model.InlineObject;
import ch.heig.amt.gamification.repositories.ApplicationRepository;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@Controller
public class ApplicationApiController implements ApplicationApi {
    @Autowired
    ApplicationRepository applicationRepository;

    public ResponseEntity<Application> createApplication(@ApiParam(value = "", required = true) @Valid @RequestBody InlineObject inlineObject) {
        ApplicationEntity applicationEntity = new ApplicationEntity();
        applicationEntity.setApiKey(ApplicationEntity.generateApiKey());
        applicationEntity.setName(inlineObject.getName());
        applicationRepository.save(applicationEntity);

        Application application = new Application();
        application.setName(applicationEntity.getName());
        application.setApiKey(applicationEntity.getApiKey());
        return ResponseEntity.ok(application);
    }

    public ResponseEntity<Application> getApplication(String xApiKey) {
        Application application = new Application();
        application.setApiKey(xApiKey);
        application.setName(applicationRepository.findByApiKey(xApiKey).getName());
        return ResponseEntity.ok(application);
    }
}
