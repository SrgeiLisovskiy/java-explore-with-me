package ru.practicum.main.service;

import ru.practicum.main.dto.*;

import java.util.List;

public interface EventService {
    List<EventShortDto> getEventsByUserId(Long userId, Integer from, Integer size);

    EventDto createEvent(NewEventDto newEventDto, Long userId);

    EventDto getUserEventById(Long userId, Long eventId);


    EventDto updateUserEventById(UpdateEventDto updateEventDto, Long userId, Long eventId);

    List<RequestDto> getRequestsForEventIdByUserId(Long userId, Long eventId);

    RequestUpdateDtoResult updateStatusRequestsForEventIdByUserId(RequestUpdateDtoRequest requestUpdateDtoRequest,
                                                                  Long userId, Long eventId);

    List<EventDto> getEventsByAdmin(List<Long> users, List<String> states, List<Long> categories, String rangeStart,
                                    String rangeEnd, Integer from, Integer size);

    EventDto updateEventByAdmin(UpdateEventDto updateEventDto, Long eventId);

    List<EventDto> getEventsByPublic(String text, List<Long> categories, Boolean paid, String rangeStart,
                                     String rangeEnd, Boolean onlyAvailable, String sort, Integer from, Integer size, String uri, String ip);

    EventDto getEventById(Long id, String uri, String ip);

}

