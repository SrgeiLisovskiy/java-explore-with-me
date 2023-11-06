package ru.practicum.main.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.main.model.enums.EventState;

import java.time.LocalDateTime;

import static ru.practicum.main.utilite.Constant.DATE_FORMAT;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {
    String annotation;

    CategoryDto category;

    Integer confirmedRequests;

    @JsonFormat(pattern = DATE_FORMAT)
    LocalDateTime createdOn;

    String description;

    @JsonFormat(pattern = DATE_FORMAT)
    LocalDateTime eventDate;

    Long id;

    UserShortDto initiator;

    LocationDto location;

    Boolean paid;

    Long participantLimit;

    @JsonFormat(pattern = DATE_FORMAT)
    LocalDateTime publishedOn;

    Boolean requestModeration;

    EventState state;

    String title;

    Long views;
}
