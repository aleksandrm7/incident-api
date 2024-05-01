package com.server.incident_api.repository;

import com.server.incident_api.entity.Incident;
import com.server.incident_api.entity.IncidentLog;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncidentLogRepository extends CrudRepository<IncidentLog, Long> {
    List<IncidentLog>findAllByIncidentIdInOrderByDateUpdatedDesc(List<Long> incidentIds);
}
