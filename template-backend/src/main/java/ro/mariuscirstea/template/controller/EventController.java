package ro.mariuscirstea.template.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.mariuscirstea.template.entity.EVT_Event;
import ro.mariuscirstea.template.model.PagedResult;
import ro.mariuscirstea.template.security.IsAdmin;
import ro.mariuscirstea.template.service.EventService;
import ro.mariuscirstea.template.specifications.GenericSpecification;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController("events")
@RequestMapping(path = "/events")
@CrossOrigin(origins = {"http://localhost:4200"})
@Slf4j
@IsAdmin
public class EventController extends PagedControllerEntityImpl<EVT_Event> {

    private final EventService eventService;

    public EventController(EventService eventService, ObjectMapper objectMapper) {
        super(objectMapper);
        this.eventService = eventService;
    }

    @GetMapping("/getLatest")
    public ResponseEntity<Map<String, List<EVT_Event>>> getLatest24Events() {
        return ResponseEntity.ok(Collections.singletonMap("events", eventService.getLatestEvents(null)));
    }

    @GetMapping("/getLatest/{noOfEvents}")
    public ResponseEntity<Map<String, List<EVT_Event>>> getLatestEvents(@PathVariable("noOfEvents") Integer noOfEvents) {
        return ResponseEntity.ok(Collections.singletonMap("events", eventService.getLatestEvents(noOfEvents)));
    }

    @PostMapping("/saveEvent")
    public ResponseEntity<Map<String, EVT_Event>> saveEvent(@RequestBody EVT_Event eventToBeSaved) {
        boolean added = eventToBeSaved.getId() == null;
        EVT_Event savedEvent = eventService.saveEvent(eventToBeSaved);
        if (added) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.ok(Collections.singletonMap("event", savedEvent));
    }

    @Override
    @GetMapping("/getPaged")
    public ResponseEntity<Map<String, PagedResult<EVT_Event>>> getPaged(@RequestParam(name = "pageSize", required = false) Integer pageSize,
                                                                        @RequestParam(name = "pageNo", required = false) Integer pageNo,
                                                                        @RequestParam(name = "sortBy", required = false) String sortBy,
                                                                        @RequestParam(name = "sortOrder", required = false) String sortOrder,
                                                                        @RequestParam(name = "filters", required = false) String filters) {
        PageRequest pageRequest = super.validateParams(pageSize, pageNo, sortBy, sortOrder);
        Map<String, String> filtersMap = super.parseFilters(filters);
        log.info("Filters map: {}", filtersMap);
        GenericSpecification<EVT_Event> evt_eventSpecification = new GenericSpecification<>(filtersMap);
        Page<EVT_Event> page = eventService.getFilteredEvents(evt_eventSpecification, pageRequest);
        PagedResult<EVT_Event> result = super.buildPagedResult(pageRequest, page);
        return ResponseEntity.ok(Collections.singletonMap("pagedResult", result));
    }

}
