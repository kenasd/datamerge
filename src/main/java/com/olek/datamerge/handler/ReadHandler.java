package com.olek.datamerge.handler;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface ReadHandler {
    void read(File file);
    List<Map<String, String>> getRecords();
    List<String> getColumns();
}
