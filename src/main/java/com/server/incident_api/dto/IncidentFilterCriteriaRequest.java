package com.server.incident_api.dto;

import com.server.incident_api.entity.enums.Priority;
import com.server.incident_api.entity.enums.Status;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class IncidentFilterCriteriaRequest {

    private Long userId;
    private String type;
    private String description;
    private String status;
    private String priority;
    private LocalDate dateRegisteredFrom;
    private LocalDate dateRegisteredTo;
    private LocalDate dateResolvedFrom;
    private LocalDate dateResolvedTo;
}
