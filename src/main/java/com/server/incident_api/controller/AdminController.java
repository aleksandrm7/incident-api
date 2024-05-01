package com.server.incident_api.controller;

import com.server.incident_api.dto.ApiResponse;
import com.server.incident_api.dto.IncidentUserUpdateRequest;
import com.server.incident_api.service.IncidentService;
import com.server.incident_api.service.IncidentUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final IncidentUserService incidentUserService;
    private final IncidentService incidentService;

    @GetMapping("/users")
    public ResponseEntity<ApiResponse> getUsers() {
        var userList = incidentUserService.getUsers();
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), userList));
    }

    @DeleteMapping("/users")
    public ResponseEntity<ApiResponse> deleteUser(@RequestParam Long id) {
        incidentUserService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), id));
    }

    @PutMapping("/users")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody @Valid IncidentUserUpdateRequest userUpdateRequest) {
        var incidentCreated = incidentUserService.updateUser(userUpdateRequest);
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), incidentCreated));
    }

    @GetMapping("/charts/status")
    public ResponseEntity<ApiResponse> getStatusChartData() {
        var chartData = incidentService.getStatusChartData();
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), chartData));
    }

    @GetMapping("/charts/priority")
    public ResponseEntity<ApiResponse> getPriorityChartData() {
        var chartData = incidentService.getPriorityChartData();
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), chartData));
    }

    @GetMapping("/charts/date-registered")
    public ResponseEntity<ApiResponse> getDateRegisteredChartData() {
        var chartData = incidentService.getDateRegisteredChartData();
        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK.value(), chartData));
    }
}
