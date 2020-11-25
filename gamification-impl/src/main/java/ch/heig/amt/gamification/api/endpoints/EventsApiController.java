package ch.heig.amt.gamification.api.endpoints;

import ch.heig.amt.gamification.api.EventsApi;
import ch.heig.amt.gamification.api.model.Event;
import ch.heig.amt.gamification.entities.ApplicationEntity;
import ch.heig.amt.gamification.entities.EventEntity;
import ch.heig.amt.gamification.repositories.ApplicationRepository;
import ch.heig.amt.gamification.repositories.EventRepository;
import io.swagger.annotations.ApiParam;
import org.openapitools.jackson.nullable.JsonNullable;
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
import java.util.LinkedList;
import java.util.List;

public class EventsApiController implements EventsApi {
    @Autowired
    EventRepository eventRepository;

    @Autowired
    ApplicationRepository applicationRepository;

    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> createEvent(@RequestHeader(value = "X-API-KEY") String xApiKey, @ApiParam(value = "") @Valid @RequestBody(required = true) Event event) {
        ApplicationEntity applicationEntity = applicationRepository.findByApiKey(xApiKey);
        if(applicationEntity != null) {
            EventEntity entity = new EventEntity();
            entity.setId(event.getId().isPresent() ? event.getId().get() : 2);
            entity.setPoints(event.getPoints().get());
            entity.setType(event.getType().get());
            entity.setUserId(event.getUserId().get());

            entity.setApplication(applicationEntity);
            eventRepository.save(entity);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(entity.getId()).toUri();

            return ResponseEntity.created(location).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    public ResponseEntity<List<Event>> getEvents(@RequestHeader(value = "X-API-KEY") String xApiKey) {
        List<Event> events = new LinkedList<>();
        for(EventEntity eventEntity : eventRepository.findAllByAppApiKey(xApiKey)) {
            Event event = new Event();
            event.setId(JsonNullable.of(eventEntity.getId()));
            event.setPoints(JsonNullable.of(eventEntity.getPoints()));
            event.setType(JsonNullable.of(eventEntity.getType()));
            event.setUserId(JsonNullable.of(eventEntity.getUserId()));
            events.add(event);
        }
        return ResponseEntity.ok(events);
    }

    public ResponseEntity<Event> getEvent(@RequestHeader(value = "X-API-KEY") String xApiKey, @ApiParam(value = "", required = true) @PathVariable("id") int id) {
        ApplicationEntity applicationEntity = applicationRepository.findByApiKey(xApiKey);
        if(applicationEntity != null) {
            String idStr = String.valueOf(id);
            EventEntity existingEventEntity = eventRepository.findByNameAndAppApiKey(idStr, xApiKey);
            if(existingEventEntity == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
            Event event = new Event();
            event.setId(JsonNullable.of(existingEventEntity.getId()));
            event.setUserId(JsonNullable.of(existingEventEntity.getUserId()));
            event.setType(JsonNullable.of(existingEventEntity.getType()));
            event.setPoints(JsonNullable.of(existingEventEntity.getPoints()));
            return ResponseEntity.ok(event);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}