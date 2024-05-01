package com.server.incident_api.dto;

import com.server.incident_api.entity.enums.Priority;
import com.server.incident_api.entity.enums.Status;
import lombok.Data;

@Data
public class IncidentUpdateRequest {

    private Long id;
    private String type;
    private String description;
    private Status status;
    private Priority priority;
}
