package com.olek.datamerge.handler;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.olek.datamerge.errors.ErrorsMessage;
import com.olek.datamerge.errors.InvalidFileFormatException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

import static com.olek.datamerge.TestHelper.COUNT_COLUMNS;
import static com.olek.datamerge.TestHelper.COUNT_RECORDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class XmlReadHandlerTest {

    private final ReadHandler xmlHandler = new XmlReadHandler(new XmlMapper());

    @Test
    public void haveToReadFiles() {
        File file = new File("reports.xml");
        xmlHandler.read(file);

        assertNotNull(xmlHandler.getColumns());
        assertEquals(COUNT_COLUMNS, xmlHandler.getColumns().size());
        assertNotNull(xmlHandler.getRecords());
        assertEquals(COUNT_RECORDS, xmlHandler.getRecords().size());
    }

    @Test()
    public void haveToThrowExceptionsWhenRead() {
        File wrongFile = new File("wrong/path.xml");
        RuntimeException thrown = Assertions.assertThrows(InvalidFileFormatException.class, () -> xmlHandler.read(wrongFile));
        Assertions.assertEquals(ErrorsMessage.INVALID_FILE_FORMAT, thrown.getMessage());
    }
}