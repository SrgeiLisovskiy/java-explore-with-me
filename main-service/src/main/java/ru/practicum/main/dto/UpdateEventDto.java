package ru.practicum.main.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static ru.practicum.main.utilite.Constant.DATE_FORMAT;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventDto {
    @Size(min = 20, max = 2000)
    private String annotation;
    @Positive
    private Long category;

    @Size(min = 20, max = 7000)
    private String description;

    @FutureOrPresent(message = "Дата события не может быть в прошлом")
    @JsonFormat(pattern = DATE_FORMAT)
    private LocalDateTime eventDate;

    private LocationDto location;

    private Boolean paid;
    @PositiveOrZero
    private Long participantLimit;

    private Boolean requestModeration;

    private String stateAction;

    @Size(min = 3, max = 120)
    private String title;
}
