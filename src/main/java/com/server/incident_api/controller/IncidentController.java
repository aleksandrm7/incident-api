package com.server.incident_api.controller;

import com.server.incident_api.dto.ApiResponse;
import com.server.incident_api.dto.IncidentCreateRequest;
import com.server.incident_api.dto.IncidentFilterCriteriaRequest;
import com.server.incident_api.dto.IncidentUpdateRequest;
import com.server.incident_api.service.IncidentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/incidents")
@RequiredArgsConstructor
public class IncidentController {

    private final IncidentService incidentService;

    @GetMapping
    public ResponseEntity<ApiResponse> getIncidentListForUser(@RequestParam Long userId) {
        var incidentList = incidentService.getIncidentListByUserId(userId);
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), incidentList));
    }

    @PostMapping("/filter")
    public ResponseEntity<ApiResponse> getIncidentListByFilter(@RequestBody IncidentFilterCriteriaRequest incidentFilterCriteria) {
        var incidentListByFilter = incidentService.getIncidentListByFilter(incidentFilterCriteria);
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), incidentListByFilter));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createIncident(@RequestBody @Valid IncidentCreateRequest incidentCreateRequest) {
        var incidentCreated = incidentService.createIncident(incidentCreateRequest);
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), incidentCreated));
    }

    @PutMapping
    public ResponseEntity<ApiResponse> updateIncident(@RequestBody IncidentUpdateRequest incidentUpdateRequest) {
        var incidentUpdated = incidentService.updateIncident(incidentUpdateRequest);
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), incidentUpdated));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse> deleteIncident(@RequestParam Long id) {
        incidentService.deleteIncident(id);
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), id));
    }

    @GetMapping("/logs")
    public ResponseEntity<ApiResponse> getIncidentLogListForUser(@RequestParam Long userId) {
        var incidentLogs = incidentService.getIncidentLogListByUserId(userId);
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), incidentLogs));
    }
}
