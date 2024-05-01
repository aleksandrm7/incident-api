package com.server.incident_api.dto;

import com.server.incident_api.entity.enums.Priority;
import com.server.incident_api.entity.enums.Status;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class IncidentCreateRequest {

    private Long userId;
    @NotBlank(message = "Тип не может быть пустым")
    private String type;
    private String description;
    private Status status;
    private Priority priority;
}
