package ru.practicum.main.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.dto.RequestDto;
import ru.practicum.main.exceptions.ConflictException;
import ru.practicum.main.exceptions.NotFoundException;
import ru.practicum.main.model.Event;
import ru.practicum.main.model.Request;
import ru.practicum.main.model.User;
import ru.practicum.main.model.enums.EventState;
import ru.practicum.main.model.enums.RequestStatus;
import ru.practicum.main.model.mappers.RequestMapper;
import ru.practicum.main.repository.EventRepository;
import ru.practicum.main.repository.RequestRepository;
import ru.practicum.main.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.main.model.enums.RequestStatus.CONFIRMED;
import static ru.practicum.main.model.enums.RequestStatus.PENDING;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;

    @Override
    @Transactional
    public RequestDto createRequest(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("События с ID = " + eventId + "  не найдено"));
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с ID = " + userId + "  не найден"));
        checkConflictRequest(event, user);
        Request request = Request.builder()
                .created(LocalDateTime.now())
                .requester(user)
                .event(event)
                .build();
        if (event.getRequestModeration() && event.getParticipantLimit() > 0) {
            request.setStatus(PENDING);
        } else {
            request.setStatus(CONFIRMED);
        }
        return RequestMapper.toRequestDto(requestRepository.save(request));
    }

    @Override
    public List<RequestDto> getRequestsByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с ID = " + userId + "  не найден"));
        return requestRepository.findAllByRequester(user).stream()
                .map(RequestMapper::toRequestDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RequestDto cancelRequest(Long userId, Long requestId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с ID = " + userId + "  не найден"));
        Request request = requestRepository.findById(requestId).orElseThrow(() ->
                new NotFoundException("Запрос с ID = " + requestId + "  не найдено"));
        if (!request.getRequester().equals(user)) {
            throw new ConflictException("Запрос не принадлежит пользователю");
        }
        request.setStatus(RequestStatus.CANCELED);
        return RequestMapper.toRequestDto(requestRepository.save(request));
    }

    private void checkConflictRequest(Event event, User user) {
        if (event.getInitiator().equals(user)) {
            throw new ConflictException("Нельзя создать запрос на свое событие");
        }
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Событие должно быть опубликовано");
        }
        if (requestRepository.existsByRequesterAndEvent(user, event)) {
            throw new ConflictException("Невозможно отправить повторный запрос");
        }
        if (event.getParticipantLimit() > 0 &&
                event.getParticipantLimit() <= requestRepository.countByEventIdAndStatus(event.getId(), CONFIRMED)) {
            throw new ConflictException("У события достигнут лимит запросов на участие.");
        }
    }
}
