package com.olek.datamerge.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.olek.datamerge.errors.ErrorsMessage;
import com.olek.datamerge.errors.InvalidFileFormatException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
public class XmlReadHandler implements ReadHandler {

    private final XmlMapper xmlMapper;
    private List<Map<String, String>> records = List.of();

    public XmlReadHandler(XmlMapper xmlMapper) {
        this.xmlMapper = xmlMapper;
    }

    @Override
    public void read(@NonNull File file) {
        try {
            records = xmlMapper.readValue(file, new TypeReference<>() {});
            log.info("XmlReadHandler read {} records", records.size());
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
}
