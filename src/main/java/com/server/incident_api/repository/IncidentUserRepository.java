package com.server.incident_api.repository;

import com.server.incident_api.entity.IncidentUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IncidentUserRepository extends CrudRepository<IncidentUser, Long> {
    Optional<IncidentUser> findById(Long id);
    Optional<IncidentUser> findByUsername(String username);
    boolean existsByUsername(String username);

}
