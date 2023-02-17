package ro.mariuscirstea.template.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ro.mariuscirstea.template.entity.EVT_Event;
import ro.mariuscirstea.template.repository.EventRepository;
import ro.mariuscirstea.template.service.EventService;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public EVT_Event saveEvent(EVT_Event evt_event) {
        if (evt_event.getCreated() == null) {
            evt_event.setCreated(Timestamp.from(Instant.now()));
        }
        evt_event.setUpdated(Timestamp.from(Instant.now()));
        return eventRepository.save(evt_event);
    }

    @Override
    public EVT_Event saveExtendedEvent(String type, String description, String ip, String jsonAdditionalDetails, Long userId) {
        EVT_Event evt_event = new EVT_Event.EventBuilder()
                .type(type)
                .description(description)
                .ip(ip)
                .additionalInfo(jsonAdditionalDetails)
                .userId(userId)
                .build();
        return saveEvent(evt_event);
    }

    public List<EVT_Event> getAllEvents() {
        return eventRepository.findAll().stream()
                .sorted(Comparator.comparing(EVT_Event::getCreated).reversed())
                .collect(Collectors.toList());
    }

    public List<EVT_Event> getLatestEvents(Integer noOfEvents) {
        int limit = noOfEvents == null ? 24 : noOfEvents;
        return eventRepository.findLatestSortedByCreatedTimestamp(PageRequest.of(0, limit)).stream()
                .sorted(Comparator.comparing(EVT_Event::getCreated).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    private final List<String> allowedEvtEventFieldNames = Arrays.stream(EVT_Event.class.getDeclaredFields()).map(Field::getName).collect(Collectors.toList());

    public Page<EVT_Event> getFilteredEvents(Specification<EVT_Event> specification, PageRequest pageRequest) {
        String sortBy = pageRequest.getSort().get().map(Sort.Order::getProperty).findFirst().orElseThrow(() -> new IllegalArgumentException("Sort by field not supplied"));

        if (!allowedEvtEventFieldNames.contains(sortBy)) {
            throw new IllegalArgumentException("SortBy field " + sortBy + " not allowed for entity evt_event!");
        }
        return eventRepository.findAll(specification, pageRequest);
    }

}
