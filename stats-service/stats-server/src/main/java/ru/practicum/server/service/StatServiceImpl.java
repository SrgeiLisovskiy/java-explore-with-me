package ru.practicum.server.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.StatDto;
import ru.practicum.server.model.Stat;
import ru.practicum.server.model.mapper.StatMapper;
import ru.practicum.server.repository.StatRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatServiceImpl implements StatService {

    private final StatRepository statRepository;

    @Override
    @Transactional
    public HitDto addHit(HitDto hitDto) {
        Stat stat = StatMapper.toStat(hitDto);
        statRepository.save(stat);
        log.info("Сохранена статистика: {}", hitDto);
        return hitDto;
    }

    @Override
    public List<StatDto> getAllStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        List<StatDto> stats;
        if (unique) {
            if (uris == null || uris.isEmpty()) {
                stats = statRepository.findAllByHitsWithUniqueByIpWithoutUris(start, end);
            } else {
                stats = statRepository.findAllHitsWithUniqueByIpWithUris(uris, start, end);
            }
        } else {
            if (uris == null || uris.isEmpty()) {
                stats = statRepository.findAllHitsWithoutUris(start, end);
            } else {
                stats = statRepository.findAllHitsWithUris(uris, start, end);
            }
        }
        log.info("Получена статистика :{}", stats);
        return stats;
    }
}
