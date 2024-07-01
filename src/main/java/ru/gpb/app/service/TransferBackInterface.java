package ru.gpb.app.service;

import org.springframework.http.ResponseEntity;
import ru.gpb.app.dto.CreateTransferRequest;

public interface TransferBackInterface {

    ResponseEntity<?> makeTransfer(CreateTransferRequest request);
}
