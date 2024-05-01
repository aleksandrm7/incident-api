package com.server.incident_api.service;

import com.server.incident_api.dto.IncidentUserUpdateRequest;
import com.server.incident_api.entity.IncidentUser;
import com.server.incident_api.repository.IncidentUserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class IncidentUserService {

    private final IncidentUserRepository incidentUserRepository;

    /**
     * Получение списка всех пользователей, только для администраторов
     * @return список всех пользователей
     */
    public List<IncidentUser> getUsers() {
        return StreamSupport.stream(incidentUserRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    /**
     * Получение объекта пользователя по айди, если его нет в базе, пробрасывается исключение
     * @param id айди пользователя
     * @return объект пользователя
     */
    public IncidentUser getById(Long id) {
        return incidentUserRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
    }

    /**
     * Сохранение пользователя
     * @param user пользователь, которого требуется сохранить
     * @return объект сохраненного пользователя
     */
    public IncidentUser save(IncidentUser user) {
        return incidentUserRepository.save(user);
    }

    /**
     * Создание нового пользователя системы, перед этим происходит проверка, что
     * пользователя с таким юзернеймом еще нет в базе
     * @param user пользователь для создания
     * @return созданный пользователь
     */
    public IncidentUser create(IncidentUser user) {
        if (incidentUserRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Пользователь с таким именем уже существует");
        }

        return save(user);
    }

    /**
     * Получение пользователя из базы по его юзернейму, если его не существует,
     * то пробрасывается исключение
     * @param username
     * @return
     */
    public IncidentUser getByUsername(String username) {
        return incidentUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
    }

    /**
     * Удаление пользователя, только для администратора
     * @param id айди пользователя, которого нужно удалить
     */
    public void deleteUser(Long id) {
        var userToDelete = getById(id);
        incidentUserRepository.delete(userToDelete);
    }

    /**
     * Обновление полей пользователя, в том числе и пароля, только для администратора
     * @param updateUserRequest объект с полями, которые нужно обновить
     * @return обновленный пользователь
     */
    public IncidentUser updateUser(IncidentUserUpdateRequest updateUserRequest) {
        var userToUpdate = getById(updateUserRequest.getId());
        var passwordEncoder = new BCryptPasswordEncoder();

        userToUpdate.setUsername(updateUserRequest.getUsername());
        userToUpdate.setRole(updateUserRequest.getRole());
        if (Strings.isNotBlank(updateUserRequest.getPassword())) {
            userToUpdate.setPassword(passwordEncoder.encode(updateUserRequest.getPassword()));
        }

        return incidentUserRepository.save(userToUpdate);
    }

    /**
     * Возвращает объект пользователя для спринг секьюрити и аутентификации
     * @return
     */
    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

}
