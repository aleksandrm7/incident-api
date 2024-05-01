package com.server.incident_api.entity.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum Priority {
    LOW("Низкий"),
    MEDIUM("Средний"),
    HIGH("Высокий"),
    CRITICAL("Критический");

    String cyrillicValue;

    Priority(String cyrillicValue) {
        this.cyrillicValue = cyrillicValue;
    }

    @JsonValue
    public String getCyrillicValue() {
        return cyrillicValue;
    }

    public static Priority findByCyrillicValue(String cyrillicValue) {
        return Arrays.stream(values())
                .filter(status -> status.getCyrillicValue().equals(cyrillicValue))
                .findFirst()
                .get();
    }
}
