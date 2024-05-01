package com.server.incident_api.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(staticName = "of")
public class ApiResponse {

    private final Integer status;
    private final Object data;

}
