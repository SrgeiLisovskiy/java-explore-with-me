package ru.practicum.client;

import io.micrometer.core.lang.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.HitDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static ru.practicum.server.utilite.Constant.DATE_FORMAT;

@Service
public class StatClient extends BaseClient {
    @Autowired
    public StatClient(@Value("${stats-service.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> addHit(HitDto hitDto) {
        return post("/hit", hitDto);
    }

    public ResponseEntity<Object> getAllStats(LocalDateTime start, LocalDateTime end, @Nullable List<String> uris, boolean unique) {
        Map<String, Object> parameters;
        if (uris == null) {
            parameters = Map.of("start", start.format(DateTimeFormatter.ofPattern(DATE_FORMAT)),
                    "end", start.format(DateTimeFormatter.ofPattern(DATE_FORMAT)),
                    "unique", unique);
            return get("/stats?start={start}&end={end}&unique={unique}", parameters);
        }
        parameters = Map.of("start", start.format(DateTimeFormatter.ofPattern(DATE_FORMAT)),
                "end", start.format(DateTimeFormatter.ofPattern(DATE_FORMAT)),
                "uris", String.join(",", uris),
                "unique", unique);
        return get("/stats?start={start}&end={end}&unique={unique}&uris={uris}", parameters);
    }


}
