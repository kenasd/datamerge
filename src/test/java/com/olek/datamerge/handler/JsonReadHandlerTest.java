package com.olek.datamerge.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.olek.datamerge.errors.ErrorsMessage;
import com.olek.datamerge.errors.InvalidFileFormatException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

import static com.olek.datamerge.TestHelper.COUNT_COLUMNS;
import static com.olek.datamerge.TestHelper.COUNT_RECORDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class JsonReadHandlerTest {
    private final ReadHandler jsonHandler = new JsonReadHandler(new ObjectMapper());

    @Test
    public void haveToReadFiles() {
        File file = new File("reports.json");
        jsonHandler.read(file);

        assertNotNull(jsonHandler.getColumns());
        assertEquals(COUNT_COLUMNS, jsonHandler.getColumns().size());
        assertNotNull(jsonHandler.getRecords());
        assertEquals(COUNT_RECORDS, jsonHandler.getRecords().size());
    }

    @Test()
    public void haveToThrowExceptionsWhenRead() {
        File wrongFile = new File("wrong/path.json");
        RuntimeException thrown = Assertions.assertThrows(InvalidFileFormatException.class, () -> jsonHandler.read(wrongFile));
        Assertions.assertEquals(ErrorsMessage.INVALID_FILE_FORMAT, thrown.getMessage());
    }
}