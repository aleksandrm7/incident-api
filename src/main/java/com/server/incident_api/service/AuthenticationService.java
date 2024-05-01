package com.server.incident_api.service;

import com.server.incident_api.dto.SignInRequest;
import com.server.incident_api.dto.SignUpRequest;
import com.server.incident_api.dto.JwtAuthenticationResponse;
import com.server.incident_api.entity.IncidentUser;
import com.server.incident_api.entity.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final IncidentUserService incidentUserService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    /**
     * Создание пользователя
     * @param request - объект с учетными данными пользователя
     * @return объект с айди пользователя, токеном для дальнейшней аутентификации
     * и флагом является ли он администратором
     */
    public JwtAuthenticationResponse signUp(SignUpRequest request) {

        var user = IncidentUser.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        user = incidentUserService.create(user);

        var jwt = jwtService.generateToken(user);
        var isAdmin = request.getRole() == Role.ROLE_ADMIN;
        return new JwtAuthenticationResponse(user.getId(), jwt, isAdmin);
    }

    /**
     * Аутентификация пользователя по логину и паролю
     * @param request - объект с учетными данными пользователя
     * @return объект с айди пользователя, токеном для дальнейшней аутентификации
     * и флагом является ли он администратором
     */
    public JwtAuthenticationResponse signIn(SignInRequest request) {
        var user = incidentUserService
                .userDetailsService()
                .loadUserByUsername(request.getUsername());

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        var currentUser = (IncidentUser) user;

        var jwt = jwtService.generateToken(user);
        var isAdmin = currentUser.getRole() == Role.ROLE_ADMIN;
        return new JwtAuthenticationResponse(currentUser.getId(), jwt, isAdmin);
    }

}
