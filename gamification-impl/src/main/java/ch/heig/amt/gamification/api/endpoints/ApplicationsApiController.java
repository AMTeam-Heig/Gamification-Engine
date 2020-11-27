package ch.heig.amt.gamification.api.endpoints;

import ch.heig.amt.gamification.api.model.NewApplication;
import ch.heig.amt.gamification.entities.ApplicationEntity;
import ch.heig.amt.gamification.api.ApplicationsApi;
import ch.heig.amt.gamification.api.model.Application;
import ch.heig.amt.gamification.repositories.ApplicationRepository;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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
    public ResponseEntity<Application> createApplication(@ApiParam(value = ""  )  @Valid @RequestBody(required = false) NewApplication newApplication)  {
        ApplicationEntity applicationEntity = new ApplicationEntity();
        applicationEntity.setApiKey(ApplicationEntity.generateApiKey());
        applicationEntity.setName(newApplication.getName());
        applicationRepository.save(applicationEntity);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{name}")
                .buildAndExpand(applicationEntity.getName()).toUri();

        return ResponseEntity.created(location).build();
    }

    @Override
    public ResponseEntity<List<Application>> getApplications() {
        List<Application> applications = new LinkedList<>();
        for (ApplicationEntity applicationEntity : applicationRepository.findAll()) {
            Application application = new Application();
            application.setName(applicationEntity.getName());
            application.setApiKey(applicationEntity.getApiKey());

            applications.add(application);
        }
        return ResponseEntity.ok(applications);
    }

    @Override
    public ResponseEntity<Application> getApplication(@ApiParam(value = "",required=true) @PathVariable("name") String name) {
        Application application = new Application();
        application.setName(name);
        application.setApiKey(applicationRepository.findByName(name).getApiKey());

        return ResponseEntity.ok(application);
    }
}
