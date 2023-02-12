package de.hsos.swa.actors.rest.dto.in.converter;

import javax.ws.rs.ext.ParamConverter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;


public class LocalDateTimeParamConverter implements ParamConverter<LocalDateTime> {
    DateTimeFormatter formatter = new DateTimeFormatterBuilder()
            .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
            .toFormatter();
    @Override
    public LocalDateTime fromString(String value) {
        if (value.matches("\\d{4}-\\d{2}-\\d{2}")) {
            value += " 00:00:00";
        }
        return LocalDateTime.parse(value, formatter);
    }

    @Override
    public String toString(LocalDateTime value) {
        return value.format(formatter);
    }
}
