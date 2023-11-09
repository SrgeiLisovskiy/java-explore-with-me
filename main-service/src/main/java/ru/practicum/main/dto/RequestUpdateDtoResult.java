package ru.practicum.main.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestUpdateDtoResult {

    private List<RequestDto> confirmedRequests = new ArrayList<>();

    private List<RequestDto> rejectedRequests = new ArrayList<>();
}
