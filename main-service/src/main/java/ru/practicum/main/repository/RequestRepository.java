package ru.practicum.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.main.model.Event;
import ru.practicum.main.model.Request;
import ru.practicum.main.model.User;
import ru.practicum.main.model.enums.RequestStatus;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByIdIn(List<Long> requestIds);

    List<Request> findAllByEventId(Long eventId);

    List<Request> findAllByRequester(User user);

    boolean existsByRequesterAndEvent(User user, Event event);

    int countByEventIdAndStatus(Long eventId, RequestStatus status);


    @Query("SELECT count(R.id) FROM Request AS R " +
            "WHERE R.event.id in :eventId " +
            "AND R.status = 'CONFIRMED' ")
    Long findConfirmedRequests(Long eventId);

}
