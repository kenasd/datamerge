package com.olek.datamerge.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.olek.datamerge.errors.ErrorsMessage;
import com.olek.datamerge.errors.InvalidDateFormatException;
import com.olek.datamerge.errors.InvalidFileFormatException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
public class JsonReadHandler implements ReadHandler {
    private static final String TIMESTAMP_FIELD = "request-time";
    private static final String ZONE_ID = "UTC-3";
    private static final String DATE_EXTENSION = " ADT";

    private final ObjectMapper objectMapper;
    private final DateTimeFormatter formatter;
    private List<Map<String, String>> records;

    public JsonReadHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.records = List.of();
        this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    @Override
    public void read(@NonNull File file) {
        try {
            records = objectMapper.readValue(file, new TypeReference<>() {});
            convertTimestamp(records);
            log.info("JsonReadHandler read {} records", records.size());
        } catch (IOException e) {
            log.error(ErrorsMessage.LOG_MESSAGE, ErrorsMessage.INVALID_FILE_FORMAT, e.getMessage());
            throw new InvalidFileFormatException(ErrorsMessage.INVALID_FILE_FORMAT, e);
        }
    }

    @Override
    public List<Map<String, String>> getRecords() {
        return records;
    }

    @Override
    public List<String> getColumns() {
        return records.stream()
                .limit(1L)
                .flatMap(record -> record.keySet().stream())
                .toList();
    }

    private void convertTimestamp(List<Map<String, String>> records) {
        try {
            for (Map<String, String> record : records) {
                long timestamp = Long.parseLong(record.get(TIMESTAMP_FIELD));
                LocalDateTime localDateTime = new Date(timestamp).toInstant()
                        .atZone(ZoneId.of(ZONE_ID))
                        .toLocalDateTime();
                String date = formatter.format(localDateTime) + DATE_EXTENSION;
                record.put(TIMESTAMP_FIELD, date);
            }
        } catch (Exception e) {
            log.error(ErrorsMessage.LOG_MESSAGE, ErrorsMessage.INVALID_DATE_FORMAT, e.getMessage());
            throw new InvalidDateFormatException(ErrorsMessage.INVALID_DATE_FORMAT, e);
        }
    }

}
