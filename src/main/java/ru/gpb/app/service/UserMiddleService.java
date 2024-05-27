package ru.gpb.app.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import ru.gpb.app.dto.CreateUserRequest;

@Service
@Slf4j
public class UserMiddleService {

    private final RestTemplate restTemplate;
    private final String backUrl;

    @Autowired
    public UserMiddleService(RestTemplate restTemplate, @Value("${service-c.url}") String backUrl) {
        this.restTemplate = restTemplate;
        this.backUrl = backUrl;
    }

    public boolean createUser(CreateUserRequest request) {
        String url = backUrl + "/users";
        try {
            log.info("Шлю запрос на сервис C: {}", url);
            ResponseEntity<Void> response = restTemplate.postForEntity(url, request, Void.class);
            if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
                log.info("Шлю запрос на сервис C");
                return true;
            } else {
                log.error("Неожиданный статус-код при регистрации пользователя: {}", response.getStatusCode());
                return false;
            }
        } catch (HttpStatusCodeException e) {
            log.error("HttpStatusCodeException исключение: ", e);
            return false;
        } catch (Exception e) {
            log.error("Серьезная ошибка при запросе на бэкенд: ", e);
            return false;
        }
    }
}