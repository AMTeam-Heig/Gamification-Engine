package io.avalia.gamification.api.endpoints;

import io.avalia.gamification.api.ApplicationsApi;
import io.avalia.gamification.api.model.Application;
import io.avalia.gamification.api.model.InlineObject;
import io.avalia.gamification.entities.ApplicationEntity;
import io.avalia.gamification.repositories.ApplicationRepository;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ApplicationsApiController implements ApplicationsApi {
    @Autowired
    ApplicationRepository applicationRepository;

    public ResponseEntity<Application> newApplication(@ApiParam(value = "", required = true) @Valid @RequestBody InlineObject inlineObject) {
        ApplicationEntity appEntity = new ApplicationEntity();
        appEntity.setApiKey(ApplicationEntity.generateApiKey());
        appEntity.setName(inlineObject.getName());
        applicationRepository.save(appEntity);

        Application app = new Application();
        app.setName(appEntity.getName());
        app.setApiKey(appEntity.getApiKey());
        return ResponseEntity.ok(app);
    }

    public ResponseEntity<List<Application>> getApplications(String xApiKey) {
        List<Application> applications = new ArrayList<>();

        applications.add(toApplication(applicationRepository.findByApiKey(xApiKey)));

        return ResponseEntity.ok(applications);
    }


    private Application toApplication(ApplicationEntity entity) {
        Application application = new Application();
        application.setApiKey(entity.getApiKey());
        application.setName(entity.getName());
        return application;
    }
}
