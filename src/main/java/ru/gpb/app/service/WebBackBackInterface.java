package ru.gpb.app.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.gpb.app.dto.CreateTransferRequest;
import ru.gpb.app.dto.CreateTransferRequestDto;
import ru.gpb.app.dto.CreateTransferResponse;
import ru.gpb.app.dto.Error;

@Slf4j
@Service
public class WebBackBackInterface implements TransferBackInterface {

    private final WebClientService webClientService;

    public WebBackBackInterface(WebClientService webClientService) {
        this.webClientService = webClientService;
    }

    @Override
    public ResponseEntity<?> makeTransfer(CreateTransferRequest request) {
        try {
            log.info("Sending request to service C to make transfer");
            ResponseEntity<CreateTransferResponse> response = webClientService.makeAccountTransfer(request);
            return response;
        } catch (Exception e) {
            log.error("Something serious happened in program while making transfer: ", e);
            return new ResponseEntity<Error>(HttpStatus.BAD_REQUEST);
        }
    }
}
