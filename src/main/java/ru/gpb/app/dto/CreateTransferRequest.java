package ru.gpb.app.dto;

import ru.gpb.app.controller.ValidId;

import javax.validation.constraints.NotBlank;

public record CreateTransferRequest(@ValidId String from,
                                    @ValidId String to,
                                    @NotBlank String amount) {
}
