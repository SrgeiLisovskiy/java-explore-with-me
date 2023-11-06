package ru.practicum.main.model.mappers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.main.dto.RequestDto;
import ru.practicum.main.model.Request;

import java.time.format.DateTimeFormatter;

import static ru.practicum.main.utilite.Constant.DATE_FORMAT;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestMapper {
    public static RequestDto toRequestDto(Request request) {
        return RequestDto.builder()
                .id(request.getId())
                .requester(request.getRequester().getId())
                .created(request.getCreated().format(DateTimeFormatter.ofPattern(DATE_FORMAT)).toString())
                .event(request.getEvent().getId())
                .status(request.getStatus().toString())
                .build();
    }

}
