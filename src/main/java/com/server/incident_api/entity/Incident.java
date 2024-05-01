package com.server.incident_api.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.server.incident_api.entity.enums.Priority;
import com.server.incident_api.entity.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "incident")
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type")
    private String type;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "priority")
    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Column(name = "date_registered", columnDefinition = "TIMESTAMP")
    @JsonFormat(pattern="HH:mm dd-MM-yyyy")
    private LocalDateTime dateRegistered;

    @Column(name = "date_resolved", columnDefinition = "TIMESTAMP")
    @JsonFormat(pattern="HH:mm dd-MM-yyyy")
    private LocalDateTime dateResolved;

    @ManyToOne
    @JoinColumn(name = "user_responsible_id", referencedColumnName = "id")
    private IncidentUser userResponsible;

    @OneToMany(mappedBy = "incident", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<IncidentLog> logList;
}
