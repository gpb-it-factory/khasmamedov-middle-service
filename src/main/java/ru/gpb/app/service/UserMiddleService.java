package ru.gpb.app.service;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.gpb.app.dto.CreateUserRequest;
import ru.gpb.app.dto.UserResponse;

import java.util.Optional;

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

    public Optional<UserResponse> createUser(CreateUserRequest request) {
        String url = backUrl + "/mock/users";
        try {
            log.info("��� ������ �� ������ � (���): {}", url);
            ResponseEntity<UserResponse> response = restTemplate.postForEntity(url, request, UserResponse.class);
            if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
                log.info("���-�� ����� �� ���, ������� 204");
                return Optional.empty();
            } else if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                log.info("�������� ����: {}", response.getBody().userId());
                return Optional.of(response.getBody());
            } else {
                log.error("���-�� ����� ������ �� ���, ������!: {}", response.getStatusCode());
                return Optional.empty();
            }
        } catch (Exception e) {
            log.error("������ �� ������ ������, �������� ������ ", e);
            return Optional.empty();
        }
    }
}