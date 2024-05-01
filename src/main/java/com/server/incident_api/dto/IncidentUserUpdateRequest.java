package com.server.incident_api.dto;

import com.server.incident_api.entity.enums.Priority;
import com.server.incident_api.entity.enums.Role;
import com.server.incident_api.entity.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class IncidentUserUpdateRequest {

    private Long id;

    @Size(max = 50, message = "Имя пользователя должно быть менее 50 символов")
    @NotBlank(message = "Имя пользователя не может быть пустым")
    private String username;

    @Size(min = 5, max = 16, message = "Пароль должен быть от 5 до 16 символов")
    private String password;
    private Role role;
}
