package ru.practicum.main.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewCompilationDto {
    private Boolean pinned = false;

    @NotBlank(message = "Заголовок не может быть пустым")
    @Size(min = 1, max = 50)
    private String title;

    private List<Long> events = new ArrayList<>();
}
