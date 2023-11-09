package ru.practicum.main.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestDto {
    private Long id;

    private Long event;

    private Long requester;

    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String created;
}
