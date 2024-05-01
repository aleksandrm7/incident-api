package com.server.incident_api.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "incident_log")
public class IncidentLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "incident_id", referencedColumnName = "id")
    private Incident incident;

    @Column(name = "date_updated", columnDefinition = "TIMESTAMP")
    @JsonFormat(pattern="HH:mm dd-MM-yyyy")
    private LocalDateTime dateUpdated;

    @Column(name = "update_description")
    private String updateDescription;
}
