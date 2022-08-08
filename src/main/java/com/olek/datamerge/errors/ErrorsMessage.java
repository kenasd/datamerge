package com.olek.datamerge.errors;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ErrorsMessage {
    public static final String LOG_MESSAGE = "{}: {}";
    public static final String EMPTY_INPUT_PATHS = "Empty input paths";
    public static final String INVALID_OUTPUT_PATH = "Invalid output paths";
    public static final String INVALID_INPUT_PATH = "Invalid input path";
    public static final String INCORRECT_FILE_EXTENSION = "Wrong input file extension";
    public static final String CSV_FILE_NOT_FOUND = "Not found CSV file";
    public static final String INVALID_FILE_FORMAT = "Invalid file format";
    public static final String WRONG_CSV_DATA = "Wrong CSV data";
    public static final String INVALID_DATE_FORMAT = "Invalid date format";
    public static final String CANNOT_WRITE_CSV = "Cannot write CSV file";
}
