package com.server.incident_api.repository.specification;

import com.server.incident_api.dto.IncidentFilterCriteriaRequest;
import com.server.incident_api.entity.Incident;
import com.server.incident_api.entity.enums.Priority;
import com.server.incident_api.entity.enums.Status;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class IncidentSpecification implements Specification<Incident> {

    private final IncidentFilterCriteriaRequest incidentFilterCriteria;

    @Override
    public Predicate toPredicate(Root<Incident> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return incidentCompositeSpecification(incidentFilterCriteria).toPredicate(root, query, criteriaBuilder);
    }

    public Specification<Incident> incidentCompositeSpecification(IncidentFilterCriteriaRequest incidentFilterCriteria) {
        return combine(getSpecifications(incidentFilterCriteria));
    }

    /**
     * Собирает все спецификации инцидента в один объект для составления критерия фильтра
     * @param specifications все спецификации по которым могут быть найдены инциденты
     * @return сложный объект спецификации, по которому происходит поиск объектов в базе
     */
    private <T> Specification<T> combine(Collection<Specification<T>> specifications) {
        Specification<T> combinedSpecification = null;
        for (Specification<T> specification : specifications) {
            if (combinedSpecification == null) {
                combinedSpecification = Specification.where(specification);
            } else {
                combinedSpecification = combinedSpecification.and(specification);
            }
        }
        return combinedSpecification;
    }

    /**
     * Список всех спецификаций созданных ниже, по которым могут быть найдены инциденты
     * @param incidentFilterCriteria объект с полями фильтрации инцидента
     * @return список всех возможных спецификаций
     */
    private List<Specification<Incident>> getSpecifications(IncidentFilterCriteriaRequest incidentFilterCriteria) {
        return List.of(
                byUserId(incidentFilterCriteria.getUserId()),
                byType(incidentFilterCriteria.getType()),
                byContainsInDescription(incidentFilterCriteria.getDescription()),
                byStatus(incidentFilterCriteria.getStatus()),
                byPriority(incidentFilterCriteria.getPriority()),
                byDateRegisteredFrom(incidentFilterCriteria.getDateRegisteredFrom()),
                byDateRegisteredTo(incidentFilterCriteria.getDateRegisteredTo()),
                byDateResolvedFrom(incidentFilterCriteria.getDateResolvedFrom()),
                byDateResolvedTo(incidentFilterCriteria.getDateResolvedTo()));
    }

    private Specification<Incident> byUserId(Long userId) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = null;
            if (ObjectUtils.isNotEmpty(userId)) {
                predicate = criteriaBuilder.equal(root.get("userResponsible").get("id"), userId);
            }
            return predicate;
        };
    }

    private Specification<Incident> byType(String type) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = null;
            if (Strings.isNotBlank(type)) {
                predicate = criteriaBuilder.equal(root.get("type"), type);
            }
            return predicate;
        };
    }

    private Specification<Incident> byContainsInDescription(String description) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = null;
            if (Strings.isNotBlank(description)) {
                predicate = criteriaBuilder.like(root.get("description"), "%" + description + "%");
            }
            return predicate;
        };
    }

    private Specification<Incident> byStatus(String status) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = null;
            if (Strings.isNotBlank(status)) {
                var statusEnum = Status.findByCyrillicValue(status).toString();
                predicate = criteriaBuilder.equal(root.get("status"), statusEnum);
            }
            return predicate;
        };
    }

    private Specification<Incident> byPriority(String priority) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = null;
            if (Strings.isNotBlank(priority)) {
                var priorityEnum = Priority.findByCyrillicValue(priority).toString();
                predicate = criteriaBuilder.equal(root.get("priority"), priorityEnum);
            }
            return predicate;
        };
    }

    private Specification<Incident> byDateRegisteredFrom(LocalDate dateRegisteredFrom) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = null;
            if (Objects.nonNull(dateRegisteredFrom)) {
                predicate = criteriaBuilder.greaterThanOrEqualTo(root.get("dateRegistered"), dateRegisteredFrom);
            }
            return predicate;
        };
    }

    private Specification<Incident> byDateRegisteredTo(LocalDate dateRegisteredTo) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = null;
            if (Objects.nonNull(dateRegisteredTo)) {
                predicate = criteriaBuilder.lessThanOrEqualTo(root.get("dateRegistered"), dateRegisteredTo);
            }
            return predicate;
        };
    }

    private Specification<Incident> byDateResolvedFrom(LocalDate dateResolvedFrom) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = null;
            if (Objects.nonNull(dateResolvedFrom)) {
                predicate = criteriaBuilder.greaterThanOrEqualTo(root.get("dateResolved"), dateResolvedFrom);
            }
            return predicate;
        };
    }

    private Specification<Incident> byDateResolvedTo(LocalDate dateResolvedTo) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = null;
            if (Objects.nonNull(dateResolvedTo)) {
                predicate = criteriaBuilder.lessThanOrEqualTo(root.get("dateResolved"), dateResolvedTo);
            }
            return predicate;
        };
    }


}
