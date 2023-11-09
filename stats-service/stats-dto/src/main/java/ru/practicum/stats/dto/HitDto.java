package ru.practicum.stats.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HitDto {
    @NotBlank(groups = {Create.class})
    private String app;
    @NotBlank(groups = {Create.class})
    private String uri;
    @NotBlank(groups = {Create.class})
    private String ip;
    @NotBlank(groups = {Create.class})
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String timestamp;
}
