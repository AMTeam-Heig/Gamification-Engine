package io.avalia.gamification.api.endpoints;

import io.avalia.gamification.api.ApplicationApi;
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

@Controller
public class ApplicationApiController implements ApplicationApi {
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
}
