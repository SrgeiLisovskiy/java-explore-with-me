package ru.practicum.main.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    @NotBlank(message = "Имя не должен быть пустым")
    @Size(max = 250, min = 2, message = "Имя должно быть не короче чем 2 симыолова и не длиннее 250")
    private String name;
    @Email
    @NotBlank(message = "Почта не должна быть пустой")
    @Size(max = 254, min = 6, message = "Email должен быть не короче чем 6 симыолова и не длиннее 254")
    private String email;
}
