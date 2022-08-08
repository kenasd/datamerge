package com.olek.datamerge.handler;

import com.olek.datamerge.errors.ErrorsMessage;
import com.olek.datamerge.errors.InvalidFileFormatException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

import static com.olek.datamerge.TestHelper.COUNT_COLUMNS;
import static com.olek.datamerge.TestHelper.COUNT_RECORDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CsvReadHandlerTest {

    private final ReadHandler csvHandler = new CsvReadHandler();

    @Test
    public void haveToReadFiles() {
        File file = new File("reports.csv");
        csvHandler.read(file);

        assertNotNull(csvHandler.getColumns());
        assertEquals(COUNT_COLUMNS, csvHandler.getColumns().size());
        assertNotNull(csvHandler.getRecords());
        assertEquals(COUNT_RECORDS, csvHandler.getRecords().size());
    }

    @Test()
    public void haveToThrowExceptionsWhenRead() {
        File wrongFile = new File("wrong/path.csv");
        RuntimeException thrown = Assertions.assertThrows(InvalidFileFormatException.class, () -> csvHandler.read(wrongFile));
        Assertions.assertEquals(ErrorsMessage.INVALID_FILE_FORMAT, thrown.getMessage());
    }
}