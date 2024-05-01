package com.server.incident_api.entity.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum Status {
    ASSIGNED("Назначена"),
    IN_PROGRESS("В работе"),
    PAUSED("На паузе"),
    DONE("Выполнена"),
    CLOSED("Закрыта");

    String cyrillicValue;

    Status(String cyrillicValue) {
        this.cyrillicValue = cyrillicValue;
    }

    @JsonValue
    public String getCyrillicValue() {
        return cyrillicValue;
    }

    public static Status findByCyrillicValue(String cyrillicValue) {
        return Arrays.stream(values())
                .filter(status -> status.getCyrillicValue().equals(cyrillicValue))
                .findFirst()
                .get();
    }
}
