package com.olek.datamerge.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.olek.datamerge.errors.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.olek.datamerge.errors.ErrorsMessage.*;

class DataMergeServiceTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final XmlMapper xmlMapper = new XmlMapper();
    private final DataMergeService dataMergeService = new DataMergeService(objectMapper, xmlMapper);

    @Test()
    public void haveToReadFiles() {
        dataMergeService.readFiles("full-reports-test.csv", "reports.json", "reports.csv", "reports.xml");
    }

    @Test()
    public void haveToThrowExceptionsWhenReadFiles() {
        assertExceptionWhenReadFiles(null, new String[]{"reports.csv"}, INVALID_OUTPUT_PATH, EmptyOutputPathException.class);
        assertExceptionWhenReadFiles("", new String[]{"reports.csv"}, INVALID_OUTPUT_PATH, EmptyOutputPathException.class);
        assertExceptionWhenReadFiles("ful.scv", null, EMPTY_INPUT_PATHS, EmptyInputPathsException.class);
        assertExceptionWhenReadFiles("ful.scv", new String[0], EMPTY_INPUT_PATHS, EmptyInputPathsException.class);
        assertExceptionWhenReadFiles("ful.scv", new String[]{}, EMPTY_INPUT_PATHS, EmptyInputPathsException.class);
        assertExceptionWhenReadFiles("ful.scv", new String[]{"reports.jpg"}, INCORRECT_FILE_EXTENSION, IncorrectFileExtensionException.class);
        assertExceptionWhenReadFiles("ful.scv", new String[]{"reports.json", null}, INVALID_INPUT_PATH, InvalidInputPathException.class);
        assertExceptionWhenReadFiles("ful.scv", new String[]{"reports.json", "something"}, INCORRECT_FILE_EXTENSION, IncorrectFileExtensionException.class);
        assertExceptionWhenReadFiles("ful.scv", new String[]{"reports.json"}, CSV_FILE_NOT_FOUND, NotFoundCsvFileException.class);
    }

    @Test()
    public void haveToReadExtension() {
        Assertions.assertEquals("json", dataMergeService.readExtension("reports.json"));
        Assertions.assertEquals("json", dataMergeService.readExtension("folder/reports.json"));
        Assertions.assertEquals("csv", dataMergeService.readExtension("reports.csv"));
        Assertions.assertEquals("csv", dataMergeService.readExtension("folder/reports.csv"));
        Assertions.assertEquals("xml", dataMergeService.readExtension("reports.xml"));
        Assertions.assertEquals("xml", dataMergeService.readExtension("root/folder/reports.xml"));
    }

    @Test()
    public void haveToThrowExceptionsWhenReadExtension() {
        assertExceptionWhenReadExtension(null, INVALID_INPUT_PATH, InvalidInputPathException.class);
        assertExceptionWhenReadExtension("something", INCORRECT_FILE_EXTENSION, IncorrectFileExtensionException.class);
        assertExceptionWhenReadExtension("reports.jpg", INCORRECT_FILE_EXTENSION, IncorrectFileExtensionException.class);
    }

    private void assertExceptionWhenReadFiles(String outputPath, String[] inputPaths, String exceptionMessage, Class<? extends RuntimeException> exceptionClass) {
        RuntimeException thrown = Assertions.assertThrows(exceptionClass, () -> dataMergeService.readFiles(outputPath, inputPaths));
        Assertions.assertEquals(exceptionMessage, thrown.getMessage());
    }

    private void assertExceptionWhenReadExtension(String inputPath, String exceptionMessage, Class<? extends RuntimeException> exceptionClass) {
        RuntimeException thrown = Assertions.assertThrows(exceptionClass, () -> dataMergeService.readExtension(inputPath));
        Assertions.assertEquals(exceptionMessage, thrown.getMessage());
    }
}