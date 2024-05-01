package com.server.incident_api.service;

import com.server.incident_api.dto.ChartResponse;
import com.server.incident_api.dto.IncidentCreateRequest;
import com.server.incident_api.dto.IncidentFilterCriteriaRequest;
import com.server.incident_api.dto.IncidentUpdateRequest;
import com.server.incident_api.entity.Incident;
import com.server.incident_api.entity.IncidentLog;
import com.server.incident_api.entity.enums.Priority;
import com.server.incident_api.entity.enums.Status;
import com.server.incident_api.repository.IncidentLogRepository;
import com.server.incident_api.repository.IncidentRepository;
import com.server.incident_api.repository.specification.IncidentSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IncidentService {

    private final IncidentRepository incidentRepository;
    private final IncidentLogRepository incidentLogRepository;
    private final IncidentUserService incidentUserService;

    /**
     * Метод берет из базы инциденты ответственного юзера
     * @param userId айди ответственного юзера
     * @return список инцидентов
     */
    public List<Incident> getIncidentListByUserId(Long userId) {
        return incidentRepository.findAllByUserResponsibleId(userId);
    }

    /**
     * Метод берет из базы логи инцидентов, которые относятся к данному юзеру
     * @param userId айди юзера
     * @return список логов инцидентов
     */
    public List<IncidentLog> getIncidentLogListByUserId(Long userId) {
        var incidentIdsForUser = getIncidentListByUserId(userId).stream()
                .map(Incident::getId)
                .collect(Collectors.toList());
        return incidentLogRepository.findAllByIncidentIdInOrderByDateUpdatedDesc(incidentIdsForUser);
    }

    /**
     * Метод создает спецификацию для поиска по фильтрам, указанным в реквесте
     * пустые поля не задействованы в поиске
     * @param incidentFilterCriteria объект с полями, по которым фильтруются инциденты
     * @return список инцидентов, удовлетворяющих критерию фильтра
     */
    public List<Incident> getIncidentListByFilter(IncidentFilterCriteriaRequest incidentFilterCriteria) {
        var incidentSpecification = new IncidentSpecification(incidentFilterCriteria);
        return incidentRepository.findAll(incidentSpecification);
    }

    /**
     * Удаление инцидента
     * @param id айди инцидента для удаления
     */
    public void deleteIncident(Long id) {
        var incidentToDelete = incidentRepository.getById(id);
        incidentRepository.delete(incidentToDelete);
    }

    /**
     * Создание нового инцидента, а так же создание записи лога о создании инцидента
     * @param incidentCreateRequest объекта с данными инцидента для создания
     * @return созданный инцидент
     */
    public Incident createIncident(IncidentCreateRequest incidentCreateRequest) {
        var currentDateTime = LocalDateTime.now();
        var userResponsible = incidentUserService.getById(incidentCreateRequest.getUserId());
        var incidentToSave = Incident.builder()
                .type(incidentCreateRequest.getType())
                .description(incidentCreateRequest.getDescription())
                .status(incidentCreateRequest.getStatus())
                .priority(incidentCreateRequest.getPriority())
                .dateRegistered(currentDateTime)
                .userResponsible(userResponsible)
                .build();
        var incidentSaved = incidentRepository.save(incidentToSave);
        var incidentCreateLog = IncidentLog.builder()
                .incident(incidentSaved)
                .dateUpdated(currentDateTime)
                .updateDescription("Инцидент создан")
                .build();
        incidentLogRepository.save(incidentCreateLog);
        return incidentSaved;
    }

    /**
     * Обновление полей инцидента, так же внутри происходит вызов метода для создания
     * записи лога инцидента со всеми изменениями. Если инцидент переходит в статус ЗАКРЫТ,
     * для него так же в базе сохраняется дата, когда он был решен
     * @param incidentUpdateRequest объект с данными инцидента, которые нужно обновить
     * @return обновленный инцидент
     */
    public Incident updateIncident(IncidentUpdateRequest incidentUpdateRequest) {
        var incidentToUpdate = incidentRepository.getById(incidentUpdateRequest.getId());

        var incidentCopy = Incident.builder()
                .type(incidentToUpdate.getType())
                .status(incidentToUpdate.getStatus())
                .priority(incidentToUpdate.getPriority())
                .description(incidentToUpdate.getDescription())
                .build();

        incidentToUpdate.setType(incidentUpdateRequest.getType());
        incidentToUpdate.setDescription(incidentUpdateRequest.getDescription());
        incidentToUpdate.setStatus(incidentUpdateRequest.getStatus());
        incidentToUpdate.setPriority(incidentUpdateRequest.getPriority());
        incidentToUpdate.setDateRegistered(incidentToUpdate.getDateRegistered());
        incidentToUpdate.setUserResponsible(incidentToUpdate.getUserResponsible());
        incidentToUpdate.setDateResolved(incidentToUpdate.getDateResolved());

        if (incidentUpdateRequest.getStatus() == Status.CLOSED) {
            incidentToUpdate.setDateResolved(LocalDateTime.now());
        }

        var incidentUpdated = incidentRepository.save(incidentToUpdate);

        createIncidentLog(incidentCopy, incidentUpdated);
        return incidentUpdated;
    }

    /**
     * Метод проверяет какие данные инцидента изменились и генерирует строку,
     * со всеми изменениями, которые произошли с инцидентом
     * во время обновления и сохраняет в базу запись лога
     * @param oldIncident объект со старыми данными инцидента
     * @param newIncident объект с обновленными данными инцидента
     */
    private void createIncidentLog(Incident oldIncident, Incident newIncident) {
        String separator = " -> ";
        String fieldSeparator = " | ";
        StringBuilder stringBuilder = new StringBuilder("Изменения: ");
        if (!oldIncident.getType().equals(newIncident.getType())) {
            stringBuilder.append("\n Тип - ")
                    .append(oldIncident.getType())
                    .append(separator)
                    .append(newIncident.getType())
                    .append(fieldSeparator);
        }

        if (!oldIncident.getStatus().equals(newIncident.getStatus())) {
            stringBuilder.append("Статус - ")
                    .append(oldIncident.getStatus().getCyrillicValue())
                    .append(separator)
                    .append(newIncident.getStatus().getCyrillicValue())
                    .append(fieldSeparator);
        }

        if (!oldIncident.getPriority().equals(newIncident.getPriority())) {
            stringBuilder.append(" Приоритет - ")
                    .append(oldIncident.getPriority().getCyrillicValue())
                    .append(separator)
                    .append(newIncident.getPriority().getCyrillicValue())
                    .append(fieldSeparator);
        }

        if (!oldIncident.getDescription().equals(newIncident.getDescription())) {
            stringBuilder.append(" Описание - ").append(newIncident.getDescription());
        }

        var incidentLog = IncidentLog.builder()
                .incident(newIncident)
                .dateUpdated(LocalDateTime.now())
                .updateDescription(stringBuilder.toString())
                .build();

        incidentLogRepository.save(incidentLog);

    }


    /**
     * Метод создает выборку инцидентов по статусам
     * @return список объектов, в котором содержится значения сколько инцидентов
     * имеется с каждым статусом
     */
    public List<ChartResponse> getStatusChartData() {
        List<ChartResponse> chartData = new ArrayList<>();
        Arrays.asList(Status.values()).forEach(status -> {
            var statusCount = incidentRepository.countByStatus(status);
            if (statusCount > 0) {
                chartData.add(ChartResponse.of(status.getCyrillicValue(), statusCount));
            }
        });

        return chartData;
    }

    /**
     * Метод создает выборку инцидентов по приоритетам
     * @return список объектов, в котором содержится значения сколько инцидентов
     * имеется с каждым приоритетом
     */
    public List<ChartResponse> getPriorityChartData() {
        List<ChartResponse> chartData = new ArrayList<>();
        Arrays.asList(Priority.values()).forEach(priority -> {
            var priorityCount = incidentRepository.countByPriority(priority);
            if (priorityCount > 0) {
                chartData.add(ChartResponse.of(priority.getCyrillicValue(), priorityCount));
            }
        });

        return chartData;
    }


    /**
     * Метод создает выборку инцидентов по дням
     * @return список объектов, в котором содержится значения сколько инцидентов
     * произошло в определенные дни
     */
    @Transactional
    public List<ChartResponse> getDateRegisteredChartData() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM");
        List<ChartResponse> chartData = new ArrayList<>();
        Map<LocalDate, Long> groupByDate = incidentRepository.findAllAsStream()
                .collect(Collectors.groupingBy(incident -> incident.getDateRegistered().toLocalDate(), Collectors.counting()));

        groupByDate.forEach((date, count) -> {
            if (count > 0) {
                chartData.add(ChartResponse.of(date.format(formatter), count));
            }
        });

        Collections.reverse(chartData);
        return chartData;
    }

}
