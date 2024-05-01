package com.server.incident_api.repository;

import com.server.incident_api.entity.Incident;
import com.server.incident_api.entity.enums.Priority;
import com.server.incident_api.entity.enums.Status;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface IncidentRepository extends CrudRepository<Incident, Long>, JpaSpecificationExecutor<Incident> {

    List<Incident>findAllByUserResponsibleId(Long id);

    Incident getById(Long id);

    Long countByStatus(Status status);

    Long countByPriority(Priority priority);

    @Query("select i from Incident i")
    Stream<Incident> findAllAsStream();

}
