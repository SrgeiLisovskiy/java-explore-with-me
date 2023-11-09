package ru.practicum.main.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static ru.practicum.main.utilite.Constant.DATE_FORMAT;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {
    @NotBlank(message = "Аннотация не может быть пустой")
    @Size(min = 20, max = 2000)
    private String annotation;

    @NotNull(message = "Id категории не может быть null")
    private Long category;

    @NotBlank(message = "Описание не может быть пустым")
    @Size(min = 20, max = 7000)
    private String description;

    @NotNull(message = "Дата события не может быть null")
    @FutureOrPresent(message = "Дата события не может быть в прошлом")
    @JsonFormat(pattern = DATE_FORMAT)
    private LocalDateTime eventDate;

    @NotNull(message = "Локация не может быть null")
    private LocationDto location;

    @NotNull(message = "Paid не может быть null")
    @Builder.Default
    private Boolean paid = false;

    @Builder.Default
    private Long participantLimit = 0L;

    @Builder.Default
    private Boolean requestModeration = true;

    @NotBlank(message = "Заголовок не может быть пустым")
    @Size(min = 3, max = 120)
    private String title;
}
