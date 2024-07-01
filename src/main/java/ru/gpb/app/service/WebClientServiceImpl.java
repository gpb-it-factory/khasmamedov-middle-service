package ru.gpb.app.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.gpb.app.dto.CreateTransferRequest;
import ru.gpb.app.dto.CreateTransferRequestDto;
import ru.gpb.app.dto.CreateTransferResponse;

@Slf4j
@Service
public class WebClientServiceImpl implements WebClientService {

    private final WebClient webClient;

    public WebClientServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public ResponseEntity<CreateTransferResponse> makeAccountTransfer(CreateTransferRequest request) {
        log.info("Using WebClient for transferring money");
        return webClient.post()
                .uri("/transfers")
                .bodyValue(request)
                .retrieve()
                .toEntity(CreateTransferResponse.class)
                .block();
    }
}
