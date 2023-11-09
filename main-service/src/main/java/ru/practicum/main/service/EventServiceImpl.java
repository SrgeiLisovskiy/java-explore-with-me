package ru.practicum.main.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.dto.*;
import ru.practicum.main.exceptions.ConflictException;
import ru.practicum.main.exceptions.NotFoundException;
import ru.practicum.main.exceptions.ValidationException;
import ru.practicum.main.model.*;
import ru.practicum.main.model.enums.EventState;
import ru.practicum.main.model.enums.EventUserState;
import ru.practicum.main.model.enums.RequestStatus;
import ru.practicum.main.model.enums.StateAction;
import ru.practicum.main.model.mappers.EventMapper;
import ru.practicum.main.model.mappers.LocationMapper;
import ru.practicum.main.model.mappers.RequestMapper;
import ru.practicum.main.repository.*;
import ru.practicum.main.utilite.CheckValidationService;
import ru.practicum.stats.dto.HitDto;
import ru.practicum.stats.stats_client.client.StatClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.main.model.enums.EventState.*;
import static ru.practicum.main.model.enums.RequestStatus.CONFIRMED;
import static ru.practicum.main.utilite.Constant.DATE_FORMAT;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final CheckValidationService checkValidationService;
    private final StatClient statClient;


    @Override
    public List<EventShortDto> getEventsByUserId(Long userId, Integer from, Integer size) {
        checkExistenceUser(userId);
        PageRequest pageRequest = checkValidationService.checkPageSize(from, size);
        return eventRepository.findAll(pageRequest).stream()
                .map(EventMapper::toEventShortDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventDto createEvent(NewEventDto newEventDto, Long userId) {
        User user = checkExistenceUser(userId);
        Category category = checkExistenceCategory(newEventDto.getCategory());
        Location location = locationRepository.save(LocationMapper.toLocation(newEventDto.getLocation()));
        Event event = eventRepository.save(EventMapper.toEvent(newEventDto, user, category, location));
        return EventMapper.toEventDto(event);
    }

    @Override
    public EventDto getUserEventById(Long userId, Long eventId) {
        User user = checkExistenceUser(userId);
        Event event = checkExistenceEvent(eventId);
        if (!event.getInitiator().equals(user)) {
            throw new ConflictException("Нельзя создать запрос на свое событие");
        }
        EventDto eventDto = EventMapper.toEventDto(event);
        eventDto.setViews(statClient.getView(eventId));
        return eventDto;
    }

    @Override
    @Transactional
    public EventDto updateUserEventById(UpdateEventDto updateEventDto, Long userId, Long eventId) {
        checkExistenceUser(userId);
        Event event = checkExistenceEventInitiator(userId, eventId);
        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Статус события не может быть обновлен, так как со статусом PUBLISHED");
        }
        if (!event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Пользователь с id= " + userId + " не автор события");
        }

        if (updateEventDto.getStateAction() != null) {
            EventUserState state = EventUserState.getStateValue(updateEventDto.getStateAction());
            switch (state) {
                case CANCEL_REVIEW:
                    event.setState(CANCELED);
                    break;
                case SEND_TO_REVIEW:
                    event.setState(PENDING);
                    break;
            }
        }
        Event updateEvent = updateEvent(event, updateEventDto);
        return EventMapper.toEventDto(eventRepository.save(updateEvent));
    }

    @Override
    public List<RequestDto> getRequestsForEventIdByUserId(Long userId, Long eventId) {
        checkExistenceUser(userId);
        checkExistenceEventInitiator(userId, eventId);
        List<Request> requests = requestRepository.findAllByEventId(eventId);
        return requests.stream().map(RequestMapper::toRequestDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RequestUpdateDtoResult updateStatusRequestsForEventIdByUserId(
            RequestUpdateDtoRequest requestUpdateDtoRequest, Long userId, Long eventId) {
        User user = checkExistenceUser(userId);
        Event event = checkExistenceEvent(eventId);
        checkConflictRequest(event, user);
        List<Request> requests = requestRepository.findAllByIdIn(requestUpdateDtoRequest.getRequestIds());
        if (!requests.stream().map(Request::getStatus).allMatch(RequestStatus.PENDING::equals)) {
            throw new ConflictException("Изменять можно только запросы находящиеся в ожидании");
        }
        RequestUpdateDtoResult requestUpdateDtoResult = new RequestUpdateDtoResult();
        RequestStatus status = requestUpdateDtoRequest.getStatus();
        if (status.equals(RequestStatus.REJECTED)) {
            for (Request request : requests) {
                request.setStatus(RequestStatus.REJECTED);
                requestUpdateDtoResult.setRejectedRequests(requests.stream()
                        .map(RequestMapper::toRequestDto).collect(Collectors.toList()));
            }
        }
        if (status.equals(RequestStatus.CONFIRMED)) {
            for (Request request : requests) {
                if (requestRepository.findConfirmedRequests(eventId).equals(event.getParticipantLimit())) {
                    request.setStatus(RequestStatus.REJECTED);
                    requestUpdateDtoResult.getRejectedRequests().add(RequestMapper.toRequestDto(request));
                } else {
                    request.setStatus(RequestStatus.CONFIRMED);
                    requestUpdateDtoResult.getConfirmedRequests().add(RequestMapper.toRequestDto(request));
                }
                requestRepository.save(request);
            }

        }
        return requestUpdateDtoResult;
    }

    @Override
    public List<EventDto> getEventsByAdmin(List<Long> users, List<String> states, List<Long> categories,
                                           String rangeStart, String rangeEnd, Integer from, Integer size) {
        checkEventDate(rangeStart, rangeEnd);
        PageRequest pageRequest = checkValidationService.checkPageSize(from, size);
        Specification<Event> specification = Specification.where(null);
        if (users != null && !users.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    root.get("initiator").get("id").in(users));
        }
        if (states != null && !states.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    root.get("state").as(String.class).in(states));
        }
        if (categories != null && !categories.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    root.get("category").get("id").in(categories));
        }
        if (rangeEnd != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("eventDate").as(String.class), rangeEnd));
        }
        if (rangeStart != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate").as(String.class), rangeStart));
        }
        List<Event> events = eventRepository.findAll(specification, pageRequest).toList();

        List<EventDto> result = new ArrayList<>();
        for (Event event : events) {
            int count = requestRepository.countByEventIdAndStatus(event.getId(), RequestStatus.CONFIRMED);
            EventDto eventDto = EventMapper.toEventDto(event);
            eventDto.setConfirmedRequests(count);
            result.add(eventDto);
        }
        return result;
    }

    @Override
    @Transactional
    public EventDto updateEventByAdmin(UpdateEventDto updateEventDto, Long eventId) {
        Event event = checkExistenceEvent(eventId);
        if (updateEventDto.getEventDate() != null &&
                updateEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new ConflictException("Дата начала изменяемого события должна быть" +
                    " не ранее чем за час от даты публикации");
        }
        if (!event.getState().equals(PENDING)) {
            throw new ConflictException("Изменять можно только ожидающие события");
        }
        if (updateEventDto.getStateAction() != null) {
            StateAction state = StateAction.valueOf(updateEventDto.getStateAction());
            switch (state) {
                case REJECT_EVENT:
                    event.setState(CANCELED);
                    break;
                case PUBLISH_EVENT:
                    event.setState(PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                    break;
            }
        }

        Event updateEvent = updateEvent(event, updateEventDto);
        return EventMapper.toEventDto(eventRepository.save(updateEvent));
    }

    @Override
    public List<EventDto> getEventsByPublic(String text, List<Long> categories, Boolean paid, String rangeStart,
                                            String rangeEnd, Boolean onlyAvailable, String sort, Integer from, Integer size,
                                            String uri, String ip) {
        checkEventDate(rangeStart, rangeEnd);
        PageRequest pageRequest = checkValidationService.checkPageSize(from, size);
        HitDto hitDto = HitDto.builder()
                .app("main-service")
                .uri(uri)
                .ip(ip)
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT)))
                .build();
        statClient.addHit(hitDto);
        Specification<Event> specification = Specification.where(null);
        if (text != null) {
            String searchText = text.toLowerCase();
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.or(
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("annotation")), "%" + searchText + "%"),
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + searchText + "%")
                    ));
        }

        if (categories != null && !categories.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    root.get("category").get("id").in(categories));
        }
        if (rangeStart != null) {
            LocalDateTime start = LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern(DATE_FORMAT));
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThan(root.get("eventDate"), start));
        }
        if (rangeEnd != null) {
            LocalDateTime end = LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern(DATE_FORMAT));
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThan(root.get("eventDate"), end));
        }

        if (onlyAvailable != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("participantLimit"), 0));
        }

        specification = specification.and((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("state"), EventState.PUBLISHED));

        List<Event> events = eventRepository.findAll(specification, pageRequest).getContent();
        List<EventDto> result = new ArrayList<>();
        for (Event event : events) {
            int count = requestRepository.countByEventIdAndStatus(event.getId(), RequestStatus.CONFIRMED);
            EventDto eventDto = EventMapper.toEventDto(event);
            eventDto.setConfirmedRequests(count);
            result.add(eventDto);
        }

        return result;
    }

    @Override
    public EventDto getEventById(Long eventId, String uri, String ip) {
        Event event = checkExistenceEvent(eventId);
        if (!EventState.PUBLISHED.equals(event.getState())) {
            throw new NotFoundException("Событие с id = " + eventId + " не опубликовано");
        }
        EventDto eventDto = EventMapper.toEventDto(event);
        HitDto hitDto = HitDto.builder()
                .app("main-service")
                .uri(uri)
                .ip(ip)
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT)))
                .build();
        statClient.addHit(hitDto);
        eventDto.setViews(statClient.getView(eventId));
        return eventDto;
    }

    private void checkConflictRequest(Event event, User user) {
        if (!event.getInitiator().equals(user)) {
            throw new ConflictException("Нельзя создать запрос на свое событие");
        }
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            throw new ConflictException("Это событие не требует подтверждения запросов");
        }
        if (requestRepository.existsByRequesterAndEvent(user, event)) {
            throw new ConflictException("Невозможно отправить повторный запрос");
        }
        if (event.getParticipantLimit() > 0 &&
                event.getParticipantLimit() <= requestRepository.countByEventIdAndStatus(event.getId(), CONFIRMED)) {
            throw new ConflictException("У события достигнут лимит запросов на участие.");
        }
    }

    private Category checkExistenceCategory(Long catId) {
        return categoryRepository.findById(catId).orElseThrow(() ->
                new NotFoundException("Категория с ID = " + catId + "  не найдена"));
    }

    private User checkExistenceUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с ID = " + userId + "  не найден"));
    }

    private Event checkExistenceEvent(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("События с ID = " + eventId + "  не найдено"));
    }

    private Event checkExistenceEventInitiator(Long userId, Long eventId) {
        return eventRepository.findByInitiatorIdAndId(userId, eventId).orElseThrow(
                () -> new NotFoundException("События с id = " + eventId + "и с пользователем с id = " + userId +
                        " не существует"));
    }

    private Event updateEvent(Event event, UpdateEventDto updateEventDto) {
        Long categoryId = updateEventDto.getCategory();
        if (categoryId != null) {
            Category category = checkExistenceCategory(categoryId);
            event.setCategory(category);
        }
        String description = updateEventDto.getDescription();
        if (description != null && !description.isBlank()) {
            event.setDescription(description);
        }
        if (updateEventDto.getLocation() != null) {
            Location location = LocationMapper.toLocation(updateEventDto.getLocation());
            locationRepository.save(location);
            event.setLocation(location);
        }
        Long participantLimit = updateEventDto.getParticipantLimit();
        if (participantLimit != null) {
            event.setParticipantLimit(participantLimit);
        }
        if (updateEventDto.getPaid() != null) {
            event.setPaid(updateEventDto.getPaid());
        }
        Boolean requestModeration = updateEventDto.getRequestModeration();
        if (requestModeration != null) {
            event.setRequestModeration(requestModeration);
        }
        String title = updateEventDto.getTitle();
        if (title != null && !title.isBlank()) {
            event.setTitle(title);
        }
        String annotation = updateEventDto.getAnnotation();
        if (annotation != null) {
            event.setAnnotation(annotation);
        }
        return event;
    }

    private void checkEventDate(String rangeStart, String rangeEnd) {
        LocalDateTime start = null;
        LocalDateTime end = null;
        if (rangeStart != null) {
            start = LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern(DATE_FORMAT));
        }
        if (rangeEnd != null) {
            end = LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern(DATE_FORMAT));
        }
        if (start != null && end != null && start.isAfter(end)) {
            throw new ValidationException("Начало события должно быть раньше его завершения");
        }
    }
}
