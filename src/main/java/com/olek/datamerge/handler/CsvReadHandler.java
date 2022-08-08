package com.olek.datamerge.handler;

import com.olek.datamerge.errors.ErrorsMessage;
import com.olek.datamerge.errors.InvalidFileFormatException;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@NoArgsConstructor
public class CsvReadHandler implements ReadHandler {
    private String[] columns = null;
    private List<Map<String, String>> records = List.of();

    @Override
    public void read(@NonNull File file) {
        try (Reader reader = Files.newBufferedReader(file.toPath())) {
            try (CSVReader csvReader = new CSVReader(reader)) {
                String[] line;
                records = new ArrayList<>();
                while ((line = csvReader.readNext()) != null) {
                    if (columns != null) {
                        records.add(convertToMap(line));
                    } else {
                        columns = line;
                    }
                }
            } catch (CsvValidationException e) {
                log.error(ErrorsMessage.LOG_MESSAGE, ErrorsMessage.INVALID_FILE_FORMAT, e.getMessage());
                throw new InvalidFileFormatException(ErrorsMessage.INVALID_FILE_FORMAT, e);
            }
            log.info("CsvReadHandler read {} records", records.size());
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
        return List.of(columns);
    }

    protected Map<String, String> convertToMap(String[] line) {
        if (line.length != columns.length) {
            log.error(ErrorsMessage.LOG_MESSAGE, ErrorsMessage.INVALID_FILE_FORMAT, ErrorsMessage.WRONG_CSV_DATA);
            throw new InvalidFileFormatException(ErrorsMessage.INVALID_FILE_FORMAT);
        }

        Map<String, String> result = new HashMap<>();
        for (int index = 0; index < columns.length; index++) {
            result.put(columns[index], line[index]);
        }
        return result;
    }
}
