package com.server.incident_api.exception;

import com.server.incident_api.dto.ErrorResponse;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class IncidentApiExceptionHandler {

    /**
     * Общий обработчик ошибок
     * @param ex ошибка приложения
     * @return объект ответа с тектом ошибки
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleApiException(Exception ex) {
        return ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
    }

    /**
     * Обработчик ошибок, связанных с валидацией полей при сохранении различных
     * объектов, например пользователя или инцидента. Преобразует стандартные сообщения
     * в удобный читаемый вид, который возвращается в ответе
     * @param ex ошибка вадилации
     * @return объект ответа с тектом ошибки
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleApiException(MethodArgumentNotValidException ex) {
        var message = "";
        var messages = Arrays.stream(ex.getDetailMessageArguments())
                .map(detailedMessage -> {
                    String result = "";
                    var detailedMessageString = detailedMessage.toString();
                    if (!detailedMessageString.isBlank()) {
                        result = detailedMessageString.replaceAll("[a-zA-Z:,]", "").trim();
                    }
                    return result;
                })
                .filter(string -> !string.isBlank())
                .collect(Collectors.toList());

        if (!messages.isEmpty()) {
            message = Arrays.stream(messages.get(0).split("  "))
                    .collect(Collectors.joining("; "));
        }

        return ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR.value(), message);
    }
}
