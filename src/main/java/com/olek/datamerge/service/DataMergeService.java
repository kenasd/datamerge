package com.olek.datamerge.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.olek.datamerge.errors.*;
import com.olek.datamerge.handler.*;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class DataMergeService {
    private static final String FILTERED_KEY = "packets-serviced";
    private static final String SKIPPED_VALUE = "0";
    private static final String SORTED_KEY = "request-time";
    private static final String CSV_EXTENSION = "csv";
    private static final String JSON_EXTENSION = "json";
    private static final String XML_EXTENSION = "xml";
    private final Map<String, ReadHandler> readHandlers;
    private final CsvWriteHandler csvWriteHandler;

    public DataMergeService(ObjectMapper objectMapper, XmlMapper xmlMapper) {
        this.readHandlers = Map.of(
                JSON_EXTENSION, new JsonReadHandler(objectMapper),
                XML_EXTENSION, new XmlReadHandler(xmlMapper),
                CSV_EXTENSION, new CsvReadHandler()
        );
        this.csvWriteHandler = new CsvWriteHandler();
    }

    public void readFiles(String output, String... inputPaths) {
        if (output == null || output.length() == 0) {
            log.error(ErrorsMessage.LOG_MESSAGE, ErrorsMessage.INVALID_OUTPUT_PATH, inputPaths);
            throw new EmptyOutputPathException(ErrorsMessage.INVALID_OUTPUT_PATH);
        }
        if (inputPaths == null || inputPaths.length == 0) {
            log.error(ErrorsMessage.LOG_MESSAGE, ErrorsMessage.EMPTY_INPUT_PATHS, inputPaths);
            throw new EmptyInputPathsException(ErrorsMessage.EMPTY_INPUT_PATHS);
        }

        Map<String, String> pathExtensions = Stream.of(inputPaths)
                .collect(Collectors.toMap(this::readExtension, Function.identity()));

        if (!pathExtensions.containsKey(CSV_EXTENSION)) {
            log.error(ErrorsMessage.LOG_MESSAGE, ErrorsMessage.CSV_FILE_NOT_FOUND, pathExtensions.keySet());
            throw new NotFoundCsvFileException(ErrorsMessage.CSV_FILE_NOT_FOUND);
        }

        List<Map<String, String>> records = pathExtensions.entrySet().stream()
                .map(entry -> readRecords(entry.getKey(), entry.getValue()))
                .flatMap(Collection::stream)
                .filter(record -> !record.get(FILTERED_KEY).equals(SKIPPED_VALUE))
                .sorted(Comparator.comparing(record -> record.get(SORTED_KEY)))
                .toList();
        log.info("Data merged {} records after filtering", records.size());

        ReadHandler csvReadHandler = readHandlers.get(CSV_EXTENSION);
        List<String> columns = csvReadHandler.getColumns();
        csvWriteHandler.write(new File(output), columns, records);
    }

    protected String readExtension(String inputPath) {
        if (inputPath == null || inputPath.isEmpty()) {
            log.error(ErrorsMessage.LOG_MESSAGE, ErrorsMessage.INVALID_INPUT_PATH, inputPath);
            throw new InvalidInputPathException(ErrorsMessage.INVALID_INPUT_PATH);
        }

        int index = inputPath.indexOf(".");
        if (index < 0 || index + 1 == inputPath.length()) {
            log.error(ErrorsMessage.LOG_MESSAGE, ErrorsMessage.INCORRECT_FILE_EXTENSION, inputPath);
            throw new IncorrectFileExtensionException(ErrorsMessage.INCORRECT_FILE_EXTENSION);
        }

        String extension = inputPath.substring(index + 1);
        if (!readHandlers.containsKey(extension)) {
            log.error(ErrorsMessage.LOG_MESSAGE, ErrorsMessage.INCORRECT_FILE_EXTENSION, extension);
            throw new IncorrectFileExtensionException(ErrorsMessage.INCORRECT_FILE_EXTENSION);
        }
        return extension;
    }

    private List<Map<String, String>> readRecords(String extension, String path) {
        ReadHandler readHandler = readHandlers.get(extension);
        readHandler.read(new File(path));
        return readHandler.getRecords();
    }
}
