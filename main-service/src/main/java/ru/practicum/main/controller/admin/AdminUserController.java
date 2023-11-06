package ru.practicum.main.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.dto.UserDto;
import ru.practicum.main.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping(path = "/admin/users")
public class AdminUserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers(
            @RequestParam(required = false) List<Long> ids,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Выполнен запрос GET/admin/users :{}", ids);
        return new ResponseEntity<>(userService.getUsers(ids, from, size), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserDto> creatUser(@RequestBody @Valid UserDto userDto) {
        log.info("Выполнен запрос POST/admin/users: {}", userDto);
        return new ResponseEntity<>(userService.addUser(userDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable Long userId) {
        log.info("Выполнен запрос DEL/{userId}:{}", userId);
        userService.deleteUserById(userId);
    }
}
