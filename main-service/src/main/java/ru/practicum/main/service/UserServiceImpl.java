package ru.practicum.main.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.dto.UserDto;
import ru.practicum.main.exceptions.ConflictException;
import ru.practicum.main.exceptions.NotFoundException;
import ru.practicum.main.model.User;
import ru.practicum.main.model.mappers.UserMapper;
import ru.practicum.main.repository.UserRepository;
import ru.practicum.main.utilite.CheckValidationService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CheckValidationService checkValidation;

    @Override
    @Transactional
    public UserDto addUser(UserDto userDto) {
        if (userRepository.existsByName(userDto.getName())) {
            throw new ConflictException("Пользователь с таким именем уже зарегестрирован");
        }
        User user = userRepository.save(UserMapper.toUser(userDto));
        return UserMapper.toUserDto(user);
    }

    @Override
    public List<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {
        PageRequest pageRequest = checkValidation.checkPageSize(from, size);
        if (ids == null) {
            return userRepository.findAll(pageRequest).stream().map(UserMapper::toUserDto).collect(Collectors.toList());
        }
        return userRepository.findByIdIn(ids, pageRequest).stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteUserById(Long userId) {
        userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с ID = " + userId + "  не найден"));
        userRepository.deleteById(userId);
    }

    private void checkUserEmailForDuplicate(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new ConflictException("Пользователь с " + email + " уже существует");
        }

    }
}
