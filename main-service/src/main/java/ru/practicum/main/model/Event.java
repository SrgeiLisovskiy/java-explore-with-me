package ru.practicum.main.model;


import lombok.*;
import ru.practicum.main.model.enums.EventState;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id", nullable = false)
    User initiator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    Location location;

    @Column(name = "title")
    String title;

    @Column(name = "annotation")
    String annotation;

    @Column(name = "event_date", nullable = false)
    LocalDateTime eventDate;

    @Column(name = "paid")
    Boolean paid;

    @Column(name = "description")
    String description;

    @JoinColumn(name = "confirmed_requests")
    Integer confirmedRequests;

    @Column(name = "participant_limit")
    Long participantLimit;

    @Column(name = "request_moderation")
    Boolean requestModeration;

    @Column(name = "published_on")
    LocalDateTime publishedOn;

    @Column(name = "created_on")
    LocalDateTime createdOn;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    EventState state;



}
