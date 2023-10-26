package ru.practicum.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.dto.StatDto;
import ru.practicum.server.model.Stat;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<Stat, Long> {
    @Query("SELECT new ru.practicum.dto.StatDto(s.app, s.uri, COUNT(DISTINCT s.ip)) " +
            "FROM Stat AS s " +
            "WHERE s.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(DISTINCT s.ip) DESC")
    List<StatDto> findAllByHitsWithUniqueByIpWithoutUris(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.dto.StatDto(s.app, s.uri, COUNT(DISTINCT s.ip)) " +
            "FROM Stat AS s " +
            "WHERE s.uri IN (?1) AND s.timestamp BETWEEN ?2 AND ?3 " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(DISTINCT s.ip) DESC")
    List<StatDto> findAllHitsWithUniqueByIpWithUris(List<String> uris, LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.dto.StatDto(s.app, s.uri, COUNT(s.uri)) " +
            "FROM Stat AS s " +
            "WHERE s.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT (s.uri) DESC")
    List<StatDto> findAllHitsWithoutUris(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.dto.StatDto(s.app, s.uri, COUNT(s.uri)) " +
            "FROM Stat AS s " +
            "WHERE s.uri IN (?1) AND s.timestamp BETWEEN ?2 AND ?3 " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT (s.uri) DESC")
    List<StatDto> findAllHitsWithUris(List<String> uris, LocalDateTime start, LocalDateTime end);
}
