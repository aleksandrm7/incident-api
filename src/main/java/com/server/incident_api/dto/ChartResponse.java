package com.server.incident_api.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(staticName = "of")
public class ChartResponse {

    private final String name;
    private final Long count;
}
