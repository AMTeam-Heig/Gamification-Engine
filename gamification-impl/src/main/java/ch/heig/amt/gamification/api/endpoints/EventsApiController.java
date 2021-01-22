package ch.heig.amt.gamification.api.endpoints;

import ch.heig.amt.gamification.api.EventsApi;
import ch.heig.amt.gamification.api.model.Event;
import ch.heig.amt.gamification.entities.ApplicationEntity;
import ch.heig.amt.gamification.entities.EventEntity;
import ch.heig.amt.gamification.repositories.ApplicationRepository;
import ch.heig.amt.gamification.repositories.EventRepository;
import ch.heig.amt.gamification.services.EventProcessor;
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
import java.util.ArrayList;
import java.util.List;

@Controller
public class EventsApiController implements EventsApi {
    @Autowired
    EventRepository eventRepository;

    @Autowired
    ApplicationRepository applicationRepository;

    @Autowired
    EventProcessor eventProcessor;

    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> createEvent(@RequestHeader(value = "X-API-KEY") String xApiKey, @ApiParam(value = "") @Valid @RequestBody(required = true) Event event) {
        ApplicationEntity applicationEntity = applicationRepository.findByApiKey(xApiKey);
        if (applicationEntity != null) {
            EventEntity entity = new EventEntity();
            entity.setName(event.getName());
            entity.setPoints(event.getPoints());
            entity.setUsername(event.getUsername());

            entity.setApplicationEntity(applicationEntity);
            eventRepository.save(entity);

            eventProcessor.process(xApiKey, entity);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{name}")
                    .buildAndExpand(entity.getName()).toUri();

            return ResponseEntity.created(location).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    public ResponseEntity<List<Event>> getEvents(@RequestHeader(value = "X-API-KEY") String xApiKey) {
        List<Event> events = new ArrayList<>();
        for (EventEntity eventEntity : eventRepository.findAllByApplicationEntity_ApiKey(xApiKey)) {
            Event event = new Event();
            event.setName(eventEntity.getName());
            event.setPoints(eventEntity.getPoints());
            event.setUsername(eventEntity.getUsername());
            events.add(event);
        }
        return ResponseEntity.ok(events);
    }

    public ResponseEntity<Event> getEvent(@RequestHeader(value = "X-API-KEY") String xApiKey, @ApiParam(value = "", required = true) @PathVariable("name") String name) {
        ApplicationEntity applicationEntity = applicationRepository.findByApiKey(xApiKey);
        if (applicationEntity != null) {
            EventEntity existingEventEntity = eventRepository.findByNameAndApplicationEntity_ApiKey(name, xApiKey);
            if (existingEventEntity == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }

            Event event = new Event();
            event.setName(existingEventEntity.getName());
            event.setUsername(existingEventEntity.getUsername());
            event.setPoints(existingEventEntity.getPoints());
            return ResponseEntity.ok(event);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
