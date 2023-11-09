package ru.practicum.main.utilite;

import org.springframework.data.domain.PageRequest;

public interface CheckValidationService {
    PageRequest checkPageSize(Integer from, Integer size);
}

