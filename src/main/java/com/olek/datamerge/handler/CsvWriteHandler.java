package com.olek.datamerge.handler;

import com.olek.datamerge.errors.ErrorsMessage;
import com.olek.datamerge.errors.InvalidFileFormatException;
import com.opencsv.CSVWriter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
public class CsvWriteHandler {
    public void write(@NonNull File file, List<String> columns, List<Map<String, String>> records) {
        List<String[]> lines = prepareLines(columns, records);
        try (CSVWriter writer = new CSVWriter(new FileWriter(file))) {
            for (String[] line : lines) {
                writer.writeNext(line);
            }
        } catch (IOException e) {
            log.error(ErrorsMessage.LOG_MESSAGE, ErrorsMessage.CANNOT_WRITE_CSV, e.getMessage());
            throw new InvalidFileFormatException(ErrorsMessage.CANNOT_WRITE_CSV, e);
        }
    }

    private List<String[]> prepareLines(List<String> columns, List<Map<String, String>> records) {
        List<String[]> arrayRecords = records.stream()
                .map(record -> convertToArray(columns, record))
                .toList();

        List<String[]> lines = new ArrayList<>();
        lines.add(columns.toArray(String[]::new));
        lines.addAll(arrayRecords);
        return lines;
    }

    private String[] convertToArray(List<String> columns, Map<String, String> fields) {
        return Stream.of(columns)
                .flatMap(Collection::stream)
                .map(fields::get)
                .toArray(String[]::new);
    }
}
