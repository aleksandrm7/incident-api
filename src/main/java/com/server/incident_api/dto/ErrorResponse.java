package com.server.incident_api.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(staticName = "of")
public class ErrorResponse {

    private final Integer statusCode;
    private final String message;
}
